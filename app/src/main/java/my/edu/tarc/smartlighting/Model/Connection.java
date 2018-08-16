package my.edu.tarc.smartlighting.Model;

import java.util.GregorianCalendar;

/**
 * Created by ken_0 on 17/8/2018.
 */

public class Connection {
    private GregorianCalendar connectedTime;
    private GregorianCalendar disconnectedTime;

    public Connection(){

    }

    public Connection(GregorianCalendar connectedTime){
        this.connectedTime = connectedTime;
    }

    public Connection(GregorianCalendar connectedTime, GregorianCalendar disconnectedTime){
        this.connectedTime = connectedTime;
        this.disconnectedTime = disconnectedTime;
    }

    public void setConnectedTime(GregorianCalendar connectedTime){
        this.connectedTime = connectedTime;
    }

    public GregorianCalendar getConnectedTime(){
        return connectedTime;
    }

    public void setDisconnectedTime(GregorianCalendar disconnectedTime){
        this.disconnectedTime = disconnectedTime;
    }

    public GregorianCalendar getDisconnectedTime(){
        return disconnectedTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

