import java.util.Arrays;
import java.util.Random;

public class GeneticMLP {

    static final Random random = new Random();

    static double[][] X = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    static double[] y = {0, 1, 1, 0};

    static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    static double forward(double[] weights, double[] inputs) {
        int hiddenSize = 2;

        double[] hiddenLayer = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenLayer[i] = sigmoid(inputs[0] * weights[i] + inputs[1] * weights[i + hiddenSize] + weights[2 * hiddenSize + i]);
        }

        double output = sigmoid(hiddenLayer[0] * weights[2 * hiddenSize + hiddenSize] + hiddenLayer[1] * weights[2 * hiddenSize + hiddenSize + 1] + weights[2 * hiddenSize + hiddenSize + 2]);

        return output;
    }

    static double fitness(double[] weights) {
        double totalError = 0.0;
        for (int i = 0; i < X.length; i++) {
            double predicted = forward(weights, X[i]);
            totalError += Math.pow(y[i] - predicted, 2);
        }
        return -totalError;
    }

    static double[][] initializePopulation(int size, int nWeights) {
        double[][] population = new double[size][nWeights];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < nWeights; j++) {
                population[i][j] = random.nextDouble() * 2 - 1;
            }
        }
        return population;
    }

    static double[] select(double[][] population, double[] fitnesses) {
        double totalFitness = Arrays.stream(fitnesses).sum();
        double rand = random.nextDouble() * totalFitness;
        double cumulative = 0.0;
        for (int i = 0; i < population.length; i++) {
            cumulative += fitnesses[i];
            if (cumulative >= rand) {
                return population[i];
            }
        }
        return population[population.length - 1];
    }

    static double[] crossover(double[] parent1, double[] parent2) {
        double[] child = new double[parent1.length];
        int crossoverPoint = random.nextInt(parent1.length);
        for (int i = 0; i < parent1.length; i++) {
            child[i] = i < crossoverPoint ? parent1[i] : parent2[i];
        }
        return child;
    }

    static void mutate(double[] individual, double mutationRate) {
        for (int i = 0; i < individual.length; i++) {
            if (random.nextDouble() < mutationRate) {
                individual[i] += random.nextGaussian() * 0.1;
            }
        }
    }

    static double[] geneticAlgorithm(int generations, int populationSize, double mutationRate) {
        int nWeights = 9;
        double[][] population = initializePopulation(populationSize, nWeights);

        for (int gen = 0; gen < generations; gen++) {
            double[] fitnesses = new double[populationSize];
            for (int i = 0; i < populationSize; i++) {
                fitnesses[i] = fitness(population[i]);
            }

            double[][] newPopulation = new double[populationSize][nWeights];
            for (int i = 0; i < populationSize; i++) {
                double[] parent1 = select(population, fitnesses);
                double[] parent2 = select(population, fitnesses);
                double[] child = crossover(parent1, parent2);
                mutate(child, mutationRate);
                newPopulation[i] = child;
            }
            population = newPopulation;
        }

        double[] fitnesses = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            fitnesses[i] = fitness(population[i]);
        }
        return population[argMax(fitnesses)];
    }

    static int argMax(double[] array) {
        int bestIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[bestIndex]) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    public static void main(String[] args) {
        int generations = 1000;
        int populationSize = 20;
        double mutationRate = 0.1;

        double[] bestWeights = geneticAlgorithm(generations, populationSize, mutationRate);

        System.out.println("Лучшие веса: " + Arrays.toString(bestWeights));

        for (double[] input : X) {
            double output = forward(bestWeights, input);
            System.out.printf("Вход: %s, Предсказание: %.5f%n", Arrays.toString(input), output);
        }
    }
}
