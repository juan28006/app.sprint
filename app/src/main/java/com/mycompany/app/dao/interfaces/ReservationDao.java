package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.ReportDTO;

public interface ReservationDao {

    public boolean existsByDocument(ReportDTO personDto) throws Exception;

    public void createPerson(ReportDTO personDTo) throws Exception;

    public void deletePerson(ReportDTO personDto) throws Exception;

    public ReportDTO findByDocument(ReportDTO personDto) throws Exception;

}
