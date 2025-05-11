/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {
    @Autowired
    private InventoryService inventoryService;
    private UserDTO usuarioActual;

    // ==================== MÉTODOS DE MENÚ ====================
    @Override
    public void mostrarMenuPrincipal(UserDTO usuario) throws Exception {
        this.usuarioActual = usuario;
        boolean sesionActiva = true;

        while (sesionActiva) {
            Utils.limpiarConsola();
            System.out.println("=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Generar reporte");
            System.out.println("2. Actualizar inventario");
            System.out.println("3. Crear orden de maquinaria");
            System.out.println("4. Consultar disponibilidad");
            System.out.println("5. Cerrar sesión");

            String opcion = Utils.leerLinea("Seleccione una opción: ");

            switch (opcion) {
                case "1":
                    ejecutarGenerarReporte();
                    break;
                case "2":
                    ejecutarActualizarInventario();
                    break;
                case "3":
                    CrearOrden();
                    break;
                case "4":
                    ejecutarConsultarDisponibilidad();
                    break;
                case "5":
                    sesionActiva = false;
                    break;
                default:
                    Utils.mostrarError("Opción inválida");
            }
        }
        cerrarSesion();
    }

    private void ejecutarGenerarReporte() {
        try {
            String tipo = Utils.leerLinea("Tipo de reporte (Uso/Estado/Mantenimiento): ");
            ReportDTO reporte = new ReportDTO();
            reporte.setType(tipo);
            inventoryService.createReport(reporte, usuarioActual);
            Utils.mostrarMensaje("Reporte generado exitosamente");
        } catch (Exception e) {
            Utils.mostrarError(e.getMessage());
        }
    }

    private void ejecutarActualizarInventario() {
        try {
            Long id = Long.parseLong(Utils.leerLinea("ID del inventario: "));
            String estado = Utils.leerLinea("Nuevo estado: ");
            InventoryDTO inventario = inventoryService.getInventoryById(id);
            inventario.setStatus(estado);
            inventoryService.updateInventory(id, inventario, usuarioActual);
            Utils.mostrarMensaje("Inventario actualizado");
        } catch (Exception e) {
            Utils.mostrarError(e.getMessage());
        }
    }

    // ==================== ENDPOINTS API ====================
    @PostMapping("/reportes")
    public ResponseEntity<?> generarReporte(@RequestBody ReportDTO reporte) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Generar_informes",
                () -> inventoryService.createReport(reporte, usuarioActual));
    }

    @PutMapping("/inventario/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long id, @RequestBody InventoryDTO inventario) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Gestionar_inventario",
                () -> inventoryService.updateInventory(id, inventario, usuarioActual));
    }

    @PostMapping("/ordenes")
    public ResponseEntity<?> crearOrden(@RequestBody OrderDTO orden) {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Orden_maquinaria",
                () -> inventoryService.createOrder(orden, usuarioActual));
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> consultarDisponibilidad() {
        return inventoryService.ejecutarConPermiso(usuarioActual, "Consultar_disponibilidad",
                () -> inventoryService.getAllMachinery(usuarioActual));
    }

    @Override
    public void cerrarSesion() {
        usuarioActual = null;
        Utils.mostrarMensaje("Sesión de administrador cerrada");
    }

}
