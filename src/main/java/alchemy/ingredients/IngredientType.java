package alchemy.ingredients;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.exceptions.IllegalNameException;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of ingredient types.
 *
 * An ingredient type represents the substance of an alchemic ingredient.
 * All fixed properties of an alchemic ingredient are determined by its type.
 *
 * The name of an ingredient type is implemented defensively.
 * The standard state and standard temperature are implemented totally.
 *
 * @invar The name of each ingredient type must be proper.
 *      | canHaveAsName(getName())
 *
 * @invar The standard state of each ingredient type must be valid.
 *      | isValidState(getStandardState())
 *
 * @invar The standard temperature of each ingredient type must be valid.
 *      | canHaveAsStandardTemperature(getStandardTemperatureObject())
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class IngredientType {

    /**********************************************************
     * CLASS PROPERTIES
     **********************************************************/

    /**
     * A variable referencing the default ingredient type, Water.
     */
    public static final IngredientType DEFAULT =
            new IngredientType(Name.WATER, State.LIQUID, new Temperature(0, 20), false);


    /**********************************************************
     * CONSTRUCTORS
     **********************************************************/

    /**
     * Create a new regular ingredient type with the given name, standard state
     * and standard temperature.
     *
     * The name is handled defensively: an invalid name results in an exception.
     * The standard state and standard temperature are handled totally: invalid
     * values are replaced by default values.
     *
     * @param name
     *        The name of this new ingredient type.
     *
     * @param standardState
     *        The standard state of this new ingredient type.
     *
     * @param standardTemperature
     *        The standard temperature of this new ingredient type.
     *
     * @post If the given standard state is valid, the standard state of this new
     *       ingredient type is equal to the given standard state.
     *     | if (isValidState(standardState)) then
     *     |   new.getStandardState() == standardState
     *
     * @post If the given standard state is not valid, the standard state of this new
     *       ingredient type is equal to the default standard state.
     *     | if (!isValidState(standardState)) then
     *     |   new.getStandardState() == DEFAULT.getStandardState()
     *
     * @throws IllegalNameException
     *         The given name is not a proper name for this ingredient type.
     *       | !canHaveAsName(name)
     */
    @Raw
    public IngredientType(Name name, State standardState, Temperature standardTemperature) {
        this(name, standardState, standardTemperature, true);
    }

    /**
     * Initialize this new ingredient type with the given name, standard state,
     * standard temperature and mixed state.
     *
     * This constructor exists to create DEFAULT without circular initialization.
     *
     * @param name
     *        The name of this new ingredient type.
     *
     * @param standardState
     *        The standard state of this new ingredient type.
     *
     * @param standardTemperature
     *        The standard temperature of this new ingredient type.
     *
     * @param useDefaultOnInvalid
     *        Whether invalid state and temperature values should be replaced
     *        by values from the default ingredient type.
     *
     * @throws IllegalNameException
     *         The given name is not a proper name for this ingredient type.
     *       | !canHaveAsName(name)
     */
    @Raw
    protected IngredientType(Name name, State standardState, Temperature standardTemperature, boolean useDefaultOnInvalid) {
        setName(name);

        this.standardState = getValidStateOrDefault(standardState, useDefaultOnInvalid);
        this.standardTemperature = getValidTemperatureOrDefault(standardTemperature, useDefaultOnInvalid);
    }

    /**
     * Initialize this new ingredient type as a regular, non-mixed ingredient type
     * with the given name.
     *
     * The standard state and standard temperature are taken from the default ingredient type.
     *
     * @param name
     *        The name of this new ingredient type.
     *
     * @effect This new ingredient type is initialized with the given name,
     *         the default standard state, the default standard temperature
     *         and mixed state false.
     *       | this(name, DEFAULT.getStandardState(), DEFAULT.getStandardTemperatureObject(), false)
     *
     * @throws IllegalNameException
     *         The given name is not a proper regular name.
     *       | !canHaveAsName(name)
     */
    public IngredientType(Name name) {
        this(name, DEFAULT.getStandardState(), DEFAULT.getStandardTemperatureObject());
    }

    /**
     * Initialize this new regular ingredient type with the given name and
     * standard temperature.
     *
     * The standard state is liquid.
     *
     * @param name
     *        The name of this new ingredient type.
     *
     * @param standardTemperature
     *        The standard temperature of this new ingredient type.
     *
     * @effect This new ingredient type is initialized with the given name,
     *         liquid as standard state, and the given standard temperature.
     *       | this(name, State.LIQUID, standardTemperature)
     *
     * @throws IllegalNameException
     *         The given name is not a proper name for this ingredient type.
     *       | !canHaveAsName(name)
     */
    public IngredientType(Name name, Temperature standardTemperature) {
        this(name, State.LIQUID, standardTemperature);
    }

    /**********************************************************
     * MIXED
     **********************************************************/

    /**
     * Check whether this ingredient type is mixed.
     *
     * A regular ingredient type is not mixed.
     *
     * @return False.
     *       | result == false
     */
    @Basic @Immutable
    public boolean isMixed() {
        return false;
    }


    /**********************************************************
     * NAME - DEFENSIVE
     **********************************************************/

    /**
     * Variable referencing the name of this ingredient type.
     */
    private Name name;

    /**
     * Return the name of this ingredient type.
     *
     * @return The name of this ingredient type.
     *       | result == this.name
     */
    @Basic
    public Name getName() {
        return name;
    }

    /**
     * Return the simple name of this ingredient type.
     *
     * @return The simple name of this ingredient type.
     *       | result.equals(getName().getSimpleName())
     */
    public String getSimpleName() {
        return getName().getSimpleName();
    }

    /**
     * Set the name of this ingredient type.
     *
     * This method is defensive: if the given name is not proper, an exception is thrown.
     *
     * @param name
     *        The name to set.
     *
     * @post The name of this ingredient type is equal to the given name.
     *     | new.getName() == name
     *
     * @throws IllegalArgumentException
     *         The given name is not proper for this ingredient type.
     *       | !canHaveAsName(name)
     */
    @Raw @Model
    private void setName(Name name) {
        if (!canHaveAsName(name)) {
            throw new IllegalNameException("Invalid name for ingredient type.");
        }

        this.name = name;
    }

    /**
     * Check whether the given name can be used as the name of this ingredient type.
     *
     * @param name
     *        The name to check.
     *
     * @return True if and only if the given name is effective and has the same
     *         mixed state as this ingredient type.
     *       | result == (name != null && name.isMixed() == isMixed())
     */
    @Raw
    public boolean canHaveAsName(Name name) {
        return name != null && name.isMixed() == isMixed();
    }


    /**********************************************************
     * STATE - TOTAL
     **********************************************************/

    /**
     * Variable storing the standard state of this ingredient type.
     */
    private final State standardState;

    /**
     * Return the standard state of this ingredient type.
     *
     * @return The standard state of this ingredient type.
     *       | result == this.standardState
     */
    @Basic @Immutable
    public State getStandardState() {
        return standardState;
    }

    /**
     * Check whether the given state is a valid state for an ingredient type.
     *
     * @param state
     *        The state to check.
     *
     * @return True if and only if the given state is effective.
     *       | result == (state != null)
     */
    @Raw
    public static boolean isValidState(State state) {
        return state != null;
    }

    /**
     * Return the given state if it is valid, otherwise return a default state.
     *
     * @param state
     *        The state to check.
     *
     * @param useDefaultOnInvalid
     *        Whether the default ingredient type may be used.
     *
     * @return If the given state is valid, the result is the given state.
     *       | if (isValidState(state)) then result == state
     *
     * @return If the given state is not valid and defaults may be used, the result
     *         is the default standard state.
     *       | if (!isValidState(state) && useDefaultOnInvalid) then result == DEFAULT.getStandardState()
     *
     * @return If the given state is not valid and defaults may not be used, the result
     *         is liquid.
     *       | if (!isValidState(state) && !useDefaultOnInvalid) then result == State.LIQUID
     */
    @Model
    private static State getValidStateOrDefault(State state, boolean useDefaultOnInvalid) {
        if (isValidState(state)) {
            return state;
        }

        if (useDefaultOnInvalid) {
            return DEFAULT.getStandardState();
        }

        return State.LIQUID;
    }


    /**********************************************************
     * TEMPERATURE - TOTAL
     **********************************************************/

    /**
     * Variable referencing the standard temperature of this ingredient type.
     */
    private final Temperature standardTemperature;

    /**
     * Return the standard temperature of this ingredient type.
     *
     * The first element is the coldness, the second element is the hotness.
     *
     * @return The standard temperature of this ingredient type as an array.
     *       | result[0] == getStandardTemperatureObject().getColdness()
     *       | && result[1] == getStandardTemperatureObject().getHotness()
     */
    @Basic
    public long[] getStandardTemperature() {
        return getStandardTemperatureObject().getTemperature();
    }

    /**
     * Return the standard temperature object of this ingredient type.
     *
     * A copy is returned so that clients cannot modify the internal standard temperature.
     *
     * @return A copy of the standard temperature object of this ingredient type.
     *       | result.getColdness() == this.standardTemperature.getColdness()
     *       | && result.getHotness() == this.standardTemperature.getHotness()
     */
    @Model
    protected Temperature getStandardTemperatureObject() {
        return new Temperature(standardTemperature);
    }

    /**
     * Return the difference between the given temperature and the standard temperature
     * of this ingredient type.
     *
     * @param temperature
     *        The temperature to compare with.
     *
     * @return The difference between the given temperature and the standard temperature.
     *       | result == getStandardTemperatureObject().difference(temperature)
     */
    public long getStandardTemperatureDifference(Temperature temperature) {
        return getStandardTemperatureObject().difference(temperature);
    }

    /**
     * Check whether the given temperature can be used as a standard temperature.
     *
     * Standard temperatures must always be strictly warmer than [0, 0].
     *
     * @param temperature
     *        The temperature to check.
     *
     * @return True if and only if the given temperature is effective,
     *         has no coldness and has strictly positive hotness.
     *       | result ==
     *       |   temperature != null
     *       |   && temperature.getColdness() == 0
     *       |   && temperature.getHotness() > 0
     */
    @Raw
    public static boolean canHaveAsStandardTemperature(Temperature temperature) {
        return temperature != null
                && temperature.getColdness() == 0
                && temperature.getHotness() > 0;
    }

    /**
     * Return the given temperature if it is a valid standard temperature,
     * otherwise return a default standard temperature.
     *
     * A copy is always returned to avoid sharing mutable Temperature objects.
     *
     * @param temperature
     *        The temperature to check.
     *
     * @param useDefaultOnInvalid
     *        Whether the default ingredient type may be used.
     *
     * @return If the given temperature is a valid standard temperature,
     *         the result has the same coldness and hotness as the given temperature.
     *
     * @return If the given temperature is not valid and defaults may be used,
     *         the result has the same coldness and hotness as the default standard temperature.
     *
     * @return If the given temperature is not valid and defaults may not be used,
     *         the result is [0, 20].
     */
    @Model
    private static Temperature getValidTemperatureOrDefault(Temperature temperature, boolean useDefaultOnInvalid) {
        if (canHaveAsStandardTemperature(temperature)) {
            return new Temperature(temperature);
        }

        if (useDefaultOnInvalid) {
            return DEFAULT.getStandardTemperatureObject();
        }

        return new Temperature(0, 20);
    }


}