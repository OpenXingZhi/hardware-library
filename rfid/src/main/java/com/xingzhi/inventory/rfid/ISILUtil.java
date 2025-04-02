package com.xingzhi.inventory.rfid;

final class ISILUtil {
    private static final String ALock = "11100"; //大小写状态下大小写锁定
    private static final String AShift = "11101"; //大小写状态下大小写转换
    private static final String NLock = "11110"; //大小写状态下数字锁定
    private static final String NShift = "11111"; //大小写状态下数字转换
    private static final String NULock = "1100"; //数字状态下大写锁定
    private static final String NUShift = "1101"; //数字状态下大写转换
    private static final String NLLock = "1110"; //数字状态下小写锁定
    private static final String NLShift = "1111"; //数字状态下小写转换

    private static final byte hyphen = 0x2D; //  -
    private static final byte colon = 0x3A; //  :
    private static final byte slash = 0x2F; //  /

    /**
     * UC - Upper case letter
     * UCT - Upper case letter transfer
     * LC - Lower case letter
     * LCT - Lower case letter transfer
     * NU - number
     * NUT - number transfer
     */
    static String Decode(String value) {
        if (value.length() < 5) {
            return null;
        } else {
            StringBuilder ret = new StringBuilder();
            String curctrl = "UC"; //控制字符
            String nextctrl = "UC";
            String tmp;

            byte[] buf = new byte[1];
            do {
                String t = "";
                //the first is upper case
                switch (curctrl) {
                    case "UC":
                    case "UCT":
                        tmp = value.substring(0, 5);
                        value = value.substring(5);
                        if (tmp.equals("00000")) {
                            t = "-";
                        } else if (tmp.equals("11011")) {
                            t = ":";
                        } else if (!tmp.startsWith("111")) {
                            buf[0] = Byte.parseByte("010" + tmp, 2);
                            t = new String(buf);
                        } else {
                            switch (tmp) {
                                case "11100":
                                    nextctrl = "LC";
                                    break;
                                case "11101":
                                    nextctrl = "LCT";
                                    break;
                                case "11110":
                                    nextctrl = "NU";
                                    break;
                                case "11111":
                                    nextctrl = "NUT";
                                    break;
                            }
                        }
                        break;
                    case "LC":
                    case "LCT":
                        tmp = value.substring(0, 5);
                        value = value.substring(5);
                        if (tmp.equals("00000")) {
                            t = "-";
                        } else if (tmp.equals("11011")) {
                            t = "/";
                        } else if (!tmp.startsWith("111")) {
                            buf[0] = Byte.parseByte("011" + tmp, 2);
                            t = new String(buf);
                        } else {
                            switch (tmp) {
                                case "11100":
                                    nextctrl = "UC";
                                    break;
                                case "11101":
                                    nextctrl = "UCT";
                                    break;
                                case "11110":
                                    nextctrl = "NU";
                                    break;
                                case "11111":
                                    nextctrl = "NUT";
                                    break;
                            }
                        }
                        break;
                    case "NU":
                    case "NUT":
                        tmp = value.substring(0, 4);
                        value = value.substring(4);
                        if (tmp.equals("1010")) {
                            t = "-";
                        } else if (tmp.equals("1011")) {
                            t = ":";
                        } else if (!tmp.startsWith("11")) {
                            buf[0] = Byte.parseByte("0011" + tmp, 2);
                            t = new String(buf);
                        } else {
                            switch (tmp) {
                                case "1100":
                                    nextctrl = "UC";
                                    break;
                                case "1101":
                                    nextctrl = "UCT";
                                    break;
                                case "1110":
                                    nextctrl = "LC";
                                    break;
                                case "1111":
                                    nextctrl = "LCT";
                                    break;
                            }
                        }
                        break;
                }
                ret.append(t);
                if (nextctrl.endsWith("T") && !curctrl.equals(nextctrl)) {
                    String ctrl = nextctrl;
                    nextctrl = curctrl;
                    curctrl = ctrl;
                } else {
                    curctrl = nextctrl;
                }
            }
            while ((curctrl.equals("UC") || curctrl.equals("UCT")
                || curctrl.equals("LC") || curctrl.equals("LCT")) && value.length() > 4
                || (curctrl.equals("NU") || curctrl.equals("NUT")) && value.length() >= 4);

            return ret.toString();
        }
    }
}
