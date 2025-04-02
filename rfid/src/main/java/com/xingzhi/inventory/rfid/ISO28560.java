package com.xingzhi.inventory.rfid;

import java.util.ArrayList;

public class ISO28560 {
    public static TagUserData ISO15960_2Decoding(byte[] byteBuffer, final StringBuilder result) {
        TagUserData tagUserData = new TagUserData();
        try {
            String BlockBuffer = DigitalTrans.bytes2BinaryStr(byteBuffer);
            for (int i = 0; i < BlockBuffer.length() - 8; ) {
                char isShift = BlockBuffer.charAt(i);
                String encoding = BlockBuffer.substring(i + 1, i + 4);
                int OID = Integer.parseInt(BlockBuffer.substring(i + 4, i + 8), 2);
                i = i + 8;
                if (OID == 0) {
                    break;
                }
                if (OID == 15) {
                    OID += Integer.parseInt(BlockBuffer.substring(i, i + 8), 2);
                    i = i + 8;
                }
                int shiftLength = 0;
                if (isShift == '1') {
                    shiftLength = Integer.parseInt(BlockBuffer.substring(i, i + 8), 2);
                    i = i + 8;
                }
                int dataLength = Integer.parseInt(BlockBuffer.substring(i, i + 8), 2);
                i = i + 8;

                StringBuffer dataBlock;
                dataBlock = new StringBuffer();
                int l = 0;
                while (l++ < dataLength && (i + 8 <= BlockBuffer.length())) {
                    dataBlock.append(BlockBuffer, i, i + 8);
                    i = i + 8;
                }
                i = i + shiftLength * 8;

                StringBuilder data = new StringBuilder();
                String strDataBlock = dataBlock.toString();
                if (OID == 2) {
                    int o = 3;
                    for (int j = 0; j < strDataBlock.length(); j++) {
                        char c = strDataBlock.charAt(j);
                        if (c == '1') {
                            data.append(o).append(" ");
                        }
                        o++;
                    }
                } else if (OID == 5) {
                    data = new StringBuilder(String.format("%x", Long.parseLong(dataBlock.toString(), 2)));
                } else switch (encoding) {
                    case "000": //基于应用程序
                        data = new StringBuilder(ISILUtil.Decode(dataBlock.toString()));
                        break;
                    case "001": //整形
                        data = new StringBuilder(String.valueOf(Long.parseLong(dataBlock.toString(), 2)));
                        break;
                    case "010": //数字
                        //data = new StringBuilder(String.format("%x", Long.parseLong(dataBlock.toString(), 2)));
                        data = new StringBuilder(DigitalTrans.BinStringToOctBinary(dataBlock.toString()));
                        if (data.toString().endsWith("15")) {
                            data = new StringBuilder(data.substring(0, data.length() - 2));
                        }
                        break;
                    case "011": //5位码，大写字母
                        ArrayList<String> tempBlock5 = new ArrayList<>();
                        String dataBlockString5 = dataBlock.toString();
                        while (dataBlockString5.length() >= 5) {
                            if (dataBlockString5.equals("00000")) {
                                break;
                            }
                            String b5 = "010" + dataBlockString5.substring(0, 5);
                            tempBlock5.add(String.format("%x", Long.parseLong(b5, 2)));
                            dataBlockString5 = dataBlockString5.substring(5);
                        }
                        byte[] buff5 = new byte[tempBlock5.size()];
                        int index5 = 0;
                        for (int j = 0; j < tempBlock5.size(); j++) {
                            byte hex = Byte.parseByte(tempBlock5.get(j), 16);
                            if (hex > 0x40 && hex < 0x60) {
                                buff5[index5++] = hex;
                            }
                        }
                        data = new StringBuilder(new String(buff5, 0, index5));
                        break;
                    case "100": //6位码，大写字母，数字等
                        ArrayList<String> tempBlock6 = new ArrayList<>();
                        String dataBlockString6 = dataBlock.toString();
                        while (dataBlockString6.length() >= 6) {
                            if (dataBlockString6.equals("100000")) {
                                break;
                            }
                            String b6 = dataBlockString6.substring(0, 6);
                            if (b6.startsWith("1")) {
                                b6 = "00" + b6;
                            } else {
                                b6 = "01" + b6;
                            }

                            tempBlock6.add(String.format("%x", Long.parseLong(b6, 2)));
                            dataBlockString6 = dataBlockString6.substring(6);
                        }
                        byte[] buff6 = new byte[tempBlock6.size()];
                        int index6 = 0;
                        for (int j = 0; j < tempBlock6.size(); j++) {
                            buff6[index6++] = Byte.parseByte(tempBlock6.get(j), 16);
                        }
                        data = new StringBuilder(new String(buff6));
                        break;
                    case "101": //7位码，US ASCII
                        ArrayList<String> tempBlock7 = new ArrayList<>();
                        String dataBlockString7 = dataBlock.toString();
                        while (dataBlockString7.length() >= 7) {
                            if (dataBlockString7.equals("1111111")) {
                                break;
                            }
                            String b7 = "0" + dataBlockString7.substring(0, 7);

                            tempBlock7.add(String.format("%x", Long.parseLong(b7, 2)));
                            dataBlockString7 = dataBlockString7.substring(7);
                        }
                        byte[] buff7 = new byte[tempBlock7.size()];
                        int index7 = 0;
                        for (int j = 0; j < tempBlock7.size(); j++) {
                            buff7[index7++] = Byte.parseByte(tempBlock7.get(j), 16);
                        }
                        data = new StringBuilder(new String(buff7));
                        break;
                    case "110": //8位码字符串
                        ArrayList<String> tempBlock8 = new ArrayList<>();
                        String dataBlockString8 = dataBlock.toString();
                        while (dataBlockString8.length() >= 8) {
                            String b8 = dataBlockString8.substring(0, 8);

                            tempBlock8.add(String.format("%x", Long.parseLong(b8, 2)));
                            dataBlockString8 = dataBlockString8.substring(8);
                        }
                        byte[] buff8 = new byte[tempBlock8.size()];
                        int index8 = 0;
                        for (int j = 0; j < tempBlock8.size(); j++) {
                            buff8[index8++] = Byte.parseByte(tempBlock8.get(j), 16);
                        }
                        data = new StringBuilder(new String(buff8));
                        break;
                }
                switch (OID) {
                    case 1:
                        tagUserData.setPrimaryItemIdentifier(data.toString());
                        break;
                    case 2:
                        tagUserData.setContentParameter(data.toString());
                        break;
                    case 3:
                        tagUserData.setOwnerInstitution(data.toString());
                        break;
                    case 4:
                        tagUserData.setSetInformation(data.toString());
                        break;
                    case 5:
                        tagUserData.setTypeOfUsage(data.toString());
                        break;
                    case 6:
                        tagUserData.setShelfLocation(data.toString());
                        break;
                    case 7:
                        tagUserData.setOnixMediaformat(data.toString());
                        break;
                    case 8:
                        tagUserData.setMarcMediaformat(data.toString());
                        break;
                    case 9:
                        tagUserData.setSupplierIdentifier(data.toString());
                        break;
                    case 10:
                        tagUserData.setOrderNumber(data.toString());
                        break;
                    case 11:
                        tagUserData.setIllBorrowingInstitution(data.toString());
                        break;
                    case 12:
                        tagUserData.setIllBorrowingTransactionNumber(data.toString());
                        break;
                    case 13:
                        tagUserData.setGs1ProductIdentifier(data.toString());
                        break;
                    case 14:
                        tagUserData.setLocalDataA(data.toString());
                        break;
                    case 15:
                        tagUserData.setLocalDataB(data.toString());
                        break;
                    case 16:
                        tagUserData.setLocalDataC(data.toString());
                        break;
                    case 17:
                        tagUserData.setTitle(data.toString());
                        break;
                    case 18:
                        tagUserData.setProductIdentifier(data.toString());
                        break;
                    case 19:
                        tagUserData.setMediaFormat(data.toString());
                        break;
                    case 20:
                        tagUserData.setSupplyChainStage(data.toString());
                        break;
                    case 21:
                        tagUserData.setSupplierInvoiceNumber(data.toString());
                        break;
                    case 22:
                        tagUserData.setAlternativeItemIdentifier(data.toString());
                        break;
                    case 23:
                        tagUserData.setAlternativeOwnerLibraryIdentifier(data.toString());
                        break;
                    case 24:
                        tagUserData.setSubsidiaryOfAnOwnerLibrary(data.toString());
                        break;
                    case 25:
                        tagUserData.setAlternativeILLBorrowingInstitution(data.toString());
                        break;
                }
            }
        } catch (RuntimeException ex) {
            result.append(ex.getMessage());
        }
        return tagUserData;
    }
}
