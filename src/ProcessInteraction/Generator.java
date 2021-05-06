import java.util.*;
import java.io.*;

class Generator extends Proc {

    Random slump = new Random();

    public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
    public double interarrival;  //Hur m�nga per sekund som ska generas //How many to generate per second

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case READY: {
                SignalList.SendSignal(ARRIVAL, sendTo, time);
                SignalList.SendSignal(READY, this, time + 2*interarrival* slump.nextDouble());
            }
            break;
        }
    }

}