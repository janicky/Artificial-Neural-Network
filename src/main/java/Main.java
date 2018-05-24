// https://github.com/janicky/Multi-Layer-Perceptron

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

        cfg.setLearningFactor(0.2);
        cfg.setMomentum(0.9);
        cfg.setBias(true);
        cfg.setInputRotation(true);

//        Perceptron
//        first: Configurator instance
//        second: input values
//        third: expected values
        Perceptron perceptron = new Perceptron(cfg, new double[] { 1d, 0d, 0d, 0d }, new double[] { 1d, 0d, 0d, 0d });


        NetworkManager nm = new NetworkManager(perceptron, cfg);
        nm.loadPatterns("patterns.txt");

//        Learning
        nm.learn();

        System.out.println("==================================================");
        System.out.println("==================================================");
        System.out.println(" TESTING ");
        System.out.println("==================================================");
        System.out.println("==================================================");
        System.out.println();

//        Testing
        nm.test();
    }



}
