import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        Configuration
//        each number - one processing layer
//        number -> number of neurons on the processing layer
//        first layer - 6 neurons, second layer - 2 neurons (output layer)
        Configurator cfg = new Configurator(new int[]{ 6, 2 });

//        cfg.setRange(double, double)          -- default: (-0.5, 0.5)
//        cfg.setLearningFactor(double)         -- default: 1.0

//        Perceptron
//        first: Configurator instance
//        second: input values
//        third: expected values
        Perceptron perceptron = new Perceptron(cfg, new double[] { 0.5, 0.2 }, new double[] { 0.2, 0.5 });


//        Simple temporary control interface
        int epoch = 0;

        Scanner keyboard = new Scanner(System.in);
        boolean next_epoch = true;

        System.out.println("Please click enter to start new epochs or q to quit...");
        while (next_epoch) {
            String readString = keyboard.nextLine();
            if ("q".equals(readString)) {
                break;
            }

            for (int i = 1; i <= 10; i++) {
                perceptron.epoch();
                epoch++;
            }

            System.out.println("Epoch #" + Integer.toString(epoch) + ": ---------------------");
            int y = 0;
            for(double result : perceptron.getResults()) {
                System.out.println("y" + Integer.toString(y) + " = " + Double.toString(result));
                y++;
            }
        }
        keyboard.close();



    }



}
