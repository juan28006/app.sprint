package com.mycompany.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TypeUserDTO {
    private Long id;
    private String type;
    private String permissions; //

}