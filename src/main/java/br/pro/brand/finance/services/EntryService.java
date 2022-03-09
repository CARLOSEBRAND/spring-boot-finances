package br.pro.brand.finance.services;

import java.util.List;

import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.enums.EntryStatus;

public interface EntryService {

    Entry save(Entry entry);

    Entry update(Entry entry);

    void delete(Entry entry);

    List<Entry> find(Entry entryFilter);

    void updateStatus(Entry entry, EntryStatus status);

    void validate(Entry entry);
    
}
