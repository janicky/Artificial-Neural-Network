import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        Configuration
//        each number - one processing layer
//        number -> number of neurons on the processing layer
//        first layer - 6 neurons, second layer - 2 neurons (output layer)
        Configurator cfg = new Configurator(new int[]{ 2, 4 });
        cfg.setBias(true);

//        cfg.setRange(double, double)          -- default: (-0.5, 0.5)
//        cfg.setLearningFactor(double)         -- default: 1.0
//        cfg.setMomentum(double)               -- default: 0.0
        cfg.setRange(-1d, 1d);
        cfg.setLearningFactor(0.2);
        cfg.setMomentum(0.9);

//        Perceptron
//        first: Configurator instance
//        second: input values
//        third: expected values
        Perceptron perceptron = new Perceptron(cfg, new double[] { 1d, 0d, 0d, 0d }, new double[] { 1d, 0d, 0d, 0d });


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
