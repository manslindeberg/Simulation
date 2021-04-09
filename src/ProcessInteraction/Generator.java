import java.util.*;


class Generator extends Proc {

    private static double rate;
    private Random rand = new Random();

    public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
    public double lambda;  //Hur m�nga per sekund som ska generas //How many to generate per second

    public Generator( double rate) {
        this.rate = rate;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case READY: {
                double timeOfDay = Global.time % HOURSPERDAY;

                if (timeOfDay >= OPENING) {
                    if (timeOfDay <= CLOSING) {
                        SignalList.SendSignal(ARRIVAL, sendTo, time);
                        SignalList.SendSignal(READY, this, time + (2.0 / rate) * rand.nextDouble());
                    } else {
                        SignalList.SendSignal(READY, this, time + (HOURSPERDAY - (CLOSING - OPENING)));
                    }
                }
            }
            break;
        }
    }
}