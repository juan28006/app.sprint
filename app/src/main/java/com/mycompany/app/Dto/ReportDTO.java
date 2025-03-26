/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Dto;

/**
 *
 * @author Farley
 */
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mycompany.app.Dto.UserDTO;

@Getter
@Setter
@NoArgsConstructor

public class ReportDTO {
    private Long id;
    private String type; // "Uso", "Estado", "Mantenimiento"
    private Date generationDate;
    private UserDTO user;

}