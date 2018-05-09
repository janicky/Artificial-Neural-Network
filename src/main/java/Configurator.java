import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Configurator {

    private List<Integer> layers = new ArrayList<Integer>();
    private double range_min = -0.5;
    private double range_max = 0.5;
    private double learning_factor = 1;
    private Random random = new Random();

    public Configurator() {}

    public Configurator(int[] layers) {
        for(int neurons : layers) {
            this.layers.add(neurons);
        }
    }

    public double getRange_min() {
        return range_min;
    }

    public void setRange_min(double range_min) {
        this.range_min = range_min;
    }

    public double getRange_max() {
        return range_max;
    }

    public void setRange_max(double range_max) {
        this.range_max = range_max;
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

    public double[][][] getWeightsMatrix(int input_count) {
        double[][][] matrix = new double[getLayersCount()][][];
        for(int i = 0; i < getLayersCount(); i++) {
            matrix[i] = new double[layers.get(i)][];
            for (int j = 0; j < layers.get(i); j++) {
                matrix[i][j] = new double[input_count];
            }
            input_count = layers.get(i);
        }
        return matrix;
    }

    public double[][] getOutputsMatrix(int input_count) {
        double[][] matrix = new double[getLayersCount() + 1][];
        matrix[0] = new double[input_count];
        for (int i = 1; i < getLayersCount() + 1; i++) {
            matrix[i] = new double[layers.get(i - 1)];
        }
        return matrix;
    }

    public List<Integer> getLayers() {
        return Collections.unmodifiableList(layers);
    }
}
