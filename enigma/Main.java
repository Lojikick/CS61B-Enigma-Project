package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Avik Samanta
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        int line = 1;
        try {
            Machine currMach = readConfig();
            while (_input.hasNextLine()) {
                String curr = _input.nextLine();
                if (line > 1 && (curr.equals(""))) {
                    if (!(_input.hasNextLine())) {
                        break;
                    }
                    _output.println();
                } else if (line == 1 || curr.charAt(0) == '*') {
                    setUp(currMach, curr);
                } else {
                    String messageLine = curr;
                    for (int i = 0; i < messageLine.length(); i++) {
                        char current = messageLine.charAt(i);
                        int test = currMach.alphabet().toInt(current);
                        if (current != ')' && current != '(') {
                            if (current != ' ') {
                                if ((test == -1)) {
                                    throw new NoSuchElementException();
                                }
                            }
                        }
                    }
                    printMessageLine(messageLine, currMach);
                }
                line++;
            }
        } catch (NoSuchElementException excp) {
            throw error("input file not ballin");
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            Alphabet alpha = new Alphabet();
            ArrayList<Rotor> rotors = new ArrayList<>();
            int pawls = 0;
            int numRotors = 0;
            int line = 1;
            while (_config.hasNextLine()) {

                if (line == 1) {
                    Scanner curr = new Scanner(_config.nextLine());
                    String temp = curr.next();
                    alpha = new Alphabet(temp);
                } else if (line == 2) {
                    Scanner curr = new Scanner(_config.nextLine());
                    numRotors = (curr.nextInt());
                    pawls = (curr.nextInt());
                } else {
                    String settingLine = _config.nextLine();
                    if (settingLine.isBlank()) {
                        if ((!_config.hasNextLine())) {
                            break;
                        } else {
                            settingLine = _config.nextLine();
                        }
                    }
                    Scanner tempest = new Scanner(settingLine);
                    String checker = tempest.next();
                    char check = checker.charAt(0);
                    if (check == '(') {
                        Rotor temp = rotors.get(rotors.size() - 1);
                        Permutation perm = temp.getPerm();
                        perm.addCycle(settingLine);
                    } else {
                        Rotor temp = readRotor(settingLine, alpha);
                        rotors.add(temp);
                    }
                }
                line++;
            }
            return new Machine(alpha, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /** Return a rotor, reading its description from _config.
     * @param settings
     * @param alpha
     * */
    private Rotor readRotor(String settings, Alphabet alpha) {
        try {
            Scanner set = new Scanner(settings);
            String name = set.next();
            String typeNotch = set.next();
            char type = (typeNotch.charAt(0));
            String notches = "";
            if (typeNotch.length() > 1) {
                notches = typeNotch.substring(1, typeNotch.length());
            }
            String cycles = "";
            while (set.hasNext()) {
                String temp = set.next();
                for (int i = 0; i < temp.length(); i++) {
                    char curr = temp.charAt(i);
                }
                cycles += temp;
            }
            Permutation perm = new Permutation(cycles, alpha);
            Rotor val = new Rotor(name, perm);
            if (type == 'M') {
                val = new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                val = new FixedRotor(name, perm);
            } else if (type == 'R') {
                val = new Reflector(name, perm);
            }
            return val;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            if (settings.charAt(0) != '*') {
                throw new NoSuchElementException();
            }
            Scanner set = new Scanner(settings);
            if (settings.length() < M._slots.length) {
                throw new NoSuchElementException();
            }
            set.next();
            String[] rotors = new String[M._slots.length];
            for (int i = 0; i < M._slots.length; i++) {
                String unique = set.next();
                rotors[i] = unique;
            }
            for (int i = 0; i < rotors.length; i++) {
                String curr = rotors[i];
                int dex = i;
                for (int j = 0; j < rotors.length; j++) {
                    if (curr == rotors[j] && i != j) {
                        throw new NoSuchElementException();
                    }
                }
            }
            M.insertRotors(rotors);
            if (!(M._slots[0] instanceof Reflector)) {
                throw new NoSuchElementException();
            }
            String setter = set.next();
            if (setter.length() != rotors.length - 1) {
                throw new NoSuchElementException();
            }
            for (int i = 0; i < setter.length(); i++) {
                if (M.alphabet().toInt(setter.charAt(i)) == -1) {
                    throw new NoSuchElementException();
                }
            }
            M.setRotors(setter);
            if (set.hasNext()) {
                String pPerms = "";
                while (set.hasNext()) {
                    {
                        pPerms += set.next();
                    }
                }
                Permutation perm = new Permutation(pPerms, M.alphabet());
                M.setPlugboard(perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("NOT BALLIN MM MM");
        }
    }
    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters).
     *  @param mach
     *  @param msg
     *  */
    private void printMessageLine(String msg, Machine mach) {
        Scanner set = new Scanner(msg);
        String result = "";
        int counter = 0;
        while (set.hasNext()) {
            String curr = set.next();
            curr = mach.convert(curr);
            for (int i = 0; i < curr.length(); i++) {
                if (counter == 5) {
                    result += " ";
                    counter = 0;
                }
                result += curr.charAt(i);
                counter++;
            }
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
