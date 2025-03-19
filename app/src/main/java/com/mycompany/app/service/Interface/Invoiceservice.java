/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.InventoryDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Invoiceservice {

    public void createInvoiceDetails(MachineryDTO invoiceDto, List<InventoryDTO> details) throws Exception;

}
