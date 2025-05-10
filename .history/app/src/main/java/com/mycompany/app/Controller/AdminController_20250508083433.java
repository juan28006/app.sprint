/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.sql.Date;
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
    private ReportInvoicesValidator validator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private ReportInvoicesService reportService;

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
            // Validación básica
            if (inventoryDTO == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos requeridos faltantes",
                        "message", "El objeto InventoryDTO no puede ser nulo",
                        "timestamp", LocalDateTime.now()));
            }

            // Generar fecha automáticamente
            inventoryDTO.setEntryDate(new Date(System.currentTimeMillis()));

            InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdInventory,
                            "message", "Inventario creado exitosamente",
                            "inventoryId", createdInventory.getId(),
                            "entryDate", createdInventory.getEntryDate(), // Mostrar fecha generada
                            "timestamp", LocalDateTime.now()));
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
            // 1. Obtener el inventario existente
            InventoryDTO existingInventory = inventoryService.getInventoryById(id);
            if (existingInventory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Inventario no encontrado",
                                "message", "No existe inventario con ID: " + id));
            }

            // 2. Actualización parcial (solo campos no nulos)
            boolean hasUpdates = false;

            if (inventoryDTO.getName() != null && !inventoryDTO.getName().isEmpty()) {
                existingInventory.setName(inventoryDTO.getName());
                hasUpdates = true;
            }

            if (inventoryDTO.getDescription() != null) {
                existingInventory.setDescription(inventoryDTO.getDescription());
                hasUpdates = true;
            }

            if (inventoryDTO.getQuantity() != null) {
                existingInventory.setQuantity(inventoryDTO.getQuantity());
                hasUpdates = true;
            }

            if (inventoryDTO.getStatus() != null && !inventoryDTO.getStatus().isEmpty()) {
                existingInventory.setStatus(inventoryDTO.getStatus());
                hasUpdates = true;
            }

            // 3. Validar si hubo cambios
            if (!hasUpdates) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "No se realizaron cambios",
                        "inventoryId", id));
            }

            // 4. Aplicar actualización
            InventoryDTO updatedInventory = inventoryS.updateInventory(id, existingInventory);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedInventory,
                    "message", "Inventario actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al actualizar inventario",
                            "message", e.getMessage()));
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
            // Validaciones básicas
            if (orderDTO == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos requeridos faltantes",
                        "message", "El objeto OrderDTO no puede ser nulo",
                        "timestamp", LocalDateTime.now()));
            }

            // Validar campos obligatorios
            if (orderDTO.getUnitPrice() == null || orderDTO.getUnitPrice() <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos inválidos",
                        "message", "El precio unitario debe ser mayor a cero",
                        "timestamp", LocalDateTime.now()));
            }

            if (orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos inválidos",
                        "message", "La cantidad debe ser mayor a cero",
                        "timestamp", LocalDateTime.now()));
            }

            // Generar valores automáticos
            orderDTO.setOrderDate(new Date(System.currentTimeMillis()));
            orderDTO.setOrderNumber("ORD-" + System.currentTimeMillis());
            orderDTO.setStatus("Pendiente");

            // Calcular totalPrice automáticamente
            Double totalPrice = orderDTO.getUnitPrice() * orderDTO.getQuantity();
            orderDTO.setTotalPrice(totalPrice);

            OrderDTO createdOrder = inventoryService.createOrder(orderDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "data", createdOrder,
                            "message", "Orden creada exitosamente",
                            "orderId", createdOrder.getId(),
                            "totalPrice", createdOrder.getTotalPrice(), // Incluimos el total en la respuesta
                            "timestamp", LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Error al crear orden",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now()));
        }
    }

    @PutMapping("/status/{id}")
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

    @PostMapping("/generar")
    public ResponseEntity<?> generateInvoices(@RequestBody OrderDTO orderDTO) {
        try {
            // Validar que la orden existe y está aprobada
            OrderDTO order = orderService.getOrderById(orderDTO.getId());
            if (order == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Orden no encontrada",
                        "message", "No se encontró orden con ID: " + orderDTO.getId()));
            }

            if (!"Aprobada".equals(order.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Orden no aprobada",
                        "message", "Solo se pueden generar facturas para órdenes aprobadas"));
            }

            // Generar factura con datos automatizados
            ReportInvoicesDTO report = new ReportInvoicesDTO();
            report.setCodigoFactura("FACT-" + System.currentTimeMillis()); // Código único
            report.setFechaGeneracion(new java.sql.Date(System.currentTimeMillis())); // Fecha actual
            report.setTotal(order.getTotalPrice()); // Total de la orden
            report.setOrder(order);
            report.setUsuario(order.getCreatedBy());
            report.setEstado("Pendiente"); // Estado inicial

            ReportInvoicesDTO createdInvoice = reportService.generateReportInvoices(report);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", createdInvoice,
                    "message", "Factura generada exitosamente",
                    "codigoFactura", createdInvoice.getCodigoFactura(),
                    "total", createdInvoice.getTotal(),
                    "fechaGeneracion", createdInvoice.getFechaGeneracion()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al generar factura",
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/factura/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
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
    public ResponseEntity<?> getByReportStatus(@PathVariable String estado) {
        try {
            List<ReportInvoicesDTO> reports = reportService.getReportsByStatus(estado);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // AdminController.java
    @DeleteMapping("/factura/{id}")
    public ResponseEntity<?> deleteReportInvoices(@PathVariable Long id) {
        try {
            validator.validateInvoiceId(id);
            reportService.deleteReportInvoices(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Factura eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al eliminar factura",
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
