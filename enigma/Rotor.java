package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Avik Samanta
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _index = 0;
        _alphabet = alphabet();
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;

    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = _alphabet.toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int result = (p + _setting) % _alphabet.size();
        char convert = _alphabet.toChar(result);
        convert = _permutation.permute(convert);
        result = _alphabet.toInt(convert);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        result = (result - _setting);
        if (result < 0) {
            result += _alphabet.size();
        }

        return result;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int result = (e + _setting) % _alphabet.size();
        char convert = _alphabet.toChar(result);
        convert = _permutation.invert(convert);
        result = _alphabet.toInt(convert);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        result = (result - _setting);
        if (result < 0) {
            result += _alphabet.size();
        }

        return result;
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (_setting == _alphabet.toInt(_notches.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }
    /** Returns the rotor's name. */
    @Override
    public String toString() {
        return "Rotor " + _name;
    }
    /** Returns the currentPermutation. */
    public Permutation getPerm() {
        return _permutation;
    }
    /** Returns the currentSetting. */
    public int getSetting() {
        return _setting;
    }
    /** Changes the currentSetting.
     * @param i
     * */
    public void changeSetting(int i) {
        _setting = i;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    protected Permutation _permutation;
    /** The index. */
    protected int _index;
    /** The Setting. */
    protected int _setting;
    /** The Alphabet. */
    protected Alphabet _alphabet;
    /** The Notches. */
    protected String _notches;


}
