package edu.isistan.spellchecker.corrector.dictionary.trie;

import java.util.HashMap;

public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private String content;
    private boolean terminal;

    public TrieNode() {
        this.children = new HashMap<>();
        this.content = "";
        this.terminal = false;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public String getContent() {
        return content;
    }

    public boolean isWord() {
        return terminal;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEndOfWord(boolean isEndOfWord) {
        terminal = isEndOfWord;
    }
}
