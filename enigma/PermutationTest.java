package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Avik Samanta
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = TestUtils.UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(TestUtils.msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(TestUtils.msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(TestUtils.msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(TestUtils.msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", TestUtils.UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermute() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(2, p.realPermute(1));
        assertEquals(3, p.realPermute(2));
        assertEquals(0, p.realPermute(3));
        assertEquals(1, p.realPermute(0));
        Permutation q = new Permutation("", a);


    }
    @Test
    public void testWhiteSpace() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("", a);

    }
    @Test
    public void testPermuteChar() {
        Alphabet a = new Alphabet("ABCDE");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals('A', p.permute('B'));
        assertEquals('E', p.permute('E'));
        Alphabet b = new Alphabet("WXYZABCDJ");
        Permutation q = new Permutation("(ZW)(YX)(CDAB)", b);
        assertEquals('W', q.permute('Z'));
        assertEquals('Z', q.permute('W'));
        assertEquals('A', q.permute('D'));
        assertEquals('Y', q.permute('X'));
        assertEquals('C', q.permute('B'));
        assertEquals('D', q.permute('C'));
        assertEquals('J', q.permute('J'));
    }
    @Test
    public void testInvert() {
        Alphabet a = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals(0, p.realInvert(1));
        assertEquals(3, p.realInvert(0));
        assertEquals(2, p.realInvert(3));
        assertEquals(1, p.realInvert(2));
        Permutation q = new Permutation("", a);


    }
    @Test
    public void testInvertChar() {
        Alphabet a = new Alphabet("ABCDE");
        Permutation p = new Permutation("(BACD)", a);
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('C', p.invert('D'));
        assertEquals('A', p.invert('C'));
        assertEquals('E', p.invert('E'));
        Alphabet b = new Alphabet("WXYZABCDJ");
        Permutation q = new Permutation("(ZW)(YX)(CDAB)", b);
        assertEquals('Z', q.invert('W'));
        assertEquals('W', q.invert('Z'));
        assertEquals('C', q.invert('D'));
        assertEquals('A', q.invert('B'));
        assertEquals('B', q.invert('C'));
        assertEquals('J', q.invert('J'));
    }



}
