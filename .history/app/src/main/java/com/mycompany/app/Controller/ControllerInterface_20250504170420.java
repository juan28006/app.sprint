/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import com.mycompany.app.dto.UserDTO;

public interface ControllerInterface {
    void mostrarMenuPrincipal(UserDTO usuario);

    void cerrarSesion();
}
