package com.mycompany.app.Controller;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.service.Interface.MachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/machinery")

public class MachineryController {

    @Autowired
    private MachineryService machineryService;

    @GetMapping
    public List<MachineryDTO> getAllMachinery() throws Exception {
        return machineryService.getAllMachinery();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineryDTO> getMachineryById(@PathVariable Long id) throws Exception {
        MachineryDTO machineryDTO = machineryService.getMachineryById(id);
        return ResponseEntity.ok(machineryDTO);
    }

    @PostMapping
    public ResponseEntity<MachineryDTO> createMachinery(@RequestBody MachineryDTO machineryDTO) throws Exception {
        MachineryDTO createdMachinery = machineryService.createMachinery(machineryDTO);
        return ResponseEntity.ok(createdMachinery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineryDTO> updateMachinery(@PathVariable Long id,
            @RequestBody MachineryDTO machineryDTO) throws Exception {
        MachineryDTO updatedMachinery = machineryService.updateMachinery(id, machineryDTO);
        return ResponseEntity.ok(updatedMachinery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachinery(@PathVariable Long id) throws Exception {
        machineryService.deleteMachinery(id);
        return ResponseEntity.noContent().build();
    }
}