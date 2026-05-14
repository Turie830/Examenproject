package alchemy;

import alchemy.lab.State;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.EnumMap;
import java.util.Map;

/**
 * An enum for storing quantity units and their factor to the base unit for each valid state.
 *
 * @invar Each quantity unit must be valid for at least one state.
 *      | for some state in State.values():
 *      |     isValidFor(state)
 *
 * @invar The factor to the base unit of each valid state must be strictly positive.
 *      | for each state in State.values():
 *      |     if (isValidFor(state)) then getFactorToBaseUnit(state) > 0
 *
 * @invar Each state must have exactly 1 base unit
 *      | for each state in State.values():
 *      |     count(unit in Unit.values() |
 *      |         unit.isValidFor(state) &&
 *      |         unit.getFactorToBaseUnit(state) == 1) == 1
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.2
 */
public enum Unit {
    // liquid units
    DROP(State.LIQUID, 1),
    VIAL(State.LIQUID, 5 * 8),
    BOTTLE(State.LIQUID, 3 * 5 * 8),
    JUG(State.LIQUID, 7 * 3 * 5 * 8),
    BARREL(State.LIQUID, 12 * 7 * 3 * 5 * 8),

    // powder units
    PINCH(State.POWDER, 1),
    SACHET(State.POWDER, 7 * 6),
    BOX(State.POWDER, 6 * 7 * 6),
    SACK(State.POWDER, 3 * 6 * 7 * 6),
    CHEST(State.POWDER, 10 * 3 * 6 * 7 * 6),

    // Shared units
    SPOON(8, 6),
    STOREROOM(5 * 12 * 7 * 3 * 5 * 8, 5 * 10 * 3 * 6 * 7 * 6);

    /**
     * A map registering the factor to the base unit for each valid state.
     */
    private final Map<State, Integer> factorsToBaseUnit = new EnumMap<>(State.class);

    /**
     * Initialise this unit as a unit that is valid for one state.
     *
     * @param state
     *      the state for which this unit is valid
     * @param factorToBaseUnit
     *      the factor to the base unit for the given state
     *
     * @post This unit is valid for the given state.
     *      | new.isValidFor(state)
     * @post The factor to the base unit for the given state is set to the given factor.
     *      | new.getFactorToBaseUnit(state) == factorToBaseUnit
     *
     * @throws IllegalArgumentException
     *      The given state is not effective.
     *      | state == null
     * @throws IllegalArgumentException
     *      The given factor to the base unit is not strictly positive.
     *      | factorToBaseUnit <= 0
     */
    Unit(State state, int factorToBaseUnit) throws IllegalArgumentException {
        addFactorToBaseUnit(state, factorToBaseUnit);
    }

    /**
     * Initialise this unit as a unit that is valid for liquids and powders.
     *
     * @param liquidFactorToBaseUnit
     *      the factor to the base unit for liquids
     * @param powderFactorToBaseUnit
     *      the factor to the base unit for powders
     *
     * @post This unit is valid for liquids.
     *      | new.isValidFor(State.LIQUID)
     * @post This unit is valid for powders.
     *      | new.isValidFor(State.POWDER)
     * @post The factor to the base unit for liquids is set to the given liquid factor.
     *      | new.getFactorToBaseUnit(State.LIQUID) == liquidFactorToBaseUnit
     * @post The factor to the base unit for powders is set to the given powder factor.
     *      | new.getFactorToBaseUnit(State.POWDER) == powderFactorToBaseUnit
     *
     * @throws IllegalArgumentException
     *      One of the given factors to the base unit is not strictly positive.
     *      | liquidFactorToBaseUnit <= 0 || powderFactorToBaseUnit <= 0
     */
    Unit(int liquidFactorToBaseUnit, int powderFactorToBaseUnit) throws IllegalArgumentException {
        addFactorToBaseUnit(State.LIQUID, liquidFactorToBaseUnit);
        addFactorToBaseUnit(State.POWDER, powderFactorToBaseUnit);
    }

    /**
     * Add the given factor to the base unit for the given state.
     *
     * @param state
     *      the state for which to add a factor
     * @param factorToBaseUnit
     *      the factor to add
     *
     * @throws IllegalArgumentException
     *      The given state is not effective.
     *      | state == null
     * @throws IllegalArgumentException
     *      The given factor is not strictly positive.
     *      | factorToBaseUnit <= 0
     */
    @Raw
    private void addFactorToBaseUnit(State state, int factorToBaseUnit) throws IllegalArgumentException {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }

        if (factorToBaseUnit <= 0) {
            throw new IllegalArgumentException("factorToBaseUnit must be positive");
        }

        factorsToBaseUnit.put(state, factorToBaseUnit);
    }

    /**
     * Checks if this unit is valid for the given state.
     *
     * @param state
     *      The state to check for
     *
     * @return True if this unit has a factor to the base unit for the given state.
     *      | result == factorsToBaseUnit.containsKey(state)
     */
    public boolean isValidFor(State state) {
        // check if the given state is in the map of this unit
        return factorsToBaseUnit.containsKey(state);
    }

    /**
     * Gets the factor to the base unit for the given state.
     *
     * @param state
     *      The state for which to get the factor to the base unit
     *
     * @return the factor to the base unit for the given state
     *      | result == factorsToBaseUnit.get(state)
     *
     * @throws IllegalArgumentException
     *      This unit is not valid for the given state.
     *      | !isValidFor(state)
     */
    @Basic
    @Immutable
    public int getFactorToBaseUnit(State state) throws IllegalArgumentException {
        if (!isValidFor(state)) {
            throw new IllegalArgumentException("Unit is not valid for the given state.");
        }

        return factorsToBaseUnit.get(state);
    }


    /**
     * Gets the base unit for the given state.
     *
     * @param state
     *      The state for which to get the base unit
     *
     * @return The unit that is valid for the given state and has a factor to the base unit equal to 1.
     *      | result.isValidFor(state)
     *      | result.getFactorToBaseUnit(state) == 1
     *
     * @throws IllegalArgumentException
     *      The given state is not effective.
     *      | state == null
     */
    @Immutable
    public static Unit getBaseUnit(State state) throws IllegalArgumentException {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }
        // run over every unit possible
        for (Unit unit : Unit.values()) {
            if (unit.isValidFor(state) && unit.getFactorToBaseUnit(state) == 1) {
                return unit;
            }
        }

        throw new IllegalStateException("No base unit found.");
    }

    /**
     * Convert the given amount expressed in this unit to the corresponding
     * amount expressed in the base unit for the given state.
     *
     * @param amount
     *      The amount expressed in this unit.
     * @param state
     *      The state for which the amount is converted.
     *
     * @return The given amount multiplied by the factor to the base unit of this unit for the given state.
     *      | result == amount * getFactorToBaseUnit(state)
     *
     * @throws IllegalArgumentException
     *      The given amount is negative.
     *      | amount < 0
     * @throws IllegalArgumentException
     *      This unit is not valid for the given state.
     *      | !isValidFor(state)
     */
    public Long convertToBaseUnit(Long amount, State state) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        return amount * getFactorToBaseUnit(state);
    }

}
