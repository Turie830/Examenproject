package alchemy.ingredients;

import alchemy.Unit;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

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
    private Long amount;

    /**
     * A variable for storing the unit
     */
    private Unit  unit;


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
        setAmount(amount);
        setUnit(unit);
    }

    /**
     * Set the amount to the given amount
     * @param amount
     *      the amount to set the amount to
     *
     * @pre The amount is not negative
     *      | amount >= 0
     * @pre the amount is not null
     *      | amount != null
     *
     * @post the amount is set to the new amount
     *      | new.getAmount() == amount
     */
    @Raw
    private void setAmount(Long amount) {
        // Nominale implementatie
        this.amount = amount;
    }

    /**
     * Set the unit to the given unit
     * @param unit
     *      the unit of this quantity
     *
     * @pre unit is not null
     *      | unit != null
     * @post the unit is set as the unit of this quantity
     *      | new.getUnit() == unit
     */
    // TODO RAW of niet??
    private void setUnit(Unit unit) {
        // Nominale implementatie
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
     * @param state
     *      the state it should get converted to
     */
    // todo, waarom state nodig? we weten state toch van object
    public void toLowestUnit(State state) {

    }
}