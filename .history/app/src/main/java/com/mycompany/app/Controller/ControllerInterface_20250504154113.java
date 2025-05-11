/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import com.mycompany.app.dto.UserDTO;

public class ControllerInterface {
    void mostrarMenuPrincipal(UserDTO usuario) throws Exception;

    void cerrarSesion();
}
