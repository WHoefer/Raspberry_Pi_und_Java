package raspi.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.BooleanControl;

public class ClipPlayer implements LineListener {

    private boolean completed;
    private FloatControl masterGain = null;
    private BooleanControl mute = null;
    private File audioFile = null;
    private Clip audioClip = null;
    private AudioInputStream audioStream = null;

    public ClipPlayer(String audiofile)  throws PlayerException{
        createClip(audiofile);
    }

    private void createClip(String file) throws PlayerException{
        if(file == null || file.isEmpty()){
            throw new PlayerException("Es wurde keine Audiodatei angegeben.");
        }
        audioFile = new File(file);
        try{
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            System.out.println(audioClip.getFrameLength());
            masterGain = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            mute = (BooleanControl) audioClip.getControl(BooleanControl.Type.MUTE);
            completed = false;
            /*Control[] controls = audioClip.getControls();
            for(int i = 0; i < controls.length; i++){
            if(controls[i] instanceof FloatControl ){
            Control.Type type = ((FloatControl)controls[i]).getType();
            if(type.equals(FloatControl.Type.MASTER_GAIN)){
            System.out.println("Master Gain:  " + controls[i]);
            }
            }
            System.out.println(controls[i]);
            }*/

        } catch (UnsupportedAudioFileException ex) {
            throw new PlayerException("Format wird nicht unterstüzt.");
        } catch (IOException ex) {
            throw new PlayerException("Audiodatei konnte nicht gelesen werden. " + file);
        }catch (LineUnavailableException ex) {
            throw new PlayerException("Audiogerät ist nicht verfügbar.");
        }
    }

    public void startPlayer(){
        audioClip.start();
    }

    public void stopPlayer(){
        audioClip.stop();
    }

    public void loop(int times){
        audioClip.loop(times);
    }

    public void closePlayer() throws PlayerException{
        audioClip.close();
        try{
            audioStream.close();
        } catch (IOException ex) {
            throw new PlayerException("Audiodatei konnte nicht gelesen werden. ");
        }
    }

    public void setLoopPoints(int start, int end ){
        audioClip.setLoopPoints(start, end);
    }

    public void setMicrosecondPosition(long start){
        audioClip.setMicrosecondPosition(start);
    }

    public boolean isCompleted(){
        return completed;
    }    

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.STOP) {
            completed = true;
        }else if (type == LineEvent.Type.START){
            completed = false;
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
            //String audioFilePath = "/home/pi/JavaProgramme/Projekte/Raspberry_Pi_und_Java/audio/Wellen.wav";
            String audioFilePath = "/home/pi/JavaProgramme/Projekte/Raspberry_Pi_und_Java/audio/Signal.wav";
            ClipPlayer player = new ClipPlayer(audioFilePath);  
            player.stopPlayer();
            //player.setLoopPoints(55270, 100000);
            //player.loop(Clip.LOOP_CONTINUOUSLY);
            player.startPlayer();
            while (true) {
                Thread.sleep(5000);
                player.closePlayer();
                player = new ClipPlayer(audioFilePath);  
                player.stopPlayer();
                player.setLoopPoints(56550, 66000);
                player.loop(Clip.LOOP_CONTINUOUSLY);
                player.startPlayer();
                System.out.println("Tick");
                Thread.sleep(5000);
                player.closePlayer();
                player = new ClipPlayer(audioFilePath);  
                player.stopPlayer();
                player.setLoopPoints(0, 53965);
                player.loop(Clip.LOOP_CONTINUOUSLY);
                player.startPlayer();
                System.out.println("Signal");
                Thread.sleep(5000);
                player.closePlayer();
                player = new ClipPlayer(audioFilePath);  
                player.stopPlayer();
                //player.setLoopPoints(55270, 56530);
                player.setLoopPoints(55270, 100000);
                player.loop(Clip.LOOP_CONTINUOUSLY);
                player.startPlayer();
                System.out.println("Silent");
                if(player.isCompleted()){
                 player.closePlayer();
                 player = new ClipPlayer(audioFilePath);  
                }
            }
            // player.closePlayer();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }catch (PlayerException ex) {
            ex.printStackTrace();
        }
    }

}