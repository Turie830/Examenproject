package alchemy.ingredients;

public class AlchemicIngredient {
    private String name;

    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }

        if (!hasOnlyAllowedCharacters(name)) {
            return false;
        }

        String[] words = name.split(" ");

        if (words.length == 1) {
            return countLetters(words[0]) >= 3 && hasCorrectCapitalization(words[0]);
        }

        for (String word : words) {
            if (countLetters(word) < 2) {
                return false;
            }
            if (!hasCorrectCapitalization(word)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasOnlyAllowedCharacters(String name) {
        for (int i = 0; i < name.length(); i++) {

            char c = name.charAt(i);

            if (!Character.isLetter(c)
                    && c != ' '
                    && !isAllowedSpecialCharacter(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllowedSpecialCharacter(char c) {
        return c == '\''
                || c == '('
                || c == ')';
    }

    private static int countLetters(String word) {
        int count = 0;
        // count gaat 1 omhoog als de char een letter is.
        // Toegelaten speciale tekens tellen niet als letter.
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetter(word.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    private static boolean hasCorrectCapitalization(String word) {
        // uitzonderingen
        if (word.equals("mixed") || word.equals("with")) {
            return true;
        }
        // de uitzonderingen mogen niet met hoofdletters geschreven worden
        if (word.equals("Mixed") || word.equals("With")) {
            return false;
        }
        boolean foundFirstLetter = false;

        for (int i = 0; i < word.length(); i++) {

            char c = word.charAt(i);

            if (isAllowedSpecialCharacter(c)) {
                continue;   // gaat naar de volgende i in de loop
            }

            if (!Character.isLetter(c)) {
                return false;
            }
            // Als we nog geen eerste letter gevonden hadden, dan moet deze letter een hoofdletter zijn.
            // Daarna zetten we foundFirstLetter op true.
            if (!foundFirstLetter) {
                if (!Character.isUpperCase(c)) {
                    return false;
                }
                foundFirstLetter = true;
            }
            else {
                if (!Character.isLowerCase(c)) {
                    return false;
                }
            }
        }
        return foundFirstLetter;
    }
















}
