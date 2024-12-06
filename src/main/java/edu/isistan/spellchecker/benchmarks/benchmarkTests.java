package edu.isistan.spellchecker.benchmarks;

import edu.isistan.spellchecker.SpellCheckerRunner;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class benchmarkTests {
    @State(Scope.Thread)
    public static class BenchmarkState {
        /**
         * Usamos FileCorrector y un diccionario grande
         * /usr/local/tallerjava/proyectofinal/Gettysburg.txt
         * /usr/local/tallerjava/proyectofinal/output.txt
         * /usr/local/tallerjava/proyectofinal/dictionary.txt
         * /usr/local/tallerjava/proyectofinal/misspellings.txt
         */
        public String[] args = {"/usr/local/tallerjava/proyectofinal/Gettysburg.txt",
                "/usr/local/tallerjava/proyectofinal/output.txt",
                "/usr/local/tallerjava/proyectofinal/dictionary.txt",
                "/usr/local/tallerjava/proyectofinal/misspellings.txt"
        };
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 50)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1) //nueva instancia de la jvm
    public void benchmarkSpellChecker(BenchmarkState state) {
        SpellCheckerRunner.start(state.args, true);
    }

}
