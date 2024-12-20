package edu.isistan.spellchecker.corrector.impl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


import edu.isistan.spellchecker.corrector.dictionary.IDictionary;
import org.junit.Test;

import edu.isistan.spellchecker.SpellChecker;
import edu.isistan.spellchecker.corrector.Corrector;
import edu.isistan.spellchecker.corrector.dictionary.Dictionary;


public class SpellCheckerTest {


	public static void spellCheckFiles(String fdict, int dictSize, String fcorr,
			String fdoc, String fout, String finput) 
					throws IOException, FileCorrector.FormatException
	{
		IDictionary dict = Dictionary.make(fdict);

		Corrector corr = null;
		if (fcorr == null) {
			corr = new SwapCorrector(dict);
		} else {
			corr = FileCorrector.make(fcorr);
		}


		if(dictSize >= 0) 
			assertEquals("Dictionary size = " + dictSize, dictSize,
					dict.getNumWords());

		FileInputStream input = new FileInputStream(finput);
		Reader in = new BufferedReader(new FileReader(fdoc));
		Writer out = new BufferedWriter(new FileWriter(fout));

		SpellChecker sc = new SpellChecker(corr,dict);

		sc.checkDocument(in, input, out);
		in.close();
		input.close();
		out.flush();
		out.close();
	}


	/**
	 * Comentario de por que falla:
	 * Yo lo que pregunte por email y no entendi la respuesta, es "como hago para resolver la situacion en la que 'brown' no es encontrada en el diccionario?".
	 * Lo que hice es tomarlo por stdin, es decir por la terminal, pero claro este test no admite esto y por eso falla.
	 */
	@Test(timeout=500) public void testCheckFoxGood() throws IOException, FileCorrector.FormatException {
		spellCheckFiles("/usr/local/tallerjava/proyectofinal/theFoxDictionary.txt",7,"/usr/local/tallerjava/proyectofinal/theFoxMisspellings.txt",
				"/usr/local/tallerjava/proyectofinal/theFox.txt","/usr/local/tallerjava/proyectofinal/foxout.txt","/usr/local/tallerjava/proyectofinal/theFox_goodinput.txt");
		compareDocs("/usr/local/tallerjava/proyectofinal/foxout.txt", "/usr/local/tallerjava/proyectofinal/theFox_expected_output.txt");
	}


	/**
	 * Comentario de por que falla (exactamente igual al de arriba):
	 * Yo lo que pregunte por email y no entendi la respuesta, es "como hago para resolver la situacion en la que 'brown' no es encontrada en el diccionario?".
	 * Lo que hice es tomarlo por stdin, es decir por la terminal, pero claro este test no admite esto y por eso falla.
	 */
	@Test(timeout=500) public void testCheckMeanInput() throws IOException, FileCorrector.FormatException {
		spellCheckFiles("/usr/local/tallerjava/proyectofinal/theFoxDictionary.txt",7,"/usr/local/tallerjava/proyectofinal/theFoxMisspellings.txt",
				"/usr/local/tallerjava/proyectofinal/theFox.txt","/usr/local/tallerjava/proyectofinal/foxout.txt","/usr/local/tallerjava/proyectofinal/theFox_meaninput.txt");
		compareDocs("/usr/local/tallerjava/proyectofinal/foxout.txt", "/usr/local/tallerjava/proyectofinal/theFox_expected_output.txt");
	}


	/**
	 * Esto lo mismo que los anteriores.
	 */
	@Test(timeout=500) public void testCheckGettysburgSwap() throws IOException, FileCorrector.FormatException {
		// Use the SwapCorrector instead!
		spellCheckFiles("/usr/local/tallerjava/proyectofinal/dictionary.txt",60822,null,
				"/usr/local/tallerjava/proyectofinal/Gettysburg.txt","/usr/local/tallerjava/proyectofinal/Gettysburg-out.txt",
				"/usr/local/tallerjava/proyectofinal/Gettysburg_input.txt");
		compareDocs("/usr/local/tallerjava/proyectofinal/Gettysburg-out.txt", "/usr/local/tallerjava/proyectofinal/Gettysburg_expected_output.txt");
	}




	public static void compareDocs(String out, String expected) 
			throws IOException, FileNotFoundException 
	{
		BufferedReader f1 = new BufferedReader(new FileReader(out));
		BufferedReader f2 = new BufferedReader(new FileReader(expected));

		try{
			String line1 = f1.readLine();
			String line2 = f2.readLine();
			while(line1 != null && line2 != null){
				assertEquals("Output file did not match expected output.", line2, line1);
				line1 = f1.readLine();
				line2 = f2.readLine();
			}
			if(line1 != null) {
				fail("Expected end of file, but found extra lines in the output.");
			} else if(line2 != null) {
				fail("Expected more lines, but found end of file in the output. ");
			}
		}
		finally{
			f1.close();
			f2.close();

		}
	}

}
