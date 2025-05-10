/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.dto;

/**
 *
 * @author Farley
 */
import java.util.Date;

import com.mycompany.app.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ReportDTO {
    private Long id;
    private String type; // "Uso", "Estado", "Mantenimiento"
    private Date generationDate;
    private UserDTO user;

}