/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.PersonDao;
import com.mycompany.app.dao.repositories.PersonRepository;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.model.Person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
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

    @Override
    public void deletePerson(PersonDTO personDto) throws Exception {
        if (personDto == null || personDto.getId() == null) {
            throw new Exception("El DTO de persona o su ID no pueden ser nulos");
        }

        // Convertir DTO a entidad
        Person person = Helpers.parse(personDto);

        // Verificar si la persona existe antes de eliminar
        if (!personRepository.existsById(person.getId())) {
            throw new Exception("No se encontró persona con ID: " + personDto.getId());
        }

        personRepository.delete(person);
    }
}
