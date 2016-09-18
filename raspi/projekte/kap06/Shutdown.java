package raspi.projekte.kap06;

import raspi.listener.ShutdownListener;
import raspi.audio.SourceDataPlayer;
import raspi.audio.PlayerException;

/**
 * Erweiter die Klasse ShutdownListener, um den Audioplayer vor dem
 * Ausschalten zu beenden.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Shutdown extends ShutdownListener
{

    /**
     * Constructor for objects of class Shutdown
     */
    public Shutdown(Object obj)
    {
        super(obj);
    }

    @Override
    public void beforeShutdown(Object obj){
        SourceDataPlayer player = (SourceDataPlayer)obj;
        if(player != null){
            player.stopPlayer();
            player.closePlayer();
            player = null;
        }

    }

}
