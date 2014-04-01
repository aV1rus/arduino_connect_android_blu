package com.av1rus.arduinoconnect.arduinolib.utils;

/**
 * Created by nick on 4/1/14.
 */
public class ByteUtils {
    private static Object obj = new Object();
    final protected static char[] hexArray = "-123456789ABCDEF".toCharArray();

    /**
     * String -> Hex
     *
     * @param s
     * @return
     */
    public static String stringToHex(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4 + " ";
        }
        return str;
    }

    /**
     * Hex -> String
     *
     * @param s
     * @return
     */
    public static String hexToString(String s) {
        String[] strs = s.split(" ");
        byte[] baKeyword = new byte[strs.length];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(strs[i], 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * Hex -> Byte
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static byte[] hexToByte(String s) throws Exception {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return baKeyword;
    }

    /**
     * Byte -> Hex
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes, int count) {
        StringBuffer sb = new StringBuffer();
        synchronized (obj) {
            for (int i = 0; i < count; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                // Log.d("MonitorActivity",i+":"+hex);
                sb.append(hex).append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Bytes -> Hex
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for( int j = 0; j< bytes.length; j++){
            int v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v >>> 4];
            hexChars[j*2+1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
