import static org.junit.Assert.assertEquals;

import crewmeister.*;
import org.junit.Test;

public class CrewmeisterChallengeTest {
  private String word;
  private StringBuilder wordBuilder;
  private String actual;
  private String expected;

  @Test
  public void palindromTest() {
    // Palindrom string
    word = "abcba";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);

    word = "abba";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void stringWithSameCharacterTest() {
    // string with same characters
    word = "AAAAA";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void randomCharactersTest() {
    // random characters
    word = "zS6eC2sJ6k";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void stringWithSpaceTest() {
    // string with space
    word = "zS 6e C2  sJ6   k";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void stringWithEnterSymbolTest() {
    // string with \n
    word = "zs6e\nC2sJ6k";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void emptyStringTest() {
    // empty string
    word = "";
    wordBuilder = new StringBuilder(word);

    actual = CrewmeisterChallenge.crewmeisterRevert(word);
    expected = wordBuilder.reverse().toString();
    assertEquals(expected,actual);
  }

  @Test
  public void longestStringTest() {
    // only use this, when it's necessary.
    // longest string
    //word = generateLongestString();
    //wordBuilder = new StringBuilder(word);

    //actual = CrewmeisterChallenge.crewmeisterRevert(word);
    //expected = wordBuilder.reverse().toString();
    //assertEquals(expected,actual);
  }

  @Test(expected = NullPointerException.class)
  public void nullInputTest() {
    // null input
    CrewmeisterChallenge.crewmeisterRevert(null);
  }

  private String generateLongestString() {
    String longestString = "";
    int maxValue = Integer.MAX_VALUE-1;
    for (int i=0;i<=maxValue;i++) {
      longestString += "a";
    }
    longestString += "b";
    return longestString;
  }
}
