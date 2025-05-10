/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import com.mycompany.app.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeDTO {
    private String employeeId;
    private String department;
    private User user;

}
