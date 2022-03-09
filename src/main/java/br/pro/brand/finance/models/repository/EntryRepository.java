package br.pro.brand.finance.models.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.enums.EntryCategory;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(value = "select sum(e.amount) from Entry e join e.user u "
        + " where u.id = :userId and e.category = :category group by u ")
    BigDecimal getBalanceForCategoryAndUser(
            @Param("userId") Long userId, 
            @Param("category") EntryCategory category
    );
    
}
