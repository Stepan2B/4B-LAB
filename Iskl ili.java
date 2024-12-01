import java.util.Arrays;
import java.util.Random;

public class Perceptron3 {
  
    private double[][] hiddenWeights; 
    private double[] outputWeights;   
    private double[] hiddenBias;   
    private double outputBias;        

    private double learningRate = 0.1;

    public Perceptron3(int inputSize, int hiddenSize) {
        Random rand = new Random();
        

        hiddenWeights = new double[hiddenSize][inputSize];
        hiddenBias = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenBias[i] = rand.nextDouble() - 0.5;
            for (int j = 0; j < inputSize; j++) {
                hiddenWeights[i][j] = rand.nextDouble() - 0.5;
            }
        }


        outputWeights = new double[hiddenSize];
        outputBias = rand.nextDouble() - 0.5;
        for (int i = 0; i < hiddenSize; i++) {
            outputWeights[i] = rand.nextDouble() - 0.5;
        }
    }


    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

  
    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }


    public double forward(double[] inputs) {

        double[] hiddenLayerOutputs = new double[hiddenWeights.length];
        for (int i = 0; i < hiddenWeights.length; i++) {
            double sum = hiddenBias[i];
            for (int j = 0; j < inputs.length; j++) {
                sum += inputs[j] * hiddenWeights[i][j];
            }
            hiddenLayerOutputs[i] = sigmoid(sum);
        }

        double outputSum = outputBias;
        for (int i = 0; i < hiddenLayerOutputs.length; i++) {
            outputSum += hiddenLayerOutputs[i] * outputWeights[i];
        }
        return sigmoid(outputSum);
    }


    public void train(double[] inputs, double expectedOutput) {

        double[] hiddenLayerOutputs = new double[hiddenWeights.length];
        for (int i = 0; i < hiddenWeights.length; i++) {
            double sum = hiddenBias[i];
            for (int j = 0; j < inputs.length; j++) {
                sum += inputs[j] * hiddenWeights[i][j];
            }
            hiddenLayerOutputs[i] = sigmoid(sum);
        }

        double outputSum = outputBias;
        for (int i = 0; i < hiddenLayerOutputs.length; i++) {
            outputSum += hiddenLayerOutputs[i] * outputWeights[i];
        }
        double output = sigmoid(outputSum);

   
        double outputError = expectedOutput - output;


        double outputDelta = outputError * sigmoidDerivative(output);

        double[] hiddenDeltas = new double[hiddenWeights.length];
        for (int i = 0; i < hiddenWeights.length; i++) {
            hiddenDeltas[i] = outputDelta * outputWeights[i] * sigmoidDerivative(hiddenLayerOutputs[i]);
        }


        for (int i = 0; i < hiddenWeights.length; i++) {
            outputWeights[i] += learningRate * outputDelta * hiddenLayerOutputs[i];
        }
        outputBias += learningRate * outputDelta;

        for (int i = 0; i < hiddenWeights.length; i++) {
            for (int j = 0; j < inputs.length; j++) {
                hiddenWeights[i][j] += learningRate * hiddenDeltas[i] * inputs[j];
            }
            hiddenBias[i] += learningRate * hiddenDeltas[i];
        }
    }

    public static void main(String[] args) {

        Perceptron3 mlp = new Perceptron3(2, 2);


        double[][] inputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };
        double[] outputs = {0, 1, 1, 0};


        for (int epoch = 0; epoch < 10000; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                mlp.train(inputs[i], outputs[i]);
            }
        }


        System.out.println("Результаты после обучения:");
        for (int i = 0; i < inputs.length; i++) {
            double prediction = mlp.forward(inputs[i]);
            System.out.printf("Вход: %s, Ожидаемый результат: %.1f, Предсказание: %.4f%n", 
                Arrays.toString(inputs[i]), outputs[i], prediction);
        }
    }
}
