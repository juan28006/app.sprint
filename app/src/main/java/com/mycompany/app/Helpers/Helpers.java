package com.mycompany.app.Helpers;

import com.mycompany.app.Dto.*;
import com.mycompany.app.model.*;
import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public abstract class Helpers {

    // Conversión de User a UserDTO
    public static UserDTO parse(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setTypeUser(parse(user.getTypeUser()));
        return userDTO;
    }

    // Conversión de UserDTO a User
    public static User parse(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setTypeUser(parse(userDTO.getTypeUser()));
        return user;
    }

    // Conversión de TypeUser a TypeUserDTO
    public static TypeUserDTO parse(TypeUser typeUser) {
        if (typeUser == null) {
            return null;
        }
        TypeUserDTO typeUserDTO = new TypeUserDTO();
        typeUserDTO.setId(typeUser.getId());
        typeUserDTO.setType(typeUser.getType());
        return typeUserDTO;
    }

    // Conversión de TypeUserDTO a TypeUser
    public static TypeUser parse(TypeUserDTO typeUserDTO) {
        if (typeUserDTO == null) {
            return null;
        }
        TypeUser typeUser = new TypeUser();
        typeUser.setId(typeUserDTO.getId());
        typeUser.setType(typeUserDTO.getType());
        return typeUser;
    }

    // Conversión de Inventory a InventoryDTO
    public static InventoryDTO parse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setId(inventory.getId());
        inventoryDTO.setEntryDate(inventory.getEntryDate());
        inventoryDTO.setStatus(inventory.getStatus());
        inventoryDTO.setUser(parse(inventory.getUser()));
        inventoryDTO.setQuantity(inventory.getQuantity());
        return inventoryDTO;
    }

    // Conversión de InventoryDTO a Inventory
    public static Inventory parse(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) {
            return null;
        }
        Inventory inventory = new Inventory();
        inventory.setId(inventoryDTO.getId());
        inventory.setEntryDate(inventoryDTO.getEntryDate());
        inventory.setStatus(inventoryDTO.getStatus());
        inventory.setUser(parse(inventoryDTO.getUser()));
        return inventory;
    }

    // Conversión de Machinery a MachineryDTO
    public static MachineryDTO parse(Machinery machinery) {
        if (machinery == null) {
            return null;
        }
        MachineryDTO machineryDTO = new MachineryDTO();
        machineryDTO.setId(machinery.getId());
        machineryDTO.setName(machinery.getName());
        machineryDTO.setStatus(machinery.getStatus());
        machineryDTO.setInventory(parse(machinery.getInventory()));
        return machineryDTO;
    }

    // Conversión de MachineryDTO a Machinery
    public static Machinery parse(MachineryDTO machineryDTO) {
        if (machineryDTO == null) {
            return null;
        }
        Machinery machinery = new Machinery();
        machinery.setId(machineryDTO.getId());
        machinery.setName(machineryDTO.getName());
        machinery.setStatus(machineryDTO.getStatus());
        machinery.setInventory(parse(machineryDTO.getInventory()));
        return machinery;
    }

    // Conversión de Reservation a ReservationDTO
    public static ReservationDTO parse(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setReservationDate(reservation.getReservationDate());
        reservationDTO.setStatus(reservation.getStatus());
        reservationDTO.setUser(parse(reservation.getUser()));
        reservationDTO.setMachinery(parse(reservation.getMachinery()));
        return reservationDTO;
    }

    // Conversión de ReservationDTO a Reservation
    public static Reservation parse(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setReservationDate(reservationDTO.getReservationDate());
        reservation.setStatus(reservationDTO.getStatus());
        reservation.setUser(parse(reservationDTO.getUser()));
        reservation.setMachinery(parse(reservationDTO.getMachinery()));
        return reservation;
    }

    // Conversión de Report a ReportDTO
    public static ReportDTO parse(Report report) {
        if (report == null) {
            return null;
        }
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(report.getId());
        reportDTO.setType(report.getType());
        reportDTO.setGenerationDate(report.getGenerationDate());
        reportDTO.setUser(parse(report.getUser()));
        return reportDTO;
    }

    // Conversión de ReportDTO a Report
    public static Report parse(ReportDTO reportDTO) {
        if (reportDTO == null) {
            return null;
        }
        Report report = new Report();
        report.setId(reportDTO.getId());
        report.setType(reportDTO.getType());
        report.setGenerationDate(reportDTO.getGenerationDate());
        report.setUser(parse(reportDTO.getUser())); // Asegúrate de que esto devuelva un User
        return report;
    }

    // Conversión de Permission a PermissionDTO
    public static PermissionDTO parse(Permission permission) {
        if (permission == null) {
            return null;
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permission.getId());
        permissionDTO.setName(permission.getName());
        return permissionDTO;
    }

    // Conversión de PermissionDTO a Permission
    public static Permission parse(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setId(permissionDTO.getId());
        permission.setName(permissionDTO.getName());
        return permission;
    }

    // Conversión de UserPermission a UserPermissionDTO
    public static UserPermissionDTO parse(UserPermission userPermission) {
        if (userPermission == null) {
            return null;
        }
        UserPermissionDTO userPermissionDTO = new UserPermissionDTO();
        userPermissionDTO.setId(userPermission.getId());
        userPermissionDTO.setUser(parse(userPermission.getUser()));
        userPermissionDTO.setPermission(parse(userPermission.getPermission()));
        return userPermissionDTO;
    }

    // Conversión de UserPermissionDTO a UserPermission
    public static UserPermission parse(UserPermissionDTO userPermissionDTO) {
        if (userPermissionDTO == null) {
            return null;
        }
        UserPermission userPermission = new UserPermission();
        userPermission.setId(userPermissionDTO.getId());
        userPermission.setUser(parse(userPermissionDTO.getUser()));
        userPermission.setPermission(parse(userPermissionDTO.getPermission()));
        return userPermission;
    }

    // Conversión de MachineryReservation a MachineryReservationDTO
    public static MachineryReservationDTO parse(MachineryReservation machineryReservation) {
        if (machineryReservation == null) {
            return null;
        }
        MachineryReservationDTO machineryReservationDTO = new MachineryReservationDTO();
        machineryReservationDTO.setId(machineryReservation.getId());
        machineryReservationDTO.setMachinery(parse(machineryReservation.getMachinery()));
        machineryReservationDTO.setReservation(parse(machineryReservation.getReservation()));
        return machineryReservationDTO;
    }

    // Conversión de MachineryReservationDTO a MachineryReservation
    public static MachineryReservation parse(MachineryReservationDTO machineryReservationDTO) {
        if (machineryReservationDTO == null) {
            return null;
        }
        MachineryReservation machineryReservation = new MachineryReservation();
        machineryReservation.setId(machineryReservationDTO.getId());
        machineryReservation.setMachinery(parse(machineryReservationDTO.getMachinery()));
        machineryReservation.setReservation(parse(machineryReservationDTO.getReservation()));
        return machineryReservation;
    }

    // Conversión de Order a OrderDTO
    public static OrderDTO parse(Order order) {
        if (order == null)
            return null;
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setCreatedBy(parse(order.getCreatedBy()));
        dto.setMachinery(parse(order.getMachinery()));
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalPrice(order.getUnitPrice() * order.getQuantity());
        return dto;
    }

    // Conversión de OrderDTO a Order
    public static Order parse(OrderDTO orderDTO) {
        if (orderDTO == null)
            return null;
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setOrderNumber(orderDTO.getOrderNumber());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setStatus(orderDTO.getStatus());
        order.setCreatedBy(parse(orderDTO.getCreatedBy()));
        order.setMachinery(parse(orderDTO.getMachinery()));
        order.setQuantity(orderDTO.getQuantity());
        order.setUnitPrice(orderDTO.getUnitPrice());
        return order;
    }
}