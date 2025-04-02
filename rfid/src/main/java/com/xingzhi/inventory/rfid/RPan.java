package com.xingzhi.inventory.rfid;

import com.rfid.api.ADReaderInterface;
import com.rfid.api.GFunction;
import com.rfid.api.ISO15693Interface;
import com.rfid.def.ApiErrDefinition;

public class RPan {
    private static final RPan instance = new RPan();

    private final ADReaderInterface m_reader;
    private final ISO15693Interface Tag15693 = new ISO15693Interface();

    private RPan() {
        m_reader = new ADReaderInterface();
    }

    public static RPan getInstance() {
        return instance;
    }

    public ADReaderInterface getReader() {
        return m_reader;
    }

    public boolean doOpen(String conStr, final StringBuilder result) {
        if (!m_reader.isReaderOpen()) {
            int iRet = m_reader.RDR_Open(conStr);
            if (iRet != ApiErrDefinition.NO_ERROR) {
                result.append("open device failed, error code: ").append(iRet);
            }
        } else {
            if (!getDevInfo(result)) {
                doClose(result);
                doOpen(conStr, result);
            }
        }
        return m_reader.isReaderOpen();
    }

    private boolean doClose(final StringBuilder result) {
        int iRet = m_reader.RDR_Close();
        if (iRet != ApiErrDefinition.NO_ERROR) {
            result.append("close the device failed, error code: ").append(iRet);
        }
        return !m_reader.isReaderOpen();
    }

    private boolean getDevInfo(final StringBuilder result) {
        StringBuffer buffer = new StringBuffer();
        int iRet = m_reader.RDR_GetReaderInfor(buffer);
        if (iRet == ApiErrDefinition.NO_ERROR) {
            result.append(buffer);
            return true;
        } else {
            result.append("get device info failed, error code: ").append(iRet);
            return false;
        }
    }

    public boolean setSystemTime(int year, int month, int day, int hour, int min, int sec, final StringBuilder result) {
        int iRet = m_reader.RPAN_SetTime(year, month, day, hour, min, sec);
        if (iRet == ApiErrDefinition.NO_ERROR) {
            return true;
        } else {
            result.append("set device time failed, error code: ").append(iRet);
            return false;
        }
    }

    public boolean cleanBuffer(final StringBuilder result) {
        int iRet = m_reader.RDR_BuffMode_FlashEmpty();
        if (iRet == ApiErrDefinition.NO_ERROR) {
            return true;
        } else {
            result.append("clean buffer failed, error code: ").append(iRet);
            return false;
        }
    }

    public boolean loadFactoryDefault(final StringBuilder result) {
        int iRet = m_reader.RDR_LoadFactoryDefault();
        if (iRet == ApiErrDefinition.NO_ERROR) {
            result.append("The operation is successful.");
            return true;
        } else {
            result.append("The operation is failure. error code:").append(iRet);
            return false;
        }
    }

    public boolean clearRecords(final StringBuilder result) {
        int iRet = m_reader.RDR_BuffMode_ClearRecords();
        if (iRet == ApiErrDefinition.NO_ERROR) {
            result.append("Clear record successfully!");
            return true;
        } else {
            result.append("Failure to clear record! error code:").append(iRet);
            return false;
        }
    }

    public boolean doEnableEAS() {
        int iRet = Tag15693.NXPICODESLI_EableEAS();
        return iRet == ApiErrDefinition.NO_ERROR;
    }

    public boolean doDisableEAS() {
        int iRet = Tag15693.NXPICODESLI_DisableEAS();
        return iRet == ApiErrDefinition.NO_ERROR;
    }

    public boolean doWriteAIF(String afiStr) {
        byte[] afi = GFunction.decodeHex(afiStr);
        if (afi == null) {
            return false;
        }
        int iRet = Tag15693.ISO15693_WriteAFI(afi[0]);
        return iRet == ApiErrDefinition.NO_ERROR;
    }

    public Object initInvenParamSpecList() {
        Boolean bMathAFI = false;
        byte mAFIVal = 0;
        Object hInvenParamSpecList = ADReaderInterface.RDR_CreateInvenParamSpecList();
        ISO15693Interface.ISO15693_CreateInvenParam(hInvenParamSpecList, (byte) 0,
            bMathAFI, mAFIVal, (byte) 0);
        return hInvenParamSpecList;
    }

    /**
     * 设备是否已经打开
     */
    public boolean isOpened() {
        return m_reader.isReaderOpen();
    }
}

