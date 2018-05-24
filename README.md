
# Multilayer Perceptron
  
> A multilayer perceptron (MLP) is a class of feedforward artificial neural network. An MLP consists of at least three layers of nodes. Except for the input nodes, each node is a neuron that uses a nonlinear activation function. MLP utilizes a supervised learning technique called backpropagation for training. Its multiple layers and non-linear activation distinguish MLP from a linear perceptron. [~Wikipedia](https://en.wikipedia.org/wiki/Multilayer_perceptron)
#####
## Configuration (*Configurator* class)
*Configurator* constructor:
```java
    Configurator cfg = new Configurator(int input_count, int[] layers);
```

Layers structure (example):
```java
    int[] layers = new int[] {
	    3, // second layer (hidden) -> 3 neurons
	    4, // third layer (hidden) -> 4 neurons,
	    2, // fourth layer (output) -> 2 neurons
    }
```
Last element - output layer.

    1st layer (input) - input_count
    2nd layer (hidden) - layers[1]
    3rd layer (hidden) - layers[2]
    ...
    n layer (output) - layers[n - 1]
Options:

 - `setInput(double[])`
 - `setExpected(double[])`
 - `setRange(double, double)` default: `(-0.5, 0.5)`
 - `setLearningFactor(double)` default: `(1.0)`
 - `setMomentum(double)` default: `(0.0)`
 - `setBias(boolean)` default: `(false)`
 - `setInputRotation(boolean)` default: `(false)`
 - `setEpochs` default: `100 000`
 - `setErrorLogStep` default: `1000`
 - `setError` default: `0.05`
