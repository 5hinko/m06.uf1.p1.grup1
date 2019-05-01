/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.model;

import java.util.ArrayList;

/**
 *
 * @author Lorenzo
 */
public class ListaReproduccion {
    private String nom;
    private String ruta_lista;
    

    public ListaReproduccion() {
    }

    public ListaReproduccion(String nom, String ruta_lista) {
        this.nom = nom;
        this.ruta_lista = ruta_lista;
    }

    public String getNom() {
        return nom;
    }

    public String getRuta_lista() {
        return ruta_lista;
    }

   
    
 
}
