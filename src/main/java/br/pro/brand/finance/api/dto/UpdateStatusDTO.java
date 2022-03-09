package br.pro.brand.finance.api.dto;

public class UpdateStatusDTO {

    private String status;


    public UpdateStatusDTO() {
    }

    public UpdateStatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdateStatusDTO status(String status) {
        setStatus(status);
        return this;
    }
    
}
