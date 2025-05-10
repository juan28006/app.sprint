/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {
    private Long id;
    private Long document;
    private String name;
    private Long cellphone;

}
