package alchemy.recipes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeBookTest {

    RecipeBook recipeBook;

    @BeforeEach
    public void setUp() {
        recipeBook = new RecipeBook();
    }


    @Test
    public void testCreateEmptyRecipeBook() {
        assertEquals(0, recipeBook.getNbRecipes());
    }

    @Test
    public void testAddRecipe_nullrecipe() {
        assertThrows(IllegalArgumentException.class, () -> recipeBook.addRecipe(null));
    }

    @Test
    public void testAddRecipe_Success() {
        int amount = 0;
        for (int i = 0; i < 4; i++) {
            Recipe recipe = createRecipe();

            recipeBook.addRecipe(recipe);
            amount += 1;
            assertEquals(amount, recipeBook.getNbRecipes());

            // test if it's actually the last item
            assertEquals(recipeBook.getRecipeAt((recipeBook.getNbRecipes()) - 1), recipe);
        }
    }

    @Test
    public void testGetRecipeAt_Success() {
        Recipe recipe1 = createRecipe();
        Recipe recipe2 = createRecipe();

        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);

        assertSame(recipe1, recipeBook.getRecipeAt(0));
        assertSame(recipe2, recipeBook.getRecipeAt(1));
    }

    @Test
    public void testRemoveRecipe_negativeIndex() {
        assertThrows(IllegalArgumentException.class,
                () -> recipeBook.removeRecipe(-1));
    }

    @Test
    public void testRemoveRecipe_indexTooLarge() {
        Recipe recipe = createRecipe();
        recipeBook.addRecipe(recipe);

        assertThrows(IllegalArgumentException.class, () -> recipeBook.removeRecipe(1));
    }

    @Test
    public void testRemoveRecipe_fromSingleRecipeBook() {
        Recipe recipe = createRecipe();
        recipeBook.addRecipe(recipe);

        recipeBook.removeRecipe(0);

        assertEquals(0, recipeBook.getNbRecipes());
    }

    @Test
    public void testRemoveRecipe_firstRecipe() {
        Recipe recipe1 = createRecipe();
        Recipe recipe2 = createRecipe();
        Recipe recipe3 = createRecipe();

        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);
        recipeBook.addRecipe(recipe3);

        recipeBook.removeRecipe(0);

        assertEquals(2, recipeBook.getNbRecipes());
        assertSame(recipe2, recipeBook.getRecipeAt(0));
        assertSame(recipe3, recipeBook.getRecipeAt(1));
    }

    @Test
    public void testRemoveRecipe_middleRecipe() {
        Recipe recipe1 = createRecipe();
        Recipe recipe2 = createRecipe();
        Recipe recipe3 = createRecipe();

        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);
        recipeBook.addRecipe(recipe3);

        recipeBook.removeRecipe(1);

        assertEquals(2, recipeBook.getNbRecipes());
        assertSame(recipe1, recipeBook.getRecipeAt(0));
        assertSame(recipe3, recipeBook.getRecipeAt(1));
    }

    @Test
    public void testRemoveRecipe_lastRecipe() {
        Recipe recipe1 = createRecipe();
        Recipe recipe2 = createRecipe();
        Recipe recipe3 = createRecipe();

        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);
        recipeBook.addRecipe(recipe3);

        recipeBook.removeRecipe(2);

        assertEquals(2, recipeBook.getNbRecipes());
        assertSame(recipe1, recipeBook.getRecipeAt(0));
        assertSame(recipe2, recipeBook.getRecipeAt(1));
    }

    private Recipe createRecipe() {
        return new Recipe(List.of(new SimpleRecipeStep(Operation.MIX)));
    }
}
