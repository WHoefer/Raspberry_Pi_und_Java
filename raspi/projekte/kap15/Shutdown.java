package raspi.projekte.kap15;


import raspi.listener.ShutdownListener;

public class Shutdown extends ShutdownListener
{

    public Shutdown(Object obj)
    {
        super(obj);
    }

    @Override
    public void beforeShutdown(Object obj){
        Boolean busy = (Boolean)obj;
        busy = false;

    }

}
