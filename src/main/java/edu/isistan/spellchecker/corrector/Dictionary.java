package edu.isistan.spellchecker.corrector;

import java.io.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * El diccionario maneja todas las palabras conocidas.
 * El diccionario es case insensitive 
 * 
 * Una palabra "valida" es una secuencia de letras (determinado por Character.isLetter) 
 * o apostrofes.
 */
public class Dictionary {
	private final Set<String> dic;

	/**
	 * Construye un diccionario usando un TokenScanner
	 * <p>
	 * Una palabra valida es una secuencia de letras (ver Character.isLetter) o apostrofes.
	 * Toda palabra no valida se debe ignorar.
	 *
	 * <p>
	 * Cierra el tokenScanner que se le da cuando lo termina de usar.
	 *
	 * @param ts 
	 * @throws IOException Error leyendo el archivo
	 * @throws IllegalArgumentException el TokenScanner es null
	 */
	public Dictionary(TokenScanner ts) throws IOException {
		if (ts == null) throw new IllegalArgumentException();
		ts.setName("TokenScannerDictionary");
		this.dic = new HashSet<>();

		while (ts.hasNext()) {
			try {
				String token = ts.next();
				if (TokenScanner.isWord(token)) {
					dic.add(token.toLowerCase());
					//dictionaryLog("Added [" + token + "]");
				}
				else {
					//dictionaryLog("Not a word: [" + token + "]");
				}
			} catch (NoSuchElementException nse) {
				dictionaryLog("Finalizado.");
			}
		}
		dictionaryLog(dic.size() + " words in dictionary");
	}

	/**
	 * Construye un diccionario usando un archivo.
	 *
	 *
	 * @param filename 
	 * @throws FileNotFoundException si el archivo no existe
	 * @throws IOException Error leyendo el archivo
	 */
	public static Dictionary make(String filename) throws IOException {
		Reader r = new FileReader(filename);
		Dictionary d = new Dictionary(new TokenScanner(r));
		r.close();
		return d;
	}

	/**
	 * Retorna el numero de palabras correctas en el diccionario.
	 * Recuerde que como es case insensitive si Dogs y doGs estan en el 
	 * diccionario, cuentan como una sola palabra.
	 * 
	 * @return numero de palabras unicas
	 */
	public int getNumWords() {
		return -1;
	}

	/**
	 * Testea si una palabra es parte del diccionario. Si la palabra no esta en
	 * el diccionario debe retornar false. null debe retornar falso.
	 * Si en el diccionario esta la palabra Dog y se pregunta por la palabra dog
	 * debe retornar true, ya que es case insensitive.
	 *
	 *Llamar a este metodo no debe reabrir el archivo de palabras.
	 *
	 * @param word verifica si la palabra esta en el diccionario. 
	 * Asuma que todos los espacios en blanco antes y despues de la palabra fueron removidos.
	 * @return si la palabra esta en el diccionario.
	 */
	public boolean isWord(String word) {
		if (word == null) return false;
		return dic.contains(word.toLowerCase());
	}

	private void dictionaryLog(String text) {
		System.out.println("Dictionary: " + text);
	}

	public Set<String> filterBy(Function<String, Integer> f) {
		Set<String> wordsThatMatchF = new HashSet<>();
		dictionaryLog("f is " + f.toString());
		wordsThatMatchF = this.dic.stream().filter(t -> f.apply(t) == 1).collect(Collectors.toSet());
		dictionaryLog("words returned: ");
		wordsThatMatchF.forEach(System.out::println);
		return wordsThatMatchF;
	}

	public void printAll() {
		this.dic.stream().sorted().forEach(System.out::println);
	}
}