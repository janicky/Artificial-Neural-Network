import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Perceptron {

    private Configurator cfg;
    private List<Layer> layers = new ArrayList<Layer>();
    private List<Double> input = new ArrayList<Double>();
    private List<Double> expected = new ArrayList<Double>();
    private double[][][] weights;
    private double[][] outputs;

    public Perceptron(Configurator cfg, List<Double> input, List<Double> expected) {
        this.cfg = cfg;
        this.input = input;
        this.expected = expected;
        initialize();
    }

    public Perceptron(Configurator cfg, Double[] input, Double[] expected) {
        this.cfg = cfg;
        this.input = new ArrayList<Double>(Arrays.asList(input));
        this.expected = new ArrayList<Double>(Arrays.asList(expected));
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
//        Prepare matrix with inputs
        outputs = cfg.getOutputsMatrix(input.size());
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

    public void epoch() {
//        Input to array
        double[] input_array = new double[input.size()];
        for (int i = 0; i < input.size(); i++) {
            input_array[i] = input.get(i);
        }
        outputs[0] = input_array;

//        Neurons instances
        for (int neurons : cfg.getLayers()) {
            layers.add(new Layer(generateNeurons(neurons)));
        }

//        Iterate layers
        for (Layer layer : layers) {
            System.out.println("Layer #" + Integer.toString(layer.getId()));
            System.out.println("--------------------------------------");
            for (Neurone neurone : layer.getNeurons()) {
                System.out.println("Neurone #" + Integer.toString(neurone.getId()));
                double sum = 0;
                for (int i = 0; i < outputs[layer.getId()].length; i++) {
                    System.out.println(Double.toString(outputs[layer.getId()][i]) + " x " + Double.toString(weights[layer.getId()][neurone.getId()][i]));
                    sum += outputs[layer.getId()][i] * weights[layer.getId()][neurone.getId()][i];
                }
                outputs[layer.getId() + 1][neurone.getId()] = sum;
                System.out.println("|--------------------");
                System.out.println(Double.toString(sum));
                System.out.println();
            }
            System.out.println();
        }

        System.out.println(Arrays.deepToString(outputs));
    }
}
