import java.util.Arrays;

/* This class implements some of the most common analysis functions on a set of data points.
 *  such as estimating mean value, variance, standarddeviation aswell as comfidence intervel.
 *  Note that the available confidence intervals are:
 *  99.9%
 *  99.8%
 *  99.0%
 *  98.0%
 *  95.0%
 *  90.0%
 *  80.0%
 */

public class Measurement {

    public static final double za2_999p = 3.27;
    public static final double za2_998p = 3.08;
    public static final double za2_990p = 2.58;
    public static final double za2_980p = 2.33;
    public static final double za2_950p = 1.96;
    public static final double za2_900p = 1.645;
    public static final double za2_800p = 1.28;

    /* Calculates the estimated mean value of data.*/
    public double mean(double[] data) {
        double sum = 0;
        int size = 0;

        for (double value : data) {
            size++;
            sum = sum + value;
        }
        return sum / size;
    }

    public void printMean(double mean) {
        System.out.println("Estimated Mean Value: " + mean);
    }

    /* Calulates the variance estimator S^2. */
    public double variance(double[] data, double mean) {
        double quadSumData = 0;
        int size = data.length;

        for (double x : data) {
            quadSumData = quadSumData + Math.pow((x - mean), 2);
        }
        return quadSumData / (size - 1);
    }

    public void printVariance(double variance) {
        System.out.println("Estimated Mean Variance: " + variance);
    }

    public double standardDeviation(double[] data, double mean) {
        return Math.sqrt(variance(data, mean));
    }

    public void printStandardDeviation(double dev) {
        System.out.println("Estimated Mean Variance: " + dev);
    }

    public double[] confidenceInterval(double[] data, double mean, double level) {
        double n = data.length;
        double standarddev = standardDeviation(data,mean);
        double[] interval = new double[2];

        if (level == 99.9) {
            double dev = za2_999p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 99.8) {
            double dev = za2_998p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 99.0) {
            double dev = za2_990p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 98.0) {
            double dev = za2_980p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 95.0) {
            double dev = za2_950p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 90.0) {
            double dev = za2_900p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else if (level == 80.0) {
            double dev = za2_800p * standarddev / Math.sqrt(n);
            interval[0] = mean - dev;
            interval[1] = mean + dev;
        } else {
            System.err.println("Error: only certain confidence levels allowed. See class header in Measurement.java");
            return new double[]{0, -1}; // note that this cannot occur in theory
        }
        return interval;
    }

    public void printConfidenceInterval(double[] interval, double mean, double level) {
        System.out.println("Estimated confidence interval: [" +
                interval[0] + " ," + interval[1] + " ]" + " Level: " + level);

        if (mean >= interval[0] && mean <= interval[1]) {
            System.out.println("Mean Value in Confidence Interval [yes]");
        } else {
            System.out.println("Mean Value in Confidence Interval [no]");
        }
    }

    public void printDataToFile(String fileName, String[] variableName, double[][] data) {
        SimpleFileWriter fw = new SimpleFileWriter(fileName, false);
        if (variableName.length != data.length) {
            System.err.println("Error: number of variable names and data columns not equal.");
        } else {
            int i = 0;
            for (String variable : variableName) {
                if (i != 0) {
                    fw.print("\r\n");
                }
                fw.print(variable);
                fw.print("=[");
                int rowLength = data[i].length;
                for (int j = 0; j < rowLength; j++) {
                    fw.print("\r\n");
                    fw.print(Double.toString((data[i][j])));
                    if (j == (rowLength - 1)) {
                        fw.print("];");
                    }
                }
                fw.println("");
                i++;
            }
        }
        fw.close();
    }
}