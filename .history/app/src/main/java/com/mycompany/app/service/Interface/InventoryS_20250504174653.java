package com.mycompany.app.service.Interface;

import java.util.List;

import com.mycompany.app.dto.InventoryDTO;
import com.mycompany.app.dto.UserDTO;

public interface InventoryS {

    List<InventoryDTO> getAllInventory() throws Exception;

    InventoryDTO getInventoryById(Long id) throws Exception;

    InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception;

    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) throws Exception;

    void deleteInventory(Long id) throws Exception;

    UserDTO login(String usuario, String contrasena) throws Exception;

}