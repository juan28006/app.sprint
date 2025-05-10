/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRegistrationRequest {
    private String name;
    private Long document;
    private Long cellphone;
    private String username;
    private String password;
    private String department;

}
