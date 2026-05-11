package alchemy.recipes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeTest {

    @Test
    public void constructor_ListWithNullStep_ThrowsIllegalArgumentException() {
        List<RecipeStep> steps = new ArrayList<RecipeStep>();
        steps.add(null);

        assertThrows(IllegalArgumentException.class, () -> new Recipe(steps));
    }

    @Test
    public void constructor_InvalidCustomStep_ThrowsIllegalArgumentException() {
        List<RecipeStep> steps = new ArrayList<RecipeStep>();
        steps.add(new InvalidRecipeStep(Operation.MIX));

        assertThrows(IllegalArgumentException.class, () -> new Recipe(steps));
    }


    @Test
    public void constructor_ListWithNonLastMixStep_AddsMixStep() {
        List<RecipeStep> steps = new ArrayList<RecipeStep>();
        steps.add(new SimpleRecipeStep(Operation.HEAT));

        Recipe recipe = new Recipe(steps);

        // also tests getNbSteps
        assertEquals(2, recipe.getNbSteps());
        assertEquals(Operation.HEAT, recipe.getStepAt(0).getOperation());
        assertEquals(Operation.MIX, recipe.getStepAt(1).getOperation());
    }



    private static class InvalidRecipeStep extends RecipeStep {

        private InvalidRecipeStep(Operation operation) {
            super(operation);
        }

        @Override
        public boolean isValidRecipeStep() {
            return false;
        }
    }

}
