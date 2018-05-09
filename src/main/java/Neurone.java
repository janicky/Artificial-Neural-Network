public class Neurone {

    private Perceptron perceptron;
    private int layer;
    private static int index = 0;
    private int id;
    private double input;

    public Neurone(Perceptron perceptron) {
        this.id = Neurone.index++;
        this.perceptron = perceptron;
        this.layer = Layer.getIndex();
    }

    public int getId() {
        return id;
    }

    public static void resetIndex() {
        Neurone.index = 0;
    }

    public double getWeight(int weight_id) {
        return perceptron.getWeights()[layer][id][weight_id];
    }

    public double getInput() {
        return input;
    }

    public int getInputsCount() {
        return perceptron.getWeights()[layer][id].length;
    }

    public double getResult(double input) {
        this.input = input;
        return Perceptron.function(input);
    }

    @Override
    public String toString() {
        return "Neurone: " + Integer.toString(layer) + ":" + Integer.toString(id);
    }

    public boolean equals(Neurone n) {
        return id == n.getId();
    }
}
