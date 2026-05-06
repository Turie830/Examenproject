package alchemy.ingredients;

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
    public void toLowestUnit_LiquidQuantity_ReturnsAmountInDrops() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertEquals(80L, quantity.toLowestUnit());
    }

    @Test
    public void toLowestUnit_PowderQuantity_ReturnsAmountInPinches() {
        Quantity quantity = new Quantity(3L, Unit.SACHET);

        assertEquals(126L, quantity.toLowestUnit());
    }

    @Test
    public void toLowestUnit_BaseUnit_ReturnsSameAmount() {
        Quantity quantity = new Quantity(7L, Unit.DROP);

        assertEquals(7L, quantity.toLowestUnit());
    }

    @Test
    public void toSpoons_LiquidQuantity_ReturnsAmountInLiquidSpoons() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertEquals(10L, quantity.toSpoons());
    }

    @Test
    public void toSpoons_PowderQuantity_ReturnsAmountInPowderSpoons() {
        Quantity quantity = new Quantity(3L, Unit.SACHET);

        assertEquals(21L, quantity.toSpoons());
    }

    @Test
    public void toSpoons_LessThanOneSpoon_ReturnsZero() {
        Quantity quantity = new Quantity(7L, Unit.DROP);

        assertEquals(0L, quantity.toSpoons());
    }

    @Test
    public void toSpoons_AmountNotDivisibleBySpoonUnit_ReturnsFlooredAmount() {
        Quantity quantity = new Quantity(10L, Unit.PINCH);

        assertEquals(1L, quantity.toSpoons());
    }

    @Test
    public void fitsIn_ExactSameCapacitySameState_ReturnsTrue() {
        Quantity quantity = new Quantity(1L, Unit.BOTTLE);

        assertTrue(quantity.fitsIn(Unit.BOTTLE));
    }

    @Test
    public void fitsIn_SmallerQuantitySameState_ReturnsTrue() {
        Quantity quantity = new Quantity(2L, Unit.VIAL);

        assertTrue(quantity.fitsIn(Unit.BOTTLE));
    }

    @Test
    public void fitsIn_LargerQuantitySameState_ReturnsFalse() {
        Quantity quantity = new Quantity(4L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.BOTTLE));
    }

    @Test
    public void fitsIn_DifferentState_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(Unit.SACHET));
    }

    @Test
    public void fitsIn_NullUnit_ReturnsFalse() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);

        assertFalse(quantity.fitsIn(null));
    }
}
