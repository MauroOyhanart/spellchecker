package edu.isistan.spellchecker.corrector.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.*;

import edu.isistan.spellchecker.corrector.Corrector;

/**
 * Corrector basado en un archivo.
 * 
 */
public class FileCorrector extends Corrector {
	private final PrintStream out;
	/*
	 * One to many
	 * Permite facilmente saber las correcciones de una palabra.
	 * No me permite facilmente saber, dado una correccion, a quien corrige (hay que iterar toda la estructura).
	 */
	private final Map<String, Set<String>> correcciones; //ineficiente, pero bien

	/**
	 * Constructor del FileReader
	 * <p>
	 * Utilice un BufferedReader para leer el archivo de definicion
	 *
	 * <p> 
	 * Cada linea del archivo del diccionario tiene el siguiente formato: 
	 * misspelled_word,corrected_version
	 *
	 * <p>
	 *Ejemplo:<br>
	 * <pre>
	 * aligatur,alligator<br>
	 * baloon,balloon<br>
	 * inspite,in spite<br>
	 * who'ev,who've<br>
	 * ther,their<br>
	 * ther,there<br>
	 * </pre>
	 * <p>
	 * Estas lineas no son case-insensitive, por lo que todas deberian generar el mismo efecto:<br>
	 * <pre>
	 * baloon,balloon<br>
	 * Baloon,balloon<br>
	 * Baloon,Balloon<br>
	 * BALOON,balloon<br>
	 * bAlOon,BALLOON<br>
	 * </pre>
	 * <p>
	 * Debe ignorar todos los espacios vacios alrededor de las palabras, por lo
	 * que estas entradas son todas equivalentes:<br>
	 * <pre>
	 * inspite,in spite<br>
	 *    inspite,in spite<br>
	 * inspite   ,in spite<br>
	 *  inspite ,   in spite  <br>
	 * </pre>
	 * Los espacios son permitidos dentro de las sugerencias. 
	 *
	 * <p>
	 * Deberia arrojar <code>FileCorrector.FormatException</code> si se encuentra algun
	 * error de formato:<br>
	 * <pre>
	 * ,correct<br>
	 * wrong,<br>
	 * wrong correct<br>
	 * wrong,correct,<br>
	 * </pre>
	 * <p>
	 *
	 * @param r Secuencia de caracteres 
	 * @throws IOException error leyendo el archivo
	 * @throws FileCorrector.FormatException error de formato
	 * @throws IllegalArgumentException reader es null
	 */
	public FileCorrector(Reader r) throws IOException, FormatException {
		this.out = System.out; //default out
		this.correcciones = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(r)) {
			Iterator<String> it = reader.lines().iterator();
			while (it.hasNext()) {
				String line = it.next();
				if (line != null) {
					line.replace(" ", ""); //sacamos espacios en blanco
					String words[] = line.split(",");
					//respetamos case
					String token = words[0];
					String correccion = words[1];
					Set<String> correcs = correcciones.get(token);
					if (correcs == null) {
						correcs = new HashSet<>();
						correcciones.put(token, correcs);
					}
					correcs.add(correccion);
				}
			}
		} catch (Exception e) {
			fileCorrectorLog("Error creando FileCorrector: " + e.getMessage());
			fileCorrectorLog("Rethrow");
			throw new FormatException(e.getMessage());
		}
		fileCorrectorLog("printing correcciones:");
		Set<Map.Entry<String, Set<String>>> entrySet = correcciones.entrySet();
		for (Map.Entry<String, Set<String>> entry: entrySet) {
			StringBuilder sb = new StringBuilder("\"" + entry.getKey() + "\" -> [");
			for (String str: entry.getValue()) {
				sb.append(str);
				sb.append(", ");
			}
			sb.delete(sb.length() -2, sb.length());
			sb.append("]");
			fileCorrectorLog(sb.toString());
		}
	}

	/** Construye el Filereader.
	 *
	 * @param filename 
	 * @throws IOException 
	 * @throws FileCorrector.FormatException 
	 * @throws FileNotFoundException 
	 */
	public static FileCorrector make(String filename) throws IOException, FormatException {
		Reader r = new FileReader(filename);
		FileCorrector fc;
		try {
			fc = new FileCorrector(r);
		} finally {
			r.close();
		}
		return fc;
	}

	/**
	 * Retorna una lista de correcciones para una palabra dada.
	 * Si la palabra mal escrita no esta en el diccionario el set es vacio.
	 * <p>
	 * Ver superclase.
	 *
	 * @param wrong la palabra erronea
	 * @return retorna un conjunto (potencialmente vacio) de sugerencias.
	 * @throws IllegalArgumentException si la entrada no es una palabra valida 
	 */
	public Set<String> getCorrections(String wrong) {
		for (int i = 0; i < wrong.length(); i++) {
			if (wrong.charAt(i) >= '0' && wrong.charAt(i) <= '9') {
				throw new IllegalArgumentException("Word cannot contain a digit");
			}
		}
		Set<String> correcs = correcciones.get(wrong.toLowerCase());
		if (correcs == null) {
			correcs = new HashSet<>();
		}
		return matchCase(wrong, correcs);
	}

	private void fileCorrectorLog(String text) {
		out.println("File Corrector: " + text);
	}
	

	/** Clase especial que se utiliza al tener 
	 * algun error de formato en el archivo de entrada.
	 */
	public static class FormatException extends Exception {
		public FormatException(String msg) {
			super(msg);
		}
	}
}
