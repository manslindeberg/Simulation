import java.util.*;


class IntervalGenerator extends Proc {

    public static double rate;
    private Random rand = new Random();

    public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
    public double lambda;  //Hur m�nga per sekund som ska generas //How many to generate per second
    private double next;

    public IntervalGenerator(double rate) {
        this.rate = rate;
    }

    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case READY: {
                double timeOfDay = Global.time % HOURSPERDAY;

                if (timeOfDay >= OPENING) {
                    if (timeOfDay <= CLOSING) {
                        SignalList.SendSignal(ARRIVAL, sendTo, time);
                        next = nextDoubleExp(rand, rate);
                        SignalList.SendSignal(READY, this, time + next);
                    } else {
                        next = nextDoubleExp(rand, rate);
                        SignalList.SendSignal(READY, this, time + (HOURSPERDAY - (time % 24) + OPENING) + next);
                    }
                }
            }
            break;
        }
    }

    /* Function returns a exponentially distributed number with a mean rate of lambda */
    public double nextDoubleExp(Random rand, double lambda) {
        return Math.log(1 - rand.nextDouble()) / (-lambda);
    }

    public double getRate() {
        return rate;
    }

}