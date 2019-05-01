/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import static java.util.Collections.list;

import m06.uf1.audioplayer.model.*;


/**
 *
 * @author Lorenzo
 */
public class Pruebas {

    static ArrayList<AudioMP3> listaAudios = new ArrayList<>();
    static ArrayList<ListaReproduccion> listaRepro = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParserConfigurationException {

        try {
            Document doc = parseXML("carrega_dades.xml");
            getCancionesALL(doc);
            AudioMP3 audioEncontrado = devolverAduio("September");
            String ruta = audioEncontrado.getRuta();
            System.out.println("Ruta " + ruta);

            File archivoAudio = new File(ruta);
            if (archivoAudio.exists() && archivoAudio.isFile()) {
                System.out.println("Encontrado");
            }

        } catch (SAXException ex) {
            Logger.getLogger(Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Añade todas als canciones a un Arraylist - para no tener que estar leyendo el fichero todo el rato
    public static void getCancionesALL(Document doc) {
        NodeList nombresMusica = doc.getElementsByTagName("arxiu");
        for (int i = 0; i < nombresMusica.getLength(); i++) {
            Node nNode = nombresMusica.item(i);
            Element eAudio = (Element) nNode;
            Element eNom = (Element) eAudio.getElementsByTagName("nom").item(0);
            Element eAutor = (Element) eAudio.getElementsByTagName("autor").item(0);
            Element eDurada = (Element) eAudio.getElementsByTagName("durada").item(0);
            Element eAlbum = (Element) eAudio.getElementsByTagName("album").item(0);
            Element eRuta = (Element) eAudio.getElementsByTagName("ruta").item(0);

            AudioMP3 audio = new AudioMP3(eNom.getTextContent(), eAutor.getTextContent(),
                    eAlbum.getTextContent(), Integer.parseInt(eDurada.getTextContent()),
                    eRuta.getTextContent());
            listaAudios.add(audio);
        }
    }

    public static AudioMP3 devolverAduio(String nombre) {
        AudioMP3 audioA = new AudioMP3();
        for (AudioMP3 audio : listaAudios) {
            if (audio.getNom().equals(nombre)) {
                audioA = audio;
            }
        }
        return audioA;
    }

    public static Document parseXML(String rutaFichero) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(rutaFichero);
        doc.getDocumentElement().normalize();
        return doc;
    }
}
