package edu.isistan.spellchecker.corrector.impl;

import java.util.Arrays;

/**
 * Contiene una implementacion recursiva de Levenshtein, e implementaciones para los metodos de delete, insert y replace a distancia uno.
 */
public class LevenshteinImpl {

    /**
     * Implementacion recursiva de la distancia de Levenshtein. O(n**3).
     * @param str1 str a modificar
     * @param str2 target
     * @return la distancia de Levenshtein
     */
    public static int levenshteinDistance(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        int replace = levenshteinDistance(str1.substring(1), str2.substring(1))
                + numOfReplacement(str1.charAt(0), str2.charAt(0));

        int insert = levenshteinDistance(str1, str2.substring(1)) + 1;
        int delete = levenshteinDistance(str1.substring(1), str2) + 1;

        return minEdits(replace, insert, delete);
    }

    public static int insertDistanceOne(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        System.out.println("str1 es " + str1);
        if (str1.length() + 1 != str2.length()) return 0; //solo podemos insertar uno

        String newStr1 = str1;
        for (int i = 0; i < newStr1.length(); i++) {
            char c = str1.charAt(i);
            char c2 = str2.charAt(i);
            if (c != c2) { //aca podemos insertar, comparar y terminar la ejecucion
                newStr1 = newStr1.substring(0, i) + c2 + newStr1.substring(i);
                if (newStr1.equals(str2)) return 1;
                else return 0;
            }
        }

        //Una ultima posiblidad: el ultimo caracter
        String possible = str1 + str2.charAt(str2.length()-1);
        if (possible.equals(str2)) return 1;

        return 0;
    }

    /**
     * Retorna 1 si es posible, eliminando un solo caracter, llevar str1 a str2. Si no, retorna 0.
     * @param str1
     * @param str2
     * @return
     */
    public static int deleteDistanceOne(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        if (str1.equals(str2)) return 0;

        for (int i = 0; i < str1.length(); i++) { //Lo implemente a mano
            String newStr = str1.substring(0, i) + str1.substring(i+1);
            if (newStr.equals(str2)) return 1;
        }

        return 0;
    }

    /**
     * Retorna 1 si es posible, reemplazando un solo caracter, llevar str1 a str2. Si no, retorna 0.
     * @param str1 el que
     * @param str2
     * @return
     */
    public static int replaceDistanceOne(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        if (str1.equals(str2)) return 0;
        if (str1.length() != str2.length()) return 0; //no va a ser posible solo reemplazando
        String newStr1 = str1;
        for (int i = 0; i < newStr1.length(); i++) {
            char c = newStr1.charAt(i);
            char c2 = str2.charAt(i);
            if (c != c2) {
                //needs replacement. aca podemos reemplazar, chequear y cortar la ejecucion.
                System.out.println("newStr1 before: " + newStr1);
                newStr1 = newStr1.substring(0, i) + c2 + newStr1.substring(i+1);
                System.out.println("newStr1 after: " + newStr1);
                if (newStr1.equals(str2)) return 1;
                else return 0;
            }
        }

        return 0;
    }

    static int numOfReplacement(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    static int minEdits(int... nums) {
        return Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        String s1 = "teh";
        String s2 = "te"; // eh th te
        /*Arrays.stream(new String[]{"heh", "meh", "tah", "tea", "tee", "ten", "tex"}).forEach(
                word -> System.out.println("substitution dist entre \"teh\" y " + word + " es " + replaceDistance("teh" , word))
        );*/
        /**
         * assertEquals("teh -> {heh, meh, tah, tea, tee, ten, tex}",
         * 				makeSet(new String[]{"heh", "meh", "tah", "tea", "tee", "ten", "tex"}),
         * 				corr.getSubstitutions("teh"));
         */

        System.out.println("dist -> " + deleteDistanceOne(s1, s2)); // 1
    }
}
