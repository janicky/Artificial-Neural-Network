import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguratorTest {

    @Test
    void addLayer() {
        Configurator cfg = new Configurator();
        assertEquals(0, cfg.getLayersCount());
        cfg.addLayer(3);
        assertEquals(1, cfg.getLayersCount());
    }

    @Test
    void constructor() {
        Configurator cfg = new Configurator(new int[]{ 2, 3, 4 });
        assertEquals(3, cfg.getLayersCount());
    }

}