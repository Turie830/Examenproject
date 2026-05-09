package alchemy.ingredients;

import alchemy.Unit;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for quantities
 *
 * @invar The amount of each quantity must be greater than or equal to 0.
 *      | getAmount() >= 0
 *
 * @invar The amount is effective.
 *      | getAmount() != null;
 *
 * @invar The unit is effective.
 *      | getUnit() != null
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public class Quantity {

    /**
     * A variable for storing the amount
     */
    private final Long amount;

    /**
     * A variable for storing the unit
     */
    private final Unit  unit;


    /**
     * A constructor for a quantity
     *
     * @param amount
     *         The amount of the quantity
     * @param unit
     *          The unit in which the amount is put
     *
     * @pre The amount is not negative
     *      | amount >= 0
     *
     * @post The amount is set to the given amount
     *      | new.getAmount() == amount
     * @post The unit in which the amount is put
     *      | new.getUnit() == unit
     */
    public Quantity(Long amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    // todo copy constructor or method

    /**
     * Gets the amount of this quantity
     * @return the amount
     */
    @Basic
    @Immutable
    public Long getAmount() {
        return amount;
    }

    /**
     * Gets the unit of this quantity
     * @return the unit
     */
    @Basic
    @Immutable
    public Unit getUnit() {
        return unit;
    }

    /**
     * Converts this quantity to the lowest unit of a given state
     *
     * @param state
     *      The state for which this quantity is converted
     *
     * @return the amount in the lowest unit
     */
    @Immutable
    public Long toLowestUnit(State state) {
        return getUnit().convertToBaseUnit(getAmount(), state);
    }

    /**
     * Converts the amount to all be in spoons
     *
     * @param state
     *      The state for which this quantity is converted
     *
     * @return The largest whole number of spoons contained in this quantity.
     *      | result == Math.floor(toLowestUnit(state) / Unit.getSpoonUnit(state).getFactorToBaseUnit(state))
     */
    @Immutable
    public Long toSpoons(State state) {
        return toLowestUnit(state) / Unit.getSpoonUnit(state).getFactorToBaseUnit(state);
    }

    /**
     * Checks whether this quantity fits in the given unit
     *
     * @param unit
     *      The unit to check for
     *
     * @return True if the given unit has the same state as this quantity unit and
     *         this quantity is smaller than or equal to one of the given unit.
     *      | result == (getUnit().isValidFor(state) && unit.isValidFor(state)
     *      |        && toLowestUnit(state) <= unit.getFactorToBaseUnit(state))
     */
    @Immutable
    public boolean fitsIn(Unit unit, State state) {
        if (unit == null || state == null || !getUnit().isValidFor(state) || !unit.isValidFor(state)) {
            return false;
        }

        return toLowestUnit(state) <= unit.getFactorToBaseUnit(state);
    }
}
