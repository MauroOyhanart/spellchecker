package edu.isistan.spellchecker;
import edu.isistan.spellchecker.corrector.Dictionary;
import edu.isistan.spellchecker.corrector.impl.FileCorrector;
import edu.isistan.spellchecker.corrector.impl.SwapCorrector;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.junit.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/** Cree sus propios tests. */
public class MyTests {

    //Tests para el TokenScanner

    /**
     * La entrada es vacia.
     */
    @Test
    public void testEmptyInput() {
        Reader in = new StringReader("");
        try (TokenScanner ts = new TokenScanner(in)){
            assertFalse(ts.hasNext());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * La entrada tiene una solo token palabra.
     */
    @Test
    public void testOneWordToken() {
        Reader in = new StringReader(" $ word$$");
        try (TokenScanner ts = new TokenScanner(in)){
            int count = 0;
            while (ts.hasNext()) {
                String token = ts.next();
                if (TokenScanner.isWord(token)) count++;
            }
            assertEquals(1, count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * La entrada tiene un solo token no-palabra.
     */
    @Test
    public void testOneNonWordToken() {
        Reader in = new StringReader("word$word");
        try (TokenScanner ts = new TokenScanner(in)){
            int count = 0;
            while (ts.hasNext()) {
                String token = ts.next();
                if (!TokenScanner.isWord(token)) count++;
            }
            assertEquals(1, count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * La entrada tiene los dos tipos de tokens, y termina en un token palabra.
     */
    @Test
    public void testTwoKindsEndWordToken() {
        Reader in = new StringReader("word$word");
        try (TokenScanner ts = new TokenScanner(in)){
            boolean hasNonWordTokens = false;
            boolean hasWordTokens = false;
            String token = "~";
            while (ts.hasNext()) {
                token = ts.next();
                if (TokenScanner.isWord(token)) hasWordTokens = true;
                else hasNonWordTokens = true;
            }
            if (token.equals("~"))
                fail("No hubo ni siquiera un token.");

            if (!hasNonWordTokens || !hasWordTokens)
                fail("Falta un tipo de token.");

            if (!TokenScanner.isWord(token)) {
                fail("No termina en un token palabra.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * La entrada tiene los dos tipos de tokens, y termina en un token no palabra.
     */
    @Test
    public void testTwoKindsEndNonWordToken() {
        Reader in = new StringReader("$word$");
        try (TokenScanner ts = new TokenScanner(in)){
            boolean hasNonWordTokens = false;
            boolean hasWordTokens = false;
            String token = "~";
            while (ts.hasNext()) {
                token = ts.next();
                if (TokenScanner.isWord(token)) hasWordTokens = true;
                else hasNonWordTokens = true;
            }
            if (token.equals("~"))
                fail("No hubo ni siquiera un token.");

            if (!hasNonWordTokens || !hasWordTokens)
                fail("Falta un tipo de token.");

            if (TokenScanner.isWord(token)) {
                fail("No termina en un token no palabra.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Tests para el Diccionario

    private Dictionary loadForTest(Reader in) throws IOException {
        TokenScanner ts = new TokenScanner(in);
        return new Dictionary(ts);
    }

    /**
     * Chequear por una palabra que está en el diccionario.
     * @throws IOException
     */
    @Test
    public void testDoesNotExistWord() throws IOException{
        Reader in = new StringReader("Some words in a soup");
        try {
            Dictionary dict = loadForTest(in);
            assertFalse(dict.isWord("south"));
        } catch (IOException ioe) {
            System.out.println("Could not complete test: " + ioe.getMessage());
            throw ioe; //rethrow
        }
    }

    /**
     * Chequear por una palabra que NO está en el diccionario.
     * @throws IOException
     */
    @Test
    public void testDoesExistWord() throws IOException {
        Reader in = new StringReader("Some words in a soup");
        try {
            Dictionary dict = loadForTest(in);
            assertTrue(dict.isWord("some"));
        } catch (IOException ioe) {
            System.out.println("Could not complete test: " + ioe.getMessage());
            throw ioe; //rethrow
        }
    }

    /**
     * Preguntar por el número de palabras en el diccionario.
     * @throws IOException
     */
    @Test
    public void testCountWordsInDict() throws IOException {
        Reader in = new StringReader("Some words in a soup");
        try {
            Dictionary dict = loadForTest(in);
            assertEquals(5, dict.getNumWords());
        } catch (IOException ioe) {
            System.out.println("Could not complete test: " + ioe.getMessage());
            throw ioe; //rethrow
        }
    }

    /**
     * Verificar que el String vacio “” no sea una palabra.
     */
    @Test
    public void testIsWord() throws IOException {
        Reader in = new StringReader("Some words in a soup");
        try {
            Dictionary dict = loadForTest(in);
            assertFalse(dict.isWord(" "));
        } catch (IOException ioe) {
            System.out.println("Could not complete test: " + ioe.getMessage());
            throw ioe; //rethrow
        }
    }

    /**
     * Chequear que la misma palabra con distintas capitalizaciones esté en el diccionario.
     */
    @Test
    public void testSameWordDiffCaps() throws IOException {
        Reader in = new StringReader("Some words in a soup");
        try {
            Dictionary dict = loadForTest(in);
            assertTrue(dict.isWord("some"));
            assertTrue(dict.isWord("soMe"));
            assertTrue(dict.isWord("sOMe"));
            assertTrue(dict.isWord("SOME"));
            assertTrue(dict.isWord("soME"));
            assertTrue(dict.isWord("somE"));
        } catch (IOException ioe) {
            System.out.println("Could not complete test: " + ioe.getMessage());
            throw ioe; //rethrow
        }
    }

    //Tests para el FileCorrector

    /**
     * Probar un archivo con espacios extras en alrededor de las líneas o alrededor de las comas.
     * /usr/local/tallerjava/proyectofinal/new_corrections.txt
     *
     * El archivo que le pase como parametro contiene solamente las dos siguientes lineas (las comillas y los palitos '|' no forman parte del archivo):
     *         "
     *         |\tcorrectme  ,  corrected
     *         |  correctthis ,  corrected
     *         "
     */
    @Test
    public void testExtraSpaces() {
        String fileName = "/usr/local/tallerjava/proyectofinal/new_corrections.txt";
        try {
            FileCorrector fileCorrector = FileCorrector.make(fileName);
            assertEquals("corrected", fileCorrector.getCorrections("correctme").stream().findFirst().orElse("-1"));
            assertEquals("corrected", fileCorrector.getCorrections("correctthis").stream().findFirst().orElse("-1"));
        } catch (Exception e) {
            fail("Could not generate corrector");
        }
    }

    /**
     * Pedir correcciones para una palabra sin correcciones.
     *
     * Mismo archivo
     */
    @Test
    public void testGetNoCorrections() {
        String fileName = "/usr/local/tallerjava/proyectofinal/new_corrections.txt";
        try {
            FileCorrector fileCorrector = FileCorrector.make(fileName);
            if (!fileCorrector.getCorrections("doesnotexist").isEmpty()) {
                fail("No deberia encontrar correcciones para esta palabra.");
            }
        } catch (Exception e) {
            fail("Could not generate corrector");
        }
    }

    /**
     * Pedir correcciones para una palabra con múltiples correcciones.
     *
     * Ahora el archivo tiene una linea mas:
     * correctme  ,  corrected
     * correctthis ,  corrected
     * correctme, correctedx
     *
     */
    @Test
    public void testMultipleCorrections() {
        String fileName = "/usr/local/tallerjava/proyectofinal/new_corrections2.txt";
        try {
            FileCorrector fileCorrector = FileCorrector.make(fileName);
            Set<String> s = new HashSet<>();
            s.add("corrected");
            s.add("correctedx");
            assertEquals(s, fileCorrector.getCorrections("correctme"));
        } catch (Exception e) {
            fail("Could not generate corrector");
        }
    }

    /**
     * Probar correcciones para palabras con distintas capitalizaciones, e.g., PaLaBar, palABAR,
     * palabra, o PALABAR.
     *
     * Ahora el archivo tiene este contenido:
     *
     * correctme, correctedx
     * correctMe, correctedx
     * coRRectMe, correctedx
     * CORRectME, correctedx
     */
    @Test
    public void testCapCorrections() {
        String fileName = "/usr/local/tallerjava/proyectofinal/new_corrections3.txt";
        try {
            FileCorrector fileCorrector = FileCorrector.make(fileName);
            assertEquals("correctedx", fileCorrector.getCorrections("correctme").stream().findFirst().orElse("-1"));
            assertEquals("correctedx", fileCorrector.getCorrections("correctMe").stream().findFirst().orElse("-1"));
            assertEquals("correctedx", fileCorrector.getCorrections("coRRectMe").stream().findFirst().orElse("-1"));
            assertEquals("correctedx", fileCorrector.getCorrections("coRRectME").stream().findFirst().orElse("-1"));

            assertEquals("Correctedx", fileCorrector.getCorrections("COrrECtme").stream().findFirst().orElse("-1")); //matchea case
            assertEquals("correctedx", fileCorrector.getCorrections("correctmE").stream().findFirst().orElse("-1"));
            assertEquals("Correctedx", fileCorrector.getCorrections("CoRrEcTmE").stream().findFirst().orElse("-1")); //matchea case
        } catch (Exception e) {
            fail("Could not generate corrector");
        }
    }

    // Tests para el SwapCorrector

    /**
     * Proveer un diccionario null.
     */
    @Test
    public void testDictIsNull() {
        try {
            Dictionary dict = null;
            SwapCorrector swapCorrector = new SwapCorrector(dict);
            fail("Deberia tirar excepcion.");
        } catch (Exception e) {

        }
    }

    /**
     * Pedir correcciones para una palabra que está en el diccionario.
     *
     */
    @Test
    public void testWordIsInDict() throws Exception {
        Reader words = new StringReader("table\nchair\ncat\ndog");
        SwapCorrector swapCorrector = new SwapCorrector(new Dictionary(new TokenScanner(words)));

        if (swapCorrector.getCorrections("table").stream().findFirst().isPresent()) {
            fail("No deberia haber correcciones para una palabra que esta en el diccionario?");
        }
    }

    /**
     * Pedir correcciones para una palabra con distintas capitalizaciones.
     * @throws Exception
     */
    @Test
    public void testDiffCaps() throws Exception {
        Reader words = new StringReader("table\nchair\ncat\ndog");
        SwapCorrector swapCorrector = new SwapCorrector(new Dictionary(new TokenScanner(words)));

        assertEquals("table", swapCorrector.getCorrections("tabel").stream().findFirst().orElse("-1"));
        assertEquals("table", swapCorrector.getCorrections("tABel").stream().findFirst().orElse("-1"));
        assertEquals("table", swapCorrector.getCorrections("tABEL").stream().findFirst().orElse("-1"));
        assertEquals("Table", swapCorrector.getCorrections("TABEL").stream().findFirst().orElse("-1"));
    }


}