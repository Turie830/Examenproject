package alchemy.ingredients;

import alchemy.Name;
import alchemy.Temperature;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class of ingredient types.
 *
 * An ingredient type represents the substance of an alchemic ingredient.
 * All fixed properties of an alchemic ingredient are determined by its type.
 *
 * Ingredient types are implemented totally: invalid constructor arguments
 * are converted to valid default values.
 *
 * @invar The name of each ingredient type must be effective.
 *      | getName() != null
 *
 * @invar The standard state of each ingredient type must be effective.
 *      | getStandardState() != null
 *
 * @invar The standard temperature of each ingredient type must be effective.
 *      | getStandardTemperature() != null
 *
 * @invar The standard temperature of each ingredient type must be valid.
 *      | canHaveAsStandardTemperature(getStandardTemperature())
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 */
public class IngredientType {

    /**
     * A variable referencing the default ingredient type, water.
     */
    public static final IngredientType DEFAULT = new IngredientType("Water", State.LIQUID, new Temperature(0, 20), false);

    //ToDO: constructor voor dit






    /**
     * Variable referencing the name of this ingredient type.
     */
    private final Name name;
    /**
     * Initialize this new ingredient type with the given name.
     *
     * @param name
     *        The name for this new ingredient type.
     *
     * @post The name of this new ingredient type is equal to the given name.
     *     | new.getName().equals(name)
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid name.
     *       | !Name.isValidName(name)
     */
    public IngredientType(String name) {
        this.name = new Name(name);
    }



    /**
     * Return the name of this ingredient type.
     *
     * @return The name of this ingredient type.
     *       | result.equals(this.name.getName())
     */
    @Basic
    @Immutable
    public String getName() {
        return name.getName();
    }



    //ToDO: nog basically alles




}
