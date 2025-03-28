package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String username);

    User findByName(String username);
}