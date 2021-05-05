import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/* This class reads and sets values read from the properties config file. */
public class Config {

    public void readConfig() throws IOException {
        Properties properties = new Properties();
        String config = "config.properties";
        InputStream input = getClass().getClassLoader().getResourceAsStream(config);

        if (input != null) {
           properties.load(input);
        } else {
            throw new FileNotFoundException("Configuration file not found");
        }
        Global.ROUND = Integer.parseInt(properties.getProperty("ROUND"));
        Global.NOSENSORS = Integer.parseInt(properties.getProperty("NOSENSORS"));
        Global.AREAHEIGHT = Integer.parseInt(properties.getProperty("AREAHEIGHT"));
        Global.AREAWIDTH = Integer.parseInt(properties.getProperty("AREAWIDTH"));
        Global.COMMUNICATIONRANGE = Integer.parseInt(properties.getProperty("COMMUNICATIONRANGE"));
        Global.TRANSMITTIME = Double.parseDouble(properties.getProperty("TRANSMITTIME"));
        Global.MEANINTERARRIVALTIME = Integer.parseInt(properties.getProperty("MEANINTERARRIVALTIME"));
        Global.CONTROLTIME = Double.parseDouble(properties.getProperty("CONTROLTIME"));
        Global.MEANSAMPLETIME = Double.parseDouble(properties.getProperty("MEANSAMPLETIME"));
        Global.CONFIDENCELEVEL = Double.parseDouble(properties.getProperty("CONFIDENCELEVEL"));
        Global.STDDEVSTOPCONDITION = Double.parseDouble(properties.getProperty("STDDEVSTOPCONDITION"));
        Global.LOWLIMIT = Double.parseDouble(properties.getProperty("LOWLIMIT"));
        Global.UPLIMIT = Double.parseDouble(properties.getProperty("UPLIMIT"));
        input.close();
    }
}
