package crewmeister;

public class CrewmeisterChallenge {
  /**
   * A method to reverse a string. For example, if the input is 'abc', then it should return 'cba'.
   * @param word is a string that has to be reversed.
   * @return a reversed string.
   */
  public static String crewmeisterRevert(String word) {
    char[] wordElements = word.toCharArray();
    StringBuilder reversedWord = new StringBuilder();
    for (int index = wordElements.length - 1;index >= 0;index--) {
      reversedWord.append(wordElements[index]);
    }
    return reversedWord.toString();
  }

}
