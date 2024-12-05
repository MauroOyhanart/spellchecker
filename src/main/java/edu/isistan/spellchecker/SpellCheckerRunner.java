package edu.isistan.spellchecker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.dictionary.Dictionary;
import edu.isistan.spellchecker.corrector.dictionary.IDictionary;
import edu.isistan.spellchecker.corrector.dictionary.TrieDictionary;
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
	private static Corrector makeCorrector(String type, IDictionary dict)
			throws IOException, FileCorrector.FormatException {
		if (type.equals("SWAP")) {
			log("Corrector -> SwapCorrector");
			return new SwapCorrector(dict);
		}
		if (type.equals("LEV")) {
			log("Corrector -> Levenshtein Corrector");
			return new Levenshtein(dict);
		}
		log("Corrector -> File Corrector");
		return FileCorrector.make(type);
	}

	public static void main(String[] args) {
		start(args, false);
	}

	public static void start(String args[], boolean trieDictionary) {
		if (args.length != 4) {
			log("uso: java SpellCheckRunner <in> <out> <dictionary> <corrector>");
			log("<corrector> es SWAP, LEV, or el path para instanciar el FileCorrector.");
			return;
		}
		try (Reader in = new BufferedReader(new FileReader(args[0]));
			 Writer out = new BufferedWriter(new FileWriter(args[1]))) {
			log("inicializando objetos");

			IDictionary dict = null;
			if (trieDictionary)
				dict = TrieDictionary.make(args[2]);
			else dict = Dictionary.make(args[2]);

			SpellChecker sp = new SpellChecker(makeCorrector(args[3], dict), dict);
			log("chequeando documento");
			sp.checkDocument(in, System.in, out);
			log("finalizado");
		} catch (IOException e) {
			logErr("main: Error procesando el documento: " + e.getMessage());
		} catch (FileCorrector.FormatException e) {
			logErr("main: Error de formato: " + e.getMessage());
		}
	}

	private static void log(Object text) {
		System.out.println("Main: " + text.toString());
	}

	private static void logErr(Object text) {
		System.err.println("Main [error]: " + text.toString());
	}
}
