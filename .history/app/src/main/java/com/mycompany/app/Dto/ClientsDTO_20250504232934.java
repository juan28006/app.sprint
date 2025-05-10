/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientsDTO extends PersonDTO {
    private Long clientId;
    private String address;
    private UserDTO user;

}
