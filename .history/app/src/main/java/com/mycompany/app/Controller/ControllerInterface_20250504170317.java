/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import com.mycompany.app.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ControllerInterface {
    void mostrarMenuPrincipal(UserDTO usuario);

    void cerrarSesion();
}
