import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.01;
    private static final double CROSSOVER_RATE = 0.7;
    private static final double LOWER_BOUND = -10;
    private static final double UPPER_BOUND = 10;

    private static Random rand = new Random();

    public static void main(String[] args) {
        double[][] population = new double[POPULATION_SIZE][2];

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i][0] = randDouble(LOWER_BOUND, UPPER_BOUND); // x
            population[i][1] = randDouble(LOWER_BOUND, UPPER_BOUND); // y
        }

        System.out.println("Метод отбора элит:");
        runGeneticAlgorithm(population, "elitism");

        System.out.println("\nМетод отбора рулетки:");
        runGeneticAlgorithm(population, "roulette");
    }

    public static void runGeneticAlgorithm(double[][] population, String selectionMethod) {
        double bestFitness = -Double.MAX_VALUE;
        double[] bestIndividual = new double[2];

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            double[][] newPopulation = new double[POPULATION_SIZE][2];

            if (selectionMethod.equals("elitism")) {
                newPopulation = selectByElitism(population);
            } else if (selectionMethod.equals("roulette")) {
                newPopulation = selectByRoulette(population);
            }

            for (int i = 0; i < POPULATION_SIZE; i += 2) {
                if (rand.nextDouble() < CROSSOVER_RATE) {
                    double[] parent1 = newPopulation[i];
                    double[] parent2 = newPopulation[i + 1];
                    double[] offspring1 = new double[2];
                    double[] offspring2 = new double[2];

                    offspring1[0] = parent1[0];
                    offspring1[1] = parent2[1];
                    offspring2[0] = parent2[0];
                    offspring2[1] = parent1[1];

                    newPopulation[i] = offspring1;
                    newPopulation[i + 1] = offspring2;
                }

                mutate(newPopulation[i]);
                mutate(newPopulation[i + 1]);
            }

            for (int i = 0; i < POPULATION_SIZE; i++) {
                double fitness = fitnessFunction(newPopulation[i]);
                if (fitness > bestFitness) {
                    bestFitness = fitness;
                    bestIndividual = Arrays.copyOf(newPopulation[i], 2);
                }
            }

            population = newPopulation;

            if (generation % 100 == 0) {
                System.out.printf("Поколение %d: f(x, y) = %.5f, x = %.5f, y = %.5f\n",
                        generation, bestFitness, bestIndividual[0], bestIndividual[1]);
            }
        }

        System.out.printf("Лучшее решение: f(x, y) = %.5f, x = %.5f, y = %.5f\n",
                bestFitness, bestIndividual[0], bestIndividual[1]);
    }

    public static double fitnessFunction(double[] individual) {
        double x = individual[0];
        double y = individual[1];
        return 1 / (1 + x * x + y * y);
    }

    public static void mutate(double[] individual) {
        if (rand.nextDouble() < MUTATION_RATE) {
            individual[0] += randDouble(-0.1, 0.1);
        }
        if (rand.nextDouble() < MUTATION_RATE) {
            individual[1] += randDouble(-0.1, 0.1);
        }
    }

    public static double[][] selectByElitism(double[][] population) {
        double[][] newPopulation = new double[POPULATION_SIZE][2];
        double[][] sortedPopulation = Arrays.copyOf(population, POPULATION_SIZE);

        Arrays.sort(sortedPopulation, (a, b) -> Double.compare(fitnessFunction(b), fitnessFunction(a)));

        for (int i = 0; i < POPULATION_SIZE; i++) {
            newPopulation[i] = Arrays.copyOf(sortedPopulation[i], 2);
        }

        return newPopulation;
    }

    public static double[][] selectByRoulette(double[][] population) {
        double[][] newPopulation = new double[POPULATION_SIZE][2];
        double[] fitnesses = new double[POPULATION_SIZE];
        double totalFitness = 0;

        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitnesses[i] = fitnessFunction(population[i]);
            totalFitness += fitnesses[i];
        }

        for (int i = 0; i < POPULATION_SIZE; i++) {
            double randValue = randDouble(0, totalFitness);
            double partialSum = 0;
            for (int j = 0; j < POPULATION_SIZE; j++) {
                partialSum += fitnesses[j];
                if (partialSum >= randValue) {
                    newPopulation[i] = Arrays.copyOf(population[j], 2);
                    break;
                }
            }
        }

        return newPopulation;
    }

    public static double randDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }
}
