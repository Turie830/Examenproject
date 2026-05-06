package alchemy;

import alchemy.ingredients.State;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * An enum for storing the Unit and in this unit we have the assosciated state and factor to the base Unit of that state
 *
 *
 * @invar The state of each quantity unit must be effective.
 *      | getState() != null
 *
 * @invar The factor to the base unit of each quantity unit must be strictly positive.
 *      | getFactorToBaseUnit() > 0
 *
 * @invar Each state must have exactly 1 base unit
 *      | for each state in State.values():
 *      |     count(unit in Unit.values() |
 *      |         unit.getState() == state &&
 *      |         unit.getFactorToBaseUnit() == 1) == 1
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public enum Unit {
    DROP(State.LIQUID, 1),
    SPOON_LIQUID(State.LIQUID, 8),
    VIAL(State.LIQUID, 5 * 8),
    BOTTLE(State.LIQUID, 3 * 5 * 8),
    JUG(State.LIQUID, 7 * 3 * 5 * 8),
    BARREL(State.LIQUID, 12 * 7 * 3 * 5 * 8),
    STOREROOM_LIQUID(State.LIQUID, 5 * 12 * 7 * 3 * 5 * 8),

    PINCH(State.POWDER, 1),
    SPOON_POWDER(State.POWDER, 6),
    SACHET(State.POWDER, 7 * 6),
    BOX(State.POWDER, 6 * 7 * 6),
    SACK(State.POWDER, 3 * 6 * 7 * 6),
    CHEST(State.POWDER, 10 * 3 * 6 * 7 * 6),
    STOREROOM_POWDER(State.POWDER, 5 * 10 * 3 * 6 * 7 * 6);

    /**
     * The state of the unit
     */
    private final State state;

    /**
     * the factor to convert it to the baseUnit of the state
     */
    private final int factorToBaseUnit;

    /**
     * Initialise the unit
     *
     * @param state the state for this unit type
     * @param factorToBaseUnit the factor to the base unit for the according state
     *
     * @post The state of this Unit is set to the given state
     *      | new.getState() == state
     * @post The factorToBaseUnit is set to the given factor
     *      | new.getFactorToBaseUnit() == factorToBaseUnit
     *
     * @throws IllegalArgumentException
     *       | factorToBaseUnit <= 0
     */
    Unit(State state, int factorToBaseUnit) throws IllegalArgumentException {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }

        if (factorToBaseUnit <= 0 ) {
            throw new IllegalArgumentException("factorToBaseUnit must be positive");
        }

        this.state = state;
        this.factorToBaseUnit = factorToBaseUnit;
    }


    /**
     * @return the state this unit is used for
     */
    @Basic
    @Immutable
    public State getState() {
        return state;
    }

    /**
     *
     * @return the factor to the base unit
     *      For liquid units, the base unit is the drop
     *      For powder units, the base unit is the pinch
     */
    @Basic
    @Immutable
    public int getFactorToBaseUnit() {
        return factorToBaseUnit;
    }

    /**
     * Gets the base unit for this quantity unit.
     *
     * @return The unit with the same state as this quantity unit and a factor to
     *         the base unit equal to 1.
     *       | result.getState() == getState()
     *       | result.getFactorToBaseUnit() == 1
     */
    @Immutable
    public Unit getBaseUnit() {
        for (Unit unit : Unit.values()) {
            if (unit.getState() == getState() && unit.getFactorToBaseUnit() == 1) {
                return unit;
            }
        }

        throw new IllegalStateException("No base unit found.");
    }

    /**
     * Gets the spoon unit for this quantity unit.
     *
     * @return The spoon unit with the same state as this quantity unit.
     *       | result.getState() == getState()
     *       | result == Unit.SPOON_LIQUID || result == Unit.SPOON_POWDER
     */
    @Immutable
    public Unit getSpoonUnit() {
        if (getState() == State.LIQUID) {
            return Unit.SPOON_LIQUID;
        }

        return Unit.SPOON_POWDER;
    }

    /**
     * Checks if the given state matches the unit type this is used on
     * @param state
     *        The state to check for
     * @return True if the state of this quantity unit is equal to the given state
     *      | result == (getState() == state)
     */
    public boolean isValidFor(State state) {
        return this.state == state;
    }

    /**
     * Convert the given amount expressed in this quantity unit to the corresponding
     * amount expressed in the base unit
     * <p>
     * For liquid units, the result is expressed in drops.
     * For powder units, the result is expressed in pinches.
     *
     * @param amount The amount expressed in this quantity unit.
     * @return The given amount multiplied by the factor to the base unit of this quantity unit.
     * | result == amount * getFactorToBaseUnit()
     * @throws IllegalArgumentException The given amount is negative.
     *                                  | amount < 0
     */
    public Long convertToBaseUnit(Long amount) throws IllegalArgumentException {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative.");

        return amount * getFactorToBaseUnit();
    }
}
