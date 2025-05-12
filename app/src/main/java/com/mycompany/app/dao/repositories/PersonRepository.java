/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.app.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByDocument(Long document);

    Person findByDocument(Long document);
}
