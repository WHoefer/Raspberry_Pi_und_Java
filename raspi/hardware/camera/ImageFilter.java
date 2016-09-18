package raspi.hardware.camera;

import java.io.FilenameFilter;
import java.io.File;
/**
 * Write a description of class ImageFilter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ImageFilter implements FilenameFilter
{
    @Override
    public boolean accept(File dir, String name){
        return  name.matches("image[\\d]{4}.jpg");
    }
}

