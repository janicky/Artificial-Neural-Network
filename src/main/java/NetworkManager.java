import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class NetworkManager {

    private Perceptron perceptron;
    private Configurator cfg;
    private List<List<Double>> inputPattern = new ArrayList<>();
    private List<List<Double>> outputPattern = new ArrayList<>();
    Set<Integer> patternsOrder = new LinkedHashSet<>();
    private double active_error = 100d;
    private NetworkStream ns;

    public enum ConditionMode {
            ERROR, EPOCHS
    }

    public NetworkManager(Configurator cfg) {
        this.cfg = cfg;
        this.perceptron = new Perceptron(cfg);
        this.ns = new NetworkStream(perceptron);
    }

    public boolean patternsLoaded() {
        return inputPattern.size() > 0 && outputPattern.size() > 0;
    }

    public String getFileName() {
        return ns.getFileName();
    }

    public void setFileName(String file_name) {
        ns.setFileName(file_name);
    }

    public void loadPatterns(String patterns_path) throws Exception {

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

    }

    public Perceptron getPerceptron() {
        return perceptron;
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

    private void resetFile(File f) {
        try {
            PrintWriter writer = new PrintWriter(f);
            writer.print("");
            writer.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void learn(JTextField jtxt, JTextArea jar) {
        setPatternsOrder();

        int epoch = 1;
        Iterator<Integer> it = patternsOrder.iterator();
        double errors_sum = 0d;

//        Reset global_error file
        File f = new File(cfg.getGlobalErrorFile());
        resetFile(f);

        do {
            if (!it.hasNext()) {
                active_error = errors_sum / (double) patternsOrder.size();
                errors_sum = 0d;
                setPatternsOrder();
                it = patternsOrder.iterator();
            }

            int current_element = it.next();

//            Set inputs
            perceptron.setInput(getInputValues(current_element));

//            Set expected values
            perceptron.setExpected(getExpectedValues(current_element));

//            Go
            perceptron.epoch(Perceptron.Mode.LEARNING);
            jtxt.setText(Double.toString(active_error));

            errors_sum += perceptron.getAverageError();

//            Save global error
            if ((epoch + cfg.getErrorLogStep()) % cfg.getErrorLogStep() == 0) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Integer.toString(epoch) + ",");
                    sb.append(Double.toString(active_error) + "\n");
                    Files.write(Paths.get(cfg.getGlobalErrorFile()), sb.toString().getBytes(), StandardOpenOption.APPEND);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } while ((active_error > cfg.getError() && cfg.getCondition() == ConditionMode.ERROR && !cfg.isStop()) || (epoch < cfg.getEpochs() && cfg.getCondition() == ConditionMode.EPOCHS) && !cfg.isStop());

        if (!cfg.isStop()) {
            jar.append("\n ══════════ Nauka została zakończona ══════════\n");
            jar.append(" -- Wykaz błędu globalnego: " + cfg.getGlobalErrorFile() + "\n");
            jar.append(" -- Ostateczny błąd globalny: " + Double.toString(active_error) + "\n\n");
        } else {
            jar.append("\n ══════════ Nauka została przerwana ══════════\n");
        }
    }

    public void test() {

//        Reset global_error file
        File f = new File(cfg.getTestingFile());
        resetFile(f);

        logNetwork();

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

            logLayer(i);
        }
    }

    public void saveNetwork() {
        try {
            ns.saveNetwork();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void loadNetwork() {
        try {
            ns.loadNetwork();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void logNetwork() {
//            Save to file
        try {
            StringBuilder header = new StringBuilder();
            header.append("=========================== Multi-Layer Perceptron ===========================\n");
            header.append("[L0] Input:        " + Integer.toString(cfg.getInputCount()) + "\n");
            int i = 0;
            for (i = 0; i < cfg.getLayersCount() - 1; i++) {
                header.append("[L" + Integer.toString(i + 1) + "] Hidden:       " + Integer.toString(cfg.getLayers().get(i)) + "\n");

            }
            header.append("[L" + Integer.toString(i + 1) + "] Output:       " + Integer.toString(cfg.getLayers().get(i)) + "\n");

            header.append("\n\n=============================== Output weights ===============================\n");
            int n = 0;
            for (double[] weights : perceptron.getWeights()[perceptron.getWeights().length - 1]) {
                header.append("[N" + Integer.toString(n++) + "]: " + Arrays.toString(weights) + "\n");
            }

            header.append("\n\n=============================== Hidden weights ===============================\n");

            for (int l = perceptron.getWeights().length - 1; l >= 0; l--) {
                int hn = 0;
                header.append("----------------------------------- Layer " + Integer.toString(l) + " ----------------------------------\n");
                for (double[] weights : perceptron.getWeights()[l]) {
                    header.append("[N" + Integer.toString(hn++) + "]: " + Arrays.toString(weights) + "\n");
                }
            }


            Files.write(Paths.get(cfg.getTestingFile()), header.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void logLayer(int i) {
//            Save to file
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\n================================= Pattern " + Integer.toString(i) + " ==================================\n");
            sb.append("Input:        " + Arrays.toString(getInputValues(i)) + "\n");
            sb.append("Expected:     " + Arrays.toString(getExpectedValues(i)) + "\n");
            sb.append("Output*:      " + Arrays.toString(getClassified(perceptron.getResults())) + "\n");
            sb.append("Output:       " + Arrays.toString(perceptron.getResults()) + "\n");
            sb.append("e(y) =        " + Arrays.toString(perceptron.getOutputErrors()) + "\n");
            sb.append("e =           " + Double.toString(perceptron.getAverageError()) + "\n");

            sb.append("\n=============================== Hidden outputs ===============================\n");

            for (int l =  0; l < perceptron.getOutputs().length; l++) {
                int hn = 0;
                sb.append("----------------------------------- Layer " + Integer.toString(l) + " ----------------------------------\n");
                sb.append(Arrays.toString(perceptron.getOutputs()[l]) + "\n");
            }

            sb.append("\n");

            Files.write(Paths.get(cfg.getTestingFile()), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
