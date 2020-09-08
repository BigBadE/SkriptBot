package software.bigbade.skriptbot.utils;

import java.util.HashMap;

/**
 * Basic HTML unescaper, original from:
 * https://gist.github.com/MarkJeronimus/798c452582e64410db769933ec71cfb7
 *
 * @author Nick Frolov, Mark Jeronimus
 */
// Created 2020-06-22
public final class HTMLUtilities {
    // Tables optimized for smallest .class size (without resorting to compression)
    private static final String[] NAMES = {"gt", "lt", "quot"};
    private static final char[] CHARACTERS = {'<', '>', '"'};

    private static final HashMap<String, Character> LOOKUP_MAP;

    private static final int MIN_ESCAPE = 2;
    private static final int MAX_ESCAPE = 4;

    static {
        HashMap<String, Character> lookupMap = new HashMap<>(NAMES.length);

        for (int i = 0; i < CHARACTERS.length; i++) {
            lookupMap.put(NAMES[i], CHARACTERS[i]);
        }

        LOOKUP_MAP = lookupMap;
    }

    private HTMLUtilities() {
    }

    //TODO reduce cognitive complexity
    public static String unescapeHtml(String input) {
        StringBuilder result = new StringBuilder();

        int len = input.length();
        int start = 0;
        int escStart = 0;
        while (true) {
            // Look for '&'
            while (escStart < len && input.charAt(escStart) != '&') {
                escStart++;
            }

            if (escStart == len) {
                break;
            }

            escStart++;

            // Found '&'. Look for ';'
            int escEnd = escStart;
            while (escEnd < len && escEnd - escStart < MAX_ESCAPE + 1 && input.charAt(escEnd) != ';') {
                escEnd++;
            }

            if (escEnd == len) {
                break;
            }

            // Bail if this is not a potential HTML entity.
            if (escEnd - escStart < MIN_ESCAPE || escEnd - escStart == MAX_ESCAPE + 1) {
                escStart++;
                continue;
            }


            // Named entity
            Character codePoint = LOOKUP_MAP.get(input.substring(escStart, escEnd));
            if (codePoint == null) {
                escStart++;
                continue;
            }

            result.append(input, start, escStart - 1).append(codePoint);

            // Skip escape
            start = escEnd + 1;
            escStart = start;
        }

        result.append(input, start, len);
        return result.toString();
    }
}
