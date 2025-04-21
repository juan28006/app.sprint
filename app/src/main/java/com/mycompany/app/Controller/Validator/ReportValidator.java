/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.UserDTO;

import org.springframework.stereotype.Component;

@Component
public class ReportValidator {

    public void validateReport(ReportDTO reportDTO) throws Exception {
        if (reportDTO.getType() == null || reportDTO.getType().isEmpty()) {
            throw new Exception("El tipo de informe no puede estar vacío");
        }
        if (reportDTO.getGenerationDate() == null) {
            throw new Exception("La fecha de generación no puede estar vacía");
        }
        if (reportDTO.getUser() == null || reportDTO.getUser().getId() == null) {
            throw new Exception("El usuario asociado al informe no puede estar vacío");
        }
    }
}
