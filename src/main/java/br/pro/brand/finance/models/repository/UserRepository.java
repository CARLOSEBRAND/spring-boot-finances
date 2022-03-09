package br.pro.brand.finance.models.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pro.brand.finance.models.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    //findByEmail = Query methods (spring gera o sql em tempo de execução)
    //nesse exemplo retorna todos os dados do usuario no objeto
    //Optional<User> findByEmail(String email);

    //aqui verifica se existe ou não e retorn true ou false
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
