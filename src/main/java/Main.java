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

//        cfg.setRange(double, double)          -- default: (-0.5, 0.5)
//        cfg.setLearningFactor(double)         -- default: 1.0
//        cfg.setMomentum(double)               -- default: 0.0
//        cfg.setBias(boolean)                  -- default: false
        cfg.setRange(-1d, 1d);
        cfg.setLearningFactor(0.2);
        cfg.setMomentum(0.9);
        cfg.setBias(true);

//        Perceptron
//        first: Configurator instance
//        second: input values
//        third: expected values
        Perceptron perceptron = new Perceptron(cfg, new double[] { 1d, 0d, 0d, 0d }, new double[] { 1d, 0d, 0d, 0d });


        NetworkManager nm = new NetworkManager(perceptron, cfg);
        nm.start();
    }



}
