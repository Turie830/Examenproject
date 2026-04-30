package alchemy.ingredients;


import alchemy.Name;
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
 * Todo: full name
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
     * Variable referencing the simple name of this alchemic ingredient.
     */
    private final Name simpleName;


    /**
     * Initialize this new alchemic ingredient with the given simple name.
     *
     * @param name
     *        The simple name for this new alchemic ingredient.
     *
     * @post The simple name of this new alchemic ingredient is equal to the given name.
     *     | new.getSimpleName().equals(name)
     *
     * @throws IllegalArgumentException
     *         The given name is not a valid name.
     *       | !Name.isValidName(name)
     */
    public AlchemicIngredient(String name) {
        this.simpleName = new Name(name);
    }


    /**
     * Return the simple name of this alchemic ingredient.
     *
     * @return The simple name of this alchemic ingredient.
     *       | result.equals(this.simpleName.getName())
     */
    @Basic
    @Immutable
    public String getSimpleName() {
        return simpleName.getName();
    }



    /**
     * Return the full name of this alchemic ingredient.
     *
     *
     * ToDo: Later, prefixes such as "Heated" and "Cooled" can be added here.
     *
     * @return The full name of this alchemic ingredient.
     *       | result.equals(...)
     */
    public String getFullName() {
        //ToDo: full name
        return "";
    }





}
