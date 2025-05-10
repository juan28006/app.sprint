package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.MachineryDTO;
import java.util.List;

public interface MachineryDao {
    List<MachineryDTO> getAllMachinery() throws Exception;

    MachineryDTO getMachineryById(Long id) throws Exception;

    MachineryDTO createMachinery(MachineryDTO machineryDTO) throws Exception;

    MachineryDTO updateMachinery(Long id, MachineryDTO machineryDTO) throws Exception;

    void deleteMachinery(Long id) throws Exception;
}