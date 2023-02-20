package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Avik Samanta
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _perm = new ArrayList<ArrayList<Character>>();
        this.addCycle(cycles);
        _currPerm = _perm.get(0);
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        if (cycle.equals("")) {
            looseEnds(cycle);
            return;
        }
        ArrayList<Character> currArr = new ArrayList<Character>();
        for (int i = 0; i < cycle.length(); i++) {
            char temp = cycle.charAt(i);
            if ((temp != '(') && (temp != ')') && (temp != ' ')) {
                currArr.add(temp);
            }
            if (temp == ')') {
                _perm.add(currArr);
                currArr = new ArrayList<Character>();
            }
        }
    }
    /** Insterts a permutation.
     * @param currPerm
     * */
    private void insertPerm(String currPerm) {
        ArrayList<Character> currArr = new ArrayList<Character>();
        for (int j = 0; j < currPerm.length(); j++) {
            currArr.add(currPerm.charAt(j));
        }
        _perm.add(currArr);
    }
    /** Creates a perm in the cycle for letters not in cycle.
     * @param cycle
     * */
    private void looseEnds(String cycle) {
        String words = _alphabet.getLetters();
        for (int i = 0; i < words.length(); i++) {
            if (cycle.indexOf(words.charAt(i)) == -1) {
                ArrayList<Character> temp = new ArrayList<Character>();
                temp.add(words.charAt(i));
                _perm.add(temp);
            }
        }
    }
    /** Finds the current perm for a letter.
     * @param a
     * */
    private void findCurrCycle(char a) {
        for (int i = 0; i < _perm.size(); i++) {
            ArrayList<Character> curr = _perm.get(i);
            if (curr.contains(a)) {
                _currPerm = curr;
                return;
            }
        }
        _currPerm = new ArrayList<Character>();
        _currPerm.add(a);

    }

    /** Return the value of P modulo the size of this permutation.*/
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }
    /** Wraps the current permutation.
     * @param p
     * @return Wraps Current Permutation.
     * */
    final int currPermWrap(int p) {
        int r = p % _currPerm.size();
        if (r < 0) {
            r += _currPerm.size();
        }
        return r;

    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size.
     *  @param p
     *  */
    int permute(int p) {
        return wrap(p);
    }
    /** My real permutation method haha.
     * @param p
     * @return Real permutation.
     * */
    int realPermute(int p) {
        return wrap(p + 1);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size.
     *  @param c
     *  */
    int invert(int c) {
        return wrap(c);
    }
    /** My real invert method haha.
     * @param c
     * @return Real Invert.
     * */
    int realInvert(int c) {
        return wrap(c - 1);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        this.findCurrCycle(p);
        int translate = _currPerm.indexOf(p);
        translate = currPermWrap(translate + 1);
        char result = _currPerm.get(translate);
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        this.findCurrCycle(c);
        int translate = _currPerm.indexOf(c);
        translate = currPermWrap(translate - 1);
        char result = _currPerm.get(translate);
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    public Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (_currPerm.size() > 1) {
            return true;
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Array list of Permutations. */
    private ArrayList<ArrayList<Character>> _perm;
    /** Sub Permutation within _Perm. */
    private ArrayList<Character> _currPerm;
}
