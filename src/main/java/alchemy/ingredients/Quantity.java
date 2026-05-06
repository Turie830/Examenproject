package alchemy.ingredients;

import alchemy.Unit;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class for quantities
 *
 * @invar The amount of each quantity must be greater than or equal to 0
 *      | getAmount() >= 0
 *
 * @invar The amount is effective
 *      | getAmount() != null;
 *
 * @invar The unit of each quantity must be compatible with the state
 *        of the ingredient
 *      | canHaveAsUnitForState(getUnit(), getState())
 *
 * @invar The state is effective
 *      | getState() != null
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

    /**
     * Gets the amount of this quantity
     * @return the amount
     */
    @Basic
    public Long getAmount() {
        return amount;
    }

    /**
     * Gets the unit of this quantity
     * @return the unit
     */
    @Immutable
    public Unit getUnit() {
        return unit;
    }

    /**
     * Converts this quantity to the lowest unit of a given state
     *
     * @return the amount in the lowest unit
     */
    public Long toLowestUnit() {
        return getUnit().convertToBaseUnit(getAmount());
    }

    /**
     * Converts the amount to all be in spoons
     *
     * @return The largest whole number of spoons contained in this quantity.
     *      | result == Math.floor(toLowestUnit() / getUnit().getSpoonUnit().getFactorToBaseUnit())
     */
    public Long toSpoons() {
        // this is floored since its Long / Long
        return toLowestUnit() / getUnit().getSpoonUnit().getFactorToBaseUnit();
    }

    /**
     * Checks whether this quantity fits in the given unit
     *
     * @param unit
     *      The unit to check for
     *
     * @return True if the given unit has the same state as this quantity unit and
     *         this quantity is smaller than or equal to one of the given unit.
     *      | result == (unit.getState() == getUnit().getState()
     *      |        && toLowestUnit() <= unit.getFactorToBaseUnit())
     */
    public boolean fitsIn(Unit unit) {
        if (unit == null || unit.getState() != getUnit().getState()) {
            return false;
        }

        return toLowestUnit() <= unit.getFactorToBaseUnit();
    }
}
