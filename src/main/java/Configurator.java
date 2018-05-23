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
    private boolean inputRotation = false;
    private Random random = new Random();

    public Configurator() {}

    public Configurator(int[] layers) {
        for(int neurons : layers) {
            this.layers.add(neurons);
        }
    }

    public double getRangeMin() {
        return range_min;
    }

    public double getRangeMax() {
        return range_max;
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
