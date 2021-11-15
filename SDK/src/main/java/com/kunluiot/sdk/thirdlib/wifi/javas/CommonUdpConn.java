package com.kunluiot.sdk.thirdlib.wifi.javas;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hucn on 2017/4/7.
 * Author: hucn
 * Description: UDP 可以共用一个端口，所以就抽取成一个共用的类
 */

public class CommonUdpConn implements IAsyncConn {

    private static final String TAG = CommonUdpConn.class.getSimpleName();
    // A port number of 0 will let the system pick up an ephemeral port
    private static final int PORT_LOCAL = 0;

    private static final int MAXIMUM_PACKET_BYTES = 102400;

    private static CommonUdpConn instance;

    private volatile boolean isRunning;
    private CommonThread mCurrentThread;
    private Context mContext;
    private List<ConfigCallback> mCallbacks;

    public static CommonUdpConn getInstance(Context context) {
        if (instance == null) {
            instance = new CommonUdpConn(context);
        }
        return instance;
    }

    private CommonUdpConn(Context context) {
        this.mContext = context.getApplicationContext();
        this.mCallbacks = new ArrayList<>();
    }

    public synchronized void start() {
        if (isRunning) {
            Log.d(TAG, "The BroadcastUdpConn is running, no need to restart");
            return;
        }
        int perm = mContext.checkCallingOrSelfPermission("android.permission.INTERNET");
        boolean has_perssion = perm == PackageManager.PERMISSION_GRANTED;
        if (!has_perssion) {
            Log.e(TAG, "Has no permission:android.permission.INTERNET");
            return;
        }

        isRunning = true;
        mCurrentThread = new CommonThread();
        mCurrentThread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        if (mCurrentThread != null) {
            mCurrentThread.stopCommon();
            mCurrentThread = null;
        }
    }

    @Override
    public void send(String message) {
        Log.w(TAG, "No implement");
    }

    @Override
    public synchronized void send(String message, String ip, int port) {
        if (TextUtils.isEmpty(message)) {
            Log.w(TAG, "Message is null or empty");
            return;
        }
        if (isActive()) {
            try {
                Log.d(TAG, "The  channel is on, send message: " + message + ", ip:" + ip + ", port: " + port);
                if (TextUtils.isEmpty(ip) || TextUtils.equals("0.0.0.0", ip)) {
                    Log.d(TAG, "The ip is invalid, ip is " + ip);
                } else {
                    InetSocketAddress address = new InetSocketAddress(ip, port);
                    byte[] data = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, address);
                    mCurrentThread.send(packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "The udp channel is off, can not send message...");
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized boolean isActive() {
        return mCurrentThread != null && mCurrentThread.isActive();
    }

    @Override
    public void addOnReceiveListener(ConfigCallback callback) {
        this.mCallbacks.add(callback);
    }

    @Override
    public void removeOnReceiveListener(ConfigCallback callback) {
        this.mCallbacks.remove(callback);
    }

    private class CommonThread extends Thread {

        private DatagramSocket mSocket;
        private boolean mStop;

        CommonThread() {
            mStop = false;
        }

        @Override
        public void run() {
            try {
                while (!mStop && !isInterrupted()) {
                    if (!NetworkUtil.isConnected(mContext)) {
                        Log.e(TAG, "Has no net, delays for 1000ms");
                        Thread.sleep(1000);
                        continue;
                    }
                    try {
                        Log.d(TAG, "Common Conn start" + ", port is: " + PORT_LOCAL);
                        mSocket = new DatagramSocket(null);
                        mSocket.setReuseAddress(true);
                        mSocket.bind(new InetSocketAddress(PORT_LOCAL));
                        while (!mStop) {
                            DatagramPacket packet = new DatagramPacket(new byte[MAXIMUM_PACKET_BYTES], MAXIMUM_PACKET_BYTES);
                            try {
                                Log.d(TAG, "Begin receiving......");
                                mSocket.receive(packet);
                                byte[] data = packet.getData();
                                int length = packet.getLength();
                                byte[] bytes = new byte[length];
                                System.arraycopy(data, 0, bytes, 0, length);
                                String message = new String(bytes);
                                String ip = packet.getAddress().getHostAddress();
                                int port = packet.getPort();
                                Log.d(TAG, "The channel is on, receive message from " + ip + ":" + PORT_LOCAL + ": " + message);
                                Object json = new JSONTokener(message).nextValue();
                                if (json instanceof JSONObject) {
                                    JSONObject object = new JSONObject(message);
                                    if (!object.has("ip")) {
                                        object.put("ip", ip);
                                    }
                                    for (ConfigCallback callback : mCallbacks) {
                                        callback.onReceive(TextUtils.concat(ip, ":", String.valueOf(port)).toString(), object.toString());
                                    }
                                } else if (json instanceof JSONArray) {
                                    for (ConfigCallback callback : mCallbacks) {
                                        callback.onReceive(TextUtils.concat(ip, ":", String.valueOf(port)).toString(), message);
                                    }
                                }else{
                                    for (ConfigCallback callback : mCallbacks) {
                                        callback.onReceive(TextUtils.concat(ip, ":", String.valueOf(port)).toString(), message);
                                    }
                                }
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (mSocket != null) {
                            try {
                                mSocket.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void stopCommon() {
            mStop = true;
            if (mSocket != null) {
                mSocket.close();
                mSocket = null;
            }
            interrupt();
        }

        private void send(DatagramPacket packet) throws IOException {
            if (mSocket != null) {
                mSocket.send(packet);
            }
        }

        private boolean isActive() {
            return mSocket != null && !mSocket.isClosed();
        }
    }
}
