package m06.uf1.audioplayer.model;

import java.io.File;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

// http://www.javazoom.net/jlgui/api.html
public class Audio {

    BasicPlayer player;
    
    public Audio(String fitxerAudio) {
        player = new BasicPlayer();
        try {
            player.open(new File(fitxerAudio));
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    Audio(String textContent, String textContent0, int parseInt, String textContent1, String textContent2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BasicPlayer getPlayer() {
        return player;
    }

    public void setPlayer(BasicPlayer player) {
        this.player = player;
    }

}
