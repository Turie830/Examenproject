package alchemy.laboratory;

import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.State;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for laboratories.
 *
 * A laboratory has a fixed capacity (in storerooms),
 * stores alchemic ingredients, and has a maximum of one
 * device of every kind. Devices are linked bidirectionally to their
 * laboratory (a device registers itself in the laboratory at construction).
 *
 * @invar The storerooms capacity of every laboratory is positive.
 *      | getStorerooms() >= 0
 *
 * @invar Each laboratory has maximum one device of each type.
 *      | for each type in Class<? extends Device>:     // ToDo ? invullen
 *      |     count(d in getDevices() | d.getClass() == type) <= 1
 *
 * @invar Every device in a laboratory references that laboratory back (bidirectional link).
 *      | for each d in getDevices():
 *      |     d.getLaboratory() == this
 *
 * @invar No two different ingredients in a laboratory share the same simple name.
 *      Same-name ingredients are merged on storage, so they always appear as one.
 *      | for any two distinct ingredients a and b in getIngredients():
 *      |     a.getSimpleName() is not equal to b.getSimpleName()
 *
 * @invar The total stored amount per state never exceeds the capacity for that state.
 *      | for each state in State.values():
 *      |     getUsedAmountInLowestUnit(state) <= getCapacityInLowestUnit(state)
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */

public class Laboratory {

    /**
     * constructors
     */

    /**
     * Initialize this new laboratory with the given storerooms capacity,
     * with no ingredients and no devices.
     *
     * @param storerooms
     *        The number of storerooms of capacity for this laboratory (>=0).
     *
     * @post The number of storerooms of this new laboratory equals the given amount.
     *     | new.getStorerooms() == storerooms
     *
     * @post This new laboratory has no ingredients.
     *     | new.getNbIngredients() == 0
     *
     * @post This new laboratory has no devices.
     *     | new.getNbDevices() == 0
     *
     * @throws IllegalArgumentException
     *         The given number of storerooms is negative.
     *       | storerooms < 0
     */
    @Raw
    public Laboratory(int storerooms) throws IllegalArgumentException {
        if (storerooms < 0) {
            throw new IllegalArgumentException("Storerooms cannot be negative");
        }
        this.storerooms = storerooms;
    }

    /**
     * Capacity             ToDo: contoleer op totaal enzo
     */

    /**
     * Variable storing the storerooms capacity of this laboratory.
     * Final means it is set once in the constructor and never changes.
     */
    private final int storerooms;


    /**
     * Return the storerooms capacity of this laboratory.
     */
    @Basic
    public int getStorerooms() {
        return storerooms;
    }


    /**
     * Return the total capacity of this laboratory in the lowest unit
     * for the given state (liquids - drops, powders - pinches).
     *
     * @param state
     *        The state we want the capacity in (= LIQUID or POWDER).
     *
     * @return The number of storerooms multiplied by how many lowest-units fit in one storeroom.
     *       | result = getStorerooms() * Unit.STOREROOM.getFactorToBaseUnit(state)
     *
     * @throws IllegalArgumentException         ToDo: moet dit of is da er heel over?
     *         The given state is not effective.
     *       | state == null
     */
    public long getCapacityInLowestUnit(State state) throws IllegalArgumentException {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        return (long) storerooms * Unit.STOREROOM.getFactorToBaseUnit(state);
    }


    /**
     *
     * @param state
     *        The state we want the total in (= LIQUID or POWDER).
     *
     * @return The sum of the amounts of every ingredient
     *         in this laboratory, in the lowest unit.
     *         | result ==
     *         |   sum of ing.getAmountInLowestUnit() for each ing in getIngredients()
     *
     *  ToDo: moet er een throw? illegalargument?
     */
    public long getUsedAmountInLowestUnit(State state) {
        long total = 0L;
        for (AlchemicIngredient ing : ingredients) {
            if (ing.getType().getStandardState() == state) {
                total += ing.getAmountInLowestUnit();
            }
        }
        return total;
    }


    /**
     * Check if this laboratory has enough capacity to add
     * the given ingredient or not.
     *
     * An ingredient can be liquid or powder. hasRoomFor looks at how much is already stored    ToDo: is dit te 'engelse' uitleg?
     * in that state, add the new amount and check that it stays below the capacity.
     *
     * @param ingredient
     *        The ingredient check room for.
     *
     * @return False if the given ingredient is not effective.
     *       | if (ingredient == null) then result == false
     *
     * @return True if (currently used) + (new amount) is less then or equal to the capacity.
     *         everything is calculated in the lowest unit for the ingredient's state.
     *       | result ==
     *       |   (getUsedAmountInLowestUnit(ingredient.getType().getStandardState())
     *       |    + ingredient.getAmountInLowestUnit())
     *       |   <= getCapacityInLowestUnit(ingredient.getType().getStandardState())
     */
    public boolean hasRoomFor(AlchemicIngredient ingredient) {
        if (ingredient == null) {
            return false;
        }
        State state = ingredient.getType().getStandardState();

        long used = getUsedAmountInLowestUnit(state);
        long extra = ingredient.getAmountInLowestUnit();
        boolean ret = used + extra <= getCapacityInLowestUnit(state);

        return ret;
    }


    /**
     * Ingredients
     */


    /**
     * The list of ingredients stored in this laboratory.
     *
     * Per simple name there is a maximum of one ingredient in this list.
     * (Because two ingredients with the same name merge into one).
     *
     * @invar No two ingredients share the same simple name.
     *
     * @invar Every ingredient is effective (not null).
     */
    private final List<AlchemicIngredient> ingredients = new ArrayList<>();


    /**
     * Return the number of ingredients currently in this laboratory.
     */
    @Basic
    public int getNbIngredients() {
        return ingredients.size();
    }

    /**
     * Check if an ingredient with a given name exists in this laboratory.
     * The name can be a simple name, or a special name (mixes).
     *
     * @param name
     *        The name to look up.
     */
    public boolean hasIngredient(String name) {
        return findIngredient(name) != null;
    }


    /**
     * Return the ingredient with the given name from this laboratory.
     * The name can be a simple name, or a special name (mixes).     *
     * @param name
     *        The name to look up.
     *
     * @throws IllegalArgumentException
     *         No ingredient in this laboratory has this name.
     *       | !hasIngredient(name)
     */
    public AlchemicIngredient getIngredient(String name) throws IllegalArgumentException {
        AlchemicIngredient result = findIngredient(name);
        if (result == null) {
            throw new IllegalArgumentException("No ingredient with name " + name);
        }
        return result;
    }

    // todo I modified it to fix build errors
    public AlchemicIngredient findIngredient(String name) {
        return null;
    }// ToDo: of toch zelfde als getingredient? (nee zeker, anders en wrs veiliger zo)


    /**
     * Devices --> bidirectioneel doen!
     */


    /**
     * Receipies (wrs??)
     */









}