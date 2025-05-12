/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRequestDTO {
    private UserDTO user;
    private String area;
    private String motivo;

}
