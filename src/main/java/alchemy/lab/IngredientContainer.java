package alchemy.lab;

import alchemy.Unit;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class of ingredient containers.
 *
 * An ingredient container can hold maximum one ingredient,
 * and can contain the capacity of the container at maximum.
 * A container can be empty, or it contains an amount of an ingredient.
 *
 * The capacity of a container is always one of a certain unit.
 * Containers do not exist for the smallest or largest units,
 * so no containers for drop, pinch and storeroom
 *
 * @invar The capacity unit of each container must be valid.
 *      | isValidCapacityUnit(getCapacityUnit())
 *
 * @invar If this container is not empty, its ingredient must fit inside.
 *      | isEmpty() || canContain(getCapacityUnit(), getIngredient())
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class IngredientContainer {

    /**
     * A variable for storing the destroyed status of this object
     */
    Boolean destroyed = false;


    /**
     * Initialize this new ingredient container with the given capacity unit,
     * with no ingredient ( = empty container).
     *
     * @param capacityUnit
     *        The capacity unit for this new container.
     *
     * @effect This new container is initialized with the given capacity unit
     *         and no ingredient.
     *       | this(capacityUnit, null)
     */
    @Raw
    public IngredientContainer(Unit capacityUnit) {
        this(capacityUnit, null);
    }



    /**
     * Variable referencing the capacity unit of this container.
     */
    private final Unit capacityUnit;


/**
 * Initialize this ingredientcontainer with a unit's capacity
 * and an ingredient.
 *
 * @param capacityUnit
 *        The capacity unit for this container.
 *
 * @param ingredient
 *        The ingredient to store in this container,
 *        or null for an empty container.
 *
 * @post The capacity unit of this new container is equal to the given capacity unit.
 *     | new.getCapacityUnit() == capacityUnit
 *
 * @post The ingredient of this new container is equal to the given ingredient.
 *     | new.getIngredient() == ingredient
 *
 * @throws IllegalArgumentException
 *         The given capacity unit is not a valid container unit.
 *       | !isValidCapacityUnit(capacityUnit)
 *
 * @throws IllegalArgumentException
 *         The given ingredient is not null and does not fit in this container.
 *       | ingredient != null && !canContain(capacityUnit, ingredient)
 */
    @Raw
    public IngredientContainer(Unit capacityUnit, AlchemicIngredient ingredient) throws IllegalArgumentException {
        if (!isValidCapacityUnit(capacityUnit))
            throw new IllegalArgumentException("Invalid container capacity unit");
        if (ingredient != null && !canContain(capacityUnit, ingredient)) {
            throw new IllegalArgumentException("Ingredient doesn't fit in the container");
        }

        this.capacityUnit = capacityUnit;
        this.ingredient = ingredient;
    }

    /**
     * Check whether the given capacity unit can contain the given ingredient.
     *
     * A capacity unit can contain an ingredient if both the capacity unit and the
     * ingredient's quantity unit are valid for the ingredient's standard state,
     * and if the ingredient's amount in the lowest unit is less than or equal to
     * the capacity of the given unit.
     *
     * @param capacityUnit
     *        The unit representing the capacity of the container.
     *
     * @param ingredient
     *        The ingredient that should be put in the container.
     *
     * @return False if the given capacity unit is not effective.
     *       | if (capacityUnit == null) then result == false
     *
     * @return False if the given ingredient is not effective.
     *       | if (ingredient == null) then result == false
     *
     * @return False if the given capacity unit is not valid for the standard state
     *         of the given ingredient.
     *       | if (capacityUnit != null && ingredient != null
     *       |     && !capacityUnit.isValidFor(ingredient.getState()())) then
     *       |   result == false
     *
     * @return False if the quantity unit of the given ingredient is not valid for
     *         the standard state of the given ingredient.
     *       | if (capacityUnit != null && ingredient != null
     *       |     && !ingredient.getQuantity().getUnit().isValidFor(ingredient.getState()())) then
     *       |   result == false
     *
     * @return True if and only if the ingredient fits in the given capacity unit.
     *       | result ==
     *       |   capacityUnit != null
     *       |   && ingredient != null
     *       |   && capacityUnit.isValidFor(ingredient.getState())
     *       |   && ingredient.getQuantity().getUnit().isValidFor(ingredient.getState()())
     *       |   && ingredient.getQuantity().toLowestUnit(ingredient.getState()())
     *       |      <= capacityUnit.getFactorToBaseUnit(ingredient.getState()())
     */
    public static boolean canContain(Unit capacityUnit, AlchemicIngredient ingredient) {
        // don't check destroyed status since a new container is never destroyed
        if (capacityUnit == null || ingredient == null)
            return false;

        State state = ingredient.getState();

        if (!capacityUnit.isValidFor(state)) {
            return false;
        }
        if (!ingredient.getQuantity().getUnit().isValidFor(state)) {
            return false;
        }
        return ingredient.getQuantity().toLowestUnit(state) <= capacityUnit.getFactorToBaseUnit(state);
    }


    /**
     * Check whether the given unit is a valid capacity unit for a container.
     *
     * Valid capacity cannot be the smallest unit and cannot be
     * the largest unit for their state.
     *
     * @param unit
     *        The unit to check.
     *
     * @return True if the given unit is effective, is not a base unit
     *         (DROP or PINCH) and is not a storeroom unit (STOREROOM or STOREROOM).
     *       | result ==
     *       |   unit != null
     *       |   && unit != Unit.DROP && unit != Unit.PINCH
     *       |   && unit != Unit.STOREROOM
     *
     * @return false otherwise
     *
     */
    public static boolean isValidCapacityUnit(Unit unit) {
        return unit != null
                && unit != Unit.DROP
                && unit != Unit.PINCH
                && unit != Unit.STOREROOM;
    }

    /**
     * Get the largest unit allowed for ingredient containers
     *
     * @param state The state for the unit
     * @return the largest unit allowed in an ingredient container depending on the state,
     * or null if the state is not known
     */
    public static Unit largestContainerUnit(State state) {
        Unit best = null;
        for (Unit u : Unit.values()) {
            if (!u.isValidFor(state)) continue;
            if (!IngredientContainer.isValidCapacityUnit(u)) continue;
            if (best == null || u.getFactorToBaseUnit(state) > best.getFactorToBaseUnit(state)) {
                best = u;
            }
        }
        return best;
    }

    /**
     * Give the capacity unit of this container.
     *
     * @return The unit that this container has to define the capacity.
     *       | result == this.capacityUnit
     */
    @Basic @Immutable
    public Unit getCapacityUnit() {
        return capacityUnit;
    }


    /**
     * Variable referencing the ingredient stored in this container.
     * Null if this container is empty.
     */
    private AlchemicIngredient ingredient;


    /**
     * Return the ingredient stored in this container, or null if empty.
     *
     * @return The ingredient in this container, or null.
     *       | result == this.ingredient
     */
    @Basic
    public AlchemicIngredient getIngredient() {
        return ingredient;
    }


    /**
     * Check whether this container is empty.
     *
     * @return True if this container holds no ingredient.
     *       | result == (getIngredient() == null)
     */
    public boolean isEmpty() {
        return ingredient == null;
    }

    /**
     * Check if the given ingredient can be stored in a container
     * with the given capacity unit.
     * <p>
     * An ingredient fits if its state matches the capacity unit's state,
     * and its quantity is not bigger than the capacity.
     *
     * @param capacityUnit The capacity unit to check against.
     * @param ingredient   The ingredient to check.
     * @return True if the given ingredient can be added to this container
     * without exceeding the capacity.
     * | result ==
     * |   capacityUnit != null
     * |   && ingredient != null
     * |   && !isDestroyed()
     * |   && ingredient.getType() == getIngredient().getType()
     * |   && capacityUnit.isValidFor(ingredient.getState())
     * |   && ingredient.getQuantity().getUnit().isValidFor(ingredient.getState())
     * |   && ingredient.getQuantity().toLowestUnit(ingredient.getState())
     * |      + getIngredient().getQuantity().toLowestUnit(ingredient.getState())
     * |      <= capacityUnit.getFactorToBaseUnit(ingredient.getState())
     * @note this function might be useful for later implementation,
     * for example to add ingredients into an already existing, not yet destroyed container
     */
    public boolean fitsIn(Unit capacityUnit, AlchemicIngredient ingredient) {
        if (capacityUnit == null || ingredient == null || isDestroyed())
            return false;

        // state, temperature, standard temperature needs to be the same
        if (ingredient.getType() != getIngredient().getType()) {
            return false;
        }

        State state = ingredient.getState();

        if (!capacityUnit.isValidFor(state)) {
            return false;
        }
        if (!ingredient.getQuantity().getUnit().isValidFor(state)) {
            return false;
        }

        long newTotal = ingredient.getQuantity().toLowestUnit(state) + getIngredient().getQuantity().toLowestUnit(state);

        return newTotal <= capacityUnit.getFactorToBaseUnit(state);
    }

    /**
     * Empty this container by removing the ingredient it contains.
     * If the container was already empty, it remains empty.
     *
     * @post This container is empty after this call.
     *     | new.isEmpty()
     */
    protected void empty() {
        this.ingredient = null;
    }

    /**
     * Empty the container and destroy it
     *
     * @post this container is empty after this call
     *      | new.isEmpty()
     *
     * @post this container is destroyed
     *      | new.isDestroyed()
     */
    protected void emptyDestroy() {
        empty();
        this.destroyed = true;
    }

    /**
     * Get the destroyed status of this object
     *
     * @return true if it's destroyed else false
     */
    public boolean isDestroyed() {
        return destroyed;
    }


}
