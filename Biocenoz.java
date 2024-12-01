import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class BiocenosisSimulation extends JPanel {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int AGENT_COUNT = 10;
    static final int PLANT_COUNT = 20;

    static Random random = new Random();

    List<Plant> plants = new ArrayList<>();
    List<Herbivore> herbivores = new ArrayList<>();
    List<Predator> predators = new ArrayList<>();

    public BiocenosisSimulation() {
        for (int i = 0; i < PLANT_COUNT; i++) {
            plants.add(new Plant(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
        }
        for (int i = 0; i < AGENT_COUNT; i++) {
            herbivores.add(new Herbivore(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
            predators.add(new Predator(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Plant plant : plants) {
            g.setColor(Color.GREEN);
            g.fillRect(plant.x, plant.y, 5, 5);
        }
        for (Herbivore herbivore : herbivores) {
            g.setColor(Color.BLUE);
            g.fillRect(herbivore.x, herbivore.y, 10, 10);
            g.drawString("Energy: " + (int) herbivore.energy, herbivore.x, herbivore.y - 5); // Энергия целым числом
        }
        for (Predator predator : predators) {
            g.setColor(Color.RED);
            g.fillRect(predator.x, predator.y, 10, 10);
            g.drawString("Energy: " + (int) predator.energy, predator.x, predator.y - 5); // Энергия целым числом
        }

        // Статистика
        g.setColor(Color.BLACK);
        g.drawString("Herbivores: " + herbivores.size(), 10, 20);
        g.drawString("Predators: " + predators.size(), 10, 35);
        g.drawString("Plants: " + plants.size(), 10, 50);
    }

    public void simulate() {
        // Движение травоядных
        for (int i = 0; i < herbivores.size(); i++) {
            Herbivore herbivore = herbivores.get(i);
            herbivore.move(plants);
            if (herbivore.energy <= 0) {
                herbivores.remove(i);
                i--; // Снижение индекса после удаления
            }
        }

        // Движение хищников
        for (int i = 0; i < predators.size(); i++) {
            Predator predator = predators.get(i);
            predator.move(herbivores);
            if (predator.energy <= 0) {
                predators.remove(i);
                i--; // Снижение индекса после удаления
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Biocenosis Simulation");
        BiocenosisSimulation simulation = new BiocenosisSimulation();
        frame.add(simulation);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer timer = new Timer(100, e -> simulation.simulate());
        timer.start();
    }
}

class Plant {
    int x, y;

    public Plant(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Perceptron {
    double[] weights;
    double bias;

    public Perceptron(int inputSize) {
        weights = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            weights[i] = Math.random() * 2 - 1;
        }
        bias = Math.random() * 2 - 1; // Случайный bias
    }

    public double activate(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < inputs.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sigmoid(sum);
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}

class Herbivore {
    int x, y;
    double energy;
    Perceptron perceptron;

    public Herbivore(int x, int y) {
        this.x = x;
        this.y = y;
        this.energy = 100.0;
        this.perceptron = new Perceptron(3);
    }

    public void move(List<Plant> plants) {
        if (energy > 0) {
            double nearestPlantDistance = findNearestPlant(plants);
            double[] inputs = {energy, nearestPlantDistance};
            double action = perceptron.activate(inputs);

            if (action > 0.5) {
                // Двигаемся к растению
                if (!plants.isEmpty()) {
                    x += (int)(Math.signum(plants.get(0).x - x) * 2);
                    y += (int)(Math.signum(plants.get(0).y - y) * 2);
                }
                energy -= 0.1; // Расход энергии

                // Проверка на поедание растения
                if (nearestPlantDistance < 10 && !plants.isEmpty()) {
                    eat(plants.get(0));
                    plants.remove(0);
                }
            }
        }
    }

    private double findNearestPlant(List<Plant> plants) {
        double nearestDistance = Double.MAX_VALUE;
        for (Plant plant : plants) {
            double distance = Math.sqrt(Math.pow(x - plant.x, 2) + Math.pow(y - plant.y, 2));
            if (distance < nearestDistance) {
                nearestDistance = distance;
            }
        }
        return nearestDistance;
    }

    public void eat(Plant plant) {
        energy += 50; // Восстановление энергии
    }
}

class Predator {
    int x, y;
    double energy;
    Perceptron perceptron;

    public Predator(int x, int y) {
        this.x = x;
        this.y = y;
        this.energy = 100.0;
        this.perceptron = new Perceptron(2); // 2 входа
    }

    public void move(List<Herbivore> herbivores) {
        if (energy > 0) {
            double nearestHerbivoreDistance = findNearestHerbivore(herbivores);
            double[] inputs = {energy, nearestHerbivoreDistance};
            double action = perceptron.activate(inputs);

            if (action > 0.5) {
                // Двигаемся к травоядному
                if (!herbivores.isEmpty()) {
                    x += (int)(Math.signum(herbivores.get(0).x - x) * 2);
                    y += (int)(Math.signum(herbivores.get(0).y - y) * 2);
                }
                energy -= 0.2; // Расход энергии

                // Проверка на поедание травоядного
                if (nearestHerbivoreDistance < 10 && !herbivores.isEmpty()) {
                    eat(herbivores.get(0));
                    herbivores.remove(0);
                }
            }
        }
    }

    private double findNearestHerbivore(List<Herbivore> herbivores) {
        double nearestDistance = Double.MAX_VALUE;
        for (Herbivore herbivore : herbivores) {
            double distance = Math.sqrt(Math.pow(x - herbivore.x, 2) + Math.pow(y - herbivore.y, 2));
            if (distance < nearestDistance) {
                nearestDistance = distance;
            }
        }
        return nearestDistance;
    }

    public void eat(Herbivore herbivore) {
        energy += 70; // Восстановление энергии
    }
}
