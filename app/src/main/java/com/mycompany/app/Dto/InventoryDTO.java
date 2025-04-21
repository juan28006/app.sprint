package com.mycompany.app.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mycompany.app.Dto.UserDTO;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InventoryDTO {
    private Long id; // ID del inventario
    private String name; // Nombre del inventario (si es necesario)
    private String status; // Estado del inventario: "Operativa", "En Mantenimiento", "Dañada"
    private Date entryDate; // Fecha de ingreso del inventario
    private UserDTO user; // Objeto UserDTO en lugar de un Long userId
    private Integer quantity;
    private List<MachineryDTO> machineries; // Añade esta línea

}