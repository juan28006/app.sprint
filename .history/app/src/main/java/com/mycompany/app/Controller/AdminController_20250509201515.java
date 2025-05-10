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
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.app.Controller.Request.AdminRegistrationRequest;
import com.mycompany.app.Controller.Request.UpdateOrderStatusRequest;
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
            InventoryDTO updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedInventory,
                    "message", "Inventario actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al actualizar inventario", "message", e.getMessage()));
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
            // Validar nombre de máquina
            if (orderDTO.getName() == null || orderDTO.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Datos inválidos",
                        "message", "El nombre de la máquina es obligatorio"));
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

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrderById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", order,
                    "message", "Orden obtenida exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Error al obtener orden",
                    "message", e.getMessage()));
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {
        try {
            orderValidator.validateStatusChange(request.getStatus());
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedOrder,
                    "message", "Estado de la orden actualizado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al actualizar estado",
                    "message", e.getMessage()));
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
            report.setFechaGeneracion(new Date(System.currentTimeMillis())); // Fecha actual
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

    @PutMapping("/factura/{id}")
    public ResponseEntity<?> updateReportInvoices(@PathVariable Long id, @RequestBody ReportInvoicesDTO reportDTO) {
        try {
            // 1. Validar que la factura existe
            ReportInvoicesDTO existingInvoice = reportService.getReportById(id);
            if (existingInvoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "error", "Factura no encontrada",
                        "message", "No se encontró factura con ID: " + id));
            }

            // 2. Asignar el ID de la URL al DTO (evita necesidad de enviarlo en el body)
            reportDTO.setId(id);

            // 3. Validar el DTO
            ReportInvoicesValidator.validateReportInvoiceDTO(reportDTO);

            // 4. Actualizar la factura
            ReportInvoicesDTO updatedReport = reportService.updateReportFacturacion(id, reportDTO);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", updatedReport,
                    "message", "Reporte de facturación actualizado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Datos inválidos",
                    "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error al actualizar factura",
                    "message", e.getMessage()));
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
