/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import com.mycompany.app.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */

// PUT /api/users/{id} → Actualizar usuario
@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
     private Long id;
     private UserDTO userDTO;;

}
