package alchemy.lab;

import alchemy.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityTest {

    @Test
    public void constructor_ValidQuantity_SetsAmountAndUnit() {
        Quantity quantity = new Quantity(3L, Unit.VIAL);

        assertEquals(3L, quantity.getAmount());
        assertEquals(Unit.VIAL, quantity.getUnit());
    }

    @Test
    public void toLowestUnit_SharedSpoonLiquid_ReturnsAmountInDrops() {
        Quantity quantity = new Quantity(10L, Unit.SPOON);

        assertEquals(80L, quantity.toLowestUnit(State.LIQUID));
    }

    @Test
    public void toLowestUnit_SharedSpoonPowder_ReturnsAmountInPinches() {
        Quantity quantity = new Quantity(10L, Unit.SPOON);

        assertEquals(60L, quantity.toLowestUnit(State.POWDER));
    }

    @Test
    public void toLowestUnit_LiquidOnlyUnitWithPowderState_ThrowsIllegalArgumentException() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertThrows(IllegalArgumentException.class, () -> quantity.toLowestUnit(State.POWDER));
    }

    @Test
    public void toLowestUnit_PowderOnlyUnitWithLiquidState_ThrowsIllegalArgumentException() {
        Quantity quantity = new Quantity(3L, Unit.SACHET);

        assertThrows(IllegalArgumentException.class, () -> quantity.toLowestUnit(State.LIQUID));
    }

    @Test
    public void toSpoons_LiquidQuantity_ReturnsAmountInWholeSpoons() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertEquals(10L, quantity.toSpoons(State.LIQUID));
    }

    @Test
    public void toSpoons_PowderQuantity_ReturnsAmountInWholeSpoons() {
        Quantity quantity = new Quantity(3L, Unit.SACHET);

        assertEquals(21L, quantity.toSpoons(State.POWDER));
    }

    @Test
    public void toSpoons_AmountNotDivisibleBySpoonUnit_ReturnsFlooredAmount() {
        Quantity quantity = new Quantity(10L, Unit.PINCH);

        assertEquals(1L, quantity.toSpoons(State.POWDER));
    }

    @Test
    public void toSpoons_InvalidState_ThrowsIllegalArgumentException() {
        Quantity quantity = new Quantity(10L, Unit.PINCH);

        assertThrows(IllegalArgumentException.class, () -> quantity.toSpoons(State.LIQUID));
    }

    @Test
    public void fitsIn_ExactSameCapacitySameState_ReturnsTrue() {
        Quantity quantity = new Quantity(1L, Unit.BOTTLE);

        assertTrue(quantity.fitsIn(Unit.BOTTLE, State.LIQUID));
    }

    @Test
    public void fitsIn_SmallerQuantitySameState_ReturnsTrue() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertTrue(quantity.fitsIn(Unit.BOTTLE, State.LIQUID));
    }

    @Test
    public void fitsIn_LargerQuantitySameState_ReturnsFalse() {
        Quantity quantity = new Quantity(4L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.BOTTLE, State.LIQUID));
    }

    @Test
    public void fitsIn_SharedStoreroomLiquid_ReturnsTrue() {
        Quantity quantity = new Quantity(1L, Unit.BARREL);

        assertTrue(quantity.fitsIn(Unit.STOREROOM, State.LIQUID));
    }

    @Test
    public void fitsIn_SharedStoreroomPowder_ReturnsTrue() {
        Quantity quantity = new Quantity(1L, Unit.CHEST);

        assertTrue(quantity.fitsIn(Unit.STOREROOM, State.POWDER));
    }

    @Test
    public void fitsIn_TargetUnitInvalidForState_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.SACHET, State.LIQUID));
    }

    @Test
    public void fitsIn_QuantityUnitInvalidForState_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.SACHET, State.POWDER));
    }

    @Test
    public void fitsIn_NullUnit_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(null, State.LIQUID));
    }

    @Test
    public void fitsIn_NullState_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.BOTTLE, null));
    }
}
