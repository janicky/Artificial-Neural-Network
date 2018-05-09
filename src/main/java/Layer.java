import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Layer {

    private static int index = 0;
    private int id;
    private List<Neurone> neurons = new ArrayList<Neurone>();

    public Layer() {
        this.id = Layer.index++;
    }

    public Layer(List<Neurone> neurons) {
        this.id = Layer.index++;
        this.neurons.addAll(neurons);
    }

    public int getId() {
        return id;
    }

    public static int getIndex() {
        return index;
    }

    public static void resetIndex() {
        index = 0;
    }

    public List<Neurone> getNeurons() {
        return Collections.unmodifiableList(neurons);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Layer: " + Integer.toString(id) + "\n");
        sb.append("---------------------------------------\n");
        for (Neurone neurone : neurons) {
            sb.append(neurone.toString() + "\n");
        }
        return sb.toString();
    }
}
