/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import org.springframework.http.ResponseEntity;

import com.mycompany.app.dto.UserDTO;

public interface ControllerInterface {
    ResponseEntity<?> iniciarSesion(UserDTO credentials);

    ResponseEntity<?> cerrarSesion();
}
