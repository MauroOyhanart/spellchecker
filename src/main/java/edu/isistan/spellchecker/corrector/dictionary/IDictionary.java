package edu.isistan.spellchecker.corrector.dictionary;

import java.io.IOException;
import java.util.Set;
import java.util.function.Function;

/**
 * Abstraccion para poder implementar el Dictionary y el TrieDictionary por separado.
 */
public interface IDictionary {

    int getNumWords();
    boolean isWord(String word);

    Set<String> filterBy(Function<String, Integer> f);

    void printAll();
}
