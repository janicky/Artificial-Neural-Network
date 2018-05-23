import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class NetworkManager {

    private Perceptron perceptron;
    private Configurator cfg;
    private List<List<Double>> inputPattern = new ArrayList<>();
    private List<List<Double>> outputPattern = new ArrayList<>();
    Set<Integer> patternsOrder = new LinkedHashSet<>();

    public NetworkManager(Perceptron perceptron, Configurator cfg) {
        this.perceptron = perceptron;
        this.cfg = cfg;
    }

    public void loadPatterns(String patterns_path) {
        try {
            File file = new File(patterns_path);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] patterns = line.split("\\|");
//                insert input pattern
                List<Double> inlist = new ArrayList<>();
                for (String value : patterns[0].split("\\,")) {
                    inlist.add(Double.parseDouble(value));
                }
                inputPattern.add(inlist);
//                insert output pattern
                List<Double> oulist = new ArrayList<>();
                for (String value : patterns[1].split("\\,")) {
                    oulist.add(Double.parseDouble(value));
                }
                outputPattern.add(oulist);
            }
            sc.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fillPatternOrder() {
        patternsOrder.clear();
        for (int i = 0; i < inputPattern.size(); i++) {
            patternsOrder.add(i);
        }
    }

    private void randPatternsOrder() {
        patternsOrder.clear();
        Random rng = new Random();
        while (patternsOrder.size() < inputPattern.size()) {
            Integer next = rng.nextInt(inputPattern.size());
            patternsOrder.add(next);
        }
    }


    public void start() {
        if (cfg.isInputRotation()) {
            randPatternsOrder();
        } else {
            fillPatternOrder();
        }

        System.out.println(patternsOrder.toString());
    }

    public void startx() {
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
