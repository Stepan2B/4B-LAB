import java.util.Random;

public class Cognitron {
    private int inputSize;
    private int receptiveFieldSize;
    private int numLayers;
    private Layer[] layers;

    public Cognitron(int inputSize, int receptiveFieldSize, int numLayers, int[] layerSizes) {
        this.inputSize = inputSize;
        this.receptiveFieldSize = receptiveFieldSize;
        this.numLayers = numLayers;
        this.layers = new Layer[numLayers];

        for (int i = 0; i < numLayers; i++) {
            layers[i] = new Layer(layerSizes[i], receptiveFieldSize);
        }
    }

    public void train(double[][] inputPatterns, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (double[] pattern : inputPatterns) {
                forward(pattern);
                for (int layer = 0; layer < numLayers; layer++) {
                    layers[layer].updateWeights();
                }
            }
        }
    }

    public double[] forward(double[] input) {
        double[] output = input;

        for (int i = 0; i < numLayers; i++) {
            output = layers[i].process(output);
        }

        return output;
    }

    public static void main(String[] args) {
        int inputSize = 100;
        int receptiveFieldSize = 3;
        int numLayers = 3;
        int[] layerSizes = {64, 32, 16};

        Cognitron cognitron = new Cognitron(inputSize, receptiveFieldSize, numLayers, layerSizes);

        double[][] inputPatterns = generateRandomPatterns(10, inputSize);
        cognitron.train(inputPatterns, 1000);

        double[] testPattern = generateRandomPattern(inputSize);
        double[] output = cognitron.forward(testPattern);

        System.out.println("Выходные данные сети: ");
        for (double val : output) {
            System.out.printf("%.2f ", val);
        }
    }

    public static double[][] generateRandomPatterns(int numPatterns, int inputSize) {
        double[][] patterns = new double[numPatterns][inputSize];
        Random rand = new Random();

        for (int i = 0; i < numPatterns; i++) {
            for (int j = 0; j < inputSize; j++) {
                patterns[i][j] = rand.nextDouble();
            }
        }

        return patterns;
    }

    public static double[] generateRandomPattern(int inputSize) {
        double[] pattern = new double[inputSize];
        Random rand = new Random();

        for (int i = 0; i < inputSize; i++) {
            pattern[i] = rand.nextDouble();
        }

        return pattern;
    }
}

class Layer {
    private int numNeurons;
    private double[][] weights;
    private double[] output;
    private int receptiveFieldSize;

    public Layer(int numNeurons, int receptiveFieldSize) {
        this.numNeurons = numNeurons;
        this.receptiveFieldSize = receptiveFieldSize;
        this.weights = new double[numNeurons][receptiveFieldSize];
        this.output = new double[numNeurons];
        initializeWeights();
    }

    public void initializeWeights() {
        Random rand = new Random();
        for (int i = 0; i < numNeurons; i++) {
            for (int j = 0; j < receptiveFieldSize; j++) {
                weights[i][j] = rand.nextDouble() - 0.5;
            }
        }
    }

    public double[] process(double[] input) {
        for (int i = 0; i < numNeurons; i++) {
            output[i] = 0;
            for (int j = 0; j < receptiveFieldSize; j++) {
                output[i] += weights[i][j] * input[j];
            }
            output[i] = activate(output[i]);
        }

        return output;
    }

    public void updateWeights() {

    }

    private double activate(double x) {
        return Math.max(0, x); 
    }
}
