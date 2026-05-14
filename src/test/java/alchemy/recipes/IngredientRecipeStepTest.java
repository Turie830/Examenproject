package alchemy.recipes;

import alchemy.Name;
import alchemy.Unit;
import alchemy.lab.Quantity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientRecipeStepTest {

    @Test
    public void constructor_ValidIngredientStep_SetsOperationNameAndQuantity() {
        Name ingredientName = new Name("Human Remains");
        Quantity quantity = new Quantity(2L, Unit.SPOON);

        IngredientRecipeStep step = new IngredientRecipeStep(Operation.ADD, ingredientName, quantity);

        assertEquals(Operation.ADD, step.getOperation());
        assertSame(ingredientName, step.getIngredientName());
        assertSame(quantity, step.getIngredientQuantity());
    }

    @Test
    public void constructor_NullIngredientName_ThrowsIllegalArgumentException() {
        Quantity quantity = new Quantity(2L, Unit.SPOON);

        assertThrows(IllegalArgumentException.class,
                () -> new IngredientRecipeStep(Operation.ADD, null, quantity));
    }

    @Test
    public void constructor_NullQuantity_ThrowsIllegalArgumentException() {
        Name ingredientName = new Name("Aura Essence");

        assertThrows(IllegalArgumentException.class,
                () -> new IngredientRecipeStep(Operation.ADD, ingredientName, null));
    }

    @Test
    public void constructor_OperationWithoutIngredient_ThrowsIllegalArgumentException() {
        Name ingredientName = new Name("Mog Potential");
        Quantity quantity = new Quantity(2L, Unit.SPOON);

        assertThrows(IllegalArgumentException.class,
                () -> new IngredientRecipeStep(Operation.MIX, ingredientName, quantity));
    }

    @Test
    public void constructor_NullOperation_ThrowsIllegalArgumentException() {
        Name ingredientName = new Name("Crushed Skulls");
        Quantity quantity = new Quantity(2L, Unit.SPOON);

        assertThrows(IllegalArgumentException.class,
                () -> new IngredientRecipeStep(null, ingredientName, quantity));
    }
}
