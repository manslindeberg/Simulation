import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WirelessNetworkSimulation {

    public static void main(String[] args) throws IOException {
        Config conf = new Config();
        conf.readConfig();

        Signal signal;
        new SignalList();

        Global.time = 0;
        NetworkArea city = new NetworkArea(Global.AREAWIDTH, Global.AREAHEIGHT, Global.NOSENSORS);
        System.out.println(Global.ROUND);
        city.init();

        boolean stop = false;
        int newtime = 0;
        int oldtime = 0;
        // stops until stddev stop condition is met given in config file
        while (!stop) {
            signal = SignalList.FetchSignal();
            Global.time = signal.arrivalTime;
            signal.destination.TreatSignal(signal);

            newtime = (int) Global.time;
            if (newtime > 0 && newtime != oldtime && ((int) Global.time % 100) == 0) {
                oldtime = newtime;
                stop = city.stopSimulation(Global.STDDEVSTOPCONDITION, Global.ROUND);
                System.out.println(Global.time);
            }
        }
        city.writeConfidenceInterval(Global.ROUND);
        city.printDataToMatlab(Global.ROUND);
        System.out.println("simulation stop time: " + Global.time);
    }
}
