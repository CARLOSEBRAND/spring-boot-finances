package br.pro.brand.finance.services;

import br.pro.brand.finance.models.entity.User;

public interface UserService {

    User autenticate(String email, String password);

    User salveUser(User user);

    void validateEmail(String email);
    
}
