package com.mycompany.app.Controller.Request;

// PUT /api/machinery/{id} → Actualizar maquinaria

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMachineryRequest {
    private Long id;
    private MachineryDTO machineryDTO; // "Operativa", "Mantenimiento"

}
