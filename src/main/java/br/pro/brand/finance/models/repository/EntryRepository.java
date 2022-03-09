package br.pro.brand.finance.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pro.brand.finance.models.entity.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    
}
