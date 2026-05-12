package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransmogrifierTest {

    private Transmogrifier transmogrifier;

    private static AlchemicIngredient ingredient(String name, State state, long amount, Unit unit) {
        IngredientType type = new IngredientType(new Name(name), state, new Temperature(0, 20));
        return new AlchemicIngredient(type, new Quantity(amount, unit));
    }

    @BeforeEach
    public void setUp() {
        transmogrifier = new Transmogrifier(new Laboratory(1));
    }

    @Test
    public void execute_NoIngredient_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> transmogrifier.execute());
    }

    @Test
    public void execute_LiquidIngredient_ChangesStateToPowder() {
        AlchemicIngredient ingredient = ingredient("Water", State.LIQUID, 1L, Unit.SPOON);
        transmogrifier.add(new IngredientContainer(Unit.BOTTLE, ingredient));

        transmogrifier.execute();

        assertEquals(State.POWDER, ingredient.getState());
    }

    @Test
    public void execute_PowderIngredient_ChangesStateToLiquid() {
        AlchemicIngredient ingredient = ingredient("Salt", State.POWDER, 1L, Unit.SPOON);
        transmogrifier.add(new IngredientContainer(Unit.SACHET, ingredient));

        transmogrifier.execute();

        assertEquals(State.LIQUID, ingredient.getState());
    }

    @Test
    public void execute_IngredientInDevice_CreatesResultAndClearsDeviceContent() {
        AlchemicIngredient ingredient = ingredient("Salt", State.POWDER, 1L, Unit.SPOON);
        transmogrifier.add(new IngredientContainer(Unit.SACHET, ingredient));

        transmogrifier.execute();

        assertNull(transmogrifier.getDeviceContent());
        assertNotNull(transmogrifier.getResult());
        assertSame(ingredient, transmogrifier.getResult().getIngredient());
    }
}
