package edu.isistan.spellchecker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.corrector.impl.FileCorrector;
import edu.isistan.spellchecker.corrector.impl.Levenshtein;
import edu.isistan.spellchecker.corrector.impl.SwapCorrector;

/**
 * 
 * Main para el programa del spellcheker.  
 * <p>
 * Puede ser usado desde linea de comando:
 * <p>
 * <code>java SpellCheckerRunner {@code in} {@code out} {@code dictionary} {@code corrector}</code>
 * <p>
 * @param in - archivo de entrada
 * @param out - archivo de salida
 * @param dictionary - diccionario.
 * @param corrector -  SWAP (para SwapCorrector), 
 * LEV (para Levenshtein), o nombre de archivo (para FileCorrector)
 * 
 */
public class SpellCheckerRunner {
	/**
	 * Crea el corrector adecuado dada la entrada de la linea de comando.
	 * 
	 * @param type
	 * @param dict
	 * @throws IOException
	 * @throws FileCorrector.FormatException
	 */
	private static Corrector makeCorrector(String type, Dictionary dict)
			throws IOException, FileCorrector.FormatException {
		if (type.equals("SWAP")) {
			return new SwapCorrector(dict);
		}
		if (type.equals("LEV")) {
			return new Levenshtein(dict);
		}
		return FileCorrector.make(type);
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("uso: java SpellCheckRunner <in> <out> <dictionary> <corrector>");
			System.out.println("<corrector> es SWAP, LEV, or el path para instanciar el FileCorrector.");
			return;
		}
		try (Reader in = new BufferedReader(new FileReader(args[0]));
			Writer out = new BufferedWriter(new FileWriter(args[1]))) {
			log("inicializando objetos");
			Dictionary dict = Dictionary.make(args[2]);
			SpellChecker sp = new SpellChecker(makeCorrector(args[3], dict), dict);
			log("chequeando documento");
			sp.checkDocument(in, System.in, out);
		} catch (IOException e) {
			System.err.println("main: Error procesando el documento: " + e.getMessage());
		} catch (FileCorrector.FormatException e) {
			System.err.println("main: Error de formato: " + e.getMessage());
		}
	}

	private static void log(String text) {
		System.out.println("main: " + text);
	}
}
