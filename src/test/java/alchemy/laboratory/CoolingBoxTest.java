package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import alchemy.ingredients.IngredientType;
import alchemy.ingredients.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoolingBoxTest {

    private CoolingBox coolingBox;
    private AlchemicIngredient ingredient;

    @BeforeEach
    public void setUp() {
        coolingBox = new CoolingBox(new Laboratory());
        ingredient = new AlchemicIngredient(new IngredientType(Name.WATER), new Quantity(1L, Unit.VIAL));
    }

    @Test
    public void setTemperatureTarget_EffectiveTemperature_StoresTemperatureCopy() {
        Temperature target = new Temperature(40, 0);

        coolingBox.setTemperatureTarget(target);

        Temperature storedTarget = coolingBox.getTemperatureTarget();
        assertNotSame(target, storedTarget);
        assertEquals(40, storedTarget.getColdness());
        assertEquals(0, storedTarget.getHotness());
    }

    @Test
    public void getTemperatureTarget_ReturnsCopy() {
        coolingBox.setTemperatureTarget(new Temperature(40, 0));

        Temperature target = coolingBox.getTemperatureTarget();
        target.cool(20);

        assertEquals(40, coolingBox.getTemperatureTarget().getColdness());
        assertEquals(0, coolingBox.getTemperatureTarget().getHotness());
    }

    @Test
    public void setTemperatureTarget_NullTemperature_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> coolingBox.setTemperatureTarget(null));
    }

    @Test
    public void execute_NoIngredient_ThrowsException() {
        coolingBox.setTemperatureTarget(new Temperature(30, 0));

        assertThrows(IllegalStateException.class, () -> coolingBox.execute());
    }

    @Test
    public void execute_NoTemperatureTarget_ThrowsException() {
        coolingBox.add(new IngredientContainer(Unit.BOTTLE, ingredient));

        assertThrows(IllegalStateException.class, () -> coolingBox.execute());
    }

    @Test
    public void execute_TargetColderThanIngredient_CoolsIngredientToTarget() {
        coolingBox.add(new IngredientContainer(Unit.BOTTLE, ingredient));
        coolingBox.setTemperatureTarget(new Temperature(30, 0));

        coolingBox.execute();

        assertEquals(30, ingredient.getColdness());
        assertEquals(0, ingredient.getHotness());
    }

    @Test
    public void execute_TargetHotterThanIngredient_DoesNotChangeIngredientTemperature() {
        ingredient.cool(40);
        coolingBox.add(new IngredientContainer(Unit.BOTTLE, ingredient));
        coolingBox.setTemperatureTarget(new Temperature(0, 20));

        coolingBox.execute();

        assertEquals(20, ingredient.getColdness());
        assertEquals(0, ingredient.getHotness());
    }
}
