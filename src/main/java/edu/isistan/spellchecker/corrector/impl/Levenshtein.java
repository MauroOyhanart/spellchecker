package edu.isistan.spellchecker.corrector.impl;
import java.util.Set;


import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;

/**
 *
 * Un corrector inteligente que utiliza "edit distance" para generar correcciones.
 * 
 * La distancia de Levenshtein es el numero minimo de ediciones que se deber
 * realizar a un string para igualarlo a otro. Por edicion se entiende:
 * <ul>
 * <li> insertar una letra
 * <li> borrar una letra
 * <li> cambiar una letra
 * </ul>
 *
 * Una "letra" es un caracter a-z (no contar los apostrofes).
 * Intercambiar letras (thsi -> this) <it>no</it> cuenta como una edicion.
 * <p>
 * Este corrector sugiere palabras que esten a edit distance uno.
 */
public class Levenshtein extends Corrector {
	private final Dictionary dictionary;

	/**
	 * Construye un Levenshtein Corrector usando un Dictionary.
	 * Debe arrojar <code>IllegalArgumentException</code> si el diccionario es null.
	 *
	 * @param dict el diccionario
	 */
	public Levenshtein(Dictionary dict) {
		if (dict == null) throw new IllegalArgumentException("Dictionary cannot be null");
		this.dictionary = dict;

	}

	/**
	 * @param s palabra
	 * @return todas las palabras a erase distance uno
	 */
	Set<String> getDeletions(String s) {
		if (s == null) throw new IllegalArgumentException("Word is null");
		System.out.println("dictionary contains this words:");
		dictionary.printAll();
		System.out.println("word to check is " + s);
		return matchCase(s,
				dictionary.filterBy(
						(String word1) -> LevenshteinImpl.deleteDistanceOne(s.toLowerCase(), word1.toLowerCase())
				)
		);
	}

	/**
	 * @param s palabra
	 * @return todas las palabras a substitution distance uno
	 */
	public Set<String> getSubstitutions(String s) {
		if (s == null) throw new IllegalArgumentException("Word is null");
		return matchCase(s,
				dictionary.filterBy(
					(String word1) -> LevenshteinImpl.replaceDistanceOne(s.toLowerCase(), word1.toLowerCase())
				)
		);
	}


	/**
	 * @param s palabra
	 * @return todas las palabras a insert distance uno
	 */
	public Set<String> getInsertions(String s) {
		if (s == null) throw new IllegalArgumentException("Word is null");
		return matchCase(s,
				dictionary.filterBy(
						(String word1) -> LevenshteinImpl.insertDistanceOne(s.toLowerCase(), word1.toLowerCase())
				)
		);
	}

	public Set<String> getCorrections(String wrong) {
		if (wrong == null) throw new IllegalArgumentException("Wrong is null");
		return matchCase(wrong,
				dictionary.filterBy(
						(String word1) -> LevenshteinImpl.levenshteinDistance(wrong.toLowerCase(), word1.toLowerCase())
				)
		);
	};
}
