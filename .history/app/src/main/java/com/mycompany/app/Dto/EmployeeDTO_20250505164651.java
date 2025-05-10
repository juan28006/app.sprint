/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import com.mycompany.app.model.Person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeDTO extends Person {

    private String department;

}
