/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.MachineryDTO;

import com.mycompany.app.Dto.PermissionDTO;

import com.mycompany.app.Dto.ReservationDTO;

import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface PartnerDao {

    // crear un partner que ya existe
    public void createPartner(PermissionDTO partnerDTO, ReservationDTO userDTO) throws Exception;

    public void PartnerFunds(PermissionDTO partnerDto) throws Exception;

    public void updatePartner(PermissionDTO partner) throws Exception;

    public List<MachineryDTO> getPendingInvoices(long id) throws Exception;

    public void payInvoice(long id) throws Exception;

    public List<MachineryDTO> getPaidInvoices(long partnerId) throws Exception;

    public PermissionDTO findPartnerById(long partnerId) throws Exception;

    public boolean isVIPSlotAvailable() throws Exception;

    // obtener Solicitudes VIP Pendientes
    public List<PermissionDTO> getPendingVIPRequests() throws Exception;

    public PermissionDTO findPartnerByUserId(long id) throws Exception;

    public void activateGuest(long guestId) throws Exception;

    public void deactivateGuest(long guestId) throws Exception;

    public void lowPartner(long partnerId) throws Exception;

    public void requestPromotion(long partnerId) throws Exception;

    public void requestVIPPromotion(long partnerId) throws Exception;

    public void uploadFunds(long partnerId, double amount) throws Exception;

    public void updatePartnerSubscription(long partnerId, String newType) throws Exception;
    // Conjunto de resultados del mapa para PartnerDTO

    public void deletePartner(long partnerId) throws Exception;

}
