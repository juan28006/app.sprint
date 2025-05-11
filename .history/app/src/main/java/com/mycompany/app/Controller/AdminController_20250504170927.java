/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements ControllerInterface {
    @Autowired
    private InventoryService inventoryService;
    private UserDTO usuarioActual;

    // ==================== ENDPOINTS CON PERMISOS ====================
    @PostMapping("/reportes")
    public ResponseEntity<?> generarReporte(@RequestBody ReportDTO reporte) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Generar_informes",
                () -> {
                    ReportDTO nuevoReporte = inventoryService.createReport(reporte);
                    return ResponseEntity.ok().body("Reporte generado: " + nuevoReporte.getId());
                });
    }

    @PutMapping("/inventario/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long id, @RequestBody InventoryDTO inventario) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Gestionar_inventario",
                () -> {
                    InventoryDTO actualizado = inventoryService.updateInventory(id, inventario);
                    return ResponseEntity.ok("Inventario actualizado: " + actualizado.getId());
                });
    }

    @PostMapping("/ordenes")
    public ResponseEntity<?> crearOrden(@RequestBody OrderDTO orden) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Orden_maquinaria",
                () -> {
                    OrderDTO nuevaOrden = inventoryService.createOrder(orden);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Orden creada: " + nuevaOrden.getId());
                });
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> consultarDisponibilidad() {
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
