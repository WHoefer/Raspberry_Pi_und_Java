package raspi.hardware.camera;

import java.util.Properties;
import raspi.process.Shell;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Write a description of class Camera here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Camera
{
    public static final String ROT_0 = "-rot 0 ";
    public static final String ROT_180 = "-rot 180 ";
    public static final String ROT_270 = "-rot 270 ";
    public static final String NOPREVIEW = "--nopreview ";
    public static final String TIMELAPSE = " --timelapse ";
    public static final String TIMEOUT = " --timeout ";

    public static boolean  takePictures(Properties props){
        StringBuilder raspistill = new StringBuilder(); 
        raspistill.append("raspistill ");
        raspistill.append(props.getProperty("foto.rotation"));
        raspistill.append(NOPREVIEW);
        raspistill.append(TIMEOUT);
        raspistill.append(props.getProperty("foto.timeout"));
        raspistill.append(TIMELAPSE);
        raspistill.append(props.getProperty("foto.timelapse"));
        raspistill.append(" -o ");
        raspistill.append(props.getProperty("foto.path"));
        raspistill.append("image%04d.jpg ");
        raspistill.append(" -w ");
        raspistill.append(props.getProperty("foto.width"));
        raspistill.append(" -h ");
        raspistill.append(props.getProperty("foto.height"));
        raspistill.append(" -q ");
        raspistill.append(props.getProperty("foto.quality"));
        System.out.println(raspistill.toString());
        return Shell.executeAndWaitFor(raspistill.toString());

    }
    
    public static String[] getImageFiles(Properties props){
        String path = props.getProperty("foto.path");
        File file = new File(path);
        ImageFilter filter = new ImageFilter();
        String[] files = file.list(filter);
        if(!path.endsWith("/")){
            path = path + "/";
        }    
        for(int i = 0; i < files.length; i++){
            files[i] = path +  files[i];
        }
        return files;
    }
    
}
