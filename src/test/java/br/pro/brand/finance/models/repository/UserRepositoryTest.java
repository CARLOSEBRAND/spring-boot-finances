package br.pro.brand.finance.models.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.pro.brand.finance.models.entity.User;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void checkIfEmailisAlreadySaved(){
        //cenario
        User user = creatUser();
        entityManager.persist(user);

        //acão - execução
        boolean result = repository.existsByEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertTrue(result); 
    }

    @Test
    public void checkifEmailCanBeSaved(){
        //cenario
        
        //acão - execução
        boolean result = repository.existsByEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertFalse(result); 
    }

    @Test
    public void checkIfUserWasPersistedInDB() {
        //cenario
        User user = creatUser();
        
        //acão - execução
        User userSaved = repository.save(user);

        //verificação
        Assertions.assertNotNull(userSaved.getId()); 

    }

    @Test
    public void checkFindUserByEmail() {
        //cenario
        User user = creatUser();
        entityManager.persist(user);

        //acão - execução
        Optional<User> result = repository.findByEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void checkFindUserByEmailReturningNull() {
        
        //acão - execução
        Optional<User> result = repository.findByEmail("carlosebrand@gmail.com");

        //verificação
        Assertions.assertFalse(result.isPresent());
    }

    public static User creatUser() {
        return new User(null, "Carlos", "carlosebrand@gmail.com", "123");
    }
    
}
