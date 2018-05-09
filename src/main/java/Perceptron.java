import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Perceptron {

    private Configurator cfg;
    private List<Layer> layers = new ArrayList<Layer>();
    private List<Number> input = new ArrayList<Number>();
    private List<Number> expected = new ArrayList<Number>();
    private double[][][] weights;

    public Perceptron(Configurator cfg, List<Number> input, List<Number> expected) {
        this.cfg = cfg;
        this.input = input;
        this.expected = expected;
        initialize();
    }

    public Perceptron(Configurator cfg, Double[] input, Double[] expected) {
        this.cfg = cfg;
        this.input = new ArrayList<Number>(Arrays.asList(input));
        this.expected = new ArrayList<Number>(Arrays.asList(expected));
        initialize();
    }

    private void initialize() {
//        Prepare weights matrix
        weights = cfg.getWeightsMatrix(input.size());
//        Rand weights
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = cfg.randWeight();
                }
            }
        }
    }

    private List<Neurone> generateNeurons(int count) {
        List<Neurone> neurons = new ArrayList<Neurone>();
        Neurone.resetIndex();
        for (int i = 0; i < count; i++) {
            neurons.add(new Neurone(this));
        }
        return neurons;
    }

    public void setWeight(int layer, int neurone, int weight, double value) {
        weights[layer][neurone][weight] = value;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public void start() {
        for (int neurons : cfg.getLayers()) {
            layers.add(new Layer(generateNeurons(neurons)));
        }
        for (Layer layer : layers) {
            System.out.println("Layer #" + Integer.toString(layer.getId()));
            for (Neurone neurone : layer.getNeurons()) {

            }
        }
    }
}
