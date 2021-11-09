package com.kunluiot.sdk.enums;

public enum RequestEnum {

    SUCCESS(200), FAILURE(404);

    private int code;

    private RequestEnum(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public static RequestEnum code2Enum(int code) {

        switch (code) {
            case 200:
                return SUCCESS;
            case 404:
                return FAILURE;
        }
        return null;
    }
}
