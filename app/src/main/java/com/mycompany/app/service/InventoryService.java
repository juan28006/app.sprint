package com.mycompany.app.service;

import com.mycompany.app.Dto.*;
import com.mycompany.app.dao.interfaces.InventoryDao;
import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.dao.interfaces.ReportDao;
import com.mycompany.app.dao.interfaces.MachineryDao;
import com.mycompany.app.dao.interfaces.OrderDao;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.dao.interfaces.TypeUserDao;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.app.service.Interface.UserService;
import com.mycompany.app.service.Interface.TypeUserService;
import com.mycompany.app.service.Interface.InventoryS;
import com.mycompany.app.service.Interface.ReservationService;
import com.mycompany.app.service.Interface.ReportService;
import com.mycompany.app.service.Interface.MachineryService;

import java.util.List;

import com.mycompany.app.service.Interface.OrderService;

@Getter
@Setter
@NoArgsConstructor
@Service
public class InventoryService implements UserService, TypeUserService, InventoryS, ReservationService, ReportService,
        MachineryService, OrderService {

    // ... (inyecciones de dependencias permanecen iguales)
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

    @Override
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        try {
            TypeUserDTO typeUserDTO = typeUserDao.getTypeUserById(userDTO.getTypeUser().getId());
            if (typeUserDTO == null) {
                throw new Exception("Tipo de usuario no encontrado con ID: " + userDTO.getTypeUser().getId());
            }

            UserDTO createdUser = userDao.createUser(userDTO);
            createdUser.setAuthenticated(true);
            return createdUser;
        } catch (Exception e) {
            throw new Exception("Error al crear usuario: " + e.getMessage());
        }
    }

    @Override
    public UserDTO createAdmin(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        try {
            TypeUserDTO adminType = typeUserDao.getTypeUserById(1L);
            if (adminType == null) {
                throw new Exception("Tipo de usuario 'Admin' no encontrado");
            }
            userDTO.setTypeUser(adminType);
            return userDao.createUser(userDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear administrador: " + e.getMessage());
        }
    }

    @Override
    public UserDTO createEmpleado(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        try {
            TypeUserDTO empleadoType = typeUserDao.getTypeUserById(2L);
            if (empleadoType == null) {
                throw new Exception("Tipo de usuario 'Empleado' no encontrado");
            }
            userDTO.setTypeUser(empleadoType);
            return userDao.createUser(userDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear empleado: " + e.getMessage());
        }
    }

    @Override
    public UserDTO createCliente(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new Exception("El DTO de usuario no puede ser nulo");
        }
        try {
            TypeUserDTO clienteType = typeUserDao.getTypeUserById(3L);
            if (clienteType == null) {
                throw new Exception("Tipo de usuario 'Cliente' no encontrado");
            }
            userDTO.setTypeUser(clienteType);
            return userDao.createUser(userDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear cliente: " + e.getMessage());
        }
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
    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws Exception {
        if (reservationDTO == null) {
            throw new Exception("El DTO de reservación no puede ser nulo");
        }
        try {
            return reservationDao.createReservation(reservationDTO);
        } catch (Exception e) {
            throw new Exception("Error al crear reservación: " + e.getMessage());
        }
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
}