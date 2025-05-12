package com.mycompany.app.dao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    boolean existsByPassword(String password);

    Optional<User> findByPersonDocument(Long document);

    boolean existsByPersonId(Long id);
}