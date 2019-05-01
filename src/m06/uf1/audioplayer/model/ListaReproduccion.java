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
    private String descripcio;
    private String rutaImatge;
    private ArrayList<String> lista_audios ;
    
    

    public ListaReproduccion() {
        
    }
    public ListaReproduccion(String nom, String descripcio, String rutaImatge, ArrayList<String> lista_audios) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.rutaImatge = rutaImatge;
        this.lista_audios = lista_audios;
    }

    public String getNom() {
        return nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getRutaImatge() {
        return rutaImatge;
    }

    public ArrayList<String> getLista_audios() {
        return lista_audios;
    }

    
    

   
    
 
}
