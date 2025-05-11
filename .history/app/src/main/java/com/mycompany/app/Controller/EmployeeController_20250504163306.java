/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.AccessRequestDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/empleado")
public class EmployeeController {
    @Autowired
    private InventoryService inventoryService;
    private UserDTO usuarioActual;

    // ==================== ENDPOINTS CON PERMISOS ====================
    @PostMapping("/acceso")
    public ResponseEntity<?> controlarAcceso(@RequestBody AccessRequestDTO acceso) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Controlar_acceso",
                () -> {
                    String resultado = inventoryService.controlAccess(acceso);
                    return ResponseEntity.ok("Acceso controlado: " + resultado);
                });
    }

    @GetMapping("/maquinaria")
    public ResponseEntity<?> consultarMaquinaria() {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_disponibilidad",
                () -> ResponseEntity.ok(inventoryService.getAllMachinery()));
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
