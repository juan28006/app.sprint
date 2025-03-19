/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.MachineryDTO;
import com.mycompany.app.Dto.ReportDTO;
import com.mycompany.app.Dto.ReservationDTO;
import java.util.List;

public interface GuestDao {

    List<MachineryDTO> getGuestPendingInvoices(long guestId) throws Exception;

    List<MachineryDTO> getGuestPaidInvoices(long guestId) throws Exception;

    MachineryDTO findGuestByUserId(long userId) throws Exception;

    public MachineryDTO findGuestById(long guestid) throws Exception;

    public void updateGuest(MachineryDTO guest) throws Exception;

    public void deleteGuest(long guestId) throws Exception;

    public void createGuest(MachineryDTO guestDTO) throws Exception;

    public List<MachineryDTO> findGuestsByPartnerId(long id) throws Exception;

}
