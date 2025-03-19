/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Helpers;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.ReservationDTO;
import com.mycompany.app.Dto.PermissionDTO;
import com.mycompany.app.model.Guest;
import com.mycompany.app.model.machinery;
import com.mycompany.app.model.Permis;
import com.mycompany.app.model.user;
import com.mycompany.app.model.TypeUser;
import com.mycompany.app.model.Report;
import java.sql.Date;

public abstract class Helpers {
    public static ReportDTO parse(user person) {

        ReportDTO personDto = new ReportDTO();
        personDto.setId(person.getId());
        personDto.setDocument(person.getDocument());
        personDto.setName(person.getName());
        personDto.setCelphone(person.getCelphone());
        return personDto;
    }

    public static user parse(ReportDTO personDto) {

        user person = new user();
        person.setId(personDto.getId());
        person.setDocument(personDto.getDocument());
        person.setName(personDto.getName());
        person.setCelphone(personDto.getCelphone());
        return person;
    }

    public static ReservationDTO parse(TypeUser user) {
        if (user == null) {
            return null;
        }
        ReservationDTO userDto = new ReservationDTO();
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPersonId(parse(user.getPersonId()));
        userDto.setRol(user.getRol());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    public static TypeUser parse(ReservationDTO userDto) {
        if (userDto == null) {
            return null;
        }
        TypeUser user = new TypeUser();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setPersonId(parse(userDto.getPersonId()));
        user.setRol(userDto.getRol());
        user.setUsername(userDto.getUsername());
        return user;
    }

    public static MachineryDTO parse(machinery invoice) {
        if (invoice == null) {
            return null;
        }
        MachineryDTO invoiceDto = new MachineryDTO();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setPersonId(parse(invoice.getPersonId()));
        invoiceDto.setPartnerId(parse(invoice.getPartnerId()));
        invoiceDto.setCreationDate(new Date(invoice.getCreationDate().getTime()));
        invoiceDto.setAmount(invoice.getAmount());
        invoiceDto.setStatus(invoice.isStatus());
        return invoiceDto;
    }

    public static machinery parse(MachineryDTO invoiceDto) {
        if (invoiceDto == null) {
            return null;
        }
        machinery invoice = new machinery();
        invoice.setId(invoiceDto.getId());
        invoice.setPersonId(parse(invoiceDto.getPersonId()));
        invoice.setPartnerId(parse(invoiceDto.getPartnerId()));
        invoice.setCreationDate(new Date(System.currentTimeMillis()));
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setStatus(invoiceDto.isStatus());
        return invoice;
    }

    // InvoiceDetail and InvoiceDetailDTO
    public static InventoryDTO parse(Permis invoiceDetail) {
        if (invoiceDetail == null) {
            return null;
        }
        InventoryDTO detailDto = new InventoryDTO();
        detailDto.setId(invoiceDetail.getId());
        detailDto.setInvoiceId(parse(invoiceDetail.getInvoiceId()));
        detailDto.setItem(invoiceDetail.getItem());
        detailDto.setDescription(invoiceDetail.getDescription());
        detailDto.setAmount(invoiceDetail.getAmount());
        return detailDto;
    }

    public static Permis parse(InventoryDTO detailDto) {
        if (detailDto == null) {
            return null;
        }
        Permis detail = new Permis();
        detail.setId(detailDto.getId());
        detail.setInvoiceId(parse(detailDto.getInvoiceId()));
        detail.setItem(detailDto.getItem());
        detail.setDescription(detailDto.getDescription());
        detail.setAmount(detailDto.getAmount());
        return detail;
    }

    // Partner and PartnerDTO
    public static PermissionDTO parse(Report partner) {
        if (partner == null) {
            return null;
        }

        PermissionDTO partnerDto = new PermissionDTO();
        partnerDto.setId(partner.getId());
        partnerDto.setUserId(parse(partner.getUserId()));
        partnerDto.setfundsMoney(partner.getFundsMoney());
        partnerDto.setDateCreated(partner.getDateCreated());
        partnerDto.setTypeSuscription(partner.getTypeSuscription());

        return partnerDto;
    }

    public static Report parse(PermissionDTO partnerDto) {
        if (partnerDto == null) {
            return null;
        }

        Report partner = new Report();
        partner.setId(partnerDto.getId());
        partner.setUserId(parse(partnerDto.getUserId()));
        partner.setFundsMoney(partnerDto.getfundsMoney());
        partner.setDateCreated(partnerDto.getDateCreated());
        partner.setTypeSuscription(partnerDto.getTypeSuscription());

        return partner;
    }

    public static MachineryDTO parse(Guest guest) {
        if (guest == null) {
            return null;
        }
        MachineryDTO guestDto = new MachineryDTO();
        guestDto.setId(guest.getId());
        guestDto.setUserId(parse(guest.getUserId()));
        guestDto.setPartnerId(parse(guest.getPartnerId()));
        guestDto.setStatus(guest.isStatus());
        return guestDto;
    }

    public static Guest parse(MachineryDTO guestDto) {
        if (guestDto == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setId(guestDto.getId());
        guest.setUserId(parse(guestDto.getUserId()));
        guest.setPartnerId(parse(guestDto.getPartnerId()));
        guest.setStatus(guestDto.isStatus());
        return guest;
    }
}
