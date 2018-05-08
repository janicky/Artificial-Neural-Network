public class Layer {

    private static int index = 1;
    private int id;
    private int neurons;

    public Layer(int neurons) {
        this.id = Layer.index++;
        this.neurons = neurons;
    }
}
