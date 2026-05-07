package alchemy.recipes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleRecipeStepTest {

    @Test
    public void constructor_OperationWithoutIngredient_SetsOperation() {
        SimpleRecipeStep step = new SimpleRecipeStep(Operation.MIX);

        assertEquals(Operation.MIX, step.getOperation());
    }

    @Test
    public void constructor_HeatOperation_SetsOperation() {
        SimpleRecipeStep step = new SimpleRecipeStep(Operation.HEAT);

        assertEquals(Operation.HEAT, step.getOperation());
    }

    @Test
    public void constructor_CoolOperation_SetsOperation() {
        SimpleRecipeStep step = new SimpleRecipeStep(Operation.COOL);

        assertEquals(Operation.COOL, step.getOperation());
    }

    @Test
    public void constructor_OperationThatRequiresIngredient_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleRecipeStep(Operation.ADD));
    }

    @Test
    public void constructor_NullOperation_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleRecipeStep(null));
    }
}
