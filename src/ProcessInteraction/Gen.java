import java.util.*;
import java.io.*;

class Gen extends Proc {

    Random slump = new Random();

    public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
    public double lambda;  //Hur m�nga per sekund som ska generas //How many to generate per second

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case READY: {
                SignalList.SendSignal(ARRIVAL, sendTo, time);
                SignalList.SendSignal(READY, this, time + (2.0 / lambda) * slump.nextDouble());
            }
            break;
        }
    }

}