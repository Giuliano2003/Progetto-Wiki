package model;

import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
                String input = "Ciao,Sono,Tema";

                // Dividi la stringa in base alla virgola
                String[] array = input.split(",");

                // Stampa gli elementi dell'array con numerazione
                for (int i = 0; i < array.length; i++) {
                    System.out.println((i + 1) + ") " + array[i]);
                }
            }
        }




