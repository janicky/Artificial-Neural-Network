import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NetworkManager {

    private Perceptron perceptron;
    private Configurator cfg;

    public NetworkManager(Perceptron perceptron, Configurator cfg) {
        this.perceptron = perceptron;
        this.cfg = cfg;
    }

    public void start() {
//        Simple temporary control interface
//        System.out.println("Please click enter to start new epochs or q to quit...");
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        int epoch = 1;
        long start = System.nanoTime();
        while (perceptron.getAverageError() > 0.0005) {
            perceptron.epoch();
            epoch++;

            System.out.println("Epoch #" + Integer.toString(epoch) + ": ---------------------");
            int y = 0;
            for(double result : perceptron.getResults()) {
                System.out.println("y" + Integer.toString(y) + " = " + df.format(result));
                y++;
            }
            System.out.println(Double.toString(perceptron.getAverageError()));
            System.out.println();
        }

        System.out.println("Elapsed time: " + Double.toString((double) (System.nanoTime() - start) / 1000000000.0));
    }
}
