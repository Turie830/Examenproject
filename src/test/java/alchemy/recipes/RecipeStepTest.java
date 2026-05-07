package alchemy.recipes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeStepTest {

    @Test
    public void constructor_ValidOperation_SetsOperation() {
        RecipeStep step = new TestRecipeStep(Operation.MIX);

        assertEquals(Operation.MIX, step.getOperation());
    }

    @Test
    public void constructor_NullOperation_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TestRecipeStep(null));
    }

    private static class TestRecipeStep extends RecipeStep {

        private TestRecipeStep(Operation operation) {
            super(operation);
        }

        @Override
        public boolean isValidRecipeStep() {
            return true;
        }
    }
}
