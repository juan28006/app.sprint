/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessLogDTO {
    private UserDTO user;
    private Date accessTime;
    private String area;
    private String reason;

}
