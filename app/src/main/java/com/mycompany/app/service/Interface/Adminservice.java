/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.PermissionDTO;
import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Adminservice {

    public void createPartner(ReservationDTO userDTO, ReportDTO personDTO) throws Exception;

    public List<PermissionDTO> getPendingVIPRequests() throws Exception;

    public double getTotalPaidInvoices(long id) throws Exception;

    List<MachineryDTO> getPartnerInvoices(long partnerId) throws Exception;

    public List<MachineryDTO> getGuestPendingInvoice(long guestId) throws Exception;

    public List<MachineryDTO> getGuestPaidInvoices(long guestId) throws Exception;

    public List<MachineryDTO> getGuestInvoices(long guestId) throws Exception;

    public void requestVIPSubscription(ReservationDTO userDto) throws Exception;

    public void createInvoice(MachineryDTO invoiceDto) throws Exception;

    public void approveVIPRequest(long partnerId) throws Exception;

    public void rejectVIPRequest(long partnerId) throws Exception;

    public List<MachineryDTO> PaidInvoices(long partnerId) throws Exception;

    public List<MachineryDTO> getPendingInvoices(long partnerId) throws Exception;

    public double getTotalInvoicesAmount(long partnerId) throws Exception;

    public List<MachineryDTO> getPaidInvoices(long partnerId) throws Exception;

    public List<MachineryDTO> getAllInvoices(long userId) throws Exception;

}
