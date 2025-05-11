package com.mycompany.app.Helpers;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.MachineryDTO;
import com.mycompany.app.dto.MachineryReservationDTO;
import com.mycompany.app.dto.OrderDTO;
import com.mycompany.app.dto.PersonDTO;
import com.mycompany.app.dto.ReportInvoicesDTO;
import com.mycompany.app.dto.ReservationDTO;
import com.mycompany.app.dto.TypeUserDTO;
import com.mycompany.app.dto.UserDTO;
import com.mycompany.app.model.Inventory;
import com.mycompany.app.model.Machinery;
import com.mycompany.app.model.MachineryReservation;
import com.mycompany.app.model.Order;
import com.mycompany.app.model.Person;
import com.mycompany.app.model.ReportInvoices;
import com.mycompany.app.model.Reservation;
import com.mycompany.app.model.TypeUser;
import com.mycompany.app.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class Helpers {

    // Conversión de User a UserDTO
    public static UserDTO parse(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());

        if (user.getTypeUser() != null) {
            TypeUserDTO typeUserDTO = new TypeUserDTO();
            typeUserDTO.setId(user.getTypeUser().getId());
            typeUserDTO.setType(user.getTypeUser().getType());
            typeUserDTO.setPermissions(user.getTypeUser().getPermissions()); // ← No olvidar esto
            userDTO.setTypeUser(typeUserDTO);
        }
        if (user.getPerson() != null) {
            userDTO.setPerson(parse(user.getPerson())); // Asegúrate de tener este método
        }
        userDTO.setAuthenticated(user.isAuthenticated());
        return userDTO;
    }

    // Conversión de UserDTO a User
    public static User parse(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        if (userDTO.getTypeUser() != null) {
            TypeUser typeUser = new TypeUser();
            typeUser.setId(userDTO.getTypeUser().getId()); // Puede ser null
            typeUser.setType(userDTO.getTypeUser().getType());
            typeUser.setPermissions(userDTO.getTypeUser().getPermissions());
            user.setTypeUser(typeUser);
        }
        if (userDTO.getPerson() != null) {
            user.setPerson(new Person());
            user.getPerson().setId(userDTO.getPerson().getId()); // Solo mapear el ID
        }

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

    // Conversión de Inventory a InventoryDTO (actualizado)
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

        // Eliminado el mapeo de machineries para evitar recursión
        // inventoryDTO.setMachineries(...);

        return inventoryDTO;
    }

    // Conversión de InventoryDTO a Inventory (actualizado)
    public static Inventory parse(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) {
            return null;
        }
        Inventory inventory = new Inventory();
        inventory.setId(inventoryDTO.getId());
        inventory.setName(inventoryDTO.getName());
        inventory.setEntryDate(inventoryDTO.getEntryDate());
        inventory.setStatus(inventoryDTO.getStatus());
        inventory.setQuantity(inventoryDTO.getQuantity());

        // Cambio clave: Solo convertir el usuario si existe y tiene ID
        if (inventoryDTO.getUser() != null && inventoryDTO.getUser().getId() != null) {
            inventory.setUser(parse(inventoryDTO.getUser()));
        } else {
            inventory.setUser(null); // Asegurar que sea null explícitamente
        }

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

        // Crear un InventoryDTO simplificado sin relaciones circulares
        if (machinery.getInventory() != null) {
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setId(machinery.getInventory().getId());
            inventoryDTO.setEntryDate(machinery.getInventory().getEntryDate());
            inventoryDTO.setStatus(machinery.getInventory().getStatus());
            // No incluir user ni machineries para evitar recursión
            machineryDTO.setInventory(inventoryDTO);
        }

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

        // Cambio clave: Solo convertir el inventory si existe
        if (machineryDTO.getInventory() != null) {
            machinery.setInventory(parse(machineryDTO.getInventory()));
        } else {
            machinery.setInventory(null);
        }

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
        OrderDTO orderdto = new OrderDTO();
        orderdto.setId(order.getId());
        orderdto.setOrderNumber(order.getOrderNumber());
        orderdto.setOrderDate(order.getOrderDate());
        orderdto.setStatus(order.getStatus());
        orderdto.setCreatedBy(parse(order.getCreatedBy()));
        orderdto.setMachinery(parse(order.getMachinery()));
        orderdto.setQuantity(order.getQuantity());
        orderdto.setUnitPrice(order.getUnitPrice());
        orderdto.setTotalPrice(order.getUnitPrice() * order.getQuantity());
        return orderdto;
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

    // En Helpers.java
    public static ReportInvoicesDTO parse(ReportInvoices report) {
        if (report == null)
            return null;
        ReportInvoicesDTO reportInvoicesDTO = new ReportInvoicesDTO();
        reportInvoicesDTO.setId(report.getId());
        reportInvoicesDTO.setCodigoFactura(report.getCodigoFactura());
        reportInvoicesDTO.setTotal(report.getTotal());
        reportInvoicesDTO.setFechaGeneracion(report.getFechaGeneracion());
        reportInvoicesDTO.setOrder(parse(report.getOrder()));
        reportInvoicesDTO.setUsuario(parse(report.getUsuario()));
        reportInvoicesDTO.setEstado(report.getEstado());
        return reportInvoicesDTO;
    }

    public static ReportInvoices parse(ReportInvoicesDTO dto) {
        if (dto == null)
            return null;
        ReportInvoices report = new ReportInvoices();
        report.setId(dto.getId());
        report.setCodigoFactura(dto.getCodigoFactura());
        report.setTotal(dto.getTotal());
        report.setFechaGeneracion(dto.getFechaGeneracion());
        report.setOrder(parse(dto.getOrder()));
        report.setUsuario(parse(dto.getUsuario()));
        report.setEstado(dto.getEstado());
        return report;
    }

    public static PersonDTO parse(Person person) {
        if (person == null)
            return null;
        PersonDTO personDto = new PersonDTO();
        personDto.setId(person.getId());
        personDto.setDocument(person.getDocument());
        personDto.setName(person.getName());
        personDto.setCellphone(person.getCellphone());
        return personDto;
    }

    public static Person parse(PersonDTO personDto) {
        if (personDto == null)
            return null;
        Person person = new Person();
        person.setId(personDto.getId());
        person.setDocument(personDto.getDocument());
        person.setName(personDto.getName());
        person.setCellphone(personDto.getCellphone());
        return person;
    }

}