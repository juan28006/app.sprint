package com.mycompany.app.Dto;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.ReservationDTO;
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