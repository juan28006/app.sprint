/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminRegistrationRequest {

    private String name;

    @NotNull(message = "El documento es obligatorio") // Solo esto
    private Long document;

    private Long cellphone;

    private String username;

    private String password;

    private String department;

}
