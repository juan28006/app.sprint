package com.mycompany.app.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.app.Controller.Validator.TypeUserValidator;
import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.TypeUserImplementation;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.dao.interfaces.PersonDao;
import com.mycompany.app.dao.interfaces.ReportDao;
import com.mycompany.app.dao.interfaces.ReportInvoicesDao;
import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.dao.interfaces.TypeUserDao;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.repositories.ReportInvoicesRepository;
import com.mycompany.app.dao.repositories.TypeUserRepository;
import com.mycompany.app.dao.repositories.UserRepository;
import com.mycompany.app.dto.AccessRequestDTO;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.model.TypeUser;
import com.mycompany.app.service.Interface.InventoryS;
import com.mycompany.app.service.Interface.MachineryService;
import com.mycompany.app.service.Interface.OrderService;
import com.mycompany.app.service.Interface.ReportInvoicesService;
import com.mycompany.app.service.Interface.ReportService;
import com.mycompany.app.service.Interface.ReservationService;
import com.mycompany.app.service.Interface.TypeUserService;
import com.mycompany.app.service.Interface.UserService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Service
public class InventoryService implements UserService, TypeUserService, InventoryS, ReservationService, ReportService,
        MachineryService, OrderService, ReportInvoicesService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TypeUserDao typeUserDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private MachineryDao machineryDao;

    @Autowired
    private ReportInvoicesDao reportInvoicesDao;

    @Autowired
    private ReportInvoicesRepository reportInvoicesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeUserImplementation typeUserImplementation;

    @Autowired
    private TypeUserValidator typeUserValidator;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Autowired
    private PersonDao personDao;

    // Implementación de UserService
    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        try {
            return userDao.getAllUsers();
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los usuarios: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido: " + id);
        }
        try {
            UserDTO user = userDao.getUserById(id);
            if (user == null) {
                throw new Exception("Usuario no encontrado con ID: " + id);
            }
            return user;
        } catch (Exception e) {
            throw new Exception("Error al obtener usuario por ID: " + e.getMessage());
        }
    }

    // Método auxiliar reutilizable
    private TypeUserDTO findTypeUserByType(String type) throws Exception {
        return typeUserDao.getAllTypeUsers().stream()
                .filter(t -> type.equalsIgnoreCase(t.getType()))
                .findFirst()
                .orElseThrow(() -> new Exception("Tipo de usuario '" + type + "' no encontrado"));
    }

    private TypeUserDTO getOrCreateTypeUser(String type, String defaultPermissions) throws Exception {
        try {
            // Buscar ignorando mayúsculas/minúsculas
            Optional<TypeUser> existingType = typeUserRepository.findAll().stream()
                    .filter(t -> t.getType().equalsIgnoreCase(type))
                    .findFirst();

            if (existingType.isPresent()) {
                return Helpers.parse(existingType.get());
            }

            // Crear nuevo si no existe
            TypeUser newType = new TypeUser();
            newType.setType(type); // Usar el tipo exacto que recibimos
            newType.setPermissions(defaultPermissions);

            if (newType.getPermissions() == null || newType.getPermissions().isEmpty()) {
                throw new Exception("Permisos no pueden ser nulos para tipo: " + type);
            }

            TypeUser savedType = typeUserRepository.save(newType);
            return Helpers.parse(savedType);

        } catch (Exception e) {
            throw new Exception("Error al procesar tipo de usuario '" + type + "'", e);
        }
    }

    // Método para obtener permisos por defecto según tipo
    private String getDefaultPermissionsForType(String type) {
        if (type == null)
            return "Iniciar_sesion,Registro";

        switch (type.toLowerCase()) {
            case "admin":
                return "Generar_informes,Gestionar_inventario,Orden_maquinaria,Controlar_acceso,Consultar_disponibilidad,Iniciar_sesion,Registro";
            case "empleado":
                return "Consultar_disponibilidad,Controlar_acceso,Iniciar_sesion,Registro";
            case "cliente":
                return "Consultar_disponibilidad,Reservar_maquinaria,Iniciar_sesion,Registro";
            default:
                return "Iniciar_sesion,Registro";
        }
    }

    // En InventoryService.java
    @Override
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        // 1. Validate basic user data
        userValidator.validateForCreation(userDTO);

        // 2. Validate user type
        if (userDTO.getTypeUser() == null || userDTO.getTypeUser().getType() == null) {
            throw new Exception("Tipo de usuario no especificado");
        }

        // 3. Get or create the TypeUser with default permissions
        TypeUserDTO typeUser = getOrCreateTypeUser(
                userDTO.getTypeUser().getType(),
                getDefaultPermissionsForType(userDTO.getTypeUser().getType()));

        // 4. Set the validated type user
        userDTO.setTypeUser(typeUser);

        // 5. Create the user
        UserDTO createdUser = userDao.createUser(userDTO);

        // 7. Return the created user with proper authentication status
        return createdUser;
    }

    @Override
    public UserDTO createAdmin(UserDTO userDTO) throws Exception {
        // Asigna automáticamente permisos de admin
        TypeUserDTO adminType = getOrCreateTypeUser("Admin",
                "Generar_informes,Gestionar_inventario,Orden_maquinaria,Consultar_disponibilidad,Iniciar_sesion,Registro");
        userDTO.setTypeUser(adminType);
        userDTO.setAuthenticated(true); // ← Cambio clave aquí
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createEmpleado(UserDTO userDTO) throws Exception {
        // Asigna permisos de empleado
        TypeUserDTO empleadoType = getOrCreateTypeUser("Empleado",
                "Consultar_disponibilidad,Controlar_acceso,Iniciar_sesion,Registro");
        userDTO.setTypeUser(empleadoType);

        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createCliente(UserDTO userDTO) throws Exception {
        // Asigna permisos de cliente
        TypeUserDTO clienteType = getOrCreateTypeUser("Cliente",
                "Consultar_disponibilidad,Iniciar_sesion,Registro,Reservar_maquinaria");
        userDTO.setTypeUser(clienteType);
        userDTO.setAuthenticated(true); // ← Cambio clave aquí
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido: " + id);
        }
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        try {
            return userDao.updateUser(id, userDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de usuario inválido: " + id);
        }
        try {
            userDao.deleteUser(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar usuario: " + e.getMessage());
        }
    }

    // Implementación de TypeUserService
    @Override
    public List<TypeUserDTO> getAllTypeUsers() throws Exception {
        try {
            return typeUserDao.getAllTypeUsers();
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los tipos de usuario: " + e.getMessage());
        }
    }

    @Override
    public TypeUserDTO getTypeUserById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de tipo de usuario inválido: " + id);
        }
        try {
            TypeUserDTO typeUser = typeUserDao.getTypeUserById(id);
            if (typeUser == null) {
                throw new Exception("Tipo de usuario no encontrado con ID: " + id);
            }
            return typeUser;
        } catch (Exception e) {
            throw new Exception("Error al obtener tipo de usuario por ID: " + e.getMessage());
        }
    }

    @Override
    public TypeUserDTO createTypeUser(TypeUserDTO typeUserDTO) throws Exception {
        if (typeUserDTO == null) {
            throw new Exception("El DTO de tipo de usuario no puede ser nulo");
        }
        try {
            return typeUserDao.createTypeUser(typeUserDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear tipo de usuario: " + e.getMessage());
        }
    }

    @Override
    public TypeUserDTO updateTypeUser(Long id, TypeUserDTO typeUserDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de tipo de usuario inválido: " + id);
        }
        if (typeUserDTO == null) {
            throw new Exception("El DTO de tipo de usuario no puede ser nulo");
        }
        try {
            return typeUserDao.updateTypeUser(id, typeUserDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar tipo de usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteTypeUser(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de tipo de usuario inválido: " + id);
        }
        try {
            typeUserDao.deleteTypeUser(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar tipo de usuario: " + e.getMessage());
        }
    }

    // Implementación de InventoryS
    @Override
    public List<InventoryDTO> getAllInventory() throws Exception {
        try {
            return inventoryDao.getAllInventory();
        } catch (Exception e) {
            throw new Exception("Error al obtener todo el inventario: " + e.getMessage());
        }
    }

    @Override
    public InventoryDTO getInventoryById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de inventario inválido: " + id);
        }
        try {
            InventoryDTO inventory = inventoryDao.getInventoryById(id);
            if (inventory == null) {
                throw new Exception("Inventario no encontrado con ID: " + id);
            }
            return inventory;
        } catch (Exception e) {
            throw new Exception("Error al obtener inventario por ID: " + e.getMessage());
        }
    }

    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception {
        if (inventoryDTO == null) {
            throw new Exception("El DTO de inventario no puede ser nulo");
        }
        try {
            return inventoryDao.createInventory(inventoryDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear inventario: " + e.getMessage());
        }
    }

    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de inventario inválido: " + id);
        }
        if (inventoryDTO == null) {
            throw new Exception("El DTO de inventario no puede ser nulo");
        }
        try {
            return inventoryDao.updateInventory(id, inventoryDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar inventario: " + e.getMessage());
        }
    }

    @Override
    public void deleteInventory(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de inventario inválido: " + id);
        }
        try {
            inventoryDao.deleteInventory(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar inventario: " + e.getMessage());
        }
    }

    // Implementación de ReservationService
    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {
        try {
            return reservationDao.getAllReservations();
        } catch (Exception e) {
            throw new Exception("Error al obtener todas las reservaciones: " + e.getMessage());
        }
    }

    @Override
    public ReservationDTO getReservationById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reservación inválido: " + id);
        }
        try {
            ReservationDTO reservation = reservationDao.getReservationById(id);
            if (reservation == null) {
                throw new Exception("Reservación no encontrada con ID: " + id);
            }
            return reservation;
        } catch (Exception e) {
            throw new Exception("Error al obtener reservación por ID: " + e.getMessage());
        }
    }

    // Método para verificar permisos
    public boolean hasPermission(UserDTO userDTO, String permission) throws Exception {
        if (userDTO == null || userDTO.getTypeUser() == null || permission == null) {
            throw new Exception("Parámetros inválidos para la verificación de permisos");
        }
        String permissions = userDTO.getTypeUser().getPermissions();
        return permissions != null && Arrays.asList(permissions.split(",")).contains(permission);
    }

    @Override
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reservación inválido: " + id);
        }
        if (reservationDTO == null) {
            throw new Exception("El DTO de reservación no puede ser nulo");
        }
        try {
            return reservationDao.updateReservation(id, reservationDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar reservación: " + e.getMessage());
        }
    }

    @Override
    public void deleteReservation(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reservación inválido: " + id);
        }
        try {
            reservationDao.deleteReservation(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar reservación: " + e.getMessage());
        }
    }

    // Implementación de ReportService
    @Override
    public List<ReportDTO> getAllReports() throws Exception {
        try {
            return reportDao.getAllReports();
        } catch (Exception e) {
            throw new Exception("Error al obtener todos los reportes: " + e.getMessage());
        }
    }

    @Override
    public ReportDTO getReportById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reporte inválido: " + id);
        }
        try {
            ReportDTO report = reportDao.getReportById(id);
            if (report == null) {
                throw new Exception("Reporte no encontrado con ID: " + id);
            }
            return report;
        } catch (Exception e) {
            throw new Exception("Error al obtener reporte por ID: " + e.getMessage());
        }
    }

    @Override
    public ReportDTO createReport(ReportDTO reportDTO) throws Exception {
        if (reportDTO == null) {
            throw new Exception("El DTO de reporte no puede ser nulo");
        }
        try {
            return reportDao.createReport(reportDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear reporte: " + e.getMessage());
        }
    }

    @Override
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reporte inválido: " + id);
        }
        if (reportDTO == null) {
            throw new Exception("El DTO de reporte no puede ser nulo");
        }
        try {
            return reportDao.updateReport(id, reportDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar reporte: " + e.getMessage());
        }
    }

    @Override
    public void deleteReport(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de reporte inválido: " + id);
        }
        try {
            reportDao.deleteReport(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar reporte: " + e.getMessage());
        }
    }

    // Implementación de MachineryService
    @Override
    public List<MachineryDTO> getAllMachinery() throws Exception {
        try {
            return machineryDao.getAllMachinery();
        } catch (Exception e) {
            throw new Exception("Error al obtener toda la maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO getMachineryById(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de maquinaria inválido: " + id);
        }
        try {
            MachineryDTO machinery = machineryDao.getMachineryById(id);
            if (machinery == null) {
                throw new Exception("Maquinaria no encontrada con ID: " + id);
            }
            return machinery;
        } catch (Exception e) {
            throw new Exception("Error al obtener maquinaria por ID: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO createMachinery(MachineryDTO machineryDTO) throws Exception {
        if (machineryDTO == null) {
            throw new Exception("El DTO de maquinaria no puede ser nulo");
        }
        try {
            return machineryDao.createMachinery(machineryDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear maquinaria: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO updateMachinery(Long id, MachineryDTO machineryDTO) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de maquinaria inválido: " + id);
        }
        if (machineryDTO == null) {
            throw new Exception("El DTO de maquinaria no puede ser nulo");
        }
        try {
            return machineryDao.updateMachinery(id, machineryDTO);
        } catch (Exception e) {
            throw new Exception("Error al actualizar maquinaria: " + e.getMessage());
        }
    }

    @Override
    public void deleteMachinery(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID de maquinaria inválido: " + id);
        }
        try {
            machineryDao.deleteMachinery(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar maquinaria: " + e.getMessage());
        }
    }

    // ==================== AUTHENTICATION & PERMISSIONS ====================

    public void validatePermission(UserDTO user, String requiredPermission) throws Exception {
        if (user == null || user.getTypeUser() == null) {
            throw new Exception("Usuario no autenticado");
        }

        String[] permissions = user.getTypeUser().getPermissions().split(",");
        if (!Arrays.asList(permissions).contains(requiredPermission)) {
            throw new Exception("Permiso requerido: " + requiredPermission);
        }
    }

    // ==================== ADMIN METHODS ====================
    public ReportDTO generateReportWithPermission(ReportDTO reportDTO) throws Exception {
        validatePermission(reportDTO.getUser(), "Generar_informes");
        return this.createReport(reportDTO);
    }

    public InventoryDTO manageInventoryWithPermission(InventoryDTO inventoryDTO) throws Exception {
        validatePermission(inventoryDTO.getUser(), "Gestionar_inventario");
        return this.updateInventory(inventoryDTO.getId(), inventoryDTO);
    }

    // ==================== CLIENT METHODS ====================
    // Client specific methods
    public ReservationDTO createReservation(ReservationDTO reservationDTO, Long userId) throws Exception {
        UserDTO user = this.getUserById(userId);
        validatePermission(user, "Reservar_maquinaria");

        // Check machinery availability
        MachineryDTO machinery = this.getMachineryById(reservationDTO.getMachinery().getId());
        if (!"Operativa".equals(machinery.getStatus())) {
            throw new Exception("La maquinaria no está disponible");
        }

        reservationDTO.setUser(user);
        return reservationDao.createReservation(reservationDTO);
    }

    public List<MachineryDTO> getAvailableMachinery() throws Exception {
        return this.getAllMachinery().stream()
                .filter(m -> "Operativa".equals(m.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        try {
            // Validar que la maquinaria existe
            MachineryDTO machinery = machineryDao.getMachineryById(orderDTO.getMachinery().getId());
            if (machinery == null) {
                throw new Exception("Maquinaria no encontrada con ID: " + orderDTO.getMachinery().getId());
            }

            // Validar que el usuario existe
            UserDTO user = userDao.getUserById(orderDTO.getCreatedBy().getId());
            if (user == null) {
                throw new Exception("Usuario no encontrado con ID: " + orderDTO.getCreatedBy().getId());
            }
            return orderDao.createOrder(orderDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear orden: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO getOrderById(Long id) throws Exception {
        try {
            return orderDao.getOrderById(id);
        } catch (Exception e) {
            throw new Exception("Error al obtener orden: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() throws Exception {
        try {
            return orderDao.getAllOrders();
        } catch (Exception e) {
            throw new Exception("Error al obtener todas las órdenes: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) throws Exception {
        try {
            return orderDao.getOrdersByStatus(status);
        } catch (Exception e) {
            throw new Exception("Error al obtener órdenes por estado: " + e.getMessage());
        }
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, String status) throws Exception {
        try {
            return orderDao.updateOrderStatus(id, status);
        } catch (Exception e) {
            throw new Exception("Error al actualizar estado de orden: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Long id) throws Exception {
        try {
            orderDao.deleteOrder(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar orden: " + e.getMessage());
        }
    }

    @Override
    public void approveOrder(Long orderId) throws Exception {
        try {
            OrderDTO order = orderDao.getOrderById(orderId);
            if (order == null) {
                throw new Exception("Orden no encontrada con ID: " + orderId);
            }

            // Actualizar estado de la orden
            orderDao.updateOrderStatus(orderId, "Aprobada");

            // Actualizar inventario (aumentar stock)
            MachineryDTO machinery = order.getMachinery();
            InventoryDTO inventory = inventoryDao.getInventoryByMachineryId(machinery.getId());

            if (inventory == null) {
                throw new Exception("No existe registro en inventario para esta maquinaria");
            }

            inventory.setQuantity(inventory.getQuantity() + order.getQuantity());
            inventoryDao.updateInventory(inventory.getId(), inventory);
        } catch (Exception e) {
            throw new Exception("Error al aprobar orden: " + e.getMessage());
        }
    }

    @Override
    public void rejectOrder(Long orderId) throws Exception {
        try {
            OrderDTO order = orderDao.getOrderById(orderId);
            if (order == null) {
                throw new Exception("Orden no encontrada con ID: " + orderId);
            }

            // Solo se puede rechazar si está pendiente
            if (!"Pendiente".equals(order.getStatus())) {
                throw new Exception("Solo se pueden rechazar órdenes en estado 'Pendiente'");
            }

            // Actualizar estado de la orden
            orderDao.updateOrderStatus(orderId, "Rechazada");

        } catch (Exception e) {
            throw new Exception("Error al rechazar orden: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO generarReporteFacturacion(OrderDTO orderDTO) throws Exception {
        try {
            // Validar orden
            OrderDTO order = orderDao.getOrderById(orderDTO.getId());
            if (order == null) {
                throw new Exception("Orden no encontrada con ID: " + orderDTO.getId());
            }
            if (!"Aprobada".equals(order.getStatus())) {
                throw new Exception("Solo se pueden generar facturas para órdenes aprobadas");
            }

            // Crear reporte
            ReportInvoicesDTO reportDTO = new ReportInvoicesDTO();
            reportDTO.setCodigoFactura("FACT-" + System.currentTimeMillis());
            reportDTO.setTotal(order.getTotalPrice());
            reportDTO.setFechaGeneracion(new java.sql.Date(System.currentTimeMillis()));
            reportDTO.setOrder(order);
            reportDTO.setUsuario(order.getCreatedBy());
            reportDTO.setEstado("Pagada");

            return reportInvoicesDao.createReportFacturacion(reportDTO);
        } catch (Exception e) {
            throw new Exception("Error al generar factura: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByOrder(Long orderId) throws Exception {
        try {
            return reportInvoicesDao.getReportsByOrderId(orderId);
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por orden: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByEstado(String estado) throws Exception {
        try {
            return reportInvoicesDao.getReportsByEstado(estado);
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por estado: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByFechaGeneracion(Date inicio, Date fin) throws Exception {
        try {
            return reportInvoicesDao.getReportsByFechaGeneracionBetween(inicio, fin);
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por fecha: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO updateReportEstado(Long id, String nuevoEstado) throws Exception {
        try {
            ReportInvoicesDTO reporte = reportInvoicesDao.getReportFacturacionById(id);
            if (reporte == null) {
                throw new Exception("Reporte no encontrado con ID: " + id);
            }

            reporte.setEstado(nuevoEstado);
            return reportInvoicesDao.updateReportFacturacion(reporte);
        } catch (Exception e) {
            throw new Exception("Error al actualizar estado del reporte: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO getReportByIdf(Long id) throws Exception {
        try {
            ReportInvoicesDTO reporte = reportInvoicesDao.getReportFacturacionById(id);
            if (reporte == null) {
                throw new Exception("Reporte no encontrado con ID: " + id);
            }
            return reporte;
        } catch (Exception e) {
            throw new Exception("Error al obtener reporte: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByUsuario(Long userId) throws Exception {
        try {
            // Verificar que el usuario existe
            UserDTO user = userDao.getUserById(userId);
            if (user == null) {
                throw new Exception("Usuario no encontrado con ID: " + userId);
            }

            return reportInvoicesDao.getReportsByUsuarioId(userId);
        } catch (Exception e) {
            throw new Exception("Error al obtener facturas por usuario: " + e.getMessage());
        }
    }

    public <T> ResponseEntity<T> ejecutarConPermiso(UserDTO user, String permission, Supplier<T> action) {
        try {
            validatePermission(user, permission);
            return ResponseEntity.ok(action.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    // Employee specific methods
    public String controlAccess(AccessRequestDTO accessDTO) throws Exception {
        // Implementation for access control
        return "Acceso registrado para área: " + accessDTO.getArea();
    }

    // Método para verificar disponibilidad de maquinaria
    public boolean isMachineryAvailable(Long machineryId) throws Exception {
        MachineryDTO machinery = this.getMachineryById(machineryId);
        return "Operativa".equals(machinery.getStatus());
    }

    // Método mejorado para crear reserva
    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception {
        // Validaciones adicionales pueden ir aquí
        return reservationDao.createReservation(reservationDTO);
    }

    // Método mejorado para login
    @Override
    public UserDTO login(String username, String password) throws Exception {
        UserDTO user = userDao.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("Credenciales inválidas");
        }
        return user;
    }

    public void createPerson(PersonDTO personDto) throws Exception {
        if (personDto.getDocument() == null) {
            throw new Exception("El documento no puede ser nulo");
        }
        if (personDao.existsByDocument(personDto.getDocument())) {
            throw new Exception("Ya existe una persona con el documento: " + personDto.getDocument());
        }
        personDao.createPerson(personDto);
    }

    public PersonDTO getPersonByDocument(Long document) throws Exception {
        return personDao.findByDocument(document);
    }

    public UserDTO getCurrentUser(String username) throws Exception {
        UserDTO user = userDao.findByUsername(username); // Usa tu DAO existente
        if (user == null) {
            throw new Exception("Usuario no encontrado");
        }
        return user;
    }

    public void deletePerson(PersonDTO personDto) throws Exception {
        // Validaciones básicas
        if (personDto == null || personDto.getDocument() == null) {
            throw new Exception("El DTO de persona o su documento no pueden ser nulos");
        }

        // Verificar si existe la persona
        if (!personDao.existsByDocument(personDto.getDocument())) {
            throw new Exception("No existe persona con documento: " + personDto.getDocument());
        }

        // 2. Verificar si tiene usuarios asociados
        if (userRepository.existsByPersonId(personDto.getId())) {
            throw new Exception("No se puede eliminar: La persona tiene usuarios asociados");
        }

        // Si pasa todas las validaciones, eliminar
        personDao.deletePerson(personDto);
    }

    public PersonDTO getPersonById(Long id) throws Exception {
        // Implementación que obtiene PersonDTO por ID de usuario
        UserDTO user = userDao.getUserById(id);
        if (user == null || user.getPerson() == null) {
            throw new Exception("Usuario o persona no encontrados");
        }
        return user.getPerson();
    }
}