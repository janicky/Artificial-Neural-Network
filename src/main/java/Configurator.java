import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Configurator {

    private List<Integer> layers = new ArrayList<Integer>();

    public Configurator() {}

    public Configurator(int[] layers) {
        for(int neurons : layers) {
            this.layers.add(neurons);
        }
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

    public List<Integer> getLayers() {
        return Collections.unmodifiableList(layers);
    }
}
