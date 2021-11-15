package com.kunluiot.sdk.thirdlib.wifi.javas;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hucn on 2017/7/3.
 * Author: hucn
 */

public class MulticastUdpConn implements IAsyncConn {

    private static final String TAG = MulticastUdpConn.class.getSimpleName();

    private static final String MULTICAST_ADDRESS = "224.0.0.207";
    // A port number of 0 will let the system pick up an ephemeral port
    private static final int PORT_LOCAL = 24253;

    private static final int MAXIMUM_PACKET_BYTES = 102400;

    private static MulticastUdpConn instance;

    private MulticastThread mThread;
    private WifiManager.MulticastLock mMulticastLock;
    private volatile boolean isRunning;
    private Context mContext;
    private List<ConfigCallback> mCallbacks;

    public static MulticastUdpConn getInstance(Context context) {
        if (instance == null) {
            instance = new MulticastUdpConn(context);
        }
        return instance;
    }

    private MulticastUdpConn(Context context) {
        this.mContext = context.getApplicationContext();
        this.mCallbacks = new ArrayList<>();
    }

    @Override
    public synchronized void start() {
        if (isRunning) {
            Log.d(TAG, "The MulticastUdpConn is running, no need to restart");
            return;
        }
        requireMulticastLock(mContext);
        int perm = mContext.checkCallingOrSelfPermission("android.permission.INTERNET");
        boolean has_perssion = perm == PackageManager.PERMISSION_GRANTED;
        if (!has_perssion) {
            Log.e(TAG, "Has no permission:android.permission.INTERNET");
            releaseMulticastLock();
            return;
        }
        isRunning = true;
        mThread = new MulticastThread();
        mThread.start();
    }

    @Override
    public synchronized void stop() {
        isRunning = false;
        releaseMulticastLock();
        if (mThread != null) {
            mThread.stopMulticast();
            mThread = null;
        }
    }

    @Override
    public synchronized void send(String message) {
        if (TextUtils.isEmpty(message)) {
            Log.w(TAG, "Message is null or empty");
            return;
        }
        if (isActive()) {
            Log.d(TAG, "The  channel is on, send message: " + message);
            try {
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length,
                        InetAddress.getByName(MULTICAST_ADDRESS), PORT_LOCAL);
                mThread.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "The udp channel is off, can not send message...");
        }
    }

    @Override
    public void send(String message, String ip, int port) {
        Log.w(TAG, "no implement");
    }

    @Override
    public synchronized boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized boolean isActive() {
        return mThread != null && mThread.isActive();
    }

    @Override
    public void addOnReceiveListener(ConfigCallback callback) {
        this.mCallbacks.add(callback);
    }

    @Override
    public void removeOnReceiveListener(ConfigCallback callback) {
        this.mCallbacks.remove(callback);
    }

    private void requireMulticastLock(Context context) {
        if (mMulticastLock == null) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                mMulticastLock = wifiManager.createMulticastLock(MulticastUdpConn.class.getSimpleName());
            }
        }
        if (!mMulticastLock.isHeld()) {
            mMulticastLock.acquire();
        }
    }

    private void releaseMulticastLock() {
        if (mMulticastLock != null && mMulticastLock.isHeld()) {
            mMulticastLock.release();
        }
    }

    private class MulticastThread extends Thread {

        private MulticastSocket mSocket;
        private boolean mStop;

        MulticastThread() {
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
                        Log.d(TAG, "Multi Conn start, ip is: " + MULTICAST_ADDRESS + ", port is: " + PORT_LOCAL);
                        InetAddress address = InetAddress.getByName(MULTICAST_ADDRESS);
                        mSocket = new MulticastSocket(null);
                        mSocket.setReuseAddress(true);
                        mSocket.setTimeToLive(255);
                        mSocket.bind(new InetSocketAddress(PORT_LOCAL));
                        mSocket.joinGroup(address);
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

        private void stopMulticast() {
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
