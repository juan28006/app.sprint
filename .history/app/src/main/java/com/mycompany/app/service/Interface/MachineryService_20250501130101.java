package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.MachineryDTO;

public interface MachineryService {
    List<MachineryDTO> getAllMachinery() throws Exception;

    MachineryDTO getMachineryById(Long id) throws Exception;

    MachineryDTO createMachinery(MachineryDTO machineryDTO) throws Exception;

    MachineryDTO updateMachinery(Long id, MachineryDTO machineryDTO) throws Exception;

    void deleteMachinery(Long id) throws Exception;
}