/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.PermissionDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

public interface Partnerservice {

    public void lowPartner(long partnerId) throws Exception;

    public PermissionDTO findPartnerByUserId(long userId) throws Exception;

    public PermissionDTO findPartnerById(long partnerId) throws Exception;

    public void createInvoicePartner(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception;

    public void uploadFunds(long userId, double amount) throws Exception;

    public void requestPromotion(long partnerId) throws Exception;

    public void desactivateGuest(long guestId, long partnerId) throws Exception;

    public void activateGuest(long guestId, long partnerId) throws Exception;

    public List<MachineryDTO> getPartnerInvoices(long partnerId) throws Exception;

    public void payInvoices(long userId) throws Exception;

    public void createGuest(MachineryDTO guestDto) throws Exception;

}
