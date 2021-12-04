package com.kunluiot.sdk.thirdlib.wifi.javas;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hekr_xm on 2017/3/30.
 **/
public class SeparatedDeviceConfig {

    private static final String TAG = SeparatedDeviceConfig.class.getSimpleName();

    private static final int TIME_SLEEP_MILLIS_BLOCK = 4;

    private Handler mHandler;
    private WifiManager.MulticastLock mLock;

    private Context mContext;

    private String mPinCode;
    private String mSSID;
    private String mPassword;

    private ExecutorService mTaskService;
    private DeviceAsyncTask mDeviceTask;
    private SoftAPAsyncTask mSoftAPTask;

    private LinkedBlockingQueue<ConfigBean> mConfigMessages;

    private ConfigCallback mCallback;

    public SeparatedDeviceConfig(Context context) {
        this.mContext = context;
        this.mTaskService = Executors.newCachedThreadPool();
        this.mConfigMessages = new LinkedBlockingQueue<>();
    }

    /**
     * 开始配网
     *
     * @param context      context
     * @param configParams 配网设置，需要传入ssid，password，pinCode这三个键值所对应的参数
     */
    public synchronized void startConfig(Context context, Map configParams, ConfigCallback callback) {
        if (!configParams.containsKey("ssid") || !configParams.containsKey("password") ||
                !configParams.containsKey("pinCode"))
            throw new IllegalArgumentException("SSID or password or pin code is null");
        // 如果没有停止，首先停止配网
        stop();
        this.mHandler = new Handler(context.getMainLooper());
        this.mCallback = callback;
        setParams(configParams);
        if (!TextUtils.isEmpty(mSSID) && !TextUtils.isEmpty(mPinCode)) {
            acquireLock(context);
            // 添加监听
            addFilter();
            config(mSSID, mPassword, mPinCode);
        } else {
            Log.d(TAG, "Pin code or SSID is null");
        }
    }

    /**
     * 开始SoftAP模式配网
     *
     * @param context      context
     * @param configParams 配网设置，需要传入ssid，password，pinCode这三个键值所对应的参数
     */
    public synchronized void startSoftAPConfig(Context context, Map configParams, ConfigCallback callback) {
        if (!configParams.containsKey("ssid") || !configParams.containsKey("password") ||
                !configParams.containsKey("pinCode"))
            throw new IllegalArgumentException("SSID or password or pin code is null");
        stop();
        this.mHandler = new Handler(context.getMainLooper());
        this.mCallback = callback;
        setParams(configParams);
        if (!TextUtils.isEmpty(mSSID) && !TextUtils.isEmpty(mPinCode)) {
            acquireLock(context);
            // 添加监听
            addFilter();
            configAP(mSSID, mPassword, mPinCode);
        } else {
            Log.d(TAG, "Pin code or SSID is null");
        }
    }

    /**
     * 强制停止配网,没有OnResult()回调
     */
    public synchronized void stopConfig() {
        releaseLock();
        stopTasks();
        Log.d(TAG, "stopConfig");
        reset();
    }

    private void stop() {
        releaseLock();
        stopTasks();
        reset();
    }

    private void setParams(Map configParams) {
        // 配置信息
        this.mSSID = (String) configParams.get("ssid");
        this.mPassword = (String) configParams.get("password");
        this.mPinCode = (String) configParams.get("pinCode");
    }

