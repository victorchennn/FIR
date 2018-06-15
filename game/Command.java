package game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** All things to do with parsing commands.
 *  @author Victor
 */
public class Command {

    /** Command types.  PIECE indicates a new piece of the form
     *  %d, %d.  ERROR indicates a parse error in the command.
     *  All other commands are upper-case versions of what the
     *  programmer writes. */
     static enum Type {

        /* Start-up state only. */
        AUTO(""),
        MANUAL(""),
        START,
        SETBOARD(""),
        /* Regular pieces (set-up or play) */
        PIECE("[1-15],[1-15]"),
        /* Valid at any time. */
        QUIT, STATUS, CLEAR, PRINT, HELP,
        /* Special "commands" internally generated. */
        /** Syntax error in command. */
        ERROR(".*"),
        /** End of input stream. */
        EOF;

        /** PATTERN is a regular expression string giving the syntax of
         *  a command of the given type.  It matches the entire command,
         *  assuming no leading or trailing whitespace.  The groups in
         *  the pattern capture the operands (if any). */
        Type(String pattern) {
            _pattern = Pattern.compile(pattern + "$");
        }

        /** A Type whose pattern is the lower-case version of its name. */
        Type() {
            _pattern = Pattern.compile(this.toString().toLowerCase());
        }

        /** The Pattern describing syntactically correct versions of this
         *  type of command. */
        private final Pattern _pattern;
    }

    /** A new Command of type TYPE with OPERANDS as its operands. */
    Command(Type type, String... operands) {
        _type = type;
        _operands = operands;
    }

    /** Parse COMMAND, returning the command and its operands. */
    static Command parseCommand(String command) {
        if (command == null) {
            return new Command(Type.EOF);
        }
        command = command.trim();
        for (Type type : Type.values()) {
            Matcher matcher = type._pattern.matcher(command);
            if (matcher.matches()) {
                String[] operands = new String[matcher.groupCount()];
                for (int i = 1; i <= operands.length; i++) {
                    operands[i - 1] = matcher.group(i);
                }
                return new Command(type, operands);
            }
        }
        throw new Error("Impossible");
    }

    /** Return the type of this Command. */
    Type commandType() {
        return _type;
    }

    /** Returns this Command's operands. */
    String[] operands() {
        return _operands;
    }

    /** The command name. */
    private final Type _type;

    /** Command arguments. */
    private final String[] _operands;
}
