package alchemy.ingredients;

import alchemy.Name;
import alchemy.Unit;
import alchemy.Temperature;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class of alchemic ingredients.
 *
 * An alchemic ingredient has a simple name and a full name.
 *
 * @invar The simple name of each alchemic ingredient must be effective.
 *      | getSimpleName() != null
 *
 * @invar The type of each alchemic ingredient must be effective.
 *      | getType() != null
 *
 * @invar The quantity of each alchemic ingredient must be effective.
 *      | getQuantity() != null
 *
 *
 *
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class AlchemicIngredient {

    /**
     * Variable referencing the type of this alchemic ingredient.
     */
    private final IngredientType type;
    /**
     * Variable referencing the current temperature of this alchemic ingredient.
     */
    private final Temperature temperature;
    /**
     * Variable referencing the quantity of this alchemic ingredient.
     */
    private final Quantity quantity;



    /**
     * Initialize this new alchemic ingredient with the given ingredient type.
     *
     * If the given type is not effective, the default ingredient type is used.
     *
     * @param type
     *        The ingredient type for this new alchemic ingredient.
     *
     * @param quantity
     *        The quantity for this new alchemic ingredient
     *
     * @post If the given type is effective, the type of this new alchemic ingredient
     *       is equal to the given type.
     *     | if (type != null) then new.getType() == type
     *
     * @post If the given type is not effective, the type of this new alchemic ingredient
     *       is equal to the default ingredient type.
     *     | if (type == null) then new.getType() == IngredientType.DEFAULT
     *
     * @post The temperature of this new alchemic ingredient is equal to the
     *       standard temperature of its type.
     *     | new.getTemperature()[0] == new.getType().getStandardTemperature()[0]
     *     | && new.getTemperature()[1] == new.getType().getStandardTemperature()[1]
     */
    public AlchemicIngredient(IngredientType type, Quantity quantity) {
        if (type == null) {
            this.type = IngredientType.DEFAULT;
        }
        else {
            this.type = type;
        }
        this.temperature = this.type.getStandardTemperatureObject();

        if (quantity == null) {
            this.quantity = new Quantity(0L, Unit.getSpoonUnit(this.type.getStandardState()));
        }
        else {
            this.quantity = quantity;
        }
    }

    /**
     * Initialize this new alchemic ingredient with the given ingredient type
     * and a default quantity.
     *
     * @param type
     *        The ingredient type for this new alchemic ingredient
     *
     * @effect This new alchemic ingredient is initialized with the given type and
     *         a default quantity.
     *       | this(type, null)
     */
    public AlchemicIngredient(IngredientType type) {
        this(type, null);
    }

    /**
     * Initialize this new alchemic ingredient with the default ingredient type.

     *
     * @effect This new alchemic ingredient is initialized with the default type.
     *       | this(IngredientType.DEFAULT)
     */
    public AlchemicIngredient() {
        this(IngredientType.DEFAULT);
    }


    /**
     * Return the type of this alchemic ingredient.
     *
     * @return The type of this alchemic ingredient.
     *       | result == this.type
     */
    @Basic @Immutable
    public IngredientType getType() {
        return type;
    }

    /**
     * Return the quantity of this alchemic ingredient.
     *
     * @return The quantity of this alchemic ingredient.
     *       | result == this.quantity
     */
    @Basic @Immutable
    public Quantity getQuantity() {
        return quantity;
    }


    /**
     * Return the amount of this alchemic ingredient.
     *
     * @return The amount of this alchemic ingredient.
     *       | result == getQuantity().getAmount()
     */
    @Basic @Immutable
    public long getAmount() {
        return getQuantity().getAmount();
    }


    /**
     * Return the unit of this alchemic ingredient.
     *
     * @return The unit of this alchemic ingredient.
     *       | result == getQuantity().getUnit()
     */
    @Basic @Immutable
    public Unit getUnit() {
        return getQuantity().getUnit();
    }


    /**
     * Return the amount of this alchemic ingredient in the lowest unit
     * of its standard state.
     *
     * @return The amount of this alchemic ingredient in the lowest unit.
     *       | result == getQuantity().toLowestUnit(getType().getStandardState())
     */
    public Long getAmountInLowestUnit() {
        return getQuantity().toLowestUnit(getType().getStandardState());
    }

    /**
     * Return the amount of this alchemic ingredient in spoons.
     *
     * @return The largest whole number of spoons contained in this ingredient.
     *       | result == getQuantity().toSpoons(getType().getStandardState())
     */
    public long getAmountInSpoons() {
        return getQuantity().toSpoons(getType().getStandardState());
    }


    /**
     * Check whether this name can have the given special name.
     *
     * @param specialName
     *        The special name to check.
     *
     * @return True if and only if the given special name is not effective,
     *         or this name is mixed and the given special name is a valid regular name.
     *       | result ==
     *       |   specialName == null
     *       |   || (isMixed() && isValidName(specialName))
     */
    public static boolean canHaveAsSpecialName(Name name, String specialName) {
        return specialName == null
                || (name.isMixed() && AlchemicIngredient.isValidName(specialName));
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
     *       | result == isValidName(name, false)
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
     *       | result == isValidName(name, true)
     */
    public static boolean isValidMixtureName(String name) {
        return isValidName(name, true);
        //ToDO: dit klopt niet
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
    public static boolean isValidName(String name, boolean allowMixedAndWith) {
        if (name == null) {
            return false;
        }

        if (name.isBlank()) {
            return false;
        }

        if (!name.equals(name.trim())) {
            return false;
        }

        if (name.contains("  ")) {
            return false;
        }

        if (!Name.hasOnlyAllowedCharacters(name)) {
            return false;
        }

        String[] words = name.split(" ");

        if (words.length == 1) {
            return !Name.isForbiddenSimpleNameWord(words[0])
                    && Name.countLetters(words[0]) >= 3
                    && Name.hasCorrectCapitalization(words[0], allowMixedAndWith);
        }

        for (String word : words) {
            if (Name.isForbiddenSimpleNameWord(word)) {
                return false;
            }

            if (Name.countLetters(word) < 2) {
                return false;
            }

            if (!Name.hasCorrectCapitalization(word, allowMixedAndWith)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Return the simple name of this alchemic ingredient.
     *
     * @return The simple name of this alchemic ingredient.
     *       | result.equals(getType().getSimpleName())
     */
    @Basic
    @Immutable
    public String getSimpleName() {
        return getType().getSimpleName();
    }



    /**
     * Return the full name of this alchemic ingredient.
     *
     * If this ingredient is hotter than its standard temperature, the full name
     * starts with "Heated". If it is colder, the full name starts with "Cooled".
     * If this ingredient has a special name, that special name is followed by
     * the full regular name between round brackets.
     *
     * @return The full name of this alchemic ingredient.
     */
    public String getFullName() {
        String baseName = getTemperaturePrefix() + getSimpleName();
        if (type.isMixed()
                && type.getName().hasSpecialName()) {
            return type.getName().getSpecialName() + " (" + baseName + ")";
        }
        return baseName;
    }


    /**
     * Return the coldness of this alchemic ingredient.
     *
     * @return The coldness of this alchemic ingredient.
     *       | result == getTemperatureObject().getColdness()
     */
    @Basic
    public long getColdness() {
        return temperature.getColdness();
    }

    /**
     * Return the hotness of this alchemic ingredient.
     *
     * @return The hotness of this alchemic ingredient.
     *       | result == getTemperatureObject().getHotness()
     */
    @Basic
    public long getHotness() {
        return temperature.getHotness();
    }


    /**
     * Return the temperature of this alchemic ingredient.
     *
     * The first element is the coldness, the second element is the hotness.
     *
     * @return The temperature of this alchemic ingredient.
     *       | result[0] == getColdness()
     *       | && result[1] == getHotness()
     */
    public long[] getTemperature() {
        return temperature.getTemperature();
    }


    /**
     * Return a copy of the temperature object of this alchemic ingredient.
     *
     * @return A copy of the temperature object of this alchemic ingredient.
     *       | result.getColdness() == getColdness()
     *       | && result.getHotness() == getHotness()
     */
    protected Temperature getTemperatureObject() {
        return new Temperature(temperature);
    }


    /**
     * Heat this alchemic ingredient with the given amount.
     *
     * @param amount
     *        The amount of heat to add.
     *
     * @effect The temperature of this ingredient is heated with the given amount.
     *       | getTemperatureObject().heat(amount)
     */
    public void heat(long amount) {
        temperature.heat(amount);
    }


    /**
     * Cool this alchemic ingredient with the given amount.
     *
     * @param amount
     *        The amount of coldness to add.
     *
     * @effect The temperature of this ingredient is cooled with the given amount.
     *       | getTemperatureObject().cool(amount)
     */
    public void cool(long amount) {
        temperature.cool(amount);
    }


    /**
     * Return the temperature prefix for the full name of this alchemic ingredient.
     *
     * @return An empty string if this ingredient has its standard temperature.
     * @return "Heated " if this ingredient is hotter than its standard temperature.
     * @return "Cooled " if this ingredient is colder than its standard temperature.
     */
    private String getTemperaturePrefix() {
        long difference = type.getStandardTemperatureDifference(temperature);
        if (difference == 0) {
            return "";
        }
        if (temperature.isHotterThan(type.getStandardTemperatureObject())) {
            return "Heated ";
        }
        return "Cooled ";
    }



}
