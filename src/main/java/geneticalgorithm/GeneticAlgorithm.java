package geneticalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgorithm {

    /***************************************************************
     * Feel free to change the private methods' signatures (I did) *
     * Only the "run" functions are tested                         *
     ***************************************************************/

    private Random r = new Random();

    private String generate(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(r.nextBoolean() ? "1" : "0");
        }
        return sb.toString();
    }

    // Returns the selected index based on the weights(probabilities)
    private int rouletteSelect(List<Double> weight) {
        // calculate the total weight
        double weight_sum = 0;
        for(int i=0; i<weight.size(); i++) {
            weight_sum += weight.get(i);
        }
        // get a random value
        double value = r.nextDouble() * weight_sum;
        // locate the random value based on the weights
        for(int i=0; i<weight.size(); i++) {
            value -= weight.get(i);
            if(value <= 0) return i;
        }
        // when rounding errors occur, we return the last item's index
        return weight.size() - 1;
    }

    private String[] select(List<String> population, List<Double> fitnesses) {
        String[] result = new String[2];
        result[0] = population.get(rouletteSelect(fitnesses));
        do {
            result[1] = population.get(rouletteSelect(fitnesses));
        } while (result[0].equals(result[1]));
        return result;
    }

    private String mutate(String chromosome, double p_m) {
        double v = r.nextDouble();
        if (v <= p_m) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chromosome.length(); i++) {
                sb.append(chromosome.charAt(i) == '0' ? '1' : '0');
            }
            return sb.toString();
        }
        return chromosome;
    }

    private String[] crossover(String chromosome1, String chromosome2, double p_c) {
        double v = r.nextDouble();
        String[] result = new String[2];
        result[0] = chromosome1;
        result[1] = chromosome2;
        if (v <= p_c) {
            int cut = r.nextInt(chromosome1.length());
            result[0] = chromosome1.substring(0, cut) + chromosome2.substring(cut);
            result[1] = chromosome2.substring(0, cut) + chromosome1.substring(cut);
        }
        return result;
    }

    public String run(ToDoubleFunction<String> fitness, int length, double p_c, double p_m) {
        return run(fitness, length, p_c, p_m, 100);
    }

    public String run(ToDoubleFunction<String> fitness, int length, double p_c, double p_m, int iterations) {
        List<String> population = Stream.generate(() -> generate(length)).limit(200).collect(Collectors.toList());
        List<Double> fitnesses = population.stream().map((x) -> fitness.applyAsDouble(x)).collect(Collectors.toList());
        for (int i = 0; i < iterations; i++) {
            List<String> newPopulation = new ArrayList<>();
            List<Double> newFitnesses = new ArrayList<>();
            while (newPopulation.size() < population.size()) {
                String[] c = select(population, fitnesses);
                c = crossover(c[0], c[1], p_c);
                c[0] = mutate(c[0], p_m);
                c[1] = mutate(c[1], p_m);
                newPopulation.add(c[0]);
                newPopulation.add(c[1]);
                newFitnesses.add(fitness.applyAsDouble(c[0]));
                newFitnesses.add(fitness.applyAsDouble(c[1]));
            }
            population = newPopulation;
            fitnesses = newFitnesses;
        }
        double max = 0;
        String better = "";
        for (String c : population) {
            double v = fitness.applyAsDouble(c);
            if (v > max) {
                max = v;
                better = c;
            }
        }
        return better;
    }
}