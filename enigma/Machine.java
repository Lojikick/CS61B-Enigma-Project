package enigma;
import java.util.Collection;
import static enigma.EnigmaException.*;
/** Class that represents a complete enigma machine.
 *  @author Avik Samanta
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _slots = new Rotor[numRotors];
        _pawls = pawls;
        _inventory = allRotors;
        Permutation perm = new Permutation("", _alphabet);
        _plugBoard = new FixedRotor("plugNchug", perm);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _slots.length;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }


    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _slots[k];
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < _slots.length; i++) {
            for (Rotor curr: _inventory) {
                String temp1 = new String();
                String temp2 = new String();
                temp1 = curr.name();
                temp2 = rotors[i];
                if (temp2.equals(temp1)) {
                    _slots[i] = curr;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            getRotor(i).changeSetting(_alphabet.toInt(setting.charAt(i - 1)));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugBoard.permutation();
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = new FixedRotor("Plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }
    /** Returns the Settings. */
    public String returnSettings() {
        String retVal = "";
        for (int i = 0; i < _slots.length; i++) {
            retVal += _alphabet.toChar(_slots[i].setting());
        }
        return retVal;
    }
    /** Advance all rotors to their next position. */
    public void advanceRotors() {
        int[] settings = new int[_slots.length];
        Boolean[] can = new Boolean[_slots.length];
        for (int i = 0; i < _slots.length; i++) {
            int temp = getRotor(i).getSetting();
            settings[i] = temp;
            can[i] = false;
            if (i == _slots.length - 1) {
                can[i] = true;
            }
        }
        for (int i = _slots.length - 1; i > 1; i--) {
            Rotor curr = getRotor(i);
            Rotor neighborL = getRotor(i - 1);
            Rotor neighborR = getRotor(i);
            if (i != _slots.length - 1) {
                neighborR = getRotor(i + 1);
            }
            if (curr.getClass() != FixedRotor.class) {
                if (curr.getClass() != Reflector.class) {
                    if (i != _slots.length) {
                        if (neighborR != _slots[_slots.length - 1]) {
                            if (neighborR.atNotch()) {
                                can[i + 1] = true;
                            }
                        }
                    }
                }
                if (curr.atNotch() && neighborL.getClass() != Reflector.class) {
                    if ((neighborL.getClass() != FixedRotor.class)) {
                        can[i - 1] = true;
                        if (i != _slots.length - 1) {
                            can[i] = true;
                        }
                    }
                }

            }
        }
        for (int i = 0; i < can.length; i++) {
            if (can[i]) {
                Rotor curr = getRotor(i);
                curr.advance();
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {

        c = _plugBoard.convertForward(c);
        for (int i = _slots.length - 1; i > 0; i--) {
            Rotor curr = getRotor(i);
            c = curr.convertForward(c);

        }
        c = getRotor(0).convertForward(c);
        for (int i = 1; i < _slots.length; i++) {
            Rotor curr = getRotor(i);
            c = curr.convertBackward(c);
        }
        c = _plugBoard.convertBackward(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        char curr;
        int index;
        int duration = msg.length();
        for (int i = 0; i < duration; i++) {
            curr = msg.charAt(i);
            index = _alphabet.toInt(curr);
            advanceRotors();
            index = applyRotors(index);
            result += _alphabet.toChar(index);
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Returns the pawls. */
    protected int _pawls;
    /** Returns the slots. */
    protected Rotor[] _slots;
    /** Returns the inventory. */
    protected Collection<Rotor> _inventory;
    /** Returns the plugboard.*/
    protected FixedRotor _plugBoard;
}
