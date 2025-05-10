package com.mycompany.app.Controller.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class DateRangeRequest {
    private Date inicio;
    private Date fin;

}
