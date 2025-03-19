/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Guestservice {

    public MachineryDTO findGuestByUserId(long userId) throws Exception;

    public void convertGuestToPartner(long userId) throws Exception;

    public void createInvoiceGuest(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception;

    // Verificar si el invitado existe
}
