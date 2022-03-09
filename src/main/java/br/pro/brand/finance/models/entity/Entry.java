package br.pro.brand.finance.models.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.pro.brand.finance.models.enums.EntryCategory;
import br.pro.brand.finance.models.enums.EntryStatus;

@Entity
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer year;
    private Integer month;
    private BigDecimal amount;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    @Enumerated(value = EnumType.STRING)
    private EntryStatus status;

    @Enumerated(value = EnumType.STRING)
    private EntryCategory category;
        
    
    public Entry() {
    }

    public Entry(Long id, String description, Integer year, Integer month, BigDecimal amount, Date createdAt, User user, EntryStatus status, EntryCategory category) {
        super();
        this.id = id;
        this.description = description;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.createdAt = createdAt;
        this.user = user;
        this.status = status;
        this.category = category;
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

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EntryStatus getStatus() {
        return this.status;
    }

    public void setStatus(EntryStatus status) {
        this.status = status;
    }

    public EntryCategory getCategory() {
        return this.category;
    }

    public void setCategory(EntryCategory category) {
        this.category = category;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", description='" + getDescription() + "'" +
            ", year='" + getYear() + "'" +
            ", month='" + getMonth() + "'" +
            ", amount='" + getAmount() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", user='" + getUser() + "'" +
            ", status='" + getStatus() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
    
}
