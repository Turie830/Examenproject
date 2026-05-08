package alchemy.ingredients;

import alchemy.Name;
import alchemy.Temperature;
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
    public MixedIngredientType(Name name) {
        this(name, DEFAULT.getStandardState(), DEFAULT.getStandardTemperatureObject());
    }

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

}
