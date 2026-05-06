package alchemy;

import alchemy.ingredients.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {

    @Test
    public void isValidFor_SharedUnits_AcceptLiquidAndPowder() {
        assertTrue(Unit.SPOON.isValidFor(State.LIQUID));
        assertTrue(Unit.SPOON.isValidFor(State.POWDER));
        assertTrue(Unit.STOREROOM.isValidFor(State.LIQUID));
        assertTrue(Unit.STOREROOM.isValidFor(State.POWDER));
    }

    @Test
    public void isValidFor_LiquidOnlyUnit_RejectsPowder() {
        assertTrue(Unit.VIAL.isValidFor(State.LIQUID));
        assertFalse(Unit.VIAL.isValidFor(State.POWDER));
    }

    @Test
    public void isValidFor_PowderOnlyUnit_RejectsLiquid() {
        assertTrue(Unit.SACHET.isValidFor(State.POWDER));
        assertFalse(Unit.SACHET.isValidFor(State.LIQUID));
    }

    @Test
    public void isValidFor_NullState_ReturnsFalse() {
        assertFalse(Unit.SPOON.isValidFor(null));
    }

    @Test
    public void getFactorToBaseUnit_SharedSpoon_ReturnsFactorForGivenState() {
        assertEquals(8, Unit.SPOON.getFactorToBaseUnit(State.LIQUID));
        assertEquals(6, Unit.SPOON.getFactorToBaseUnit(State.POWDER));
    }

    @Test
    public void getFactorToBaseUnit_SharedStoreroom_ReturnsFactorForGivenState() {
        assertEquals(5 * 12 * 7 * 3 * 5 * 8, Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID));
        assertEquals(5 * 10 * 3 * 6 * 7 * 6, Unit.STOREROOM.getFactorToBaseUnit(State.POWDER));
    }

    @Test
    public void getFactorToBaseUnit_InvalidState_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.VIAL.getFactorToBaseUnit(State.POWDER));
        assertThrows(IllegalArgumentException.class, () -> Unit.SACHET.getFactorToBaseUnit(State.LIQUID));
    }

    @Test
    public void getFactorToBaseUnit_NullState_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.SPOON.getFactorToBaseUnit(null));
    }

    @Test
    public void getBaseUnit_LiquidState_ReturnsDrop() {
        assertEquals(Unit.DROP, Unit.getBaseUnit(State.LIQUID));
    }

    @Test
    public void getBaseUnit_PowderState_ReturnsPinch() {
        assertEquals(Unit.PINCH, Unit.getBaseUnit(State.POWDER));
    }

    @Test
    public void getBaseUnit_NullState_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.getBaseUnit(null));
    }

    @Test
    public void getSpoonUnit_LiquidState_ReturnsSharedSpoon() {
        assertEquals(Unit.SPOON, Unit.getSpoonUnit(State.LIQUID));
    }

    @Test
    public void getSpoonUnit_PowderState_ReturnsSharedSpoon() {
        assertEquals(Unit.SPOON, Unit.getSpoonUnit(State.POWDER));
    }

    @Test
    public void getSpoonUnit_NullState_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.getSpoonUnit(null));
    }

    @Test
    public void convertToBaseUnit_PositiveAmount_ReturnsAmountInBaseUnitForState() {
        assertEquals(80L, Unit.SPOON.convertToBaseUnit(10L, State.LIQUID));
        assertEquals(60L, Unit.SPOON.convertToBaseUnit(10L, State.POWDER));
        assertEquals(126L, Unit.SACHET.convertToBaseUnit(3L, State.POWDER));
    }

    @Test
    public void convertToBaseUnit_ZeroAmount_ReturnsZero() {
        assertEquals(0L, Unit.BOTTLE.convertToBaseUnit(0L, State.LIQUID));
    }

    @Test
    public void convertToBaseUnit_NegativeAmount_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.BOTTLE.convertToBaseUnit(-1L, State.LIQUID));
    }

    @Test
    public void convertToBaseUnit_InvalidState_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.BOTTLE.convertToBaseUnit(1L, State.POWDER));
    }
}
