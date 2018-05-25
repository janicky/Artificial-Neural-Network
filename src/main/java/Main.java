// https://github.com/janicky/Multi-Layer-Perceptron

public class Main {

    public static void main(String[] args) {

        Configurator cfg = new Configurator(4, new int[]{ 2, 4 });

//        cfg.setLearningFactor(0.2);
//        cfg.setMomentum(0.9);
        cfg.setBias(true);
        cfg.setInputRotation(true);

        NetworkManager nm = new NetworkManager(cfg);
        nm.loadPatterns("patterns.txt");

//        Learning
        nm.learn();
        nm.saveNetwork();

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
