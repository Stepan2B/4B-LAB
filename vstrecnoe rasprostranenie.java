import java.util.Arrays;
import java.util.Random;

public class CounterPropagationNetwork {
    private int inputSize;
    private int kohonenSize;
    private int grossbergSize;
    
    private double[][] kohonenWeights;
    private double[][] grossbergWeights;
    
    private double learningRate = 0.1;

    public CounterPropagationNetwork(int inputSize, int kohonenSize, int grossbergSize) {
        this.inputSize = inputSize;
        this.kohonenSize = kohonenSize;
        this.grossbergSize = grossbergSize;
        
        kohonenWeights = new double[kohonenSize][inputSize];
        grossbergWeights = new double[grossbergSize][kohonenSize];
        
        initializeWeights(kohonenWeights);
        initializeWeights(grossbergWeights);
    }

    private void initializeWeights(double[][] weights) {
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = rand.nextDouble() - 0.5;
            }
        }
    }

    private int forwardKohonen(double[] input) {
        double maxSimilarity = -Double.MAX_VALUE;
        int winnerIndex = -1;

        for (int i = 0; i < kohonenWeights.length; i++) {
            double similarity = 0;
            for (int j = 0; j < input.length; j++) {
                similarity += kohonenWeights[i][j] * input[j];
            }

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                winnerIndex = i;
            }
        }
        return winnerIndex;
    }

    private double[] forwardGrossberg(int kohonenIndex) {
        return grossbergWeights[kohonenIndex];
    }

    public void train(double[][] inputs, double[][] outputs, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                int kohonenWinner = forwardKohonen(inputs[i]);

                for (int j = 0; j < inputSize; j++) {
                    kohonenWeights[kohonenWinner][j] += learningRate * (inputs[i][j] - kohonenWeights[kohonenWinner][j]);
                }

                for (int j = 0; j < grossbergSize; j++) {
                    grossbergWeights[kohonenWinner][j] += learningRate * (outputs[i][j] - grossbergWeights[kohonenWinner][j]);
                }
            }
        }
    }

    public double[] predict(double[] input) {
        int kohonenWinner = forwardKohonen(input);
        return forwardGrossberg(kohonenWinner);
    }

    public static void main(String[] args) {
        int inputSize = 100;
        int kohonenSize = 10;
        int outputSize = 10;

        CounterPropagationNetwork cpn = new CounterPropagationNetwork(inputSize, kohonenSize, outputSize);

        double[][] inputs = generateFakeData();
        double[][] outputs = generateLabels();

        cpn.train(inputs, outputs, 1000);

        for (int i = 0; i < inputs.length; i++) {
            double[] prediction = cpn.predict(inputs[i]);
            System.out.println("Ожидаемый: " + Arrays.toString(outputs[i]));
            System.out.println("Предсказанный: " + Arrays.toString(prediction));
        }
    }

    public static double[][] generateFakeData() {
        double[][] data = new double[10][100];
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                data[i][j] = rand.nextDouble() > 0.5 ? 1 : 0;
            }
        }
        return data;
    }

    public static double[][] generateLabels() {
        double[][] labels = new double[10][10];

        for (int i = 0; i < 10; i++) {
            labels[i][i] = 1.0;
        }
        return labels;
    }
}
