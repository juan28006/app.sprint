package com.mycompany.app.dao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.dao.repositories.MachineryRepository;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.OrderDTO;
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
            // 1. Convertir DTO a Entidad
            Machinery machinery = new Machinery();
            machinery.setName(machineryDTO.getName());
            machinery.setStatus(machineryDTO.getStatus());

            // 2. Mapear Inventory (solo ID)
            if (machineryDTO.getInventory() != null) {
                Inventory inventory = new Inventory();
                inventory.setId(machineryDTO.getInventory().getId());
                machinery.setInventory(inventory); // ← Clave para evitar el error SQL
            } else {
                throw new Exception("El ID de inventario es obligatorio");
            }

            // 3. Mapear Order (opcional, solo ID)
            if (machineryDTO.getOrder() != null && machineryDTO.getOrder().getId() != null) {
                Order order = new Order();
                order.setId(machineryDTO.getOrder().getId());
                machinery.setOrder(order);
            }

            // 4. Guardar en base de datos
            Machinery savedMachinery = machineryRepository.save(machinery);

            // 5. Construir respuesta
            MachineryDTO result = new MachineryDTO();
            result.setId(savedMachinery.getId());
            result.setName(savedMachinery.getName());
            result.setStatus(savedMachinery.getStatus());

            // Incluir IDs de relaciones en la respuesta
            if (savedMachinery.getInventory() != null) {
                InventoryDTO inventoryDTO = new InventoryDTO();
                inventoryDTO.setId(savedMachinery.getInventory().getId());
                result.setInventory(inventoryDTO);
            }

            if (savedMachinery.getOrder() != null) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(savedMachinery.getOrder().getId());
                result.setOrder(orderDTO);
            }

            return result;

        } catch (Exception e) {
            throw new Exception("Error al crear maquinaria en el DAO: " + e.getMessage());
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