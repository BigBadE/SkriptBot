package software.bigbade.skriptbot.utils;

public final class StringUtils {
    private StringUtils() {}

    public static boolean matchesArray(String string, String[] array) {
        for(String found : array) {
            if(found.equals(string)) {
                return true;
            }
        }
        return false;
    }
}
