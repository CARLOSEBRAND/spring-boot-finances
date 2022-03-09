package br.pro.brand.finance.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.enums.EntryCategory;
import br.pro.brand.finance.models.enums.EntryStatus;
import br.pro.brand.finance.models.repository.EntryRepository;

@Service
public class EntryServiceImp implements EntryService {

       
    private EntryRepository repository;


    public EntryServiceImp(EntryRepository repository) {
        this.repository = repository;
    }


    @Override
    @Transactional
    public Entry save(Entry entry) {
        validate(entry);
        entry.setStatus(EntryStatus.PENDING);
        return repository.save(entry);
    }

    @Override
    @Transactional
    public Entry update(Entry entry) {
        Objects.requireNonNull(entry.getId());
        validate(entry);
        return repository.save(entry);
    }

    @Override
    @Transactional
    public void delete(Entry entry) {
        Objects.requireNonNull(entry.getId());
        repository.delete(entry);        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entry> find(Entry entryFilter) {
        
        Example<Entry> ex = Example.of(entryFilter, 
            ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING)
        );

        return repository.findAll(ex);
    }

    @Override
    public void updateStatus(Entry entry, EntryStatus status) {
        entry.setStatus(status);
        update(entry);
        
    }


    @Override
    public void validate(Entry entry) {
        
        if (entry.getDescription() == null || entry.getDescription().trim().equals("")) {
            throw new BussinessRuleException("Enter with a valid description");
        }

        if (entry.getMonth() == null || entry.getMonth() < 1 || entry.getMonth() > 12) {
            throw new BussinessRuleException("Enter with a valid month");
        }

        if (entry.getYear() == null || entry.getYear().toString().length() != 4) {
            throw new BussinessRuleException("Enter with a valid year");
        }

        if (entry.getUser() == null || entry.getUser().getId() == null) {
            throw new BussinessRuleException("Enter with a valid user");
        }

        if (entry.getAmount() == null || !(entry.getAmount() instanceof BigDecimal)) {
            throw new BussinessRuleException("Enter with a valid amount");
        }

        if (entry.getCategory() == null) {
            throw new BussinessRuleException("Enter with a valid category");
        }
    }


    @Override
    public Optional<Entry> findByEntryId(Long id) {
        return repository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceForUser(Long id) {
        BigDecimal incomes = repository.getBalanceForCategoryAndUser(id, EntryCategory.INCOME);
        BigDecimal expenses = repository.getBalanceForCategoryAndUser(id, EntryCategory.EXPENSE);

        if (incomes == null) {
            incomes = BigDecimal.ZERO;
        }

        if (expenses == null) {
            expenses = BigDecimal.ZERO;
        }

        return incomes.subtract(expenses);
    }

}
