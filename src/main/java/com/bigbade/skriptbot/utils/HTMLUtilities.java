package com.bigbade.skriptbot.utils;

import java.util.HashMap;

public final class HTMLUtilities {
    private static final String[] NAMES = {"gt", "lt", "quot"};
    private static final char[] CHARACTERS = {'>', '<', '"'};

    private static final HashMap<String, Character> LOOKUP_MAP;

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

    public static String unescapeHtml(String input) {
        StringBuilder result = new StringBuilder();

        int i = -1;
        char current;
        while(++i < input.length()) {
            current = input.charAt(i);
            if(current != '&') {
                result.append(current);
                continue;
            }
            StringBuilder found = new StringBuilder();
            int j = 0;
            while(i < input.length()-1 && j++ <= MAX_ESCAPE && (current = input.charAt(++i)) != ';') {
                found.append(current);
            }
            Character output = LOOKUP_MAP.get(found.toString());
            if(output == null) {
                result.append('&').append(found);
                if(input.charAt(i) == ';') {
                    result.append(';');
                }
            } else {
                result.append(output);
            }
        }

        return result.toString();
    }
}
