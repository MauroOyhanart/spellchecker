package edu.isistan.spellchecker.corrector.impl;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

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

    public static int insertDistance(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        int insert = insertDistance(str1, str2.substring(1)) + 1;

        return insert;
    }

    public static int deleteDistance(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        int delete = deleteDistance(str1.substring(1), str2) + 1;

        return delete;
    }

    public static int replaceDistance(String str1, String str2) {
        if (str1.isEmpty()) return str2.length();
        if (str2.isEmpty()) return str1.length();

        int replace = replaceDistance(str1.substring(1), str2.substring(1))
                + numOfReplacement(str1.charAt(0), str2.charAt(0));

        return replace;
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

        System.out.println("dist -> " + deleteDistance(s1, s2)); // 1
    }
}
