package com.mycompany.app.dao;

import com.mycompany.app.Dto.InventoryDTO;
import com.mycompany.app.dao.interfaces.InvoiceDetailDao;

import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.InvoiceDetailRepository;
import com.mycompany.app.model.Permis;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter

public class InvoiceDetailImplementation implements InvoiceDetailDao {

    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public void createInvoiceDetail(InventoryDTO detailDto) throws Exception {
        Permis detail = Helpers.parse(detailDto);
        invoiceDetailRepository.save(detail);
    }

    @Override
    public InventoryDTO findInvoiceDetailById(long id) throws Exception {
        Permis detail = invoiceDetailRepository.findInvoiceDetailById(id);
        if (detail == null) {
            throw new Exception("Detalle de factura no encontrado con id: " + id);
        }
        return Helpers.parse(detail);
    }

    @Override
    public List<InventoryDTO> findInvoiceDetailsByInvoiceId(long invoiceId) throws Exception {
        List<Permis> details = invoiceDetailRepository.findByInvoiceId_Id(invoiceId);
        if (details.isEmpty()) {
            throw new Exception("No se encontraron detalles de factura para el ID de factura: " + invoiceId);
        }

        List<InventoryDTO> detailDTOs = new ArrayList<>();
        for (Permis detail : details) {
            detailDTOs.add(Helpers.parse(detail));
        }
        return detailDTOs;
    }

    @Override
    public void updateInvoiceDetail(InventoryDTO detailDto) throws Exception {
        if (!invoiceDetailRepository.existsById(detailDto.getId())) {
            throw new Exception("Detalle de factura no encontrado con id: " + detailDto.getId());
        }
        Permis detail = Helpers.parse(detailDto);
        invoiceDetailRepository.save(detail);
    }

    @Override
    public void deleteInvoiceDetail(long id) throws Exception {
        if (!invoiceDetailRepository.existsById(id)) {
            throw new Exception("Detalle de factura no encontrado con id: " + id);
        }
        invoiceDetailRepository.deleteById(id);
    }

    /*
     * private InvoiceDetail mapResultSetToInvoiceDetail(ResultSet rs) throws
     * SQLException {
     * InvoiceDetail detail = new InvoiceDetail();
     * detail.setId(rs.getLong("ID"));
     * 
     * Invoice invoice = new Invoice();
     * invoice.setId(rs.getLong("INVOICEID"));
     * detail.setInvoiceId(invoice);
     * 
     * detail.setItem(rs.getInt("ITEM"));
     * detail.setDescription(rs.getString("DESCRIPTION"));
     * detail.setAmount(rs.getDouble("AMOUNT"));
     * return detail;
     * }
     */
}
// select busca
// insertar
// update buscar
// delete eliminiar
