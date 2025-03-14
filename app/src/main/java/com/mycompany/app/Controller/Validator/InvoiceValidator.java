/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import com.mycompany.app.Controller.Validator.CommonsValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * @author CLAUDIA
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class InvoiceValidator extends CommonsValidator {

    public long validID(String id) throws Exception {
        return super.isValidlong("id de la factura", id);

    }

    public double validAmount(String amount) throws Exception {
        return super.isValidDouble("el monto de la factura ", amount);
    }

}
