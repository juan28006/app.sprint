/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.AdminRegistrationRequest;
import com.mycompany.app.Controller.Validator.AdminValidator;
import com.mycompany.app.Controller.Validator.OrderValidator;
import com.mycompany.app.Controller.Validator.ReportInvoicesValidator;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.service.InventoryService;
import com.mycompany.app.service.Interface.InventoryS;
import com.mycompany.app.service.Interface.OrderService;
import com.mycompany.app.service.Interface.ReportInvoicesService;

@RestController
@RequestMapping("/api/admin")
public class AdminController implements ControllerInterface {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private AdminValidator adminValidator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private ReportInvoicesService reportService;

    @Autowired
    private ReportInvoicesValidator validator;

    @Autowired
    InventoryS inventoryS;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegistrationRequest request) { // Agregar @Valid
        try {

            adminValidator.validateForCreation(request);
            // Validar que los campos requeridos no sean nulos
            if (request.getDocument() == null || request.getCellphone() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos requeridos faltantes",
                        "message", "Documento y celular son campos obligatorios"));
            }

            // 1. Registrar Persona
            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(request.getName());
            personDTO.setDocument(request.getDocument());
            personDTO.setCellphone(request.getCellphone());
            inventoryService.createPerson(personDTO);

            // 2. Registrar Usuario (Admin)
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(request.getUsername());
            userDTO.setPassword(request.getPassword());
            // Establecer la relación con la persona recién creada
            userDTO.setPerson(personDTO); // ← Aquí está el cambio cl
            UserDTO createdUser = inventoryService.createAdmin(userDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", createdUser.getId(),
                    "message", "Administrador registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al registrar admin",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            UserDTO user = inventoryService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No se encontró usuario con ID: " + id,
                                "timestamp", LocalDateTime.now()));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", user,
                    "message", "Usuario obtenido exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al obtener usuario",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    // Inventory Management
    @PostMapping("/inventory")
    public ResponseEntity<?> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdInventory,
                            "message", "Inventario creado exitosamente",
                            "inventoryId", createdInventory.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear inventario",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    @PutMapping("inventory/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        try {
            InventoryDTO updatedInventory = inventoryS.updateInventory(id, inventoryDTO);
            if (updatedInventory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró registro de inventario con ID: " + id);
            }
            return ResponseEntity.ok("Inventario actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar inventario: " + e.getMessage());
        }
    }

    @DeleteMapping("inventory/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryS.deleteInventory(id);
            return ResponseEntity.ok("Registro de inventario con ID " + id + " eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al eliminar registro de inventario: " + e.getMessage());
        }
    }

    // Order Management
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = inventoryService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdOrder,
                            "message", "Orden creada exitosamente",
                            "orderId", createdOrder.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear orden",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
            @RequestParam String status) {
        try {
            orderValidator.validateStatusChange(status);
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Report Management

    // verificar que si genere facturas,obtenga facturas por idfactura
    // y por estado de factura pendiente,pagada,cancelada
    // IMPLEMENTAR METODO DE OBTENER LAS FACTURAS POR MEDIO DE SU ID(CAMBIAR
    // GETFACTURASBYUSUARIO
    // POR GETINVOICEBYID)
    @PostMapping("/generar")
    public ResponseEntity<?> generateInvoices(@RequestBody OrderDTO orderDTO) {
        try {
            ReportInvoicesDTO report = reportService.generateReportInvoices(orderDTO);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/invoices{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        try {
            validator.validateInvoiceId(id);
            ReportInvoicesDTO invoice = reportService.getReportById(id);
            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al obtener factura",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getByEstado(@PathVariable String estado) {
        try {
            List<ReportInvoicesDTO> reports = reportService.getReportsByEstado(estado);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            ReportInvoicesDTO updatedReport = reportService.updateReportEstado(id, nuevoEstado);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {

            // 1. Obtener y validar el usuario
            UserDTO user = inventoryService.getUserById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Usuario no encontrado",
                                "message", "No existe un usuario con ID: " + id));
            }

            if (!"Admin".equalsIgnoreCase(user.getTypeUser().getType())) {
                throw new Exception("Solo se pueden eliminar personas de tipo Admin");
            }
            // 1. Obtener la persona por ID (o documento) para tener el DTO completo
            PersonDTO personToDelete = inventoryService.getPersonById(id); // Necesitarás implementar este método

            // 3. Eliminar el usuario (esto debería activar la eliminación en cascada de la
            // persona si está configurado)
            inventoryService.deleteUser(id);
            // 2. Eliminar la persona
            inventoryService.deletePerson(personToDelete);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Persona eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Error al eliminar persona",
                    "message", e.getMessage()));
        }
    }

    @Override
    public void session() throws Exception {

    }
}

// @GetMapping
// public ResponseEntity<List<OrderDTO>> getAllOrders() {
// try {
// List<OrderDTO> orders = orderService.getAllOrders();
// return ResponseEntity.ok(orders);
// } catch (Exception e) {
// return ResponseEntity.status(500).body(null);
// }
// }

// @GetMapping("/status/{status}")
// public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
// try {
// List<OrderDTO> orders = orderService.getOrdersByStatus(status);
// return ResponseEntity.ok(orders);
// } catch (Exception e) {
// return ResponseEntity.badRequest().body(e.getMessage());
// }
// }
