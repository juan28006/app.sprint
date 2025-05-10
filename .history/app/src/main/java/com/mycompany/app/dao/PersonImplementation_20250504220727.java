/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.model.Person;

public class PersonImplementation implements PersonDao {
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void createPerson(PersonDTO personDto) throws Exception {
        Person person = Helpers.parse(personDto);
        personRepository.save(person);
        personDto.setId(person.getId());
    }

    @Override
    public boolean existsByDocument(Long document) throws Exception {
        return personRepository.existsByDocument(document);
    }

    @Override
    public PersonDTO findByDocument(Long document) throws Exception {
        Person person = personRepository.findByDocument(document);
        return Helpers.parse(person);
    }
}
