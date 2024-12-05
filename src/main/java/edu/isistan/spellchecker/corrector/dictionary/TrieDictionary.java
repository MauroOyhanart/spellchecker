package edu.isistan.spellchecker.corrector.dictionary;

import edu.isistan.spellchecker.corrector.dictionary.trie.Trie;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

public class TrieDictionary implements IDictionary {
    private final Trie dic;

    public TrieDictionary(TokenScanner ts) {
        if (ts == null) throw new IllegalArgumentException();
        ts.setName("TokenScannerTrieDictionary");
        this.dic = new Trie();

        while (ts.hasNext()) {
            try {
                String token = ts.next();
                if (TokenScanner.isWord(token)) {
                    dic.insert(token.toLowerCase());
                }
            } catch (NoSuchElementException nse) {
                dictionaryLog("Finalizado.");
            }
        }
        dictionaryLog(dic.getNumWords() + " words in dictionary");
    }

    public static TrieDictionary make(String filename) throws IOException {
        Reader r = new FileReader(filename);
        TrieDictionary d = new TrieDictionary(new TokenScanner(r));
        r.close();
        return d;
    }

    @Override
    public int getNumWords() {
        return dic.getNumWords();
    }

    @Override
    public boolean isWord(String word) {
        return dic.search(word.toLowerCase());
    }

    @Override
    public Set<String> filterBy(Function<String, Integer> f) {
        return dic.filterBy(f);
    }

    @Override
    public void printAll() {
        dic.printTrie();
    }

    private void dictionaryLog(Object text) {
        System.out.println("TrieDictionary: " + text);
    }
}
