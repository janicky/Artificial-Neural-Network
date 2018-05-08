import java.util.ArrayList;
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
}
