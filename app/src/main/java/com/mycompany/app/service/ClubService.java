package com.mycompany.app.service;

import java.sql.Date;

import com.mycompany.app.dao.interfaces.ReservationDao;
import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.ReservationDTO;
import com.mycompany.app.Dto.PermissionDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;

import com.mycompany.app.dao.interfaces.GuestDao;
import com.mycompany.app.dao.interfaces.InvoiceDao;
import com.mycompany.app.dao.interfaces.InvoiceDetailDao;
import com.mycompany.app.dao.interfaces.PartnerDao;
import com.mycompany.app.service.Interface.Loginservice;
import com.mycompany.app.service.Interface.Adminservice;
import com.mycompany.app.service.Interface.Partnerservice;
import java.util.ArrayList;

import java.util.List;
// veriificar si esta buena la conexion a la base de datos y se guardena alli 
import com.mycompany.app.dao.interfaces.UserDao;

import com.mycompany.app.service.Interface.Guestservice;
import com.mycompany.app.service.Interface.ReservationService;
import com.mycompany.app.service.Interface.Invoiceservice;
import com.mycompany.app.service.Interface.Personservice;
import com.mycompany.app.service.Interface.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class ClubService implements Adminservice, Loginservice, Partnerservice, Guestservice, Invoiceservice,
        ReservationService, UserService, Personservice {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReservationDao personDao;

    @Autowired
    public PartnerDao partnerDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private GuestDao guestDao;

    @Autowired
    private InvoiceDetailDao invoiceDetailDao;

    public static ReservationDTO user;

    @Override
    // Solicitud de suscripción VIP
    public void requestVIPSubscription(ReservationDTO userDto) throws Exception {
        PermissionDTO partnerDto = partnerDao.findPartnerById(userDto.getId());

        // Verificar si ya es VIP
        if (partnerDto.getTypeSuscription().equalsIgnoreCase("vip")) {
            throw new Exception("Ya eres un socio VIP.");
        }

        // Verificar disponibilidad de cupos
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles en este momento.");
        }

        // Actualizar PartnerDTO con la solicitud VIP
        partnerDto.setDateCreated(new Date(System.currentTimeMillis()));

        // Actualizar el socio en la base de datos
        partnerDao.updatePartner(partnerDto);
    }

    @Override

    // obtener Solicitudes VIP Pendientes
    public List<PermissionDTO> getPendingVIPRequests() throws Exception {
        try {
            return partnerDao.getPendingVIPRequests();
        } catch (Exception e) {
            throw new Exception(" erro, no se encontraron solicitudes  vip pendientes");
        }
    }

    @Override
    // Aprobar solicitud VIP
    public void approveVIPRequest(long partnerId) throws Exception {
        PermissionDTO partnerDto = partnerDao.findPartnerById(partnerId);

        // Verificar si aún hay cupos disponibles
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles.");
        }

        partnerDto.setTypeSuscription("vip");
        System.out.println(" aprobada ");

        partnerDao.updatePartner(partnerDto);
    }

    // Rechazar solicitud VIP
    @Override
    public void rejectVIPRequest(long partnerId) throws Exception {
        PermissionDTO partnerDto = partnerDao.findPartnerById(partnerId);

        if (partnerDto == null) {
            throw new Exception("No se encontró el socio.");
        }

        partnerDao.updatePartner(partnerDto);
    }

    @Override
    // obtener Facturas Totales Pagadas
    public double getTotalPaidInvoices(long id) throws Exception {
        List<MachineryDTO> paidInvoices = partnerDao.getPaidInvoices(id);
        return paidInvoices.stream().mapToDouble(MachineryDTO::getAmount).sum();
    }

    @Override
    public List<MachineryDTO> getPartnerInvoices(long partnerId) throws Exception {
        PermissionDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        return invoiceDao.getInvoicesPartner(partnerId);
    }

    // Método para crear factura
    @Override
    public void createInvoice(MachineryDTO invoiceDto) throws Exception {
        if (invoiceDto.getPartnerId() == null && invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un socio o un invitado ");
        }
        invoiceDao.createAllInvoices(invoiceDto);

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PermissionDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.PartnerFunds(partner);
        }
    }

    @Override
    public void payInvoices(long userId) throws Exception {
        try {
            // Validar que el userId sea válido
            if (userId <= 0) {
                throw new Exception("ID de usuario inválido");
            }

            // Validar que el usuario existe
            ReservationDTO userDTO = userDao.findUserById(userId);
            if (userDTO == null) {
                throw new Exception("Usuario no encontrado");
            }

            // Obtener el socio basado en el ID de usuario
            PermissionDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual");
            }

            // Obtener las facturas pendientes
            List<MachineryDTO> unpaidInvoices = invoiceDao.UnpaidInvoicesPartner(partnerDTO.getId());
            if (unpaidInvoices.isEmpty()) {
                throw new Exception("No hay facturas pendientes de pago");
            }

            // Calcular el monto total a pagar
            double totalAmount = 0;
            for (MachineryDTO invoice : unpaidInvoices) {
                totalAmount += invoice.getAmount();
            }

            // Verificar si hay fondos suficientes
            if (partnerDTO.getfundsMoney() < totalAmount) {
                throw new Exception("Fondos insuficientes. Total a pagar: $ " + totalAmount + "\n"
                        + ", Fondos disponibles: $" + partnerDTO.getfundsMoney());
            }

            // Procesar el pago de todas las facturas
            for (MachineryDTO invoice : unpaidInvoices) {
                invoice.setStatus(true);
                invoiceDao.updateInvoice(invoice);
            }

            // Actualizar los fondos del socio
            double newFunds = partnerDTO.getfundsMoney() - totalAmount;
            partnerDTO.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partnerDTO);

            System.out.println("Pago exitoso. Se pagaron " + unpaidInvoices.size()
                    + " facturas por un total de $" + totalAmount);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override

    // Facturas pagadas
    public List<MachineryDTO> PaidInvoices(long partnerId) throws Exception {
        PermissionDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        List<MachineryDTO> allInvoices = invoiceDao.getInvoicesPartner(partnerId);
        List<MachineryDTO> paidInvoices = new ArrayList<>();
        for (MachineryDTO invoiceDto : allInvoices) {
            if (!invoiceDto.isStatus()) {
            } else {
                paidInvoices.add(invoiceDto);
            }
        }
        return paidInvoices;

    }

    @Override
    // obtener facturas pendientes

    public List<MachineryDTO> getPendingInvoices(long partnerId) throws Exception {
        PermissionDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        List<MachineryDTO> allInvoices = invoiceDao.getInvoicesPartner(partnerId);
        List<MachineryDTO> pendingInvoices = new ArrayList<>();
        for (MachineryDTO invoice : allInvoices) {
            if (!invoice.isStatus()) {
                pendingInvoices.add(invoice);
            }
        }
        return pendingInvoices;
    }

    @Override
    // Obtener facturas pagadas
    public List<MachineryDTO> getPaidInvoices(long partnerId) throws Exception {

        return PaidInvoices(partnerId);

    }

    @Override
    public List<MachineryDTO> getGuestInvoices(long guestId) throws Exception {
        try {
            // Validar que existe el invitado
            MachineryDTO guest = guestDao.findGuestById(guestId);
            if (guest == null) {
                throw new Exception("Invitado no encontrado con ID: " + guestId);
            }

            // Validar que tiene un socio asociado
            PermissionDTO partner = guest.getPartnerId();
            if (partner == null) {
                throw new Exception("El invitado no tiene un socio responsable asociado");
            }

            // Obtener todas las facturas del invitado
            List<MachineryDTO> guestInvoices = invoiceDao.getInvoicesByGuestId(guestId);

            // Si no hay facturas, devolver lista vacía en lugar de null
            if (guestInvoices == null) {
                return new ArrayList<>();
            }

            return guestInvoices;
        } catch (Exception e) {
            throw new Exception("Error al obtener las facturas del invitado: " + e.getMessage());
        }
    }

    //
    @Override
    public List<MachineryDTO> getGuestPendingInvoice(long guesId) throws Exception {
        try {
            // Validar que existe el invitado
            MachineryDTO guest = guestDao.findGuestById(guesId);
            if (guest == null) {
                throw new Exception("Invitado no encontrado con ID: " + guesId);
            }

            // Validar que tiene un socio asociado
            PermissionDTO partner = guest.getPartnerId();
            if (partner == null) {
                throw new Exception("El invitado no tiene un socio responsable asociado");
            }

            // Obtener todas las facturas del invitado
            List<MachineryDTO> allGuestInvoices = guestDao.getGuestPendingInvoices(guesId);

            // Filtrar solo las facturas pendientes (status = false)
            List<MachineryDTO> pendingInvoices = new ArrayList<>();
            for (MachineryDTO invoice : allGuestInvoices) {
                if (!invoice.isStatus()) {
                    pendingInvoices.add(invoice);
                }
            }

            return pendingInvoices;
        } catch (Exception e) {
            throw new Exception("Error al obtener las facturas pendientes del invitado: " + e.getMessage());
        }
    }

    @Override
    public List<MachineryDTO> getGuestPaidInvoices(long guestId) throws Exception {
        try {
            // Validar que existe el invitado
            MachineryDTO guest = guestDao.findGuestById(guestId);
            if (guest == null) {
                throw new Exception("Invitado no encontrado con ID: " + guestId);
            }

            // Validar que tiene un socio asociado
            PermissionDTO partner = guest.getPartnerId();
            if (partner == null) {
                throw new Exception("El invitado no tiene un socio responsable asociado");
            }

            // Obtener todas las facturas del invitado
            List<MachineryDTO> allGuestInvoices = guestDao.getGuestPaidInvoices(guestId);

            // Filtrar solo las facturas pagadas (status = true)
            List<MachineryDTO> paidInvoices = new ArrayList<>();
            for (MachineryDTO invoice : allGuestInvoices) {
                if (invoice.isStatus()) {
                    paidInvoices.add(invoice);
                }
            }

            return paidInvoices;
        } catch (Exception e) {
            throw new Exception("Error al obtener las facturas pagadas del invitado: " + e.getMessage());
        }
    }

    @Override
    public double getTotalInvoicesAmount(long partnerId) throws Exception {
        double totalAmount = 0;

        try {
            // Obtener el socio
            PermissionDTO partner = partnerDao.findPartnerById(partnerId);
            if (partner == null) {
                throw new Exception("Socio no encontrado con ID: " + partnerId);
            }

            // 1. Obtener todas las facturas del socio
            List<MachineryDTO> partnerInvoices = invoiceDao.getInvoicesPartner(partnerId);
            for (MachineryDTO invoice : partnerInvoices) {
                totalAmount += invoice.getAmount();
            }

            // 2. Obtener todos los invitados del socio
            List<MachineryDTO> guests = guestDao.findGuestsByPartnerId(partnerId);

            // 3. Para cada invitado, obtener sus facturas
            for (MachineryDTO guest : guests) {
                List<MachineryDTO> guestInvoices = invoiceDao.getInvoicesByGuestId(guest.getId());
                for (MachineryDTO invoice : guestInvoices) {
                    totalAmount += invoice.getAmount();
                }
            }

            return totalAmount;

        } catch (Exception e) {
            throw new Exception("Error al calcular el total de facturas: " + e.getMessage());
        }
    }

    // activar invitado
    @Override
    public void activateGuest(long guestId, long partnerId) throws Exception {
        try {
            // Obtener el socio
            PermissionDTO currentPartner = partnerDao.findPartnerById(partnerId);
            if (currentPartner == null) {
                throw new Exception("No se encontró el socio asociado.");
            }

            // Obtener el invitado
            MachineryDTO guestDTO = guestDao.findGuestById(guestId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID: " + guestId);
            }

            // Verificar que el invitado pertenece al socio actual
            if (guestDTO.getPartnerId() == null) {
                throw new Exception("El invitado no está asociado a su cuenta.");
            }

            // Verificar si ya está activo
            if (guestDTO.isStatus()) {
                throw new Exception("El invitado ya se encuentra activo.");
            }

            // Verificar facturas pendientes del socio responsable
            List<MachineryDTO> unpaidPartnerInvoices = invoiceDao.UnpaidInvoicesPartner(partnerId);
            if (!unpaidPartnerInvoices.isEmpty()) {
                double totalPartnerPending = 0;
                for (MachineryDTO invoice : unpaidPartnerInvoices) {
                    totalPartnerPending += invoice.getAmount();
                }
                throw new Exception("No se puede activar el invitado.\n"
                        + "El socio responsable tiene facturas pendientes\n"
                        + "por un monto total de $" + totalPartnerPending + ".\n");
            }

            // Si pasa todas las validaciones, activar el invitado
            guestDTO.setStatus(true);
            guestDao.updateGuest(guestDTO);

        } catch (Exception e) {
            throw new Exception("Error al activar el invitado: " + e.getMessage());
        }
    }

    @Override
    public void desactivateGuest(long guestId, long partnerId) throws Exception {
        try {
            // Obtener el socio
            PermissionDTO currentPartner = partnerDao.findPartnerById(partnerId);
            if (currentPartner == null) {
                throw new Exception("No se encontró el socio asociado.");
            }

            // Obtener el invitado
            MachineryDTO guestDTO = guestDao.findGuestById(guestId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID: " + guestId);
            }

            // Verificar que el invitado pertenece al socio actual
            if (guestDTO.getPartnerId() == null) {
                throw new Exception("El invitado no está asociado a su cuenta.");
            }

            // Desactivar el invitado
            guestDTO.setStatus(false);
            guestDao.updateGuest(guestDTO);

        } catch (Exception e) {
            throw new Exception("Error al desactivar el invitado: " + e.getMessage());
        }
    }

    // baja del socio
    @Override
    public void lowPartner(long userId) throws Exception {
        // Validar que el userId no sea 0 o negativo
        if (userId <= 0) {
            throw new Exception("El ID del usuario no puede ser un valor vacío o inválido");
        }

        // Buscar el usuario primero para validar que existe
        ReservationDTO userDTO = userDao.findUserById(userId);
        if (userDTO == null) {
            throw new Exception("No se encontró ningún usuario con el ID: " + userId);
        }

        PermissionDTO partner = partnerDao.findPartnerByUserId(userId);
        if (partner == null) {
            throw new Exception("No se encontró socio para el ID de usuario proporcionado: " + userId);
        }

        long partnerId = partner.getId();

        // Verificar facturas sin pagar
        List<MachineryDTO> unpaidInvoices = invoiceDao.UnpaidInvoicesPartner(partnerId);
        if (!unpaidInvoices.isEmpty()) {
            throw new Exception("El socio tiene facturas pendientes de pago.");
        }

        try {
            // Eliminar todas las facturas pagadas (si existen)
            try {
                invoiceDao.deleteAllInvoicesByPartnerId(partnerId);
            } catch (Exception e) {
                System.out.println("No se encontraron facturas asociadas al socio.");
            }

            // Eliminar todos los invitados
            List<MachineryDTO> guests = guestDao.findGuestsByPartnerId(partnerId);
            for (MachineryDTO guest : guests) {
                try {
                    invoiceDao.deleteAllInvoicesByGuestId(guest.getId());
                } catch (Exception e) {
                    System.out.println("No se encontraron facturas asociadas al invitado: " + guest.getId());
                }
                guestDao.deleteGuest(guest.getId());
                userDao.deleteUser(guest.getUserId().getId());
            }

            // Eliminar el socio
            partnerDao.deletePartner(partnerId);

            // Eliminar el usuario asociado al socio
            userDao.deleteUser(userId);

            // Eliminar la persona asociada al usuario
            ReportDTO personToDelete = partner.getUserId().getPersonId();
            personDao.deletePerson(personToDelete);

            System.out.println("La cuenta del socio con ID " + partnerId + " se eliminó correctamente.");
        } catch (Exception e) {
            throw new Exception("Se produjo un error al procesar la eliminación de la cuenta del socio con ID "
                    + partnerId + ": " + e.getMessage());
        }
    }

    @Override
    public void requestPromotion(long userId) throws Exception {
        try {
            // Encontrar el socio basado en el ID de usuario
            PermissionDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual. ID de usuario: " + userId);
            }

            // Verificar si el socio ya es VIP
            if ("vip".equalsIgnoreCase(partnerDTO.getTypeSuscription())) {
                throw new Exception("El socio ya tiene una suscripción VIP");
            }

            // Verificar si hay una solicitud VIP pendiente
            if ("vip_pendiente".equalsIgnoreCase(partnerDTO.getTypeSuscription())) {
                throw new Exception("Ya existe una solicitud de promoción pendiente para este socio");
            }

            // Verificar facturas pendientes
            List<MachineryDTO> unpaidInvoices = getPendingInvoices(partnerDTO.getId());
            if (!unpaidInvoices.isEmpty()) {
                double totalPending = 0;
                for (MachineryDTO invoice : unpaidInvoices) {
                    totalPending += invoice.getAmount();
                }
                throw new Exception("No se puede procesar la solicitud VIP. \n El socio tiene " + unpaidInvoices.size()
                        + " facturas pendientes.\n por un monto total de $" + totalPending + ". \n"
                        + "Debe pagar todas las facturas pendientes antes de solicitar la promoción.");
            }

            // Crear la solicitud de promoción
            partnerDTO.setTypeSuscription("vip_pendiente");
            partnerDTO.setDateCreated(new Date(System.currentTimeMillis()));
            partnerDao.updatePartner(partnerDTO);

            System.out.println("Solicitud de promoción creada exitosamente. Estado: Pendiente");
        } catch (Exception e) {
            throw new Exception("Error al procesar la solicitud de promoción: \n" + e.getMessage());
        }
    }

    @Override
    public PermissionDTO findPartnerById(long partnerId) throws Exception {
        // busca directamente por el ID de socio.
        // en algunos metodos que cumplen reglas de negocio del socio

        try {
            return partnerDao.findPartnerById(partnerId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el socio: " + e.getMessage());
        }
    }

    // Nuevo método para encontrar un socio por ID de usuario
    @Override
    public PermissionDTO findPartnerByUserId(long userId) throws Exception {
        // con este metodo busco por medio del ID del usuario, el ID del socio y
        // este despues llevarlo a los metodos para realizar los procesos de negocios
        try {
            return partnerDao.findPartnerByUserId(userId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el socio por ID de usuario: " + e.getMessage());
        }
    }

    @Override
    public void uploadFunds(long userId, double amount) throws Exception {
        try {
            // 1. Validar usuario
            ReservationDTO userDTO = userDao.findUserById(userId);
            if (userDTO == null) {
                throw new Exception("Usuario no encontrado");
            }

            // 2. Validar que el usuario tiene datos de persona
            // 3. Validar que existe el socio
            PermissionDTO partnerDto = findPartnerByUserId(userId);
            if (partnerDto == null) {
                throw new Exception("No se encontró el socio para el ID de usuario: " + userId);
            }

            // 4. Validar límites de suscripción
            double maxFunds = partnerDto.getTypeSuscription().equalsIgnoreCase("vip") ? 5000000 : 1000000;
            double newFunds = partnerDto.getfundsMoney() + amount;

            if (amount > maxFunds) {
                throw new Exception("El monto total excedería el límite máximo de " + maxFunds
                        + " para su tipo de suscripción (" + partnerDto.getTypeSuscription() + ")");
            }

            // 5. Actualizar fondos
            partnerDto.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partnerDto);

        } catch (Exception e) {
            throw new Exception("Error al procesar la carga de fondos: " + e.getMessage());
        }
    }

    @Override
    public void createPartner(ReservationDTO userDTO, ReportDTO personDTO) throws Exception {
        try {
            // Create the person

            if (personDao.existsByDocument(personDTO)) {
                throw new Exception("Ya existe una persona con ese documento");
            }
            personDao.createPerson(personDTO);

            // Create the user
            if (userDao.existsByUserName(userDTO)) {
                personDao.deletePerson(personDTO);
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }
            userDao.createUser(userDTO);

            // Create the partner
            PermissionDTO partnerDTO = new PermissionDTO();
            partnerDTO.setUserId(userDTO);
            partnerDTO.setfundsMoney(50000); // Initial fund for regular partners
            partnerDTO.setTypeSuscription("regular");
            partnerDTO.setDateCreated(new Date(System.currentTimeMillis()));
            partnerDao.createPartner(partnerDTO, userDTO);
        } catch (Exception e) {

            throw new Exception("Error al crear el socio: " + e.getMessage());
        }
    }

    @Override
    public List<MachineryDTO> getAllInvoices(long userId) throws Exception {
        try {
            // Lista para almacenar todas las facturas
            List<MachineryDTO> allInvoices = new ArrayList<>();

            // Obtener el socio por userId
            PermissionDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario con ID: " + userId);
            }

            // Obtener todas las facturas del socio
            List<MachineryDTO> partnerInvoices = invoiceDao.getInvoicesPartner(partnerDTO.getId());
            if (partnerInvoices != null) {
                allInvoices.addAll(partnerInvoices);
            }

            // Obtener todos los invitados asociados al socio
            List<MachineryDTO> guests = guestDao.findGuestsByPartnerId(partnerDTO.getId());

            // Para cada invitado, obtener sus facturas
            for (MachineryDTO guest : guests) {
                List<MachineryDTO> guestInvoices = invoiceDao.getInvoicesByGuestId(guest.getId());
                if (guestInvoices != null) {
                    allInvoices.addAll(guestInvoices);
                }
            }

            // Si no se encontraron facturas, devolver lista vacía
            if (allInvoices.isEmpty()) {
                return new ArrayList<>();
            }

            return allInvoices;

        } catch (Exception e) {
            throw new Exception("Error al obtener todas las facturas: " + e.getMessage());
        }

    }

    @Override
    public void createInvoicePartner(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception {
        // Validación explícita del socio
        if (invoiceDto.getPartnerId() == null) {
            throw new Exception("La factura debe estar asociada a un socio");
        }

        // Verificar que el socio existe en la base de datos
        PermissionDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
        if (partner == null) {
            throw new Exception("El socio asociado a la factura no existe");
        }

        try {
            // Calcular el total de la factura
            double total = 0;
            for (InventoryDTO detail : details) {
                total += detail.getAmount();
            }
            invoiceDto.setAmount(total);

            // Verificar fondos suficientes
            if (partner.getfundsMoney() < total) {
                throw new Exception("Fondos insuficientes para crear la factura. Total: " + total
                        + ", Fondos disponibles: " + partner.getfundsMoney());
            }

            // Crear la factura
            long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
            invoiceDto.setId(invoiceId);

            // Crear los detalles de la factura
            for (InventoryDTO detail : details) {
                detail.setInvoiceId(invoiceDto);
                invoiceDetailDao.createInvoiceDetail(detail);
            }

            // Actualizar los fondos del socio
            double newFunds = partner.getfundsMoney() - total;
            partner.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partner);

            System.out.println("Factura creada exitosamente con ID: " + invoiceId + " y monto total: " + total);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void createInvoiceGuest(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception {
        // Validación explícita del invitado y socio
        if (invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un invitado");
        }

        if (invoiceDto.getPartnerId() == null) {
            throw new Exception("La factura debe estar asociada a un socio responsable");
        }

        // Verificar que el invitado existe en la base de datos
        MachineryDTO guest = guestDao.findGuestById(invoiceDto.getGuestid().getId());
        if (guest == null) {
            throw new Exception("El invitado asociado a la factura no existe");
        }

        // Verificar que el socio existe y es el responsable del invitado
        PermissionDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
        if (partner == null) {
            throw new Exception("El socio asociado a la factura no existe");
        }

        // Validación segura de la relación entre el invitado y el socio
        if (guest.getPartnerId() == null) {
            throw new Exception("El invitado no tiene un socio responsable asignado");
        }

        Long guestPartnerId = guest.getPartnerId().getId();

        if (guestPartnerId == null) {
            throw new Exception("El socio no es el responsable de este invitado");
        }

        try {
            // Calcular el total de la factura
            double total = 0;
            for (InventoryDTO detail : details) {
                total += detail.getAmount();
            }
            invoiceDto.setAmount(total);

            // Verificar fondos suficientes del socio responsable
            if (partner.getfundsMoney() < total) {
                throw new Exception("Fondos insuficientes del socio responsable para crear la factura. Total: " + total
                        + ", Fondos disponibles: " + partner.getfundsMoney());
            }

            // Crear la factura
            long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
            invoiceDto.setId(invoiceId);

            // Crear los detalles de la factura
            for (InventoryDTO detail : details) {
                detail.setInvoiceId(invoiceDto);
                invoiceDetailDao.createInvoiceDetail(detail);
            }

            // Actualizar los fondos del socio responsable
            double newFunds = partner.getfundsMoney() - total;
            partner.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partner);

            System.out.println(
                    "Factura del invitado creada exitosamente con ID: " + invoiceId + " y monto total: " + total);
        } catch (Exception e) {
            throw new Exception("Error al crear la factura del invitado: " + e.getMessage());
        }
    }

    @Override
    public void createGuest(MachineryDTO guestDto) throws Exception {
        try {
            // Encontrar el socio basado en el ID de usuario
            PermissionDTO partnerDTO = partnerDao.findPartnerById(guestDto.getPartnerId().getId());
            if (partnerDTO == null) {

                throw new Exception("No se encontró un socio asociado al usuario actual ");
            }

            if (personDao.existsByDocument(guestDto.getUserId().getPersonId())) {
                throw new Exception(" ya existe una persona con ese documento ");
            }

            // Crear el usuario
            if (userDao.existsByUserName(guestDto.getUserId())) {
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }

            personDao.createPerson(guestDto.getUserId().getPersonId());

            userDao.createUser(guestDto.getUserId());

            guestDao.createGuest(guestDto);

            System.out.println("Invitado creado exitosamente.");
        } catch (Exception e) {
            // En caso de error, intentar revertir las operaciones realizadas
            throw new Exception("Error al crear el invitado: " + e.getMessage());
        }
    }

    // conversion invitado a socio
    @Override
    public void convertGuestToPartner(long userId) throws Exception {
        System.out.println("Iniciando proceso de conversión de invitado a socio. ID del usuario: " + userId);

        // 1. Buscar y validar el invitado
        MachineryDTO guestDTO;
        try {
            guestDTO = guestDao.findGuestByUserId(userId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID de usuario: " + userId);
            }
            System.out.println("Invitado encontrado correctamente.");
        } catch (Exception e) {
            throw new Exception("Error al buscar el invitado: " + e.getMessage());
        }

        // 2. Obtener y validar el socio que lo creó
        PermissionDTO sponsorPartner = guestDTO.getPartnerId();
        if (sponsorPartner == null) {
            throw new Exception("No se encontró el socio que creó al invitado");
        }
        System.out.println("Socio responsable encontrado - ID: " + sponsorPartner.getId());

        // 3. Verificar facturas pendientes del invitado
        try {
            List<MachineryDTO> unpaidGuestInvoices = invoiceDao.UnpaidInvoicesGuest(guestDTO.getId());
            if (!unpaidGuestInvoices.isEmpty()) {
                double totalGuestPending = 0;
                for (MachineryDTO invoice : unpaidGuestInvoices) {
                    totalGuestPending += invoice.getAmount();
                }
                throw new Exception("El invitado tiene " + unpaidGuestInvoices.size() + "\n"
                        + " factura(s) pendiente(s) por un total de: $" + totalGuestPending + "\n"
                        + "\nDebe pagar todas las facturas antes de convertirse en socio.");
            }
            System.out.println("Verificación de facturas del invitado: Sin facturas pendientes");
        } catch (Exception e) {
            throw new Exception("Error al verificar facturas del invitado: " + e.getMessage());
        }

        // 4. Verificar facturas pendientes del socio responsable
        try {
            List<MachineryDTO> unpaidSponsorInvoices = invoiceDao.UnpaidInvoicesPartner(sponsorPartner.getId());
            if (!unpaidSponsorInvoices.isEmpty()) {
                double totalSponsorPending = 0;
                for (MachineryDTO invoice : unpaidSponsorInvoices) {
                    totalSponsorPending += invoice.getAmount();
                }
                throw new Exception("El socio responsable tiene facturas pendientes por $" + "\n"
                        + totalSponsorPending + " que deben ser pagadas antes de la conversión.");
            }

        } catch (Exception e) {
            throw new Exception("Error al verificar facturas del socio responsable: " + "\n " + e.getMessage());
        }

        try {
            // 5. Crear nuevo PartnerDTO
            ReservationDTO userDTO = userDao.findUserById(userId);
            PermissionDTO newPartnerDTO = new PermissionDTO();
            newPartnerDTO.setUserId(userDTO);
            newPartnerDTO.setfundsMoney(50000); // Fondo inicial para socios regulares
            newPartnerDTO.setTypeSuscription("regular");
            newPartnerDTO.setDateCreated(new Date(System.currentTimeMillis()));

            // 6. Realizar la conversión
            partnerDao.createPartner(newPartnerDTO, userDTO);
            userDTO.setRol("Partner");
            userDao.updateUser(userDTO);
            guestDao.deleteGuest(guestDTO.getId());

            System.out.println("Conversión completada exitosamente:");
            System.out.println("- Nuevo socio creado con ID: " + newPartnerDTO.getId());
            System.out.println("- Fondos iniciales: $50,000");
            System.out.println("- Tipo de suscripción: Regular");
        } catch (Exception e) {
            throw new Exception("Error en el proceso de conversión: " + e.getMessage());
        }
    }

    @Override
    public MachineryDTO findGuestByUserId(long userId) throws Exception {
        try {
            return guestDao.findGuestByUserId(userId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el invitado por ID de usuario: " + e.getMessage());
        }

    }

    @Override
    public void createInvoiceDetails(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception {
        if (invoiceDto.getPartnerId() == null && invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un socio o un invitado");
        }

        long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
        invoiceDto.setId(invoiceId);

        for (InventoryDTO detail : details) {
            detail.setInvoiceId(invoiceDto);
            invoiceDetailDao.createInvoiceDetail(detail);
        }

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PermissionDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.PartnerFunds(partner);
        }
    }

    @Override
    public void login(ReservationDTO userDto) throws Exception {
        ReservationDTO validateDto = userDao.findByUserName(userDto);
        if (validateDto == null) {
            throw new Exception("no existe usuario registrado");
        }
        if (!userDto.getPassword().equals(validateDto.getPassword())) {
            throw new Exception("usuario o contraseña incorrecto");
        }
        userDto.setRol(validateDto.getRol());
        user = validateDto;
    }

    @Override
    public void logout() {
        user = null;
        System.out.println("se ha cerrado sesión");
    }

    @Override

    public void createUser(ReservationDTO userDTO) throws Exception {
        this.createPerson(userDTO.getPersonId());
        if (this.userDao.existsByUserName(userDTO)) {
            this.personDao.deletePerson(userDTO.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userDao.createUser(userDTO);
        } catch (Exception e) {
            this.personDao.deletePerson(userDTO.getPersonId());
        }

    }

    @Override

    public void createPerson(ReportDTO personDto) throws Exception {
        if (this.personDao.existsByDocument(personDto)) {
            throw new Exception("ya existe una persona con ese documento");
        }
        this.personDao.createPerson(personDto);
    }

}
