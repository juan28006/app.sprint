package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.InventoryDTO;
import java.util.List;

public interface InventoryS {

    List<InventoryDTO> getAllInventory() throws Exception;

    InventoryDTO getInventoryById(Long id) throws Exception;

    InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception;

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception;

    void deleteInventory(Long id) throws Exception;

}