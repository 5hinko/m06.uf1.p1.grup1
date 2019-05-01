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
    private String ruta_imatge;
    private ArrayList<AudioMP3> listaAudios;

    public ListaReproduccion() {
    }

    public ListaReproduccion(String nom, String descripcio, String ruta_imatge, ArrayList<AudioMP3> listaAudios) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.ruta_imatge = ruta_imatge;
        this.listaAudios = listaAudios;
    }

    public String getNom() {
        return nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getRuta_imatge() {
        return ruta_imatge;
    }

    public ArrayList<AudioMP3> getListaAudios() {
        return listaAudios;
    }

    
 
}
