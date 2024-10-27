package edu.isistan.spellchecker;
import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

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
 * El SpellChecker es usado por el SpellCheckerRunner. Ver:
 * @see SpellCheckerRunner
 */
public class SpellChecker {
	private final Corrector corr;
	private final Dictionary dict;

	/**
	 * Constructor del SpellChecker
	 * 
	 * @param c un Corrector
	 * @param d un Dictionary
	 * @throws NullPointerException si el corrector es null o el diccionario es null
	 */
	public SpellChecker(Corrector c, Dictionary d) {
		this.corr = Objects.requireNonNull(c);
		this.dict = Objects.requireNonNull(d);
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

        try (TokenScanner tokenScanner = new TokenScanner(in);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            tokenScanner.setName("TokenScannerFileIn"); //para logging
            checkDocumentP(tokenScanner, reader, out);
        } catch (IOException ioe) {
            spellCheckerLog("Error escribiendo o leyendo: " + ioe.getMessage() + "\nRethrow:\n");
            throw ioe;
        } catch (NoSuchElementException nse) {
            spellCheckerLog("Se ha llegado al final del documento");
        } catch (Exception e) {
            spellCheckerLog("Error: " + e.getMessage());
        }
		spellCheckerLog("Documento chequeado.");
	}

	private void checkDocumentP(TokenScanner tokenScanner, BufferedReader reader, Writer out) throws IOException {
		while (tokenScanner.hasNext()) {
			String token = tokenScanner.next();
			spellCheckerLog("\t1. Got token " + Utils.wrap(token));
			if (TokenScanner.isWord(token)) {
				if (dict.isWord(token)) { // la considero como correcta si esta en el diccionario
					out.write(token);
				} else { //y si no, la intento corregir
					spellCheckerLog("\t2. Getting correcciones for token " + Utils.wrap(token));
					Set<String> correcciones = corr.getCorrections(token);
					if (correcciones != null && !correcciones.isEmpty()) {
						String correccion = corr.getCorrections(token).stream().findFirst().orElse(Utils.wrap(token));
						//una correccion fue encontrada
						spellCheckerLog("\t3. Usamos " + Utils.wrap(correccion));
						out.write(correccion);
					} else {
						spellCheckerLog("\t3. No hay correcciones. Ingresela por consola");
						String correccion = readNext(reader);
						out.write(correccion);
						spellCheckerLog("\t4. Correccion manual: " + Utils.wrap(token) + " -> " + correccion);
					}
				}
			} else { //Si no es una palabra del diccionario, la agrego.
				out.write(token);
			}
		}
	}

	/**
	 * Lee el siguiente string desde el inputStream
	 */
	private String readNext(BufferedReader reader) throws IOException {
		String next = reader.readLine();
		while (!dict.isWord(next)) {
			next = reader.readLine();
		}
		if (next == null) next = "";
		return next;
	}

	private void spellCheckerLog(String text) {
		System.out.println("\tSpell Checker: " + text);
	}
}
