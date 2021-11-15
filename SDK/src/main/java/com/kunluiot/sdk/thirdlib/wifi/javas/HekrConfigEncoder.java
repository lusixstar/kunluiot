package com.kunluiot.sdk.thirdlib.wifi.javas;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hekr_xm on 2016/12/15.
 **/

public class HekrConfigEncoder {

    private static final String TAG = HekrConfigEncoder.class.getSimpleName();

    private List<byte[]> mEncodedLeadingPart;
    private List<byte[]> mMagicCode;
    private List<byte[]> mPrefixCode;
    private List<byte[]> mLengthData;
    private byte[] mAddressData;


    public HekrConfigEncoder(String ssid, String password, String pinCode) throws UnsupportedEncodingException {
        // 前导码
        mEncodedLeadingPart = leadingPart();
        // MagicCode
        mMagicCode = magicCode(ssid, password, pinCode);
        // 密码的长度
        mPrefixCode = prefixCode(password);
        // 内容
        mLengthData = getData(password, pinCode);
        // 地址编码
        mAddressData = (ssid + '\n' + password + '\n' + pinCode).getBytes("utf-8");
    }

    public List<byte[]> getEncodedLeadingPart() {
        return mEncodedLeadingPart;
    }

    public List<byte[]> getMagicCode() {
        return mMagicCode;
    }

    public List<byte[]> getPrefixCode() {
        return mPrefixCode;
    }

    public List<byte[]> getLengthData() {
        return mLengthData;
    }

    /**
     * 长度编码，获取地址编码的数据
     *
     * @return 地址编码后的数据
     */
    public byte[] getAddressData() {
        return mAddressData;
    }

    /**
     * 长度编码，获取前导码
     *
     * @return 长度为4的数组
     */
    private List<byte[]> leadingPart() {
        return getBytes(new int[]{1, 11, 21, 31});
    }

    /**
     * 长度编码，获取ssid，password和pincode的长度
     *
     * @return 长度为4的数组
     */
    private List<byte[]> magicCode(String ssid, String password, String pin) {
        int length = password.length() + pin.length() + ssid.length();
        int[] magicCode = new int[4];
        magicCode[0] = (length >>> 4 & 0xF);
        if (magicCode[0] == 0)
            magicCode[0] = 0x08;
        magicCode[1] = 0x10 | (length & 0xF);
        int crc8 = CRC8(ssid);
        magicCode[2] = 0x20 | (crc8 >>> 4 & 0xF);
        magicCode[3] = 0x30 | (crc8 & 0xF);

        return getBytes(magicCode);
    }

    /**
     * 长度编码，获取密码长度和CRC8
     *
     * @return 长度为4的数组
     */
    private List<byte[]> prefixCode(String password) {
        int length = password.length();
        int[] prefixCode = new int[4];
        prefixCode[0] = 0x40 | (length >>> 4 & 0xF);
        prefixCode[1] = 0x50 | (length & 0xF);
        int crc8 = CRC8(new byte[]{(byte) length});
        prefixCode[2] = 0x60 | (crc8 >>> 4 & 0xF);
        prefixCode[3] = 0x70 | (crc8 & 0xF);

        return getBytes(prefixCode);
    }

    /**
     * 长度编码，获取password和pincode内容
     *
     * @return int类型的数组
     */
    private List<byte[]> getData(String password, String pinCode) {
        List<Integer> result = new ArrayList<>();

        // 密码和pincode数据
        String data = password + pinCode;
        int index;
        byte content[] = new byte[4];

        // password pincode 4 整除部分
        for (index = 0; index < data.length() / 4; ++index) {
            System.arraycopy(data.getBytes(), index * 4, content, 0, content.length);
            sequence(index, content, result);
        }
        // password pincode 取余部分
        if (data.length() % 4 != 0) {
            byte less[] = new byte[data.length() % 4];
            System.arraycopy(data.getBytes(), index * 4, less, 0, less.length);
            System.arraycopy(less, 0, content, 0, less.length);
            int length = data.length() % 4;
            for (int j = 0; j < 4 - length; j++) {
                int seat = length + j;
                content[seat] = 0;
            }
            sequence(index, content, result);
        }
        // 获取最后的bytes
        int[] bytes = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            bytes[i] = result.get(i);
        }

        return getBytes(bytes);
    }

    private void sequence(int index, byte data[], List<Integer> result) {
        byte content[] = new byte[data.length + 1];
        content[0] = (byte) (index & 0xFF);
        System.arraycopy(data, 0, content, 1, data.length);
        int crc8 = CRC8(content);
        result.add(0x80 | crc8);
        result.add(0x80 | index);
        for (byte b : data)
            result.add(b | 0x100);
    }

    /**
     * 获取CRC8
     *
     * @return CRC8
     */
    private int CRC8(String stringData) {
        return CRC8(stringData.getBytes());
    }

    private int CRC8(byte data[]) {
        int len = data.length;
        int i = 0;
        byte crc = 0x00;
        while (len-- > 0) {
            byte extract = data[i++];
            for (byte tempI = 8; tempI != 0; tempI--) {
                byte sum = (byte) ((crc & 0xFF) ^ (extract & 0xFF));
                sum = (byte) ((sum & 0xFF) & 0x01);
                crc = (byte) ((crc & 0xFF) >>> 1);
                if (sum != 0) {
                    crc = (byte) ((crc & 0xFF) ^ 0x8C);
                }
                extract = (byte) ((extract & 0xFF) >>> 1);
            }
        }
        return (crc & 0xFF);
    }

    private List<byte[]> getBytes(int[] data) {
        List<byte[]> result = new ArrayList<>();
        for (int j = 0; j < data.length; j++) {
            byte[] bytes = new byte[data[j]];
            for (int k = 0; k < bytes.length; k++) {
                bytes[k] = 1;
            }
            result.add(bytes);
        }
        return result;
    }
}
