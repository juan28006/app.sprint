/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.Dto.MachineryDTO;
import java.util.List;

public interface InvoiceDao {

    public void createInvoice(MachineryDTO invoiceDto) throws Exception;

    public List<MachineryDTO> getInvoicesPartner(long partnerId) throws Exception;

    public void payInvoices(long partnerId) throws Exception;

    public List<MachineryDTO> getInvoicesByGuestId(long guestId) throws Exception;

    public MachineryDTO findInvoiceById(long invoiceId) throws Exception;

    public void updateInvoice(MachineryDTO invoiceDto) throws Exception;

    public List<MachineryDTO> getInvoicesByStatus(boolean status) throws Exception;

    public void deleteAllInvoicesByPartnerId(long id) throws Exception;

    public List<MachineryDTO> UnpaidInvoicesPartner(long partnerId) throws Exception;

    public void deleteAllInvoicesByGuestId(long id) throws Exception;

    public List<MachineryDTO> UnpaidInvoicesGuest(long id) throws Exception;

    public long createAllInvoices(MachineryDTO invoiceDto) throws Exception;

}
