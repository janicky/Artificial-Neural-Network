public class Neurone {

    private Perceptron perceptron;
    private int layer;
    private static int index = 0;
    private int id;

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

    public int getInputsCount() {
        return perceptron.getWeights()[layer][id].length;
    }

    public double getResult(double input) {
        return perceptron.function(input);
    }

    @Override
    public String toString() {
        return "Neurone: " + Integer.toString(layer) + ":" + Integer.toString(id);
    }

    public boolean equals(Neurone n) {
        return id == n.getId();
    }
}
