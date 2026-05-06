package alchemy;

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
}
