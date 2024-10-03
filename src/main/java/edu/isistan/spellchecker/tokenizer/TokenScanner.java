package edu.isistan.spellchecker.tokenizer;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Dado un archivo provee un metodo para recorrerlo.
 */
public class TokenScanner implements Iterator<String>, AutoCloseable {
    private final Scanner sc;
    private String name;
    /**
     * Crea un TokenScanner.
     * <p>
     * Como un iterador, el TokenScanner solo debe leer lo justo y
     * necesario para implementar los metodos next() y hasNext().
     * No se debe leer toda la entrada de una.
     * <p>
     *
     * @param in fuente de entrada
     * @throws IOException              si hay algun error leyendo.
     * @throws IllegalArgumentException si el Reader provisto es null
     */
    public TokenScanner(java.io.Reader in) throws IOException {
        if (in == null) throw new IllegalArgumentException("Reader provisto es null.");
        this.sc = new Scanner(in);
        sc.useDelimiter("(?<=\\s)|(?=\\s)|(?<=\\n)|(?=[.,;!?$])|(?<=[.,;!?$])");
        this.name = "TokenScanner";
    }

    /**
     * Determina si un caracter es una caracter valido para una palabra.
     * <p>
     * Un caracter valido es una letra (
     * Character.isLetter) o una apostrofe '\''.
     *
     * @param c
     * @return true si es un caracter
     */
    public static boolean isWordCharacter(int c) {
        return (Character.isLetter(c) || c == '\'');
    }

    /**
     * Determina si un string es una palabra valida.
     * Null no es una palabra valida.
     * Un string que todos sus caracteres son validos es una
     * palabra. Por lo tanto, el string vacio es una palabra valida.
     * 
     * @param s
     * @return true si el string es una palabra.
     */
    public static boolean isWord(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++){
            if (!isWordCharacter(s.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Determina si hay otro token en el reader.
     */
    public boolean hasNext() {
        return sc.hasNext();
    }

    /**
     * Retorna el siguiente token.
     *
     * @throws NoSuchElementException cuando se alcanzo el final de stream
     */
    public String next() throws NoSuchElementException {
        String token = sc.next();
        //tokenScannerLog("returning " + token);
        return token;
    }

    public void close() {
        this.sc.close();
    }

    //Utils
    private void tokenScannerLog(Object text) {
        System.out.println(getName() + ": " + text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
