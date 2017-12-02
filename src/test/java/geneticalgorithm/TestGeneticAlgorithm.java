package geneticalgorithm;

import org.junit.Test;

import java.util.Random;
import java.util.function.ToDoubleFunction;

import static org.junit.Assert.assertEquals;

public class TestGeneticAlgorithm {

    @Test
    public void testRun() {
        String r = "10111111011101101101110110001100011";

        ToDoubleFunction<String> fitness = new ToDoubleFunction<String>() {
            @Override
            public double applyAsDouble(String value) {

                return 0;
            }
        };
        new GeneticAlgorithm().run(fitness, 35, 0.6, 0.4);
    }
}
