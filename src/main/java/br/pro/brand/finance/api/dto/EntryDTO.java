package br.pro.brand.finance.api.dto;

import java.math.BigDecimal;

public class EntryDTO {

    private Long id;
    private String description;
    private Integer year;
    private Integer month;
    private BigDecimal amount;
    private Long user;
    private String category;
    private String status;


    public EntryDTO() {
    }

    public EntryDTO(Long id, String description, Integer year, Integer month, BigDecimal amount, Long user, String category, String status) {
        this.id = id;
        this.description = description;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.user = user;
        this.category = category;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUser() {
        return this.user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EntryDTO id(Long id) {
        setId(id);
        return this;
    }

    public EntryDTO description(String description) {
        setDescription(description);
        return this;
    }

    public EntryDTO year(Integer year) {
        setYear(year);
        return this;
    }

    public EntryDTO month(Integer month) {
        setMonth(month);
        return this;
    }

    public EntryDTO amount(BigDecimal amount) {
        setAmount(amount);
        return this;
    }

    public EntryDTO user(Long user) {
        setUser(user);
        return this;
    }

    public EntryDTO category(String category) {
        setCategory(category);
        return this;
    }

    public EntryDTO status(String status) {
        setStatus(status);
        return this;
    }
    
}
