package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Permis;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InvoiceDetailRepository extends JpaRepository<Permis, Long> {
    
    
    
    
    public Permis findInvoiceDetailById(long id);

    public List<Permis> findByInvoiceId_Id(Long invoiceId);
    

}
