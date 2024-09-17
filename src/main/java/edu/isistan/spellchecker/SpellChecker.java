package edu.isistan.spellchecker;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

/**
 * El SpellChecker usa un Dictionary, un Corrector, and I/O para chequear
 * de forma interactiva un stream de texto. Despues escribe la salida corregida
 * en un stream de salida. Los streams pueden ser archivos, sockets, o cualquier
 * otro stream de Java.
 * <p>
 * Nota:
 * <ul>
 * <li> La implementacion provista provee metodos utiles para implementar el SpellChecker.
 * <li> Toda la salida al usuario deben enviarse a System.out (salida estandar)
 * </ul>
 * <p>
 * El SpellChecker es usado por el SpellCkecherRunner. Ver:
 * @see SpellCheckerRunner
 */
public class SpellChecker {
	private Corrector corr;
	private Dictionary dict;
	private PrintStream out;

	/**
	 * Constructor del SpellChecker
	 * 
	 * @param c un Corrector
	 * @param d un Dictionary
	 * @throws NullPointerException si el corrector es null o el diccionario es null
	 */
	public SpellChecker(Corrector c, Dictionary d) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(d);
		this.out = System.out; //default out
		corr = c;
		dict = d;
	}

	/**
	 * Retorna un entero desde el Scanner provisto. El entero estara en el rango [min, max].
	 * Si no se ingresa un entero o este esta fuera de rango, repreguntara.
	 *
	 * @param min
	 * @param max
	 * @param sc
	 */
	private int getNextInt(int min, int max, Scanner sc) {
		while (true) {
			try {
				int choice = Integer.parseInt(sc.next());
				if (choice >= min && choice <= max) {
					return choice;
				}
			} catch (NumberFormatException ex) {
				// Was not a number. Ignore and prompt again.
			}
			spellCheckerLog("Entrada invalida. Pruebe de nuevo!");
		}
	}

	/**
	 * Retorna el siguiente String del Scanner.
	 *
	 * @param sc
	 */
	private String getNextString(Scanner sc) {
		return sc.next();
	}



	/**
	 * checkDocument interactivamente chequea un archivo de texto..  
	 * Internamente, debe usar un TokenScanner para parsear el documento.  
	 * Tokens de tipo palabra que no se encuentran en el diccionario deben ser corregidos
	 * ; otros tokens deben insertarse tal cual en en documento de salida.
	 * <p>
	 *
	 * @param in stream donde se encuentra el documento de entrada.
	 * @param input entrada interactiva del usuario. Por ejemplo, entrada estandar System.in
	 * @param out stream donde se escribe el documento de salida.
	 * @throws IOException si se produce algun error leyendo el documento.
	 */
	public void checkDocument(Reader in, InputStream input, Writer out) throws IOException {
		spellCheckerLog("Iniciando");
		Scanner sc = new Scanner(input);
		try (TokenScanner tokenScanner = new TokenScanner(in)) {
			//STUB
			while (tokenScanner.hasNext()) {
				String token = tokenScanner.next();
				if (TokenScanner.isWord(token)) { // [[es palabra]]
					//chequear que no tenga errores o cosas para mejorar
					//if (tiene errores o cosas para mejorar)
					//	mejorar
				} else {

				}
			}
		} catch(NoSuchElementException nse) {
			spellCheckerLog("Se ha llegado al final del documento");
		} catch (Exception e) {
			spellCheckerLog("Error: " + e.getMessage());
		}
		finally {
			sc.close();
		}
	}

	private void spellCheckerLog(String text) {
		out.println("Spell Checker: " + text);
	}
}
