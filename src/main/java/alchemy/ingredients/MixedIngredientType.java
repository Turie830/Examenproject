package alchemy.ingredients;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.exceptions.IllegalNameException;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * A class of mixed ingredient types.
 *
 * A mixed ingredient type represents the substance of a mixture.
 *
 * @invar The name of each mixed ingredient type must be proper.
 *      | canHaveAsName(getName())
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class MixedIngredientType extends IngredientType{

    /**
     * Create a new mixed ingredient type with the given name, standard state
     * and standard temperature.
     *
     * @param name
     *        The name of this new mixed ingredient type.
     *
     * @param standardState
     *        The standard state of this new mixed ingredient type.
     *
     * @param standardTemperature
     *        The standard temperature of this new mixed ingredient type.
     *
     * @effect This new mixed ingredient type is initialized as an ingredient type
     *         with the given name, standard state and standard temperature.
     *       | super(name, standardState, standardTemperature)
     *
     * @throws alchemy.exceptions.IllegalNameException
     *         The given name is not a proper mixed name.
     *       | !canHaveAsName(name)
     */
    @Raw
    public MixedIngredientType(Name name, State standardState, Temperature standardTemperature) {
        super(name, standardState, standardTemperature);
    }


    /**
     * Initialize this new mixed ingredient type with the given name.
     *
     * The standard state and standard temperature are taken from the default ingredient type.
     *
     * @param name
     *        The name of this new mixed ingredient type.
     *
     * @effect This new mixed ingredient type is initialized with the given name,
     *         the default standard state and the default standard temperature.
     *       | this(name, DEFAULT.getStandardState(), DEFAULT.getStandardTemperatureObject())
     */
    @Raw
    public MixedIngredientType(Name name) {
        this(name, DEFAULT.getStandardState(), DEFAULT.getStandardTemperatureObject());
    }

    // todo copy constructor or method


    /**
     * Check whether this ingredient type is mixed.
     *
     * A mixed ingredient type is always mixed.
     *
     * @return True.
     *       | result == true
     */
    @Override @Basic @Immutable
    public boolean isMixed() {
        return true;
    }


    /**
     * Return the special name of this mixed ingredient type.
     *
     * @return The special name of this mixed ingredient type.
     *       | result == getName().getSpecialName()
     */
    @Basic
    public String getSpecialName() {
        return getName().getSpecialName();
    }


    /**
     * Check whether this mixed ingredient type has a special name.
     *
     * @return True if and only if the special name is effective.
     *       | result == (getSpecialName() != null)
     */
    public boolean hasSpecialName() {
        return getName().hasSpecialName();
    }


    /**
     * Set the special name of this mixed ingredient type.
     *
     * @param specialName
     *        The special name to set.
     *
     * @effect The special name of the name of this mixed ingredient type
     *         is set to the given special name.
     *       | getName().setSpecialName(specialName)
     *
     * @throws IllegalNameException
     *         The given special name is effective but not valid.
     *       | specialName != null && !AlchemicIngredient.isValidName(specialName)
     */
    @Raw
    public void setSpecialName(String specialName) throws IllegalNameException {
        getName().setSpecialName(specialName);
    }
}
