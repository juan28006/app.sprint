/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements ControllerInterface {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/reportes")
    public ResponseEntity<?> generarReporte(@RequestBody ReportDTO reporte,
            @RequestHeader("Authorization") String token) {
        try {
            UserDTO user = inventoryService.validateToken(token);
            return inventoryService.executeWithPermission(
                    user,
                    "Generar_informes",
                    () -> {
                        ReportDTO createdReport = inventoryService.createReport(reporte);
                        return ResponseEntity.ok()
                                .body(Map.of(
                                        "message", "Reporte generado exitosamente",
                                        "id", createdReport.getId()));
                    });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/inventario/{id}")
    public ResponseEntity<?> actualizarInventario(
            @PathVariable Long id,
            @RequestBody InventoryDTO inventario,
            @RequestHeader("Authorization") String token) {

        try {
            UserDTO user = inventoryService.validateToken(token);
            return inventoryService.executeWithPermission(
                    user,
                    "Gestionar_inventario",
                    () -> {
                        InventoryDTO updated = inventoryService.updateInventory(id, inventario);
                        return ResponseEntity.ok()
                                .body(Map.of(
                                        "message", "Inventario actualizado",
                                        "id", updated.getId()));
                    });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> consultarDisponibilidad(@RequestHeader("Authorization") String token) {
        try {
            UserDTO user = inventoryService.validateToken(token);
            return inventoryService.executeWithPermission(
                    user,
                    "Consultar_disponibilidad",
                    () -> ResponseEntity.ok(inventoryService.getAllMachinery()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== MÉTODOS DE INTERFAZ ====================
    @Override
    public void mostrarMenuPrincipal(UserDTO usuario) {
        this.usuarioActual = usuario;
    }

    @PostMapping("/logout")
    @Override
    public void cerrarSesion() {
        usuarioActual = null;
    }

}
