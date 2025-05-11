/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
@Getter
@Setter
@NoArgsConstructor
// POST /api/reports → Generar reporte
public class CreateReportRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
