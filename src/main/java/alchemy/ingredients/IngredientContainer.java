package alchemy.ingredients;

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
 * @invar If a container of a certain unit is not empty, the ingredient fits inside.
 *      | isEmpty() ||
 *      |   (getIngredient().getState() == getCapacityUnit().getState()
 *      |   && getIngredient().getQuantity().toLowestUnit()
 *      |       <= getCapacityUnit().getFactorToBaseUnit())
 *
 *
 * ToDo: check men documentatie pls, kwn zeker of dit juist is 🥹😭. vooral invars :)
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class IngredientContainer {

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
    public IngredientContainer(Unit capacityUnit, AlchemicIngredient ingredient) {
        if (!isValidCapacityUnit(capacityUnit))
            throw new IllegalArgumentException("Invalid container capacity unit");
        if (ingredient != null && !canContain(capacityUnit, ingredient)) {
            throw new IllegalArgumentException("Ingredient doesn't fit in the container");
        }

        this.capacityUnit = capacityUnit;
        this.ingredient = ingredient;
    }


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
    public IngredientContainer(Unit capacityUnit) {
        this(capacityUnit, null);
    }



    /**
     * Variable referencing the capacity unit of this container.
     */
    private final Unit capacityUnit;


    /**
     * Return the capacity unit of this container.
     *
     * @return The unit that this container has to define the capacity.
     *       | result == this.capacityUnit
     */
    @Basic @Immutable
    public Unit getCapacityUnit() {
        return capacityUnit;
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
     *         (DROP or PINCH) and is not a storeroom unit (STOREROOM_LIQUID or STOREROOM_POWDER).
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
                && unit != Unit.STOREROOM
                && unit != Unit.STOREROOM;
    }


    /**
     * Check if the given ingredient can be stored in a container
     * with the given capacity unit.
     *
     * An ingredient fits if its state matches the capacity unit's state,
     * and its quantity is not bigger than the capacity.
     *
     * @param capacityUnit
     *        The capacity unit to check against.
     *
     * @param ingredient
     *        The ingredient to check.
     *
     * @return True if the ingredient's state matches the unit's state
     *         and the ingredient's quantity is less then or equal to one unit of capacity.
     *       | result ==
     *       |   ingredient.getState() == capacityUnit.getState()
     *       |   && ingredient.getQuantity().toLowestUnit()
     *       |       <= capacityUnit.getFactorToBaseUnit()
     */
    // ToDo : mag static zijn? (ja zeker?)
    // ToDo: deze functie vind ik stom
    public static boolean canContain(Unit capacityUnit, AlchemicIngredient ingredient) {
        if (capacityUnit == null || ingredient == null)
            return false;          // ToDo: is dit goed zo? of exception throwen beter?
        if (ingredient.getState() != capacityUnit.getState())
            return false;
        return ingredient.getQuantity().toLowestUnit() <= capacityUnit.getFactorToBaseUnit(ingredient.getState());   // ToDo: wat moet er tss de haakjes? : Kdenk de state van et je ingredient
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
     * Empty this container by removing the ingredient it contains.
     * If the container was already empty, it remains empty.
     *
     * @post This container is empty after this call.
     *     | new.isEmpty()
     */
    // ToDo: mag public zijn? of niet?
    public void empty() {
        this.ingredient = null;
    }


}
