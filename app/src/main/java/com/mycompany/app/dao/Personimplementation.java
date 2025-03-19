package com.mycompany.app.dao;

import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.model.user;
import com.mycompany.app.dao.repositories.PersonRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter

public class Personimplementation implements ReservationDao {

    @Autowired
    PersonRepository personRepository;

    @Override
    public void createPerson(ReportDTO personDto) throws Exception {
        user person = Helpers.parse(personDto);
        personRepository.save(person);
        personDto.setId(person.getId());
    }

    @Override
    public boolean existsByDocument(ReportDTO personDto) throws Exception {
        return personRepository.existsByDocument(Helpers.parse(personDto).getDocument());

    }

    @Override
    public void deletePerson(ReportDTO personDto) throws Exception {
        user person = Helpers.parse(personDto);
        personRepository.delete(person);

    }

    @Override
    public ReportDTO findByDocument(ReportDTO personDto) throws Exception {
        user person = personRepository.findByDocument(personDto.getDocument());
        return Helpers.parse(person);

    }

}
