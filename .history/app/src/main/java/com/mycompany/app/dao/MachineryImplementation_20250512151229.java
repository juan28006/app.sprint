package com.mycompany.app.dao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.dao.repositories.MachineryRepository;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.model.Inventory;
import com.mycompany.app.model.Machinery;
import com.mycompany.app.model.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@NoArgsConstructor
@Setter
@Getter
public class MachineryImplementation implements MachineryDao {

    @Autowired
    private MachineryRepository machineryRepository;

    @Autowired
    private InventoryDao inventoryDao;

    @Override
    public List<MachineryDTO> getAllMachinery() throws Exception {
        try {
            List<Machinery> machineryList = machineryRepository.findAll();

            if (machineryList.isEmpty()) {
                return Collections.emptyList();
            }

            return machineryList.stream()
                    .map(machinery -> {
                        try {
                            return Helpers.parse(machinery);
                        } catch (Exception e) {
                            return null; // Filtramos los nulos después
                        }
                    })
                    .filter(machineryDTO -> machineryDTO != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO getMachineryById(Long id) throws Exception {
        try {
            return machineryRepository.findById(id)
                    .map(machinery -> {
                        MachineryDTO dto = new MachineryDTO();
                        dto.setId(machinery.getId());
                        dto.setName(machinery.getName());
                        dto.setStatus(machinery.getStatus());

                        // Solo ID de inventory
                        if (machinery.getInventory() != null) {
                            InventoryDTO inventoryDTO = new InventoryDTO();
                            inventoryDTO.setId(machinery.getInventory().getId());
                            dto.setInventory(inventoryDTO);
                        }

                        return dto;
                    })
                    .orElseThrow(() -> new Exception("Maquinaria no encontrada"));
        } catch (Exception e) {
            throw new Exception("Error al obtener maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO createMachinery(MachineryDTO machineryDTO) throws Exception {
        try {
            // Validar ID de inventario
            if (machineryDTO.getInventory() == null || machineryDTO.getInventory().getId() == null) {
                throw new Exception("El ID de inventario es obligatorio");
            }

            // 1. Crear entidad
            Machinery machinery = new Machinery();
            machinery.setName(machineryDTO.getName());
            machinery.setStatus(machineryDTO.getStatus() != null ? machineryDTO.getStatus() : "Disponible");

            // 2. Mapear Inventory (CRÍTICO: Usar la entidad completa o solo el ID)
            Inventory inventory = new Inventory();
            inventory.setId(machineryDTO.getInventory().getId());
            machinery.setInventory(inventory); // ← Esto asocia el ID correctamente

            // 3. Mapear Order (opcional)
            if (machineryDTO.getOrder() != null && machineryDTO.getOrder().getId() != null) {
                Order order = new Order();
                order.setId(machineryDTO.getOrder().getId());
                machinery.setOrder(order);
            }

            // 4. Guardar
            Machinery savedMachinery = machineryRepository.save(machinery);

            // 5. Convertir a DTO para respuesta
            return Helpers.parse(savedMachinery);

        } catch (DataIntegrityViolationException e) {
            throw new Exception("Error de integridad en la BD: Verifica que el inventory_id y order_id existan");
        } catch (Exception e) {
            throw new Exception("Error al crear maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO updateMachinery(Long id, MachineryDTO machineryDTO) throws Exception {
        try {
            Machinery existingMachinery = machineryRepository.findById(id)
                    .orElseThrow(() -> new Exception("Maquinaria no encontrada con ID: " + id));

            // Conditional updates
            if (machineryDTO.getName() != null) {
                existingMachinery.setName(machineryDTO.getName());
            }
            if (machineryDTO.getStatus() != null) {
                existingMachinery.setStatus(machineryDTO.getStatus());
            }
            if (machineryDTO.getInventory() != null) {
                existingMachinery.setInventory(Helpers.parse(machineryDTO.getInventory()));
            }

            Machinery updatedMachinery = machineryRepository.save(existingMachinery);
            return Helpers.parse(updatedMachinery);
        } catch (Exception e) {
            throw new Exception("Error al actualizar maquinaria: " + e.getMessage());
        }
    }

    @Override
    public void deleteMachinery(Long id) throws Exception {
        try {
            if (!machineryRepository.existsById(id)) {
                throw new Exception("Maquinaria no encontrada con ID: " + id);
            }
            machineryRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar maquinaria: " + e.getMessage());
        }
    }
}