package raspi.audio;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Control;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.BooleanControl;

public class SourceDataPlayer extends Thread implements LineListener  {

    private static final int BUFFER_SIZE = 4096;
    private boolean ready;
    private FloatControl masterGain = null;
    private BooleanControl mute = null;
    private File audioFile = null;
    private AudioInputStream audioStream = null;
    private SourceDataLine audioLine = null; 

    public SourceDataPlayer(String file)  throws PlayerException{
        createPlayer(file);
    }

    private void createPlayer(String file) throws PlayerException{
        File audioFile = null;
        if(file == null || file.isEmpty()){
            throw new PlayerException("Es wurde keine Audiodatei angegeben.");
        }
        audioFile = new File(file);
        try{
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(format);
            masterGain = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
            mute = (BooleanControl) audioLine.getControl(BooleanControl.Type.MUTE);
            ready = false;

        } catch (UnsupportedAudioFileException ex) {
            throw new PlayerException("Format wird nicht unterstüzt.");
        } catch (IOException ex) {
            throw new PlayerException("Audiodatei konnte nicht gelesen werden. " + file);
        }catch (LineUnavailableException ex) {
            throw new PlayerException("Audiogerät ist nicht verfügbar.");
        }
    }

    @Override
    public void run(){
        ready = false;
        if(audioLine != null){
            audioLine.start();
            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            try{
                while (((bytesRead = audioStream.read(bytesBuffer)) != -1)&& !Thread.interrupted()) {
                    audioLine.write(bytesBuffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                throw new Error("Fehler beim Streamen der Audiodatei.");
            }   
            audioLine.drain();
            ready = true;
        }else{
            throw new Error("Player ist nicht verfügbar.");
        } 
    }

    public void stopPlayer(){
        this.interrupt();
    }

    public void startPlayer(){
        this.start();
    }    

    public void closePlayer(){
        audioLine.close();
        try{
            audioStream.close();
        } catch (IOException ex) {
            throw new Error("Fehler beim Schließen des Audiostreams.");
        }   
    }  

    public boolean isReady(){
        return ready;
    }   

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.STOP) {
            ready = true;
        }
        if (type == LineEvent.Type.START) {
            ready = false;
        }

    }

    public void setMute(boolean m){
        mute.setValue(m);
    }

    public boolean getMute(){
        return mute.getValue();
    }

    public void setGain(float g){
        masterGain.setValue(g);
    }

    public float getGain(){
        return masterGain.getValue();
    }

    public static void main(String[] args) {
        try {
            String audioFilePath = "/home/pi/JavaProgramme/Projekte/Raspberry_Pi_und_Java/audio/Wellen.wav";
            SourceDataPlayer player = new SourceDataPlayer(audioFilePath);   
            player.setGain(0f);
            player.startPlayer();
            Thread.sleep(6000);
            player.setGain(-10f);
            Thread.sleep(1000);
            player.setGain(-20f);
            Thread.sleep(1000);
            player.setGain(-30f);
            Thread.sleep(1000);
            player.stopPlayer();
            player.closePlayer();
            player = player = new SourceDataPlayer(audioFilePath);  
            player.setGain(0f);
            player.startPlayer();
            Thread.sleep(6000);
            player.setGain(-10f);
            Thread.sleep(1000);
            player.setGain(-20f);
            Thread.sleep(1000);
            player.setGain(0f);
            while (!player.isReady()) {
                Thread.sleep(1000);
            }
            player.closePlayer();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (PlayerException ex) {
            ex.printStackTrace();
        }
    }
}
