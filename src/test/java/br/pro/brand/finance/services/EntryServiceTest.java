package br.pro.brand.finance.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.models.enums.EntryCategory;
import br.pro.brand.finance.models.enums.EntryStatus;
import br.pro.brand.finance.models.repository.EntryRepository;
import br.pro.brand.finance.models.repository.EntryRepositoryTest;
import br.pro.brand.finance.models.repository.UserRepositoryTest;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EntryServiceTest {

    @SpyBean
    EntryServiceImp service;

    @MockBean
    EntryRepository repository;

    private Example any;

    @Test
    public void checkIfEntryWasSaved() {

        // cenário
        Entry entryToSave = EntryRepositoryTest.creatEntry();
        Mockito.doNothing().when(service).validate(entryToSave);

        Entry entrySaved = EntryRepositoryTest.creatEntry();
        entrySaved.setId(1l);
        entrySaved.setStatus(EntryStatus.PENDING);
        Mockito.when(repository.save(entryToSave)).thenReturn(entrySaved);

        // execução
        Entry entry = service.save(entryToSave);

        // verificação
        assertEquals(entry.getId(), entrySaved.getId());
        assertEquals(entry.getStatus(), EntryStatus.PENDING);
    }

    @Test
    public void checkIfEntryWasNotValidatedWhenSaved() {

        // cenário
        Entry entryToSave = EntryRepositoryTest.creatEntry();
        Mockito.doThrow(BussinessRuleException.class).when(service).validate(entryToSave);

        // execução e verificação
        assertThrows(BussinessRuleException.class, () -> service.save(entryToSave));
        Mockito.verify(repository, Mockito.never()).save(entryToSave);
    }

    @Test
    public void checkIfEntryWasUpdated() {

        // cenário
        Entry entrySaved = EntryRepositoryTest.creatEntry();
        entrySaved.setId(1l);
        entrySaved.setStatus(EntryStatus.PENDING);

        Mockito.doNothing().when(service).validate(entrySaved);
        Mockito.when(repository.save(entrySaved)).thenReturn(entrySaved);

        // execução
        service.update(entrySaved);

        // verificação
        Mockito.verify(repository, Mockito.times(1)).save(entrySaved);
    }

    @Test
    public void checkIfThrowExceptionForUpdateWithoutEntry() {

        // cenário
        Entry entryToSave = EntryRepositoryTest.creatEntry();

        // execução e verificação
        assertThrows(NullPointerException.class, () -> service.update(entryToSave));
        Mockito.verify(repository, Mockito.never()).save(entryToSave);
    }

    @Test
    public void checkDeleteEntry() {

        // cenário
        Entry entry = EntryRepositoryTest.creatEntry();
        entry.setId(1l);

        // execução
        service.delete(entry);

        // verificação
        Mockito.verify(repository).delete(entry);
    }

    @Test
    public void checkIfThrowExceptionForDeleteWithoutEntry() {

        // cenário
        Entry entry = EntryRepositoryTest.creatEntry();

        // execução
        assertThrows(NullPointerException.class, () -> service.delete(entry));

        // verificação
        Mockito.verify(repository, Mockito.never()).delete(entry);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void checkEntryFilter() {

        // cenário
        Entry entry = EntryRepositoryTest.creatEntry();
        entry.setId(1l);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(entry);

        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(entries);

        // execução
        List<Entry> result = service.find(entry);

        // verificação
        assertFalse(result.isEmpty());
        assertTrue(result.size() == 1);
        assertTrue(result.contains(entry));
    }

    @Test
    public void checkUpdateStatusEntry() {

        // cenário
        Entry entry = EntryRepositoryTest.creatEntry();
        entry.setId(1l);
        entry.setStatus(EntryStatus.PENDING);

        EntryStatus newStatus = EntryStatus.DONE;
        Mockito.doReturn(entry).when(service).update(entry);

        // execução
        service.updateStatus(entry, newStatus);

        // verificação
        assertEquals(entry.getStatus(), newStatus);
        Mockito.verify(service).update(entry);
    }

    @Test
    public void checkReturnEntryForId() {

        // cenário
        Long id = 1l;
        Entry entry = EntryRepositoryTest.creatEntry();
        entry.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entry));

        // execução
        Optional<Entry> result = service.findByEntryId(id);

        // verificação
        assertTrue(result.isPresent());
    }

    @Test
    public void checkReturnEntryEmpty() {

        // cenário
        Long id = 1l;
        Entry entry = EntryRepositoryTest.creatEntry();
        entry.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // execução
        Optional<Entry> result = service.findByEntryId(id);

        // verificação
        assertFalse(result.isPresent());
    }

    @Test
    public void checkValidate() {

        Entry entry = new Entry();

        Throwable exception1 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid description.", exception1.getMessage());

        entry.setDescription("Conta de gas");

        // -------------------------------------------------

        Throwable exception2 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid month.", exception2.getMessage());

        entry.setMonth(3);

        // -------------------------------------------------

        Throwable exception3 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid year.", exception3.getMessage());

        entry.setYear(2022);

        // -------------------------------------------------

        Throwable exception4 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid user.", exception4.getMessage());

        User user = UserRepositoryTest.creatUser(); 
        entry.setUser(user);
        entry.getUser().setId(1l);

        // -------------------------------------------------

        Throwable exception5 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid amount.", exception5.getMessage());

        entry.setAmount(BigDecimal.TEN);

        // -------------------------------------------------

        Throwable exception6 = assertThrows(BussinessRuleException.class, () -> {
            service.validate(entry);
        });
        assertEquals("Enter with a valid category.", exception6.getMessage());

        entry.setCategory(EntryCategory.INCOME);

        // -------------------------------------------------

    }

}
