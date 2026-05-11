package alchemy.ingredients;

import alchemy.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AlchemicIngredientTest {


    @Test
    public void changeState_nullState_throws() {
        AlchemicIngredient ing = new AlchemicIngredient();
        assertThrows(IllegalArgumentException.class, () -> ing.changeState(null));
    }

    @Test
    public void changeState_sameState_DoesNothing() {
        AlchemicIngredient ing = new AlchemicIngredient();
        Quantity before = ing.getQuantity();
        ing.changeState(State.LIQUID);

        assertEquals(State.LIQUID, ing.getState());
        assertEquals(before, ing.getQuantity());
    }

    @Test
    public void changeState_Success() {
        AlchemicIngredient ing = new AlchemicIngredient();
        Quantity before = ing.getQuantity();
        ing.changeState(State.POWDER);

        assertEquals(State.POWDER, ing.getState());
        assertEquals(1L, ing.getQuantity().getAmount());
        assertEquals(Unit.SPOON, ing.getQuantity().getUnit());
    }
}
