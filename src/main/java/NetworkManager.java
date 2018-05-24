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

    private void fillPatternsOrder() {
        for (int i = 0; i < inputPattern.size(); i++) {
            patternsOrder.add(i);
        }
    }

    private void randPatternsOrder() {
        Random rng = new Random();
        while (patternsOrder.size() < inputPattern.size()) {
            Integer next = rng.nextInt(inputPattern.size());
            patternsOrder.add(next);
        }
    }

    private void setPatternsOrder() {
        if (cfg.isInputRotation()) {
            patternsOrder.clear();
            randPatternsOrder();
        } else if (patternsOrder.size() == 0) {
            fillPatternsOrder();
        }
    }

    private double[] getInputValues(int current_element) {
        double[] output = new double[inputPattern.get(current_element).size()];
        int i = 0;
        for (Double out : inputPattern.get(current_element)) {
            output[i++] = out;
        }
        return output;
    }

    private double[] getExpectedValues(int current_element) {
        double[] output = new double[outputPattern.get(current_element).size()];
        int i = 0;
        for (Double out : outputPattern.get(current_element)) {
            output[i++] = out;
        }
        return output;
    }


    public void start() {
        setPatternsOrder();

        int epoch = 1;
        Iterator<Integer> it = patternsOrder.iterator();

        while (epoch < 1000000) {
            if (!it.hasNext()) {
                setPatternsOrder();
                it = patternsOrder.iterator();
            }

            System.out.println("Epoch " + Integer.toString(epoch++));
            System.out.println("--------------------------------");
            int current_element = it.next();

//            Set inputs
            perceptron.setInput(getInputValues(current_element));
            System.out.println("Input: " + Arrays.toString(getInputValues(current_element)));
//            Set expected values
            perceptron.setExpected(getExpectedValues(current_element));
            System.out.println("Expct: " + Arrays.toString(getExpectedValues(current_element)));

//            Go
            perceptron.epoch();

//            Get output
            System.out.println("Outpt: " + Arrays.toString(perceptron.getResults()));

//            Get error
            System.out.println("e = " + Double.toString(perceptron.getAverageError()));
            System.out.println();
        }
    }

}
