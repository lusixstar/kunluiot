package com.kunluiot.sdk.thirdlib.ws;

import java.io.Serializable;

public class CommonResponseEntity  implements Serializable {

    /**
     * msgId : 1
     * params :
     * action : appLoginResp
     * code : 200
     * desc : success
     */

    private int msgId = -1;
    private String action;
    private int code;
    private String desc;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
