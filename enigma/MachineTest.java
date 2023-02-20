package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTS ***** */

    private static final Alphabet AZ = new Alphabet(TestUtils.UPPER_STRING);
    private static final Alphabet AC = new Alphabet("ABC");

    private static final HashMap<String, Rotor> ROTORS = new HashMap<>();

    static {
        HashMap<String, String> nav = TestUtils.NAVALA;
        ROTORS.put("B", new Reflector("B", new Permutation(nav.get("B"), AZ)));
        ROTORS.put("Beta",
                new FixedRotor("Beta",
                        new Permutation(nav.get("Beta"), AZ)));
        ROTORS.put("III",
                new MovingRotor("III",
                        new Permutation(nav.get("III"), AZ), "V"));
        ROTORS.put("IV",
                new MovingRotor("IV", new Permutation(nav.get("IV"), AZ),
                        "J"));
        ROTORS.put("I",
                new MovingRotor("I", new Permutation(nav.get("I"), AZ),
                        "Q"));
    }

    private static final String[] ROTORS1 = { "B", "Beta", "III", "IV", "I" };
    private static final String[] SIMPARR = { "r", "a", "b", "c"};
    private static final String SETTING1 = "AXLE";

    private Machine mach1() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        mach.setRotors(SETTING1);
        return mach;
    }
    private Machine simp() {
        ArrayList<Rotor> simpArr = new ArrayList<Rotor>();
        Permutation p = new Permutation("(ABC)", AC);
        MovingRotor a = new MovingRotor("a", p, "C");
        MovingRotor b = new MovingRotor("b", p, "C");
        MovingRotor c = new MovingRotor("c", p, "C");
        Reflector r = new Reflector("r", p);
        simpArr.add(r);
        simpArr.add(a);
        simpArr.add(b);
        simpArr.add(c);
        Machine mach = new Machine(AC, 4, 3, simpArr);
        mach.insertRotors(SIMPARR);
        mach.setRotors("AAA");
        return mach;
    }

    @Test
    public void testInsertRotors() {
        Machine mach = new Machine(AZ, 5, 3, ROTORS.values());
        mach.insertRotors(ROTORS1);
        assertEquals(5, mach.numRotors());
        assertEquals(3, mach.numPawls());
        assertEquals(AZ, mach.alphabet());
        assertEquals(ROTORS.get("B"), mach.getRotor(0));
        assertEquals(ROTORS.get("Beta"), mach.getRotor(1));
        assertEquals(ROTORS.get("III"), mach.getRotor(2));
        assertEquals(ROTORS.get("IV"), mach.getRotor(3));
        assertEquals(ROTORS.get("I"), mach.getRotor(4));
    }

    @Test
    public void testConvertChar() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(YF) (HZ)", AZ));
        assertEquals(25, mach.convert(24));
    }
    @Test
    public void testSimpleAdvance() {
        Machine mach = simp();
        mach.setPlugboard(new Permutation("(YF) (HZ)", AZ));
        mach.advanceRotors();
        assertEquals("AAAB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AAAC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AABA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AABB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AABC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AACA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABAB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABAC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABBA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABBB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABBC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ABCA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACAB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACAC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACBA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACBB", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACBC", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("ACCA", mach.returnSettings());
        mach.advanceRotors();
        assertEquals("AAAB", mach.returnSettings());
    }
    @Test
    public void testConvertMsg() {
        Machine mach = mach1();
        mach.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", AZ));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                mach.convert("FROMHISSHOULDERHIAWATHA"));
    }
}
