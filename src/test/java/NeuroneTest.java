import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeuroneTest {

    private Perceptron perceptron;

    @BeforeEach
    void createPerceptron() {
        Configurator cfg = new Configurator(new int[] { 1, 2, 3 });
        perceptron = new Perceptron(cfg, new double[] { 0.5, 0d }, new double[] { 0d, 1d });
    }


    @Test
    void id() {
        Neurone.resetIndex();
        Neurone neurone_1 = new Neurone(perceptron);
        assertEquals(0, neurone_1.getId());

        Neurone neurone_2 = new Neurone(perceptron);
        assertEquals(1, neurone_2.getId());
    }

    @Test
    void resetIndex() {
        id();
        Neurone.resetIndex();

        Neurone neurone_3 = new Neurone(perceptron);
        assertEquals(0, neurone_3.getId());
    }

    @Test
    void getWeight() {
        Neurone.resetIndex();
        Neurone neurone = new Neurone(perceptron);
        perceptron.setWeight(0, 0, 0, 1.5);
        assertEquals(1.5, neurone.getWeight(0));
    }

    @Test
    void getInputsCount() {
        Neurone.resetIndex();
        Neurone neurone = new Neurone(perceptron);
        assertEquals(2, neurone.getInputsCount());
    }

}