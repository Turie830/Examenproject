package alchemy;

public class Name {
    private final String name;

    private static final String ALLOWED_SPECIAL_CHARACTERS = "'()";

    public static boolean isValidName(String name) {
        return isValidName(name, false);
    }

    public static boolean isValidMixtureName(String name) {
        return isValidName(name, true);
    }


    public Name(String name) {
        this(name, false);
    }

    public String getName() {
        return name;
    }


    public static Name createMixtureName(String name) {
        if (!isValidMixtureName(name)) {
            throw new IllegalArgumentException("Invalid mixture name.");
        }
        return new Name(name, true);
    }

    private Name(String name, boolean allowMixedAndWith) {
        if (!isValidName(name, allowMixedAndWith)) {
            throw new IllegalArgumentException("Invalid name.");
        }
        this.name = name;
    }






    private static boolean isValidName(String name, boolean allowMixedAndWith) {
        if (name == null) {
            return false;
        }
        // geen blank naam
        if (name.isBlank()) {
            return false;
        }
        // er mogen geen spaties voor of achter een woord (natuurlijk wel als het een spatie is tussen 2 woorden)
        if (!name.equals(name.trim())) {
            return false;
        }
        // dubbele spaties mogen niet
        if (name.contains("  ")) {
            return false;
        }
        if (!hasOnlyAllowedCharacters(name)) {
            return false;
        }

        String[] words = name.split(" ");

        if (words.length == 1) {
            return !isForbiddenSimpleNameWord(words[0])
                    && countLetters(words[0]) >= 3
                    && hasCorrectCapitalization(words[0], allowMixedAndWith);
        }

        for (String word : words) {
            if (isForbiddenSimpleNameWord(word)) {
                return false;
            }
            if (countLetters(word) < 2) {
                return false;
            }
            if (!hasCorrectCapitalization(word, allowMixedAndWith)) {
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
        // als Java het karakter niet vindt, geeft indexOf(c) altijd -1 terug.
        return ALLOWED_SPECIAL_CHARACTERS.indexOf(c) != -1;
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

    private static boolean hasCorrectCapitalization(String word, boolean allowMixedAndWith) {
        // mixed en with zijn niet toegelaten in gewone namen, wel in mixtures
        if (word.equals("mixed") || word.equals("with")) {
            return allowMixedAndWith;
        }
        // met hoofdletters mag nooit
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

    private static boolean isForbiddenSimpleNameWord(String word) {
        return word.equals("Heated")
                || word.equals("Cooled");
    }




















}
