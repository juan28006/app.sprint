package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class GetFacturasByFechasRequest {
    private Date fechaInicio;
    private Date fechaFin;

}
