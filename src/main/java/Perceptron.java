import java.util.ArrayList;
import java.util.List;

public class Perceptron {

    private Configurator cfg;
    private List<Layer> layers = new ArrayList<Layer>();
    private List<Number> input = new ArrayList<Number>();
    private double[][][] weights;

    public Perceptron(Configurator cfg, List<Number> input) {
        this.cfg = cfg;
        this.input = input;
        weights = cfg.getWeightsMatrix(input.size());
    }

    private List<Neurone> generateNeurons(int count) {
        List<Neurone> neurons = new ArrayList<Neurone>();
        Neurone.resetIndex();
        for (int i = 0; i < count; i++) {
            neurons.add(new Neurone(this));
        }
        return neurons;
    }

    public void init() {
        for (int neurons : cfg.getLayers()) {
            layers.add(new Layer(generateNeurons(neurons)));
        }
        for (Layer layer : layers) {
            for (Neurone neurone : layer.getNeurons()) {
                System.out.println(neurone.toString() + " - " + neurone.getInputsCount());
            }
        }
    }

    public double[][][] getWeights() {
        return weights;
    }
}