package com.mycompany.app.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.Controller.Validator.TypeUserValidator;
import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.dao.TypeUserImplementation;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.dao.interfaces.ReportDao;
import com.mycompany.app.dao.interfaces.ReportInvoicesDao;
import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.dao.interfaces.TypeUserDao;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.repositories.ReportInvoicesRepository;
import com.mycompany.app.dao.repositories.UserRepository;
import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.ReportDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.dto.UserDTO;
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
    TypeUserValidator typeUserValidator;

    @Autowired
    UserValidator userValidator;

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
            // 1. Buscar el tipo existente
            Optional<TypeUserDTO> existingType = typeUserDao.getAllTypeUsers().stream()
                    .filter(t -> type.equalsIgnoreCase(t.getType()))
                    .findFirst();

            if (existingType.isPresent()) {
                return existingType.get();
            }

            // 2. Crear nuevo tipo con permisos por defecto garantizados
            TypeUserDTO newType = new TypeUserDTO();
            newType.setType(type);

            // Asignar permisos por defecto si no se proporcionan
            String finalPermissions = (defaultPermissions != null && !defaultPermissions.trim().isEmpty())
                    ? defaultPermissions
                    : "Iniciar_sesion,Registro"; // Permisos mínimos

            newType.setPermissions(finalPermissions);

            // Validar
            typeUserValidator.validate(newType);

            // Crear en base de datos
            return typeUserDao.createTypeUser(newType);
        } catch (Exception e) {
            throw new Exception("Error al obtener/crear tipo de usuario '" + type + "': " + e.getMessage(), e);
        }
    }

    // Método para obtener permisos por defecto según tipo
    private String getDefaultPermissionsForType(String type) {
        switch (type.toLowerCase()) {
            case "admin":
                return "Generar_informes,Gestionar_inventario,Orden_maquinaria,Controlar_acceso,Consultar_disponibilidad,Iniciar_sesion,Registro";
            case "empleado":
                return "Consultar_disponibilidad,Controlar_acceso,Seleccionar_operacion,Iniciar_sesion,Registro,Reservar_maquinaria";
            case "cliente":
                return "Consultar_disponibilidad,Iniciar_sesion,Registro";
            default:
                return "Iniciar_sesion,Registro"; // Permisos mínimos por defecto
        }
    }

    // En InventoryService.java
    @Override
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        userValidator.validate(userDTO); // Usa el validador inyectado

        if (userDTO.getTypeUser() == null || userDTO.getTypeUser().getType() == null) {
            throw new Exception("Tipo de usuario no especificado");
        }

        TypeUserDTO typeUser = getOrCreateTypeUser(
                userDTO.getTypeUser().getType(),
                getDefaultPermissionsForType(userDTO.getTypeUser().getType()));

        userDTO.setTypeUser(typeUser);
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createAdmin(UserDTO userDTO) throws Exception {
        validateUserDTO(userDTO);
        TypeUserDTO adminType = getOrCreateTypeUser("Admin",
                "Generar_informes,Gestionar_inventario,Orden_maquinaria,Controlar_acceso,Consultar_disponibilidad,Iniciar_sesion,Registro");
        userDTO.setTypeUser(adminType);
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createEmpleado(UserDTO userDTO) throws Exception {
        validateUserDTO(userDTO);
        TypeUserDTO empleadoType = getOrCreateTypeUser("Empleado",
                "Consultar_disponibilidad,Controlar_acceso,Seleccionar_operacion,Iniciar_sesion,Registro,Reservar_maquinaria");
        userDTO.setTypeUser(empleadoType);
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createCliente(UserDTO userDTO) throws Exception {
        TypeUserDTO clienteType = getOrCreateTypeUser("Cliente",
                "Consultar_disponibilidad,Iniciar_sesion,Registro");
        userDTO.setTypeUser(clienteType);
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

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO, Long userId) throws Exception {
        if (reservationDTO == null) {
            throw new Exception("El DTO de reservación no puede ser nulo");
        }

        // Usa userDao en lugar de userRepository para mantener consistencia con DTOs
        UserDTO user = userDao.getUserById(userId);
        if (user == null) {
            throw new Exception("Usuario no encontrado");
        }

        // Verifica permisos (ajusta según la estructura de tus DTOs)
        String[] permissions = user.getTypeUser().getPermissions().split(",");
        if (!Arrays.asList(permissions).contains("Reservar_maquinaria")) {
            throw new Exception("No tienes permiso para reservar maquinaria");
        }

        try {
            return reservationDao.createReservation(reservationDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear reservación: " + e.getMessage());
        }
    }

    // Modifica assignDefaultPermissions para trabajar con DTOs
    public void assignDefaultPermissions(UserDTO userDTO) throws Exception {
        TypeUserDTO typeUserDTO = typeUserDao.getTypeUserById(userDTO.getTypeUser().getId());
        if (typeUserDTO.getPermissions() == null) {
            if ("Admin".equals(typeUserDTO.getType())) {
                typeUserDTO.setPermissions(
                        "Generar_informes,Gestionar_inventario,Orden_maquinaria," +
                                "Controlar_acceso,Consultar_disponibilidad,Seleccionar_operacion," +
                                "Iniciar_sesion,Registro");
            } else if ("Employee".equals(typeUserDTO.getType())) {
                typeUserDTO.setPermissions(
                        "Consultar_disponibilidad,Controlar_acceso," +
                                "Seleccionar_operacion,Iniciar_sesion,Registro,Reservar_maquinaria");
            }
            typeUserDao.updateTypeUser(typeUserDTO.getId(), typeUserDTO);
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
}