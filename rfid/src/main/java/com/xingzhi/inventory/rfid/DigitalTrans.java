package com.xingzhi.inventory.rfid;

public class DigitalTrans {
    private static final String hexStr = "0123456789ABCDEF";
    private static final String[] binaryArray =
        {"0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111",
            "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111"};

    /**
     * 数字字符串转ASCII码字符串
     *
     * @param content 字符串
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                String b = Integer.toHexString(c);
                result = result + b;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * ascii decoding for byte array
     *
     * @param content
     * @return
     */
    public static String ByteToAsciiString(byte[] content) {
        StringBuilder result = new StringBuilder();
        for (byte aContent : content) {
            char c = (char) aContent;
            if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                result.append(c);
            } else {
                break;
            }
        }
        return result.toString();
    }

    /**
     * @param bArray
     * @return 转换为二进制字符串
     */
    static String bytes2BinaryStr(byte[] bArray) {
        StringBuilder outStr = new StringBuilder();
        int pos;
        for (byte b : bArray) {
            //高四位
            pos = (b & 0xF0) >> 4;
            outStr.append(binaryArray[pos]);
            //低四位
            pos = b & 0x0F;
            outStr.append(binaryArray[pos]);
        }
        return outStr.toString();
    }

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String BinaryToHexStringSplitByChar(byte[] bytes, char splitChar) {
        StringBuilder result = new StringBuilder();
        String hex;
        for (byte aByte : bytes) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((aByte & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(aByte & 0x0F));
            result.append(hex).append(splitChar);
        }
        return result.toString();
    }

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String BinaryToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        String hex;
        for (byte aByte : bytes) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((aByte & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(aByte & 0x0F));
            result.append(hex);
        }
        return result.toString();
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static byte[] HexStringToBinary(String hexString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high;//字节高四位
        byte low;//字节低四位

        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }

    public static String BinStringToOctBinary(String binString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = binString.length() / 4;
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(Integer.parseInt(binString.substring(i * 4, (i + 1) * 4), 2));
        }
        return sb.toString();
    }
}
