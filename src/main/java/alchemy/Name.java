package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;


/**
 * A class of names for alchemic ingredients.
 *
 * A name consists of one or more words separated by single spaces.
 * Each word may only contain letters and allowed special characters.
 * The allowed special characters are the apostrophe and round brackets.
 *
 * @invar The name of each Name object must be a valid regular name or a valid mixture name.
 *        | isValidName(getName()) || isValidMixtureName(getName())
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class Name {
    /**
     * Variable storing the actual name.
     */
    private final String name;

    /**
     * A string containing all allowed special characters in names.
     */
    private static final String ALLOWED_SPECIAL_CHARACTERS = "'()";

    /**
     * An array containing all words that may not occur as separate words
     * in the simple name of an alchemic ingredient.
     *
     * These words are reserved for prefixes or suffixes in full ingredient names.
     */
    private static final String[] FORBIDDEN_SIMPLE_NAME_WORDS = {
            "Heated",
            "Cooled"
            //ToDo: extra woorden??
    };

    /**
     * Initialize this new name with the given string.
     *
     * @param name
     *        The string for this new name.
     *
     * @effect This new name is initialized with the given name,
     *         where the words "mixed" and "with" are not allowed.
     *         | this(name, false)
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid name.
     *         | !isValidName(name)
     */
    public Name(String name) {
        this(name, false);
    }

    /**
     * Initialize this new name with the given string, optionally allowing
     * the words "mixed" and "with".
     *
     * @param name
     *        The string for this new name.
     *
     * @param allowMixedAndWith
     *        Whether the words "mixed" and "with" are allowed.
     *
     * @post The name of this new Name object is equal to the given name.
     *     | new.getName().equals(name)
     *
     * @throws IllegalArgumentException
     *         The given name is not valid, taking into account whether
     *         the words "mixed" and "with" are allowed.
     *       | !isValidName(name, allowMixedAndWith)
     */
    private Name(String name, boolean allowMixedAndWith) {
        if (!isValidName(name, allowMixedAndWith)) {
            throw new IllegalArgumentException("Invalid name.");
        }
        this.name = name;
    }

    /**
     * Return the string value of this name.
     *
     * @return The string value of this name.
     *       | result == this.name
     */
    @Basic
    @Immutable
    public String getName() {
        return name;
    }

    /**
     * Create a new mixture name with the given string.
     *
     * @param name
     *        The string for the new mixture name.
     *
     * @return A new Name object whose name is equal to the given name.
     *         | result.getName().equals(name)
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid mixture name.
     *         | !isValidMixtureName(name)
     */
    public static Name createMixtureName(String name) {
        if (!isValidMixtureName(name)) {
            throw new IllegalArgumentException("Invalid mixture name.");
        }
        return new Name(name, true);
    }

    /**
     * Check whether the given string is a valid name for a non-mixed ingredient
     * or for a special name of a mixed ingredient.
     *
     * In this kind of name, the words "mixed" and "with" are not allowed.
     *
     * @param name
     *        The string to check.
     *
     * @return True if and only if the given string is a valid name,
     *         where the words "mixed" and "with" are not allowed.
     *         | result == isValidName(name, false)
     */
    public static boolean isValidName(String name) {
        return isValidName(name, false);
    }

    /**
     * Check whether the given string is a valid simple name for a mixed ingredient.
     *
     * In this kind of name, the words "mixed" and "with" are allowed.
     *
     * @param name
     *        The string to check.
     *
     * @return True if and only if the given string is a valid mixture name,
     *         where the words "mixed" and "with" are allowed.
     *         | result == isValidName(name, true)
     */
    public static boolean isValidMixtureName(String name) {
        return isValidName(name, true);
    }

    /**
     * Check whether the given string is a valid name, taking into account
     * whether the words "mixed" and "with" are allowed.
     *
     * @param name
     *        The string to check.
     *
     * @param allowMixedAndWith
     *        Whether the words "mixed" and "with" are allowed.
     *
     * @return False if the given string is not effective.
     *       | if (name == null) then result == false
     *
     * @return False if the given string is blank.
     *       | if (name != null && name.isBlank()) then result == false
     *
     * @return False if the given string has spaces at the beginning or at the end.
     *       | if (name != null && !name.equals(name.trim())) then result == false
     *
     * @return False if the given string contains multiple consecutive spaces.
     *       | if (name != null && name.contains("  ")) then result == false
     *
     * @return False if the given string contains illegal characters.
     *       | if (name != null && !hasOnlyAllowedCharacters(name)) then result == false
     *
     * @return If the given string consists of one word, true if and only if
     *         that word contains at least three letters, is not a forbidden
     *         simple name word, and has correct capitalization.
     *
     * @return If the given string consists of more than one word, true if and only if
     *         every word contains at least two letters, is not a forbidden
     *         simple name word, and has correct capitalization.
     */
    @Model
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

    /**
     * Check whether the given string only contains allowed characters.
     *
     * Allowed characters are letters, spaces, and allowed special characters.
     *
     * @param name
     *        The string to check.
     *
     * @return True if and only if every character of the given string is either
     *         a letter, a space, or an allowed special character.
     */
    @Model
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

    /**
     * Check whether the given character is an allowed special character.
     *
     * @param c
     *        The character to check.
     *
     * @return True if and only if the given character occurs in the string
     *         of allowed special characters.
     *       | result == (ALLOWED_SPECIAL_CHARACTERS.indexOf(c) != -1)
     */
    @Model
    private static boolean isAllowedSpecialCharacter(char c) {
        // als Java het karakter niet vindt, geeft indexOf(c) altijd -1 terug.
        return ALLOWED_SPECIAL_CHARACTERS.indexOf(c) != -1;
    }

    /**
     * Count the number of letters in the given word.
     *
     * Allowed special characters do not count as letters.
     *
     * @param word
     *        The word whose letters must be counted.
     *
     * @return The number of characters in the given word that are letters.
     */
    @Model
    private static int countLetters(String word) {
        int count = 0;

        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetter(word.charAt(i))) {
                count++;
            }
        }
        return count;
    }



    /**
     * Check whether the given word has correct capitalization.
     *
     * The first actual letter of a regular word must be uppercase.
     * All other letters must be lowercase.
     * Allowed special characters are ignored for capitalization.
     *
     * The words "mixed" and "with" are only allowed if the given boolean
     * allowMixedAndWith is true. The words "Mixed" and "With" are never allowed.
     *
     * @param word
     *        The word to check.
     *
     * @param allowMixedAndWith
     *        Whether the words "mixed" and "with" are allowed.
     *
     * @return True if and only if the word has correct capitalization,
     *         taking into account whether "mixed" and "with" are allowed.
     */
    @Model
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


    /**
     * Check whether the given word is forbidden in the simple name of an
     * alchemic ingredient.
     *
     * @param word
     *        The word to check.
     *
     * @return True if and only if the given word occurs in the list of forbidden
     *         simple name words.
     *         | result == (for some forbiddenWord in FORBIDDEN_SIMPLE_NAME_WORDS :
     *         |              word.equals(forbiddenWord))
     */
    @Model
    private static boolean isForbiddenSimpleNameWord(String word) {
        for (String forbiddenWord : FORBIDDEN_SIMPLE_NAME_WORDS) {
            if (word.equals(forbiddenWord)) {
                return true;
            }
        }
        return false;
    }



















}
