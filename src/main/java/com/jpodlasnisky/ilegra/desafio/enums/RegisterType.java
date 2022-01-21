package com.jpodlasnisky.ilegra.desafio.enums;

public enum RegisterType {

    SALESMAN("001"),
    CUSTOMER("002"),
    SALE("003");

    private final String code;

    RegisterType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
