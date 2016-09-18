package raspi.projekte.kap04;


import com.pi4j.system.SystemInfo;
import java.io.IOException;

/**
 * Write a description of class ProzessTemp here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ProzessTemp
{
    public static void main(String[] args)throws IOException, InterruptedException{
        System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
    }    
}
