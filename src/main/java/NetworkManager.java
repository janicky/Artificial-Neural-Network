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
    private double active_error = 100d;

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

    private double[] getClassified(double[] values) {
        double[] classified = new double[values.length];
        int i = 0;
        for (double d : values) {
            if (d >= 0.5) {
                classified[i++] = 1d;
            } else {
                classified[i++] = 0d;
            }
        }
        return classified;
    }

    public void learn() {
        setPatternsOrder();

        int epoch = 1;
        Iterator<Integer> it = patternsOrder.iterator();
        double errors_sum = 0d;

        do {
            if (!it.hasNext()) {
                active_error = errors_sum / (double) patternsOrder.size();
                errors_sum = 0d;
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
            perceptron.epoch(Perceptron.Mode.LEARNING);

//            Get output
            System.out.println("Outpt: " + Arrays.toString(perceptron.getResults()));

//            Get error
            System.out.println("e  = " + Double.toString(perceptron.getAverageError()));
            System.out.println("ge = " + Double.toString(active_error));
            System.out.println();

            errors_sum += perceptron.getAverageError();

        } while (epoch < 100000);
    }

    public void test() {

        for (int i = 0; i < outputPattern.size(); i++) {
            System.out.println("Pattern #" + Integer.toString(i));
            System.out.println("--------------------------------");

//            Set inputs
            perceptron.setInput(getInputValues(i));
            System.out.println("Input: " + Arrays.toString(getInputValues(i)));
//            Set expected values
            perceptron.setExpected(getExpectedValues(i));
            System.out.println("Expct: " + Arrays.toString(getExpectedValues(i)));

//            Go
            perceptron.epoch(Perceptron.Mode.TESTING);

//            Get classified
            if (Arrays.equals(getExpectedValues(i), getClassified(perceptron.getResults()))) {
                System.out.print("\033[0;32m");
            } else {
                System.out.print("\033[0;31m");
            }
            System.out.println("Outpt: " + Arrays.toString(getClassified(perceptron.getResults())));
            System.out.print("\033[0m");


//            Get output
            System.out.println("Outpt: " + Arrays.toString(perceptron.getResults()));

//            Get error
            System.out.println("e = " + Double.toString(perceptron.getAverageError()));
            System.out.println();
        }
    }

}
