package edu.isistan.spellchecker.corrector.dictionary.trie;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Implementacion de un Trie (arbol de reTRIEval o recuperacion)
 * Decision: Un simbolo por nodo.
 */
public class Trie {
    private final TrieNode root;

    private int count;

    public Trie() {
        this.root = new TrieNode();
        this.count = 0;
    }

    /**
     *
     * @param word to be inserted in the trie
     * @return true if the word did not exist before, false otherwise
     */
    public boolean insert(String word) {
        TrieNode current = root;

        for (char l: word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }

        if (current.isWord()) {
            return false;
        } else {
            current.setEndOfWord(true);
            count++;
            return true;
        }
    }

    /**
     *
     * @param word to search
     * @return true if the word exists in the trie
     */
    public boolean search(String word) {
        char text[] = word.toCharArray();
        int length = text.length;

        AtomicReference<TrieNode> temp = new AtomicReference<>(root);

        //DFS para la palabra
        for (int i = 0; i < length; i++) {
            char c = text[i];
            Optional<Map.Entry<Character, TrieNode>> opt = temp.get().getChildren().entrySet().stream().filter(entry -> {
                char ca = entry.getKey();
                return ca == c; //basicamente, buscamos el hijo de temp que tenga el character i
            }).findFirst();

            opt.ifPresent(entry -> { //if this char matched, get the following node
                TrieNode node = entry.getValue();
                temp.set(node); //update the temp for the following iteration
            });
            if (!opt.isPresent()) {
                return false;
            }
        }
        return temp.get().isWord();
    }

    public boolean isEmpty() {
        return (root == null); //?
    }

    public void printTrie() {
        if (root == null) {
            System.out.println("Trie is empty");
            return;
        }
        printTrieRecursive(root, null, 0);
    }

    private int printTrieRecursive(TrieNode current, char[] prefix, int length) {
        char newPrefix[] = new char[length+2];
        if (prefix != null) {
            System.arraycopy(prefix, 0, newPrefix, 0, length);
        } else {
            for (int i = 0; i < length+2; i++) {
                newPrefix[i] = ' ';
            }
        }
        newPrefix[length+1] = 0;

        if (current.isWord()) {
            //TODO for printing (and probably should consider in search), consider from 0 to prefix.length-1.
            System.out.println("Word: [" + String.valueOf(prefix).substring(0, prefix.length-1) +"]");
        }

        Set<Map.Entry<Character, TrieNode>> entries = current.getChildren().entrySet();

        for (Map.Entry<Character, TrieNode> entry: entries) {
            if (entry != null) {
                Character c = entry.getKey();
                TrieNode node = entry.getValue();

                newPrefix[length] = c;
                printTrieRecursive(node, newPrefix, length+1);
            }
        }
        return 0;
    }

    public int getNumWords() {
        return count;
    }

    /**
     * Necesito dar soporte al filterBy en el trie. Si no lo hago, no me sirve tenerlo, porque tendria que devolver un Set
     * con todas las palabras, y eso arruina el proposito del trie, que justamente es guardar data de manera eficiente.
     * @return todas las palabras que si las aplicamos a f, el resultado de f es 1.
     */
    public Set<String> filterBy(Function<String, Integer> f) {
        //DFS aplicando filterBy f==1
        Set<String> words = new HashSet<>();
        return filterByRecursive(words, f, root, null, 0);
    }

    private Set<String> filterByRecursive(Set<String> words, Function<String, Integer> f, TrieNode currentNode, char[] currentWord, int length) {
        char newPrefix[] = new char[length+2];
        if (currentWord != null) {
            System.arraycopy(currentWord, 0, newPrefix, 0, length);
        } else {
            for (int i = 0; i < length+2; i++) {
                newPrefix[i] = ' ';
            }
        }
        newPrefix[length+1] = 0;

        if (currentNode.isWord()) {
            //TODO for printing (and probably should consider in search), consider from 0 to prefix.length-1.
            String word = String.valueOf(currentWord).substring(0, currentWord.length-1);
            if (f.apply(word) == 1)
                words.add(word);
        }

        Set<Map.Entry<Character, TrieNode>> entries = currentNode.getChildren().entrySet();

        for (Map.Entry<Character, TrieNode> entry: entries) {
            if (entry != null) {
                Character c = entry.getKey();
                TrieNode node = entry.getValue();

                newPrefix[length] = c;
                words.addAll(filterByRecursive(words, f, node, newPrefix, length+1));
            }
        }
        return words;
    }
}
