package br.pro.brand.finance.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.pro.brand.finance.api.dto.EntryDTO;
import br.pro.brand.finance.api.dto.UpdateStatusDTO;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.Entry;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.models.enums.EntryCategory;
import br.pro.brand.finance.models.enums.EntryStatus;
import br.pro.brand.finance.services.EntryService;
import br.pro.brand.finance.services.UserService;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    private EntryService service;
    private UserService userService;


    public EntryController(EntryService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<?> find(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam("user") Long userId) {

                Entry entryFilter = new Entry();
                entryFilter.setDescription(description);
                entryFilter.setYear(year);
                entryFilter.setMonth(month);
                
                Optional<User> user = userService.findByUserId(userId);
                if (user.isPresent()) {
                    entryFilter.setUser(user.get());
                } else {
                    return new ResponseEntity<String>(
                        "Its was not possible to make this search. User not found.", 
                        HttpStatus.BAD_REQUEST
                    );
                }

                List<Entry> entries = service.find(entryFilter);

                return new ResponseEntity<List<Entry>>(entries, HttpStatus.OK);

    }

    @RequestMapping(value = "/save", method=RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody EntryDTO dto) {
        try {
            Entry entry = convertEntry(dto);
            entry = service.save(entry);
            return new ResponseEntity<Entry>(entry, HttpStatus.CREATED);
            
        } catch (BussinessRuleException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);            
        }
	}

    @RequestMapping(value = "/{id}", method=RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable Long id,  @RequestBody EntryDTO dto) {

        return service.findByEntryId(id).map(entity -> {
            try {
                Entry entry = convertEntry(dto);
                entry.setId(entity.getId());
                service.update(entry);
                return new ResponseEntity<Entry>(entry, HttpStatus.OK);
                
            } catch (BussinessRuleException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }).orElseGet(() -> 
            new ResponseEntity<String>("Entry not found.", HttpStatus.BAD_REQUEST));

	}

    @RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id) {

        return service.findByEntryId(id).map(entity -> {
            service.delete(entity);
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

        }).orElseGet(() -> 
            new ResponseEntity<String>("Entry not found.", HttpStatus.BAD_REQUEST));

	}

    @RequestMapping(value = "/{id}/update-status", method=RequestMethod.PUT)
	public ResponseEntity<?> updateStatus(@PathVariable Long id,  @RequestBody UpdateStatusDTO dto) {

        return service.findByEntryId(id).map(entity -> {
            EntryStatus entryStatus = EntryStatus.valueOf(dto.getStatus());
            if (entryStatus == null) {
                new ResponseEntity<String>("Invalid Status.", HttpStatus.BAD_REQUEST); 
            }
            try {
                entity.setStatus(entryStatus);
                service.update(entity);
                return new ResponseEntity<Entry>(entity, HttpStatus.OK);
                
            } catch (BussinessRuleException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }).orElseGet(() -> 
            new ResponseEntity<String>("Entry not found.", HttpStatus.BAD_REQUEST));

	}


    private Entry convertEntry(EntryDTO dto) {

        Entry entry = new Entry();
        entry.setId(dto.getId());
        entry.setDescription(dto.getDescription());
        entry.setYear(dto.getYear());
        entry.setMonth(dto.getMonth());
        entry.setAmount(dto.getAmount());

        User user = userService
            .findByUserId(dto.getUser())
            .orElseThrow( () -> new BussinessRuleException("User not find with the given ID."));

        entry.setUser(user);

        if (dto.getCategory() != null) {
            entry.setCategory(EntryCategory.valueOf(dto.getCategory()));    
        }

        if (dto.getStatus() != null) {
            entry.setStatus(EntryStatus.valueOf(dto.getStatus()));
        }

        return entry;
    }

    
}
