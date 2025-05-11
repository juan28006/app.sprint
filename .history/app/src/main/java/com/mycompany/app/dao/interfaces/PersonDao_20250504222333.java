/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dao.interfaces;

import com.mycompany.app.dto.PersonDTO;

public interface PersonDao {
    void createPerson(PersonDTO personDto) throws Exception;

    boolean existsByDocument(Long document) throws Exception;

    PersonDTO findByDocument(Long document) throws Exception;
}