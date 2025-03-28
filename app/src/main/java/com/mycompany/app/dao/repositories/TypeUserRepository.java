package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {

    // Buscar un TypeUser por su tipo
    Optional<TypeUser> findByType(String type);

    // Verificar si existe un TypeUser por su tipo
    boolean existsByType(String type);

    // Eliminar un TypeUser por su tipo
    void deleteByType(String type);
}