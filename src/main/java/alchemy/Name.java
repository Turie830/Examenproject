package alchemy;

import alchemy.exceptions.IllegalNameException;
import alchemy.ingredients.AlchemicIngredient;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of names for alchemic ingredients.
 *
 * A name consists of one or more words separated by single spaces.
 * Each word may only contain letters and allowed special characters.
 * The allowed special characters are the apostrophe and round brackets.
 *
 * @invar The name of each Name object must be a valid regular name or a valid mixture name.
 *      | isValidName(getName()) || isValidMixtureName(getName())
 *
 * @invar The special name of each Name object must be proper.
 *      | canHaveAsSpecialName(getSpecialName())
 *
 * @note Name beheert zijn eigen toestand, AlchemicIngredient bevat de geldigheidsregels/checkers.
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.1
 */


public class Name {

    /**********************************************************
     * CLASS PROPERTIES
     **********************************************************/


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
            // TODO: Add extra prefix/suffix words when they are introduced.
    };

    /**
     * A variable referencing the default name, Water.
     *
     * @note We make this variable public because it is a constant and
     *       clients can use it freely, i.e. they cannot do illegal things with it.
     */
    public static final Name WATER = new Name("Water");


    /**********************************************************
     * CONSTRUCTORS
     **********************************************************/

    /**
     * Initialize this new name with the given string.
     *
     * The words "mixed" and "with" are not allowed.
     *
     * @param name
     *        The string for this new name.
     *
     * @effect This new name is initialized with the given name,
     *         where the words "mixed" and "with" are not allowed.
     *       | this(name, false)
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid regular name.
     *       | !isValidName(name)
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
     * @post This new Name object is mixed if and only if the words "mixed"
     *       and "with" were allowed.
     *     | new.isMixed() == allowMixedAndWith
     *
     * @post The special name of this new Name object is not effective.
     *     | new.getSpecialName() == null
     *
     * @throws IllegalArgumentException
     *         The given name is not valid, taking into account whether
     *         the words "mixed" and "with" are allowed.
     *       | !isValidName(name, allowMixedAndWith)
     */
    private Name(String name, boolean allowMixedAndWith) {
        if (!AlchemicIngredient.isValidName(name, allowMixedAndWith)) {
            throw new IllegalArgumentException("Invalid name.");
        }

        this.name = name;
        this.mixed = allowMixedAndWith;
        this.specialName = null;
    }

    /**
     * Create a new mixture name with the given string.
     *
     * @param name
     *        The string for the new mixture name.
     *
     * @return A new Name object whose name is equal to the given name.
     *       | result.getName().equals(name)
     *
     * @return The resulting Name object is mixed.
     *       | result.isMixed()
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid mixture name.
     *       | !isValidMixtureName(name)
     */
    public static Name createMixtureName(String name) {
        if (!AlchemicIngredient.isValidMixtureName(name)) {
            throw new IllegalArgumentException("Invalid mixture name.");
        }

        return new Name(name, true);
    }


    /**********************************************************
     * SIMPLE NAME
     **********************************************************/

    /**
     * Variable storing the actual simple name.
     */
    private final String name;

    /**
     * Variable registering whether this name is a mixture name.
     */
    private final boolean mixed;

    /**
     * Return the string value of this name.
     *
     * @return The string value of this name.
     *       | result == this.name
     */
    @Basic @Immutable
    public String getName() {
        return name;
    }

    /**
     * Return the simple name.
     *
     * @return The simple name.
     *       | result == getName()
     */
    @Immutable
    public String getSimpleName() {
        return getName();
    }

    /**
     * Check whether this name is a mixture name.
     *
     * @return True if and only if this name is a mixture name.
     *       | result == this.mixed
     */
    @Basic @Immutable
    public boolean isMixed() {
        return mixed;
    }


    /**********************************************************
     * SPECIAL NAME
     **********************************************************/

    /**
     * A variable referencing the special name of this name.
     */
    private String specialName;

    /**
     * Return the special name of this name.
     *
     * @return The special name of this name.
     *       | result == this.specialName
     */
    @Basic
    public String getSpecialName() {
        return specialName;
    }

    /**
     * Check whether this name has a special name.
     *
     * @return True if and only if the special name of this name is effective.
     *       | result == (getSpecialName() != null)
     */
    public boolean hasSpecialName() {
        return getSpecialName() != null;
    }

    /**
     * Set the special name of this name to the given special name.
     *
     * A special name is only allowed for mixture names.
     * The special name itself must satisfy the rules for a regular name.
     *
     * @param specialName
     *        The special name to set.
     *
     * @post If this name is mixed and the given special name is effective and valid,
     *       the special name of this name is equal to the given special name.
     *     | if (isMixed() && specialName != null && isValidName(specialName)) then
     *     |   new.getSpecialName().equals(specialName)
     *
     * @post If this name is mixed and the given special name is not effective,
     *       the special name of this name is not effective.
     *     | if (isMixed() && specialName == null) then
     *     |   new.getSpecialName() == null
     *
     * @throws IllegalStateException
     *         This name is not mixed and the given special name is effective.
     *       | !isMixed() && specialName != null
     *
     * @throws IllegalNameException
     *         The given special name is effective but not valid.
     *       | specialName != null && !isValidName(specialName)
     */
    @Raw
    @Model
    public void setSpecialName(String specialName) throws IllegalStateException, IllegalNameException {
        if (!isMixed() && specialName != null) {
            throw new IllegalStateException("No special name allowed!");
        }

        if (specialName != null && !AlchemicIngredient.isValidName(specialName)) {
            throw new IllegalNameException(specialName);
        }

        this.specialName = specialName;
    }

    /**********************************************************
     * VALIDATION
     **********************************************************/

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
    public static boolean hasOnlyAllowedCharacters(String name) {
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
    public static boolean isAllowedSpecialCharacter(char c) {
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
    public static int countLetters(String word) {
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
    public static boolean hasCorrectCapitalization(String word, boolean allowMixedAndWith) {
        if (word.equals("mixed") || word.equals("with")) {
            return allowMixedAndWith;
        }

        if (word.equals("Mixed") || word.equals("With")) {
            return false;
        }

        boolean foundFirstLetter = false;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            if (isAllowedSpecialCharacter(c)) {
                continue;
            }

            if (!Character.isLetter(c)) {
                return false;
            }

            if (!foundFirstLetter) {
                if (!Character.isUpperCase(c)) {
                    return false;
                }

                foundFirstLetter = true;
            } else {
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
     *       | result == (for some forbiddenWord in FORBIDDEN_SIMPLE_NAME_WORDS :
     *       |              word.equals(forbiddenWord))
     */
    @Model
    public static boolean isForbiddenSimpleNameWord(String word) {
        for (String forbiddenWord : FORBIDDEN_SIMPLE_NAME_WORDS) {
            if (word.equals(forbiddenWord)) {
                return true;
            }
        }

        return false;
    }








    //ToDO: volledige naam



}
