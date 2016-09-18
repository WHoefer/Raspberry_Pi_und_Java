package raspi.schedule;

import raspi.schedule.ScheduleService;
import raspi.logger.DataLogger;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.text.ParseException;
/**
 * Abstract class Executable - write a description of the class here
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Scheduler extends Thread
{
    List<String> list;
    boolean enabled;
    long loop;

    public Scheduler(long loop){
        if(loop > 1000){
            this.loop = loop;
        }else{
            this.loop = 1000;  
        }
        enabled = true;
        list = new ArrayList<String>();

    }

    public synchronized void  setCommandStringList(List<String> list){
        this.list = list;
    }
    

    public synchronized List<String>  getCommandStringList(){
        return this.list;
    }    

    public synchronized void  setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    @Override
    public void run(){
        while(!Thread.interrupted()){
            try{
                execute();
                Thread.sleep(loop); 
            }catch(InterruptedException ex){
                break;
            } 
        }
    }

    private synchronized void execute(){
        if(enabled){
            boolean act = false;
            boolean error = false;
            if(list != null && !list.isEmpty()){
                Iterator<String> it = list.iterator();
                while(it.hasNext()){
                    String commandString = it.next();
                    try{
                        if(ScheduleService.scheduleCheckForDalyUse(commandString)){
                            act = true;;
                        }  
                    }catch(ParseException ex){
                        error = true;;
                    }
                }
                if(!error){
                    if(act){
                        executeActive();
                    }else{
                        executePassive();
                    }
                }
            }
        }else{
            executeDisabled();
        }
    }    

    public synchronized void executeActive(){
    }

    public synchronized void executePassive(){
    }

    public synchronized void executeDisabled(){
    }

}
