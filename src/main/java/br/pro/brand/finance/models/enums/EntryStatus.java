package br.pro.brand.finance.models.enums;

public enum EntryStatus {

    PENDING("pending"), CANCELED("canceled"), DONE("done");

    private String code;

    private EntryStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
}
