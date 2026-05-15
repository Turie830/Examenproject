package alchemy.lab;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OvenTest {

    private Oven oven;
    private AlchemicIngredient ingredient;

    @BeforeEach
    public void setUp() {
        oven = new Oven(new Laboratory(1));
        ingredient = new AlchemicIngredient(new IngredientType(Name.WATER), new Quantity(1L, Unit.VIAL));
    }


    @Test
    public void setTemperatureTarget_EffectiveTemperature_StoresTemperatureCopy() {
        Temperature target = new Temperature(0, 80);

        oven.setTemperatureTarget(target);

        Temperature storedTarget = oven.getTemperatureTarget();
        assertNotSame(target, storedTarget);
        assertEquals(0, storedTarget.getColdness());
        assertEquals(80, storedTarget.getHotness());
    }

    @Test
    public void getTemperatureTarget_ReturnsCopy() {
        oven.setTemperatureTarget(new Temperature(0, 80));

        Temperature target = oven.getTemperatureTarget();
        target.heat(20);

        assertEquals(0, oven.getTemperatureTarget().getColdness());
        assertEquals(80, oven.getTemperatureTarget().getHotness());
    }

    @Test
    public void setTemperatureTarget_NullTemperature_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> oven.setTemperatureTarget(null));
    }

    @Test
    public void execute_NoIngredient_ThrowsException() {
        oven.setTemperatureTarget(new Temperature(0, 80));

        assertThrows(IllegalStateException.class, () -> oven.execute());
    }

    @Test
    public void execute_NoTemperatureTarget_ThrowsException() {
        oven.add(new IngredientContainer(Unit.BOTTLE, ingredient));

        assertThrows(IllegalStateException.class, () -> oven.execute());
    }

    @Test
    public void execute_TargetHotterThanIngredient_HeatsIngredientWithinFiveDegreesOfTarget() {
        oven.add(new IngredientContainer(Unit.BOTTLE, ingredient));
        oven.setTemperatureTarget(new Temperature(0, 80));

        oven.execute();

        assertEquals(0, ingredient.getColdness());
        assertTrue(ingredient.getHotness() >= 75);
        assertTrue(ingredient.getHotness() <= 85);
    }

    @Test
    public void execute_TargetColderThanIngredient_DoesNotChangeIngredientTemperature() {
        ingredient.heat(80);
        oven.add(new IngredientContainer(Unit.BOTTLE, ingredient));
        oven.setTemperatureTarget(new Temperature(0, 20));

        oven.execute();

        assertEquals(0, ingredient.getColdness());
        assertEquals(100, ingredient.getHotness());
    }
}
