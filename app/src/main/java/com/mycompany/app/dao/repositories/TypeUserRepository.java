package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {
    Optional<TypeUser> findByType(String type);

    boolean existsByType(String type);

    void deleteByType(String type);
}