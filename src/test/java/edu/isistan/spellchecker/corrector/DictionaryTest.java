package edu.isistan.spellchecker.corrector;
import static org.junit.Assert.*;

import java.io.*;


import edu.isistan.spellchecker.corrector.dictionary.IDictionary;
import org.junit.Test;

import edu.isistan.spellchecker.corrector.dictionary.Dictionary;
import edu.isistan.spellchecker.tokenizer.TokenScanner;

public class DictionaryTest {

  
  @Test(timeout=500) public void testDictionaryContainsSimple() throws IOException {
    IDictionary d = new Dictionary(new TokenScanner(new FileReader("/usr/local/tallerjava/proyectofinal/smallDictionary.txt")));
    assertTrue("'apple' -> should be true ('apple' in file)", d.isWord("apple"));
    assertTrue("'Banana' -> should be true ('banana' in file)", d.isWord("Banana"));
    assertFalse("'pineapple' -> should be false", d.isWord("pineapple"));
  }

  
  @Test(timeout=500) public void testDictionaryContainsApostrophe() throws IOException {
    IDictionary d = new Dictionary(new TokenScanner(new FileReader("/usr/local/tallerjava/proyectofinal/smallDictionary.txt")));
    assertTrue("'it's' -> should be true ('it's' in file)", d.isWord("it's"));
  }

  
  @Test(timeout=500) public void testConstructorInvalidTokenScanner() throws IOException {
    try {
      TokenScanner ts = null;
      new Dictionary(ts);
      fail("Expected IllegalArgumentException - null TokenScanner");
    } catch (IllegalArgumentException e){
      //Do nothing - it's supposed to throw this
    }
  }

	 

  // Do NOT add your own tests here. Put your tests in MyTest.java
}
