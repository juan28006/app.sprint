package com.mycompany.app.dao.interfaces;

import java.util.List;

import com.mycompany.app.dto.InventoryDTO;

public interface InventoryDao {

    List<InventoryDTO> getAllInventory() throws Exception;

    InventoryDTO getInventoryById(Long id) throws Exception;

    InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception;

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception;

    void deleteInventory(Long id) throws Exception;

    public InventoryDTO getInventoryByMachineryId(Long id) throws Exception;
}
