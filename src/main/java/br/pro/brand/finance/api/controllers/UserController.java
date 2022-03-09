package br.pro.brand.finance.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import br.pro.brand.finance.api.dto.UserDTO;
import br.pro.brand.finance.exceptions.AutenticationException;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @RequestMapping(value = "/save", method=RequestMethod.POST)
	public ResponseEntity<String> save(@RequestBody UserDTO objDTO) {

        User user = new User(null, objDTO.getName(), objDTO.getEmail(), objDTO.getPassword());
		
        try {
            user = service.salveUser(user);
            return ResponseEntity.ok().body("User Saved with ID: " + user.getId());
            
        } catch (BussinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}

    @RequestMapping(value = "/auth", method=RequestMethod.POST)
	public ResponseEntity<String> auth(@RequestBody UserDTO objDTO) {

        try {
            User userAuth = service.autenticate(objDTO.getEmail(), objDTO.getPassword());
            return ResponseEntity.ok().body("User Authenticated" + userAuth.toString());
            
        } catch (AutenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}
}
