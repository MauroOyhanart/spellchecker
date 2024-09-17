package edu.isistan.spellchecker.corrector;
import java.util.TreeSet;
import java.util.Set;

/**
 * Esqueleto de las clases Corrector.
 * <p>
 * El metodo matchCase es compartido por todas las classes Correctors.
 * <p>
 * Implementaciones concretas deben implementar el metodo getCorrections
 * que en general invocara al metodo matchCase.
 */
public abstract class Corrector {

	/**
	 * Retorna un conjunto con los mismos elementos del set de correcciones
	 * salvo que o todas las letras son lower-case o la primera es upper-case y
	 * el resto lower-case.
	 * 
	 * @param incorrectWord palabra a corregir
	 * @param corrections correciones sugeridas
	 * @return correcciones sugeridas con la correcta capitalizacion
	 */
	public Set<String> matchCase(String incorrectWord, Set<String> corrections) {
		if (incorrectWord == null || corrections == null) {
			throw new IllegalArgumentException("null input given");
		}
		Set<String> revisedSet = new TreeSet<String>();
		boolean capitalizeFirst = Character.isUpperCase(incorrectWord.charAt(0));
		for (String s : corrections) {
			if (capitalizeFirst) {
				String ucfirst =
						s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
				revisedSet.add(ucfirst);
			} else {
				revisedSet.add(s.toLowerCase());
			}
		}
		return revisedSet;
	}

	/**
	 * Retoran una lista de sugerencias para una palabra mal escrita.
	 * <p>
	 * Si la entrada no es una palabra valida (por ejemplo es un numero)
	 * debe arrojar IllegalArgumentException. 
	 *
	 * @param wrong 
	 * @return retorna un conjunto (potencialmente vacio) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra valida 
	 */
	public abstract Set<String> getCorrections(String wrong);
}
