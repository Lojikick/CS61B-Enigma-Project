package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Avik Samanta
 */
class Alphabet {
    /** The Letters of the Alphabet. */
    private String _letters;
    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _letters = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _letters.length();
    }
    String getLetters() {
        return _letters;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        int found = _letters.indexOf(ch);
        if (found < 0) {
            return false;
        }
        return true;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _letters.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _letters.indexOf(ch);
    }

}
