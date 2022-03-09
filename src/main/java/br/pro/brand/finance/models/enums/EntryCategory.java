package br.pro.brand.finance.models.enums;

public enum EntryCategory {

    INCOME("income"), EXPENSE("expense");

    private String code;

    private EntryCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
}
