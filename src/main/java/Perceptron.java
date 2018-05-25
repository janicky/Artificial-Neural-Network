import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Perceptron {

    private Configurator cfg;
    private List<Layer> layers = new ArrayList<Layer>();
    private double[] input;
    private double[] expected;
    private double[][][] weights;
    private double[][][] last_delta;
    private double[][] outputs;
    private double[] results;
    private double b0;

    public enum Mode {
        LEARNING, TESTING
    }

    public Perceptron(Configurator cfg) {
        this.cfg = cfg;
        initialize();
    }

    private void initialize() {
//        Initialize layers with neurons
        for (int neurons : cfg.getLayers()) {
            layers.add(new Layer(generateNeurons(neurons)));
        }
//        Prepare weights matrix
        weights = cfg.getWeightsMatrix(cfg.getInputCount());
//        Rand weights
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = cfg.randWeight();
                }
            }
        }
//        Initialize last propagation array for momentum
        last_delta = cfg.getWeightsMatrix(cfg.getInputCount());
//        Prepare matrix with inputs
        outputs = cfg.getOutputsMatrix(cfg.getInputCount());
//        Initialize results array
        results = new double[layers.get(layers.size() - 1).getNeuronsCount()];
    }

    public static double function(double x) {
        return 1d / (1 + Math.pow(Math.E, -x));
    }

    public static double derivative(double x) {
        return Math.pow(Math.E, -x) / Math.pow(1 + Math.pow(Math.E, -x), 2);
    }

    private List<Neurone> generateNeurons(int count) {
        List<Neurone> neurons = new ArrayList<Neurone>();
        Neurone.resetIndex();
        for (int i = 0; i < count; i++) {
            neurons.add(new Neurone(this));
        }
        return neurons;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public void setExpected(double[] expected) {
        this.expected = expected;
    }

    public void setWeight(int layer, int neurone, int weight, double value) {
        weights[layer][neurone][weight] = value;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    public void epoch(Mode mode) {
//        Assign input as first output
        outputs[0] = new double[input.length + 1];
        System.arraycopy(input, 0, outputs[0], 0, input.length);
        outputs[0][input.length] = (cfg.isBias() ? 1 : 0);


//        Iterate layers
        for (Layer layer : layers) {
            for (Neurone neurone : layer.getNeurons()) {
                double sum = 0;
                for (int i = 0; i < outputs[layer.getId()].length; i++) {
                    sum += outputs[layer.getId()][i] * weights[layer.getId()][neurone.getId()][i];
                }
                outputs[layer.getId() + 1][neurone.getId()] = neurone.getResult(sum);
                if (layer.getId() < cfg.getLayersCount() - 1) {
                    outputs[layer.getId() + 1][layer.getNeuronsCount()] = (cfg.isBias() ? 1 : 0);
                }
            }
        }

//        Call Backpropagation
        if (mode == Mode.LEARNING) {
            backpropagation();
        }

//        Assign results
        results = outputs[outputs.length - 1];
    }

    private void backpropagation() {
//        Iterate layers in reverse order
        for (int l = layers.size() - 1; l >= 0; l--) {
            Layer layer = layers.get(l);
            for (int n = 0; n < layer.getNeuronsCount(); n++) {
                Neurone neurone = layer.getNeurone(n);
                for (int w = 0; w < weights[l][n].length; w++) {
//                    Delta calculation
                    weights[l][n][w] += weightCorrection(layer, neurone, w);
                }
            }
        }
    }

    private double weightCorrection(Layer layer, Neurone neurone, int weight) {
        int l = layer.getId(), n = neurone.getId();
        double b = 0;
        if (l == layers.size() - 1) {
            b = (results[n] - expected[n]) * derivative(neurone.getInput());
            b0 = b;
        } else {
            for (int i = 0; i < weights[l][n].length; i++) {
                b += b0 * weights[l][n][i];
            }
        }
        last_delta[l][n][weight] = -cfg.getLearningFactor() * b * outputs[l][weight]
                                   + cfg.getMomentum() * last_delta[l][n][weight];
        return last_delta[l][n][weight];
    }

    public double[] getResults() {
        return results;
    }

    public double[] getOutputErrors() {
        double[] diff = new double[results.length];
        for (int i = 0; i < results.length; i++) {
            diff[i] = Math.abs(expected[i] - results[i]);
        }
        return diff;
    }

    public double getAverageError() {
        double sum = 0d;
        for (int i = 0; i < results.length; i++) {
            sum += Math.abs(expected[i] - results[i]);
        }
        if (results.length == 0) {
            return 0d;
        }
        return Math.abs(sum / results.length);
    }
}
