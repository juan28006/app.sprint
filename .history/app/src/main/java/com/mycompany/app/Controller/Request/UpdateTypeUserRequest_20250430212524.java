/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
// PUT /api/type-users/{id} → Actualizar tipo de usuario
@Getter
@Setter
@NoArgsConstructor
public class UpdateTypeUserRequest {
   private Long id;
   private TypeUserDTO typeUserDTO;

}
