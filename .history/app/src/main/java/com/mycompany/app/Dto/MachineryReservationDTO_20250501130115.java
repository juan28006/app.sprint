package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MachineryReservationDTO {
    private Long id;
    private MachineryDTO machinery;
    private ReservationDTO reservation;

}