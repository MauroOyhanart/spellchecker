package edu.isistan.spellchecker.corrector.impl;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
/**
 * Este corrector sugiere correciones cuando dos letras adyacentes han sido cambiadas.
 * <p>
 * Un error comun es cambiar las letras de orden, e.g.
 * "with" -> "wiht". Este corrector intenta dectectar palabras con exactamente un swap.
 * <p>
 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
 * tanto "heat" como "hate".
 * <p>
 * Solo cambio de letras contiguas se considera como swap.
 */
public class SwapCorrector extends Corrector {
	private final Dictionary dictionary;
	/**
	 * Construye el SwapCorrector usando un Dictionary.
	 *
	 * @param dict 
	 * @throws IllegalArgumentException si el diccionario provisto es null
	 */
	public SwapCorrector(Dictionary dict) {
		if (dict == null) throw new IllegalArgumentException("Dict is null");
		this.dictionary = dict;
	}

	/**
	 * 
	 * Este corrector sugiere correciones cuando dos letras adyacentes han sido cambiadas.
	 * <p>
	 * Un error comun es cambiar las letras de orden, e.g.
	 * "with" -> "wiht". Este corrector intenta dectectar palabras con exactamente un swap.
	 * <p>
	 * Por ejemplo, si la palabra mal escrita es "haet", se debe sugerir
	 * tanto "heat" como "hate".
	 * <p>
	 * Solo cambio de letras contiguas se considera como swap.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong 
	 * @return retorna un conjunto (potencialmente vacio) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra valida 
	 */
	public Set<String> getCorrections(String wrong) {
		if (wrong == null) throw new IllegalArgumentException("Wrong word is null");
		return matchCase(wrong,
			dictionary.filterBy(
				(String dictWord) -> {
					String wrongWord = wrong.toLowerCase();
					//Intento ver si, cuando sea necesario, puedo hacer que sean iguales haciendo un solo swap
					//En dicho caso, retorno 1. Si no, retorno 0.
					if (wrongWord.length() != dictWord.length()) return 0;

					for (int i = 0; i < wrongWord.length(); i++) {
						char c1 = wrongWord.charAt(i);
						char c2 = dictWord.charAt(i);
						if (c1 != c2) {
							//Swapeo hacia adelante porque hacia atras son iguales (por como estoy iterando)
							//wrong: wiht       length = 4, i = 2
							//dictWord: with
							if (i == wrongWord.length()-1) //ultima posicion, no podemos avanzar
								return 0;

							String possible = "";
							if (i < wrongWord.length()-2) //anteultima posicion
								 possible = wrongWord.substring(0, i) + wrongWord.charAt(i+1) + wrongWord.charAt(i) + wrongWord.substring(i+2);
							else possible = wrongWord.substring(0, i) + wrongWord.charAt(i+1) + wrongWord.charAt(i);

							if (possible.equals(dictWord)) return 1;
							else return 0;
						}
					}
					return 0;
				}
			)
		);
	}
}
