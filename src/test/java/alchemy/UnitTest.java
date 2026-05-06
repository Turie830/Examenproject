package alchemy;

import alchemy.ingredients.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UnitTest {

    @Test
    public void getBaseUnit_LiquidUnit_ReturnsDrop() {
        assertEquals(Unit.DROP, Unit.BOTTLE.getBaseUnit());
    }

    @Test
    public void getBaseUnit_PowderUnit_ReturnsPinch() {
        assertEquals(Unit.PINCH, Unit.SACK.getBaseUnit());
    }

    @Test
    public void getBaseUnit_BaseUnit_ReturnsSameUnit() {
        assertEquals(Unit.DROP, Unit.DROP.getBaseUnit());
        assertEquals(Unit.PINCH, Unit.PINCH.getBaseUnit());
    }

    @Test
    public void getBaseUnit_AllLiquidUnits_ReturnDrop() {
        for (Unit unit : Unit.values()) {
            if (unit.getState() == State.LIQUID) {
                assertEquals(Unit.DROP, unit.getBaseUnit());
            }
        }
    }

    @Test
    public void getBaseUnit_AllPowderUnits_ReturnPinch() {
        for (Unit unit : Unit.values()) {
            if (unit.getState() == State.POWDER) {
                assertEquals(Unit.PINCH, unit.getBaseUnit());
            }
        }
    }

    @Test
    public void getSpoonUnit_LiquidUnit_ReturnsLiquidSpoon() {
        assertEquals(Unit.SPOON_LIQUID, Unit.BOTTLE.getSpoonUnit());
    }

    @Test
    public void getSpoonUnit_PowderUnit_ReturnsPowderSpoon() {
        assertEquals(Unit.SPOON_POWDER, Unit.SACK.getSpoonUnit());
    }

    @Test
    public void getSpoonUnit_AllLiquidUnits_ReturnLiquidSpoon() {
        for (Unit unit : Unit.values()) {
            if (unit.getState() == State.LIQUID) {
                assertEquals(Unit.SPOON_LIQUID, unit.getSpoonUnit());
            }
        }
    }

    @Test
    public void getSpoonUnit_AllPowderUnits_ReturnPowderSpoon() {
        for (Unit unit : Unit.values()) {
            if (unit.getState() == State.POWDER) {
                assertEquals(Unit.SPOON_POWDER, unit.getSpoonUnit());
            }
        }
    }

    @Test
    public void getState_LiquidUnit_ReturnsLiquid() {
        assertEquals(State.LIQUID, Unit.VIAL.getState());
    }

    @Test
    public void getState_PowderUnit_ReturnsPowder() {
        assertEquals(State.POWDER, Unit.SACHET.getState());
    }

    @Test
    public void getFactorToBaseUnit_ReturnsConversionFactor() {
        assertEquals(1, Unit.DROP.getFactorToBaseUnit());
        assertEquals(8, Unit.SPOON_LIQUID.getFactorToBaseUnit());
        assertEquals(42, Unit.SACHET.getFactorToBaseUnit());
    }

    @Test
    public void isValidFor_SameState_ReturnsTrue() {
        assertTrue(Unit.BOTTLE.isValidFor(State.LIQUID));
        assertTrue(Unit.SACK.isValidFor(State.POWDER));
    }

    @Test
    public void isValidFor_DifferentState_ReturnsFalse() {
        assertFalse(Unit.BOTTLE.isValidFor(State.POWDER));
        assertFalse(Unit.SACK.isValidFor(State.LIQUID));
    }

    @Test
    public void isValidFor_NullState_ReturnsFalse() {
        assertFalse(Unit.BOTTLE.isValidFor(null));
    }

    @Test
    public void convertToBaseUnit_PositiveAmount_ReturnsAmountInBaseUnit() {
        assertEquals(80L, Unit.VIAL.convertToBaseUnit(2L));
        assertEquals(126L, Unit.SACHET.convertToBaseUnit(3L));
    }

    @Test
    public void convertToBaseUnit_ZeroAmount_ReturnsZero() {
        assertEquals(0L, Unit.BOTTLE.convertToBaseUnit(0L));
    }

    @Test
    public void convertToBaseUnit_NegativeAmount_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Unit.BOTTLE.convertToBaseUnit(-1L));
    }
}
