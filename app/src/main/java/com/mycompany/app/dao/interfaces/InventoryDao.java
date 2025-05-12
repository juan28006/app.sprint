package com.mycompany.app.dao.interfaces;

import com.mycompany.app.dto.InventoryDTO;

public interface InventoryDao {

    InventoryDTO getInventoryById(Long id) throws Exception;

    InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception;

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception;

    void deleteInventory(Long id) throws Exception;
}
