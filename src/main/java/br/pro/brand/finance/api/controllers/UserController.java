package br.pro.brand.finance.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import br.pro.brand.finance.api.dto.UserDTO;
import br.pro.brand.finance.exceptions.AutenticationException;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.services.EntryService;
import br.pro.brand.finance.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private UserService service;
    private EntryService entryService;

    public UserController(UserService service, EntryService entryService){
        this.service = service;
        this.entryService = entryService;

    }

    @RequestMapping(value = "/save", method=RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody UserDTO objDTO) {

        User user = new User(null, objDTO.getName(), objDTO.getEmail(), objDTO.getPassword());
		
        try {
            user = service.salveUser(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
            
        } catch (BussinessRuleException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
	}

    @RequestMapping(value = "/auth", method=RequestMethod.POST)
	public ResponseEntity<?> auth(@RequestBody UserDTO objDTO) {

        try {
            User userAuth = service.autenticate(objDTO.getEmail(), objDTO.getPassword());
            return new ResponseEntity<User>(userAuth, HttpStatus.OK);
            
        } catch (AutenticationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);             
        }
	}


    @RequestMapping(value = "/{id}/balance", method=RequestMethod.GET)
	public ResponseEntity<?> getBalance(@PathVariable Long id) {

        Optional<User> user =  service.findByUserId(id);

        if (!user.isPresent()) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND); 
        }

        BigDecimal balance = entryService.getBalanceForUser(id);

        return new ResponseEntity<BigDecimal>(balance, HttpStatus.OK);
	}


}
