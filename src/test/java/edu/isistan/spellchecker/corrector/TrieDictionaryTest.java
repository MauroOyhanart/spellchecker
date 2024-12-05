package edu.isistan.spellchecker.corrector;

import edu.isistan.spellchecker.corrector.dictionary.Dictionary;
import edu.isistan.spellchecker.corrector.dictionary.IDictionary;
import edu.isistan.spellchecker.corrector.dictionary.TrieDictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class TrieDictionaryTest {

  
  @Test(timeout=500) public void testTrieDictionaryContainsSimple() throws IOException {
    IDictionary d = new TrieDictionary(new TokenScanner(new FileReader("/usr/local/tallerjava/proyectofinal/smallDictionary.txt")));
    assertTrue("'apple' -> should be true ('apple' in file)", d.isWord("apple"));
    assertTrue("'Banana' -> should be true ('banana' in file)", d.isWord("Banana"));
    assertFalse("'pineapple' -> should be false", d.isWord("pineapple"));
  }

  
  @Test(timeout=500) public void testTrieDictionaryContainsApostrophe() throws IOException {
    IDictionary d = new TrieDictionary(new TokenScanner(new FileReader("/usr/local/tallerjava/proyectofinal/smallDictionary.txt")));
    assertTrue("'it's' -> should be true ('it's' in file)", d.isWord("it's"));
  }

  
  @Test(timeout=500) public void testConstructorInvalidTokenScanner() throws IOException {
    try {
      TokenScanner ts = null;
      new TrieDictionary(ts);
      fail("Expected IllegalArgumentException - null TokenScanner");
    } catch (IllegalArgumentException e){
      //Do nothing - it's supposed to throw this
    }
  }

	 

  // Do NOT add your own tests here. Put your tests in MyTest.java
}
