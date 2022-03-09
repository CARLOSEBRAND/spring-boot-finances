package br.pro.brand.finance.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.pro.brand.finance.exceptions.AutenticationException;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.models.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @SpyBean
    UserServiceImp service;

    @MockBean
    UserRepository repository;    

    @Test
    public void checkIfUserWasSaved() {

        //cenário
        Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
        User user = new User(1l, "Carlos", "carlosebrand@gmail.com", "123");
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);

        //acão
        User userSaved = service.salveUser(new User());
        Executable error = () -> service.salveUser(new User());

        //verificação
        Assertions.assertDoesNotThrow(error);
        Assertions.assertNotNull(userSaved);
        Assertions.assertEquals(1, userSaved.getId());
        Assertions.assertEquals("Carlos", userSaved.getName());
        Assertions.assertEquals("carlosebrand@gmail.com", userSaved.getEmail());
        Assertions.assertEquals("123", userSaved.getPassword());
        
    }

    @Test
    public void checkIfUserWasNotSaved() {

        //cenário
        String email = "carlosebrand@gmail.com";
        User user = new User(null, null, email, null);
        Mockito.doThrow(BussinessRuleException.class).when(service).validateEmail(email);

        //acão - execução
        Executable error = () -> service.salveUser(user);

        //verificação
        Mockito.verify(repository, Mockito.never()).save(user);
        Assertions.assertThrows(BussinessRuleException.class, error);
    }

    @Test
    public void checkIfUserWasAuthenticated() {

        //cenário
        String email = "carlosebrand@gmail.com";
        String password = "123";

        User user = new User(null, "Carlos", "carlosebrand@gmail.com", "123");
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        //acão
        Executable result = () -> service.autenticate(email, password);

        //verificação
        Assertions.assertDoesNotThrow(result);
    }

    @Test
    public void checkAuthenticationUserWithAnInValidEmail() {

        //cenário mock com um email fake
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //acão testa se este email está na base (não estará)
        Executable result = () -> service.autenticate("carlosebrand@gmail.com", "123");

        String message = "User not found.";
        Throwable expectedException =  Assertions.assertThrows(
                AutenticationException.class,
                result
        );

        //verificação deve retornar erro
        Assertions.assertThrows(AutenticationException.class, result);
        Assertions.assertEquals(message, expectedException.getMessage());
    }

    @Test
    public void checkAuthenticationUserWithAnInvalidPassword() {

        //cenário
        String email = "carlosebrand@gmail.com";
        String password = "123";
        User user = new User(null, "Carlos", email, password);
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        //acão
        Executable result = () -> service.autenticate(email, "456");
        String message = "Invalid Password.";
        Throwable expectedException =  Assertions.assertThrows(
                AutenticationException.class,
                result
        );

        //verificação
        Assertions.assertThrows(AutenticationException.class, result);
        Assertions.assertEquals(message, expectedException.getMessage());
    }

    @Test
    public void checkValidEmail(){
        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //acão - execução
        Executable error = () -> service.validateEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertDoesNotThrow(error);
        
    }

    @Test
    public void checkNotValidEmail(){
        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //acão - execução
        Executable error = () -> service.validateEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertThrows(BussinessRuleException.class, error);
    }
    
    
    
}
