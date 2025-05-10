/**
 * @author CLAUDIA
 * @version 1.0
 * @created 
 */
package com.mycompany.app.Controller;

import java.util.Scanner;

public abstract class Utils {
    private static Scanner reader = new Scanner(System.in);

    public static Scanner getReader() {
        return reader;
    }

    public static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

}
