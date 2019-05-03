/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.model;

/**
 *
 * @author Lorenzo
 */
public class AudioMP3 {

    private String nom;
    private String autor;
    private String album;
    private int durada;
    private String ruta;

    public AudioMP3() {
    }

    public AudioMP3(String nom, String autor, String album, int durada, String ruta) {
        this.nom = nom;
        this.autor = autor;
        this.album = album;
        this.durada = durada;
        this.ruta = ruta;
    }

    public String getNom() {
        return nom;
    }

    public String getAutor() {
        return autor;
    }

    public String getAlbum() {
        return album;
    }

    public int getDurada() {
        return durada;
    }

    public String getRuta() {
        return ruta;
    }
}
