package com.alconn.copang.shipment;

public enum LogisticCode {
    HYUNDAI("롯데택배"),
    KGB("로젠택배"),
    EPOST("우체국"),
    HANJIN("한진택배"),
    CJGLS("CJ대한통운");

    private String companyName;

    LogisticCode(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
