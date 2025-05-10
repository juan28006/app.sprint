/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.jcp.xml.dsig.internal.dom.Utils;
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
import com.mycompany.app.service.InventoryService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private InventoryService inventoryService;

    @Override
    public void iniciarSesion() throws Exception {
        // Lógica heredada pero no usada
    }

    @Override
    public void mostrarMenuPrincipal() throws Exception {
        boolean mantenerSesion = true;

        while (mantenerSesion) {
            Utils.limpiarConsola();
            System.out.println("=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Generar reportes");
            System.out.println("2. Gestionar inventario");
            System.out.println("3. Controlar acceso");
            System.out.println("4. Cerrar sesión");

            String opcion = Utils.leerLinea("Seleccione una opción: ");

            switch (opcion) {
                case "1":
                    generarReporte();
                    break;
                case "2":
                    gestionarInventario();
                    break;
                case "3":
                    controlarAcceso();
                    break;
                case "4":
                    mantenerSesion = false;
                    break;
                default:
                    System.out.println("Opción inválida");
                    Utils.leerLinea("Presione enter para continuar...");
            }
        }
    }

    // Generar_informes
    @PostMapping("/reportes")
    public ResponseEntity<?> generarReporte(@RequestBody ReportDTO reportDTO) {
        try {
            return ResponseEntity.ok(inventoryService.createReport(reportDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Gestionar_inventario
    @PutMapping("/inventario/{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Orden_maquinaria
    @PostMapping("/ordenes")
    public ResponseEntity<?> crearOrden(@RequestBody OrderDTO orderDTO) {
        try {
            return ResponseEntity.ok(inventoryService.createOrder(orderDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar_disponibilidad
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> consultarDisponibilidad() {
        try {
            return ResponseEntity.ok(inventoryService.getAllMachinery());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
