/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.controlador;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import m06.uf1.audioplayer.Pruebas;
import static m06.uf1.audioplayer.Pruebas.parseXML;
import m06.uf1.audioplayer.model.AudioMP3;
import m06.uf1.audioplayer.model.ListaReproduccion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Lorenzo
 */
public class Listas {

    
     public ArrayList<AudioMP3> listaAudios = new ArrayList<>();
     public ArrayList<ListaReproduccion> listaRepro = new ArrayList<>();


    //Añade todas als canciones a un Arraylist - para no tener que estar leyendo el fichero todo el rato
    public void getCancionesALL(Document doc) {
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

    public  AudioMP3 getCancion(String nombre) {
        AudioMP3 audioA = new AudioMP3();
        for (AudioMP3 audio : listaAudios) {
            if (audio.getNom().equals(nombre)) {
                audioA = audio;
            }
        }
        return audioA;
    }

    public void getListasALL(Document doc) throws FileNotFoundException, IOException, ParseException {
        NodeList nombresListas = doc.getElementsByTagName("llista");
        for (int i = 0; i < nombresListas.getLength(); i++) {
            Node nNode = nombresListas.item(i);
            Element eLlista = (Element) nNode;
            Element eNom = (Element) eLlista.getElementsByTagName("nom").item(0);
            Element eRuta = (Element) eLlista.getElementsByTagName("ruta_llista").item(0);

            JSONParser parser = new JSONParser();

            //Habria que darle el fichero en concreto
            JSONObject lista = (JSONObject) parser
                    .parse(new FileReader(eRuta.getTextContent()));

            JSONArray cancioneslista = (JSONArray) lista.get("archius_audi");
            System.out.println("Lista de canciones");
            for (Object object : cancioneslista) {
                System.out.println("Nombre: " + object);
                //super marronero
                /*for (AudioMP3 audio : listaAudios) {
                    if (audio.getNom().equals(object)) {
                        System.out.println("Ruta: " + audio.getRuta());
                    }
                }
                 */
            }
            String nom = (String)lista.get("nom");
            
            System.out.println("Nombre con el JSON " + nom);
            
            
            //ListaReproduccion listarepro = new ListaReproduccion(nom, ruta_lista);

        }
    }
    public Document parseXML(String rutaFichero) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(rutaFichero);
        doc.getDocumentElement().normalize();
        return doc;
    }
}
