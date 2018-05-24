import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Configurator {

    private List<Integer> layers = new ArrayList<Integer>();
    private double range_min = -0.5;
    private double range_max = 0.5;
    private double learning_factor = 1;
    private double momentum = 0;
    private boolean bias = false;
    private int input_count;
    private boolean inputRotation = false;
    private int epochs = 100000;
    private int error_log_step = 1000;
    private double error = 0.05;
    private NetworkManager.ConditionMode condition = NetworkManager.ConditionMode.EPOCHS;
    private Random random = new Random();
    private String global_error_file = "global_error.csv";

    public Configurator(int input_count) {
        this.input_count = input_count;
    }

    public Configurator(int input_count, int[] layers) {
        for(int neurons : layers) {
            this.layers.add(neurons);
        }
        this.input_count = input_count;
    }

    public String getGlobalErrorFile() {
        return global_error_file;
    }

    public void setGlobalErrorFile(String global_error_file) {
        this.global_error_file = global_error_file;
    }

    public int getInputCount() {
        return input_count;
    }

    public void setInputCount(int input_count) {
        this.input_count = input_count;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public NetworkManager.ConditionMode getCondition() {
        return condition;
    }

    public void setCondition(NetworkManager.ConditionMode condition) {
        this.condition = condition;
    }

    public int getErrorLogStep() {
        return error_log_step;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public void setErrorLogStep(int error_log_step) {
        this.error_log_step = error_log_step;
    }

    public void setRange(double range_min, double range_max) {
        this.range_min = range_min;
        this.range_max = range_max;
    }

    public void setLearningFactor(double learning_factor) {
        this.learning_factor = learning_factor;
    }

    public double getLearningFactor() {
        return learning_factor;
    }

    public double randWeight() {
        return range_min + (range_max - range_min) * random.nextDouble();
    }

    public void addLayer(int neurons) {
        layers.add(neurons);
    }

    public int getLayersCount() {
        return layers.size();
    }

    public boolean isBias() {
        return bias;
    }

    public void setBias(boolean bias) {
        this.bias = bias;
    }


    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public boolean isInputRotation() {
        return inputRotation;
    }

    public void setInputRotation(boolean inputRotation) {
        this.inputRotation = inputRotation;
    }

    public double[][][] getWeightsMatrix(int input_count) {
        double[][][] matrix = new double[getLayersCount()][][];
        for(int i = 0; i < getLayersCount(); i++) {
            matrix[i] = new double[layers.get(i)][];
//            shift for bias
            int shift = (i < getLayersCount() - 1 ? 1 : 1);
            for (int j = 0; j < layers.get(i); j++) {
                matrix[i][j] = new double[input_count + shift];
            }
            input_count = layers.get(i);
        }
        return matrix;
    }

    public double[][] getOutputsMatrix(int input_count) {
        double[][] matrix = new double[getLayersCount() + 1][];
        matrix[0] = new double[input_count];
        for (int i = 1; i < getLayersCount() + 1; i++) {
//            shift for bias
            int shift = (i < getLayersCount() ? 1 : 0);
            matrix[i] = new double[layers.get(i - 1) + shift];
        }
        return matrix;
    }

    public List<Integer> getLayers() {
        return Collections.unmodifiableList(layers);
    }
}