    private void acquireLock(Context context) {
        releaseLock();
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mLock == null && manager != null) {
            mLock = manager.createMulticastLock("config");
            mLock.setReferenceCounted(true);
            mLock.acquire();
        }
    }

    private void releaseLock() {
        if (mLock != null && mLock.isHeld()) {
            mLock.release();
            mLock = null;
        }
    }

    private void stopTasks() {
        stopDeviceTask();
        stopSoftAPTask();
    }

    private void stopDeviceTask() {
        if (mDeviceTask != null && !mDeviceTask.isCancelled()) {
            mDeviceTask.cancel(true);
        }
        mDeviceTask = null;
    }

    private void stopSoftAPTask() {
        if (mSoftAPTask != null && !mSoftAPTask.isCancelled()) {
            mSoftAPTask.cancel(true);
        }
        mSoftAPTask = null;
    }

    private void addFilter() {
        ConfigCallback callback = new ConfigCallback() {
            @Override
            public void onReceive(String ip, String message) {
                handlerConfigFromDevice(ip, message);
            }
        };
        BroadcastUdpConn.getInstance(mContext).addOnReceiveListener(callback);
        MulticastUdpConn.getInstance(mContext).addOnReceiveListener(callback);
    }

    private void config(String ssid, String password, String pinCode) {
        mDeviceTask = new DeviceAsyncTask(ssid, password, pinCode);
        mDeviceTask.executeOnExecutor(mTaskService);
    }

    private void configAP(String ssid, String password, String pinCode) {
        mSoftAPTask = new SoftAPAsyncTask(ssid, password, pinCode);
        mSoftAPTask.executeOnExecutor(mTaskService);
    }

    private synchronized void handlerConfigFromDevice(String ip, String message) {
        try {
            JSONObject json = new JSONObject(message);
            if (json.has("action") && json.has("params")) {
                String action = json.getString("action");
                if (!TextUtils.equals(action, "devConfig")) {
                    return;
                }
                JSONObject params = json.getJSONObject("params");
                if (params.has("devTid")
                        && params.has("STEP")
                        && params.has("code")
                        && params.has("PIN")
                        && params.has("bind")) {
                    String devTid = params.getString("devTid");
                    String pinCode = params.getString("PIN");
                    int step = params.getInt("STEP");
                    int code = params.getInt("code");
                    int bind = params.getInt("bind");
                    // 判断是否已经存在了设备信息
                    if (!TextUtils.equals(pinCode, mPinCode)) {
                        Log.d(TAG, "PinCode is unmatched");
                        return;
                    }
                    if (bind == 0) {
                        Log.d(TAG, "Not binding action");
                        return;
                    }
                }
                if (mCallback != null) {
                    mCallback.onReceive(ip, message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean handlerConfigFromAP(String ip, String message) {
        // {"action":"devConfig","params":{"STEP":0,"devTid":"ESP_2M_600194593FBF","PIN":"xXWuXQ","code":"200"}}
        try {
            JSONObject json = new JSONObject(message);
            if (json.has("action") && json.has("params")) {
                String action = json.getString("action");
                if (!TextUtils.equals(action, "devConfig")) {
                    return false;
                }
                JSONObject params = json.getJSONObject("params");
                if (params.has("devTid")
                        && params.has("STEP")
                        && params.has("code")
                        && params.has("PIN")) {
                    String devTid = params.getString("devTid");
                    String pinCode = params.getString("PIN");
                    int step = params.getInt("STEP");
                    int code = params.getInt("code");
                    // 判断是否已经存在了设备信息
                    if (!TextUtils.equals(pinCode, mPinCode)) {
                        Log.d(TAG, "PinCode is unmatched");
                        return false;
                    }
                    if (mCallback != null) {
                        mCallback.onReceive(ip, message);
                    }
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void reset() {
        // 清除状态信息
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        mPinCode = null;
        mSSID = null;
        mPassword = null;
        mCallback = null;
        mConfigMessages.clear();
    }

    private class DeviceAsyncTask extends AsyncTask<Void, Integer, Void> {

        private String pinCode;
        private String ssid;
        private String password;

        DeviceAsyncTask(String ssid, String password, String pinCode) {
            this.pinCode = pinCode + "";
            this.ssid = ssid + "";
            this.password = password + "";
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatagramSocket ds = null;
            Future mixedFuture = null;
            try {
                Log.i(TAG, "Start config for new devices");
                Log.i(TAG, "Send ssid: " + ssid + ", password: " + password + ", pinCode: " + pinCode);

                HekrConfigEncoder encoder = new HekrConfigEncoder(ssid, password, pinCode);
                ds = new DatagramSocket();
                MixedConfigRunnable mixedConfigRunnable = new MixedConfigRunnable(encoder);
                mixedFuture = mTaskService.submit(mixedConfigRunnable);

                while (!isCancelled() && !Thread.interrupted()) {
                    try {
                        ConfigBean bean = mConfigMessages.take();
                        // 如果需要同时发送多个
                        if (bean.getPackets() != null) {
                            for (DatagramPacket packet : bean.getPackets()) {
                                ds.send(packet);
                            }
                        }
                        // 如果只需要发送一个
                        if (bean.getSingle() != null) {
                            DatagramPacket packet = bean.getSingle();
                            ds.send(packet);
                        }
                    } catch (IOException e) {
//                        Log.e(TAG, "Error happened when send config message through socket");
//                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ds != null) {
                    try {
                        ds.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mixedFuture != null) {
                    mixedFuture.cancel(true);
                }

                Log.i(TAG, "Task of sending encoded config data finished");
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "Cancel device config");
        }
    }

    class MixedConfigRunnable implements Runnable {

        HekrConfigEncoder encoder;

        private MixedConfigRunnable(HekrConfigEncoder encoder) {
            this.encoder = encoder;
        }

        @Override
        public void run() {
            try {
                // 实际发送间隔是1ms 2ms 4ms循环
                int sendingMicrosInterval = 1000;
                while (!Thread.currentThread().isInterrupted()) {
                    // -----先发长度编码和地址编码的前导码-----
                    Log.d(TAG, "Start send config message");
                    Log.d(TAG, "Start send leading part message");
                    for (int i = 0; i < 32 / (sendingMicrosInterval / 1000); i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        // Log.d(TAG, "Leading part count: " + i);
                        // 发送一遍完整的地址编码
                        sendEncodedLeadingPartByAddress(encoder.getAddressData(), 1, sendingMicrosInterval);
                        for (int j = 0; j < encoder.getAddressData().length; j++) {
                            sendEncodedDataByAddress(encoder.getAddressData(), j, 1, sendingMicrosInterval);
                        }
                        sendEncodedLeadingPartByAddress(encoder.getAddressData(), 1, sendingMicrosInterval);
                        // 发送长度编码的前导码
                        // Log.d(TAG, "===Start send leading length  message");
                        List<byte[]> leadingPart = encoder.getEncodedLeadingPart();
                        sendEncodedDataByLength(leadingPart, 16, 4, sendingMicrosInterval);
                    }

                    // -----发送长度编码和地址编码的剩余部分-----
                    Log.d(TAG, "Start send left part message");
                    for (int i = 0; i < 16 / (sendingMicrosInterval / 1000); i++) {
                        if (Thread.currentThread().isInterrupted()) {
                            return;
                        }
                        // Log.d(TAG, "Left part count: " + i);
                        // -----发送地址编码-----
                        sendEncodedLeadingPartByAddress(encoder.getAddressData(), 4, sendingMicrosInterval);
                        for (int j = 0; j < encoder.getAddressData().length; j++) {
                            sendEncodedDataByAddress(encoder.getAddressData(), j, 4, sendingMicrosInterval);
                        }
                        sendEncodedLeadingPartByAddress(encoder.getAddressData(), 4, sendingMicrosInterval);
                        // -----发送长度编码-----
                        // 发送MagicCode
                        List<byte[]> magicCode = encoder.getMagicCode();
                        sendEncodedDataByLength(magicCode, 8, 4, sendingMicrosInterval);

                        // 发送PrefixCode
                        List<byte[]> prefixCode = encoder.getPrefixCode();
                        // 发送LengthData
                        List<byte[]> lengthData = encoder.getLengthData();
                        for (int j = 0; j < 8; j++) {
                            sendEncodedDataByLength(prefixCode, 1, 4, sendingMicrosInterval);
                            sendEncodedDataByLength(lengthData, 1, 6, sendingMicrosInterval);
                        }
                    }
                    // 1ms, 2ms, 4ms循环
                    sendingMicrosInterval = sendingMicrosInterval * 2;
                    if (sendingMicrosInterval > 4000) {
                        sendingMicrosInterval = 1000;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class SoftAPAsyncTask extends AsyncTask<Void, Integer, Void> {

        private String pinCode;
        private String ssid;
        private String password;
        private boolean received;

        SoftAPAsyncTask(String ssid, String password, String pinCode) {
            this.pinCode = pinCode;
            this.ssid = ssid;
            this.password = password;
            this.received = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!TextUtils.isEmpty(pinCode) && !TextUtils.isEmpty(ssid)) {
                // 在给固件发送数据时，不需要经过编码，编码后固件会不识别
                String reallySsid = ssid;
                try {
                    while (!isCancelled() && !Thread.interrupted() && !received) {
                        Log.i(TAG, "Start AP config");
                        String body = "ssid=" + reallySsid + "&password=" + password + "&pinCode=" + pinCode + "&end";
                        Log.d(TAG, "Send message: " + body);
                        sendByTcp(body);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private void sendByTcp(String message) {
            try {
                Socket socket = new Socket("192.168.4.1", 7001);
                try {
                    socket.setSoTimeout(5000);
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    outputStream.write(message.getBytes(Charset.forName("UTF-8")));
                    outputStream.flush();
                    String line;
                    if ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    final String response = stringBuilder.toString();
                    Log.d(TAG, "AP response: " + response);
                    if (mHandler != null && !isCancelled() && !received) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isCancelled()) {
                                    received = handlerConfigFromAP("192.168.4.1:7001", response);
                                }
                            }
                        });
                    }
                } finally {
                    if (!socket.isClosed()) {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 地址编码添加前导码(IP:224.127.XXX.255)，XXX代表数据长度
     *
     * @param data  数据
     * @param times 数据发送次数
     * @throws InterruptedException exception
     * @throws IOException          exception
     */
    private void sendEncodedLeadingPartByAddress(byte[] data, int times, int micros)
            throws InterruptedException, IOException {
        for (int i = 0; i < times; i++) {
            DatagramPacket dp = getAddressLeadingPartPackage(data);
            mConfigMessages.put(new ConfigBean(dp));
            sleep(micros);
        }
    }

    /**
     * 地址编码添加发送数据队列
     *
     * @param data  数据
     * @param index 增加的需要
     * @throws InterruptedException exception
     * @throws IOException          exception
     */
    private void sendEncodedDataByAddress(byte[] data, int index, int times, int micros)
            throws InterruptedException, IOException {
        for (int i = 0; i < times; i++) {
            DatagramPacket dp = getAddressPackage(data, index);
            mConfigMessages.put(new ConfigBean(dp));
            sleep(micros);
        }
    }

    /**
     * 长度编码添加到发送数据队列
     *
     * @param data     数据
     * @param times    重复发送次数
     * @param interval 间隔几个数据添加到队列一次
     * @throws InterruptedException exception
     * @throws IOException          exception
     */
    private void sendEncodedDataByLength(List<byte[]> data, int times, int interval, int micros)
            throws  InterruptedException, IOException {
            for (int i = 0; i < times; i++) {
                // 到达interval后需要发送数据
                int count = 0;
                for (int j = 0; j < data.size(); j++) {
                    DatagramPacket dp = getLengthPackage(data.get(j));
                    mConfigMessages.put(new ConfigBean(dp));
                    sleep(micros);

                    if (count % interval == interval - 1) {
                        count = 0;
                        Thread.sleep(TIME_SLEEP_MILLIS_BLOCK);
                    } else {
                        count++;
                    }
                }

                if (count != 0) {
                    Log.d(TAG, "Address data not enough to the interval");
                    // 如果有剩余的数据，那么后面全部填充byte[1]
                    for (int j = 0; j < interval - count; j++) {
                        DatagramPacket dp = getLengthPackage(new byte[1]);
                        mConfigMessages.put(new ConfigBean(dp));
                        sleep(micros);
                    }
                    Thread.sleep(TIME_SLEEP_MILLIS_BLOCK);
                }
            }
    }

    private DatagramPacket getLengthPackage(byte[] data) throws UnknownHostException {
        String ip = getLengthConfigIp();
        // 长度编码端口是7001
        return new DatagramPacket(data, data.length, InetAddress.getByName(ip), 7001);
    }

    private DatagramPacket getAddressLeadingPartPackage(byte[] data) throws UnknownHostException {
        byte[] bytes = new byte[1];
        String ip = getAddressLeadingPartIp(data);
        return new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), 7002);
    }

    private DatagramPacket getAddressPackage(byte[] data, int index) throws UnknownHostException {
        // 地址编码中不发送有效的数据，只发送一个固定长度的包
        byte[] bytes = new byte[1];
        int n = index % (data.length + 2);
        // 地址编码中的组播地址
        String ip = getAddressConfigIp(data, n);
        // 地址编码端口是7002
        return new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), 7002);
    }

    private String getAddressLeadingPartIp(byte[] addressData) {
        return "224.127." + addressData.length + ".255";
    }

    private String getAddressConfigIp(byte[] addressData, int n) {
        return "224." + n + "." + unsignedByteToInt(addressData[n]) + ".255";
    }

    private int lastIp = 1;

    private String getLengthConfigIp() {
        String ip = "255.255.255." + lastIp;
        lastIp++;
        if (lastIp == 255) {
            lastIp = 1;
        }
        return ip;
    }

    private int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * sleep 微秒
     *
     * @param micros 微秒
     * @throws InterruptedException interrupt
     */
    private void sleep(int micros) throws InterruptedException {
        try {
            if (micros <= 0) {
                return;
            }
            int millis = micros / 1000;
            int nanos = (micros % 1000) * 1000;
            Thread.sleep(millis, nanos);
        } catch(IllegalArgumentException e){
//            KunLuLog.INSTANCE.e("IllegalArgumentException == ", e.getMessage());
        } //            e.printStackTrace();
    }

    private class ConfigBean {

        private List<DatagramPacket> packets;
        private DatagramPacket single;

        private ConfigBean(List<DatagramPacket> packets) {
            this.packets = packets;
        }

        private ConfigBean(DatagramPacket single) {
            this.single = single;
        }

        public List<DatagramPacket> getPackets() {
            return packets;
        }

        public void setPackets(List<DatagramPacket> packets) {
            this.packets = packets;
        }

        public DatagramPacket getSingle() {
            return single;
        }

        public void setSingle(DatagramPacket single) {
            this.single = single;
        }
    }
}
