package br.pro.brand.finance.models.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.enums.EntryCategory;
import br.pro.brand.finance.models.enums.EntryStatus;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EntryRepositoryTest {

    @Autowired
    EntryRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void checkSaveEntry() {

         Entry entry = repository.save(creatEntry());
         
         assertNotNull(entry.getId());
    }

    @Test
    public void checkDeleteEntry() {

        Entry entry = entityManager.persist(creatEntry());

        entry = entityManager.find(Entry.class, entry.getId());
        repository.delete(entry);

        Entry entryNotfound = entityManager.find(Entry.class, entry.getId());

        assertNull(entryNotfound);
    }

    @Test
    public void checkUpdateEntry() {

        Entry entry = entityManager.persist(creatEntry());

        entry.setYear(2021);
        entry.setDescription("conta de gas");
        entry.setStatus(EntryStatus.CANCELED);

        repository.save(entry);

        Entry entryUpdated = entityManager.find(Entry.class, entry.getId());

        assertEquals(entryUpdated.getYear(), 2021);
        assertEquals(entryUpdated.getDescription(), "conta de gas");
        assertEquals(entryUpdated.getStatus(), EntryStatus.CANCELED);

    }

    @Test
    public void checkFindByID() {

        Entry entry = entityManager.persist(creatEntry());

        Optional<Entry> entryFounded =  repository.findById(entry.getId());

        assertTrue(entryFounded.isPresent());

    }


    public static Entry creatEntry() {

        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Entry entry = new Entry();
        entry.setDescription("conta de luz");
        entry.setMonth(3);
        entry.setYear(2022);
        entry.setAmount(BigDecimal.valueOf(100));
        entry.setStatus(EntryStatus.PENDING);
        entry.setCategory(EntryCategory.EXPENSE);
        entry.setCreatedAt(today);

        return entry;
    }
    
}
