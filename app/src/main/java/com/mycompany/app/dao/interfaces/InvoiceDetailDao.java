/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.InventoryDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface InvoiceDetailDao {

    public void createInvoiceDetail(InventoryDTO detail) throws Exception;

    InventoryDTO findInvoiceDetailById(long id) throws Exception;

    List<InventoryDTO> findInvoiceDetailsByInvoiceId(long invoiceId) throws Exception;

    public void updateInvoiceDetail(InventoryDTO detail) throws Exception;

    public void deleteInvoiceDetail(long id) throws Exception;

}
