package com.mycompany.app.dao;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.MachineryRepository;
import com.mycompany.app.model.Machinery;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Setter
@Getter
public class MachineryImplementation implements MachineryDao {

    @Autowired
    private MachineryRepository machineryRepository;

    @Override
    public List<MachineryDTO> getAllMachinery() throws Exception {
        try {
            return machineryRepository.findAll().stream()
                    .map(Helpers::parse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener toda la maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO getMachineryById(Long id) throws Exception {
        try {
            Optional<Machinery> machinery = machineryRepository.findById(id);
            return machinery.map(Helpers::parse)
                    .orElseThrow(() -> new Exception("Maquinaria no encontrada con ID: " + id));
        } catch (Exception e) {
            throw new Exception("Error al obtener maquinaria por ID: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO createMachinery(MachineryDTO machineryDTO) throws Exception {
        try {
            Machinery machinery = Helpers.parse(machineryDTO);
            Machinery savedMachinery = machineryRepository.save(machinery);
            return Helpers.parse(savedMachinery);
        } catch (Exception e) {
            throw new Exception("Error al crear maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO updateMachinery(Long id, MachineryDTO machineryDTO) throws Exception {
        try {
            Machinery existingMachinery = machineryRepository.findById(id)
                    .orElseThrow(() -> new Exception("Maquinaria no encontrada con ID: " + id));

            existingMachinery.setName(machineryDTO.getName());
            existingMachinery.setStatus(machineryDTO.getStatus());
            existingMachinery.setInventory(Helpers.parse(machineryDTO.getInventory()));

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