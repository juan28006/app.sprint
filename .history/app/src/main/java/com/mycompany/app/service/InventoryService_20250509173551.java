package com.mycompany.app.service;

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
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.model.TypeUser;
import com.mycompany.app.service.Interface.InventoryS;
import com.mycompany.app.service.Interface.MachineryService;
import com.mycompany.app.service.Interface.OrderService;
import com.mycompany.app.service.Interface.ReportInvoicesService;
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
public class InventoryService implements UserService, TypeUserService, InventoryS, ReservationService,
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
                "Iniciar_sesion,Registro,Generar_informes,Gestionar_inventario,Orden_maquinaria,Consultar_disponibilidad");
        userDTO.setTypeUser(adminType);
        userDTO.setAuthenticated(true); // ← Cambio clave aquí
        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createEmpleado(UserDTO userDTO) throws Exception {
        // Asigna permisos de empleado
        TypeUserDTO empleadoType = getOrCreateTypeUser("Empleado",
                "Iniciar_sesion,Registro,Consultar_disponibilidad,Controlar_acceso,Agregar_maquinaria");
        userDTO.setTypeUser(empleadoType);

        return userDao.createUser(userDTO);
    }

    @Override
    public UserDTO createCliente(UserDTO userDTO) throws Exception {
        // Asigna permisos de cliente
        TypeUserDTO clienteType = getOrCreateTypeUser("Cliente",
                "Iniciar_sesion,Registro,Consultar_disponibilidad,Reservar_maquinaria");
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
        // Validación 1: Objeto no nulo
        if (inventoryDTO == null) {
            throw new Exception("El objeto InventoryDTO no puede ser nulo");
        }

        // Validación 2: Usuario asociado existe y está autenticado
        if (inventoryDTO.getUser() == null || inventoryDTO.getUser().getId() == null) {
            throw new Exception("El inventario debe estar asociado a un usuario válido");
        }

        // Obtener el usuario de la base de datos
        UserDTO user = userDao.getUserById(inventoryDTO.getUser().getId());

        // Validación 3: Usuario existe
        if (user == null) {
            throw new Exception("No se encontró usuario con ID: " + inventoryDTO.getUser().getId()
                    + ". Registre al usuario primero.");
        }

        // Validación 4: Usuario autenticado
        if (!user.isAuthenticated()) {
            throw new Exception(
                    "El usuario con ID: " + user.getId() + " no está autenticado. No puede crear inventarios.");
        }

        // Validación 5: Permisos del usuario
        if (!hasPermission(user, "Gestionar_inventario")) {
            throw new Exception("El usuario no tiene permisos para gestionar inventarios");
        }

        // Validación 6: Campos obligatorios del inventario
        if (inventoryDTO.getName() == null || inventoryDTO.getName().trim().isEmpty()) {
            throw new Exception("El nombre del inventario es obligatorio");
        }

        // Establecer estado por defecto si no viene
        if (inventoryDTO.getStatus() == null) {
            inventoryDTO.setStatus("Operativo");
        }
        // Si pasa todas las validaciones, crear el inventario
        try {
            InventoryDTO createdInventory = inventoryDao.createInventory(inventoryDTO);

            // Mensaje de éxito con detalles
            System.out.println("Inventario creado exitosamente por el usuario autenticado: " +
                    user.getUsername() + " (ID: " + user.getId() + ")");

            return createdInventory;
        } catch (Exception e) {
            throw new Exception("Error al guardar el inventario: " + e.getMessage());
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
            InventoryDTO existingInventory = inventoryDao.getInventoryById(id);
            if (existingInventory == null) {
                throw new Exception("Inventario no encontrado con ID: " + id);
            }

            // Conditional updates
            if (inventoryDTO.getName() != null) {
                existingInventory.setName(inventoryDTO.getName());
            }
            if (inventoryDTO.getStatus() != null) {
                existingInventory.setStatus(inventoryDTO.getStatus());
            }
            if (inventoryDTO.getEntryDate() != null) {
                existingInventory.setEntryDate(inventoryDTO.getEntryDate());
            }
            if (inventoryDTO.getUser() != null) {
                existingInventory.setUser(inventoryDTO.getUser());
            }
            if (inventoryDTO.getQuantity() != null) {
                existingInventory.setQuantity(inventoryDTO.getQuantity());
            }

            return inventoryDao.updateInventory(id, existingInventory); // Use existingInventory!

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
        ReservationDTO existingReservation = reservationDao.getReservationById(id);
        if (existingReservation == null) {
            throw new Exception("Reservation not found with ID: " + id);
        }

        // Conditional updates
        if (reservationDTO.getReservationDate() != null) {
            existingReservation.setReservationDate(reservationDTO.getReservationDate());
        }
        if (reservationDTO.getStatus() != null) {
            existingReservation.setStatus(reservationDTO.getStatus());
        }
        if (reservationDTO.getUser() != null) {
            existingReservation.setUser(reservationDTO.getUser());
        }
        if (reservationDTO.getMachinery() != null) {
            existingReservation.setMachinery(reservationDTO.getMachinery());
        }

        try {
            return reservationDao.updateReservation(id, existingReservation);
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

    @Override
    public ReportInvoicesDTO getReportById(Long id) throws Exception {
        try {
            ReportInvoicesDTO invoice = reportInvoicesDao.getReportById(id);
            if (invoice == null) {
                throw new Exception("Factura no encontrada con ID: " + id);
            }
            return invoice;
        } catch (Exception e) {
            throw new Exception("Error al obtener factura: " + e.getMessage());
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
        // Validación 1: Objeto no nulo
        if (machineryDTO == null) {
            throw new Exception("El objeto MachineryDTO no puede ser nulo");
        }

        // Validación 2: Inventario asociado
        if (machineryDTO.getInventory() == null || machineryDTO.getInventory().getId() == null) {
            throw new Exception("Se requiere un ID de inventario válido");
        }

        // Validación 3: Estado del inventario
        InventoryDTO inventory = inventoryDao.getInventoryById(machineryDTO.getInventory().getId());
        if (inventory == null) {
            throw new Exception("Inventario no encontrado con ID: " + machineryDTO.getInventory().getId());
        }
        if (!"Operativo".equalsIgnoreCase(inventory.getStatus())) {
            throw new Exception("El inventario asociado debe estar en estado 'Operativo'");
        }

        // Validación 4: Campos obligatorios
        if (machineryDTO.getName() == null || machineryDTO.getName().trim().isEmpty()) {
            throw new Exception("El nombre de la maquinaria es obligatorio");
        }

        // Establecer estado por defecto si no viene
        if (machineryDTO.getStatus() == null) {
            machineryDTO.setStatus("Disponible");
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
        MachineryDTO existingMachinery = machineryDao.getMachineryById(id);
        if (existingMachinery == null) {
            throw new Exception("Machinery not found with ID: " + id);
        }

        // Conditional updates
        if (machineryDTO.getName() != null) {
            existingMachinery.setName(machineryDTO.getName());
        }
        if (machineryDTO.getStatus() != null) {
            existingMachinery.setStatus(machineryDTO.getStatus());
        }
        if (machineryDTO.getInventory() != null) {
            existingMachinery.setInventory(machineryDTO.getInventory());
        }
        try {
            return machineryDao.updateMachinery(id, existingMachinery);
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

    public void validatePermission(UserDTO user, String requiredPermission) throws Exception {
        if (user == null || user.getTypeUser() == null) {
            throw new Exception("Usuario no autenticado");
        }

        String[] permissions = user.getTypeUser().getPermissions().split(",");
        if (!Arrays.asList(permissions).contains(requiredPermission)) {
            throw new Exception("Permiso requerido: " + requiredPermission);
        }
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
        // Validación 1: Objeto no nulo
        if (orderDTO == null) {
            throw new Exception("El objeto OrderDTO no puede ser nulo");
        }

        // Validación 2: Usuario asociado existe y está autenticado
        if (orderDTO.getCreatedBy() == null || orderDTO.getCreatedBy().getId() == null) {
            throw new Exception("La orden debe estar asociada a un usuario válido");
        }

        // Obtener el usuario de la base de datos
        UserDTO user = userDao.getUserById(orderDTO.getCreatedBy().getId());

        // Validación 3: Usuario existe
        if (user == null) {
            throw new Exception("No se encontró usuario con ID: " + orderDTO.getCreatedBy().getId()
                    + ". Registre al usuario primero.");
        }

        // Validación 4: Usuario autenticado
        if (!user.isAuthenticated()) {
            throw new Exception("El usuario con ID: " + user.getId() + " no está autenticado. No puede crear órdenes.");
        }

        // Validación 5: Permisos del usuario
        if (!hasPermission(user, "Orden_maquinaria")) {
            throw new Exception("El usuario no tiene permisos para crear órdenes");
        }

        // Validación 6: Maquinaria existe
        MachineryDTO machinery = machineryDao.getMachineryById(orderDTO.getMachinery().getId());
        if (machinery == null) {
            throw new Exception("No se encontró maquinaria con ID: " + orderDTO.getMachinery().getId());
        }

        // Validación 7: Campos obligatorios
        if (orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero");
        }

        if (orderDTO.getUnitPrice() == null || orderDTO.getUnitPrice() <= 0) {
            throw new Exception("El precio unitario debe ser mayor a cero");
        }

        // Si pasa todas las validaciones, crear la orden
        try {
            OrderDTO createdOrder = orderDao.createOrder(orderDTO);

            System.out.println("Orden creada exitosamente por el usuario autenticado: " +
                    user.getUsername() + " (ID: " + user.getId() + ")");

            return createdOrder;
        } catch (Exception e) {
            throw new Exception("Error al crear la orden: " + e.getMessage());
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
    public ReportInvoicesDTO generateReportInvoices(ReportInvoicesDTO reportDTO) throws Exception {
        // Validación 1: Objeto no nulo
        if (reportDTO == null) {
            throw new Exception("El objeto ReportInvoicesDTO no puede ser nulo");
        }

        // Validación 2: Orden asociada existe
        if (reportDTO.getOrder() == null || reportDTO.getOrder().getId() == null) {
            throw new Exception("El reporte debe estar asociado a una orden válida");
        }

        // Obtener la orden de la base de datos
        OrderDTO order = orderDao.getOrderById(reportDTO.getOrder().getId());

        // Validación 3: Orden existe
        if (order == null) {
            throw new Exception("No se encontró orden con ID: " + reportDTO.getOrder().getId());
        }

        // Validación 4: Orden está aprobada
        if (!"Aprobada".equals(order.getStatus())) {
            throw new Exception("Solo se pueden generar facturas para órdenes aprobadas");
        }

        // Validación 5: Usuario asociado existe y está autenticado
        if (reportDTO.getUsuario() == null || reportDTO.getUsuario().getId() == null) {
            throw new Exception("El reporte debe estar asociado a un usuario válido");
        }

        UserDTO user = userDao.getUserById(reportDTO.getUsuario().getId());

        // Validación 6: Usuario existe
        if (user == null) {
            throw new Exception("No se encontró usuario con ID: " + reportDTO.getUsuario().getId());
        }

        // Validación 7: Usuario autenticado
        if (!user.isAuthenticated()) {
            throw new Exception("El usuario con ID: " + user.getId() +
                    " no está autenticado. No puede generar facturas.");
        }

        // Validación 8: Permisos del usuario
        if (!hasPermission(user, "Generar_informes")) {
            throw new Exception("El usuario no tiene permisos para generar facturas");
        }

        // Validación 9: Campos obligatorios del reporte
        if (reportDTO.getCodigoFactura() == null || reportDTO.getCodigoFactura().trim().isEmpty()) {
            throw new Exception("El código de factura es obligatorio");
        }

        if (reportDTO.getTotal() == null || reportDTO.getTotal() <= 0) {
            throw new Exception("El total debe ser mayor a cero");
        }

        if (reportDTO.getFechaGeneracion() == null) {
            throw new Exception("La fecha de generación es obligatoria");
        }

        // Validación 10: Coherencia entre el total del reporte y el total de la orden
        if (!order.getTotalPrice().equals(reportDTO.getTotal())) {
            throw new Exception("El total de la factura no coincide con el total de la orden");
        }

        // Si pasa todas las validaciones, crear el reporte de factura
        try {
            // Establecer estado por defecto si no viene
            if (reportDTO.getEstado() == null) {
                reportDTO.setEstado("Pendiente");
            }

            ReportInvoicesDTO createdInvoice = reportInvoicesDao.createReportFacturacion(reportDTO);

            System.out.println("Factura generada exitosamente por el usuario autenticado: " +
                    user.getUsername() + " (ID: " + user.getId() + ")");
            System.out.println("Detalles de la factura:");
            System.out.println("- Código: " + createdInvoice.getCodigoFactura());
            System.out.println("- Total: " + createdInvoice.getTotal());
            System.out.println("- Fecha: " + createdInvoice.getFechaGeneracion());
            System.out.println("- Estado: " + createdInvoice.getEstado());

            return createdInvoice;
        } catch (Exception e) {
            throw new Exception("Error al generar la factura: " + e.getMessage());
        }
    }

    @Override
    public ReportInvoicesDTO updateReportFacturacion(Long id, ReportInvoicesDTO reportDTO) throws Exception {
        // Validaciones básicas
        if (id == null || id <= 0) {
            throw new Exception("ID de factura inválido");
        }
        if (reportDTO == null) {
            throw new Exception("Datos de factura no proporcionados");
        }

        try {
            // Obtener factura existente
            ReportInvoicesDTO existingInvoice = reportInvoicesDao.getReportById(id);
            if (existingInvoice == null) {
                throw new Exception("Factura no encontrada con ID: " + id);
            }

            // Validar estado si se está actualizando
            if (reportDTO.getEstado() != null) {
                if ("Pagada".equalsIgnoreCase(reportDTO.getEstado())) {
                    // Validar que la orden asociada existe y está aprobada
                    if (existingInvoice.getOrder() == null || existingInvoice.getOrder().getId() == null) {
                        throw new Exception("No se puede marcar como pagada: La factura no tiene orden asociada");
                    }

                    OrderDTO order = orderDao.getOrderById(existingInvoice.getOrder().getId());
                    if (!"Aprobada".equalsIgnoreCase(order.getStatus())) {
                        throw new Exception("No se puede marcar como pagada: La orden asociada no está aprobada");
                    }
                }
                existingInvoice.setEstado(reportDTO.getEstado());
            }

            // Actualizar otros campos si vienen
            if (reportDTO.getCodigoFactura() != null) {
                existingInvoice.setCodigoFactura(reportDTO.getCodigoFactura());
            }
            if (reportDTO.getTotal() != null) {
                existingInvoice.setTotal(reportDTO.getTotal());
            }

            // Enviar al DAO para actualización
            return reportInvoicesDao.updateReportFacturacion(id, existingInvoice);
        } catch (Exception e) {
            throw new Exception("Error al actualizar factura: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByStatus(String estado) throws Exception {
        try {
            return reportInvoicesDao.getReportsByEstado(estado);
        } catch (Exception e) {
            throw new Exception("Error al obtener reportes por estado: " + e.getMessage());
        }
    }

    // InventoryService.java
    @Override
    public void deleteReportInvoices(Long id) throws Exception {
        try {
            reportInvoicesDao.deleteReportInvoices(id);
        } catch (Exception e) {
            throw new Exception("Error al eliminar factura en el servicio: " + e.getMessage());
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

    @Override
    public List<OrderDTO> getOrdersByInventoryId(Long inventoryId) throws Exception {
        try {
            return orderDao.getOrdersByInventoryId(inventoryId);
        } catch (Exception e) {
            throw new Exception("Error al obtener órdenes por ID de inventario: " + e.getMessage());
        }
    }

    @Override
    public List<ReportInvoicesDTO> getReportsByOrderId(Long orderId) throws Exception {
        try {
            return reportInvoicesDao.getReportsByOrderId(orderId);
        } catch (Exception e) {
            throw new Exception("Error al obtener facturas por ID de orden: " + e.getMessage());
        }
    }

}
