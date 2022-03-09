package br.pro.brand.finance.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pro.brand.finance.exceptions.AutenticationException;
import br.pro.brand.finance.exceptions.BussinessRuleException;
import br.pro.brand.finance.models.entity.User;
import br.pro.brand.finance.models.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

    private UserRepository repository;

    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User autenticate(String email, String password) {
        Optional<User> user = repository.findByEmail(email);
        if (!user.isPresent()) {
            throw new AutenticationException("User not found.");
        }
        if (!user.get().getPassword().equals(password)) {
            throw new AutenticationException("Invalid Password.");
        }
        return user.get();
    }

    @Override
    @Transactional
    public User salveUser(User user) {
        validateEmail(user.getEmail());
        return repository.save(user);
    }

    @Override
    public void validateEmail(String email) {
        boolean hasEmail = repository.existsByEmail(email);
        if (hasEmail) {
            throw new BussinessRuleException("There is already a user registered with this email");
        }
        
    }
    
}
