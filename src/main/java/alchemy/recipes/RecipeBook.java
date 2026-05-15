package alchemy.recipes;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for recipe books
 *
 * @invar Each recipe inside the book must be effective
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public class RecipeBook {

    /**
     * The list for storing the recipes
     */
    private List<Recipe> recipes = new ArrayList<Recipe>();


    /**
     * A constructor to create a new empty recipe book
     *
     * @post an empty recipe book is created
     *      | getNbRecipes() == 0
     */
    @Raw
    public RecipeBook() {}


    /**
     * Add a recipe to the book
     *
     * @param recipe The recipe to add
     *
     * @post The recipe book is longer
     *      | new.getNbRecipes() == old.getNbRecipes() + 1
     *
     * @post The recipe is at the end of the book
     *      | recipe == new.getRecipeAt(new.getNbRecipes() - 1)
     *
     * @throws IllegalArgumentException
     *      The recipe must be effective
     *      | recipe == null
     */
    public void addRecipe(Recipe recipe) throws IllegalArgumentException {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        recipes.add(recipe);
    }

    /**
     * Remove a recipe at an index from the book
     *
     * @param index The index of the item to remove
     *
     * @post The recipe book contains one recipe less
     *      | new.getNbRecipes() == old.getNbRecipes() - 1
     *
     * @post All recipes after the given index are shifted one position to the left.
     *      | for each I in index..new.getNbRecipes()-1:
     *      |     new.getRecipeAt(I) == old.getRecipeAt(I + 1)
     *
     * @throws IllegalArgumentException
     *      The index must be a valid index
     *      | index < 0 || index >= old.getNbRecipes()
     */
    public void removeRecipe(int index) throws IllegalArgumentException {
        if (index < 0 || index >= getNbRecipes()) {
            throw new IllegalArgumentException("Recipe index out of range");
        }

        recipes.remove(index);
    }




    /**
     * Return the recipe at the given index.
     *
     * @param index
     *      The index of the requested recipe.
     *
     * @pre The index of the requested recipe must be less than the total amount of recipes
     *      | index < getNbRecipes()
     *
     * @return The recipe at the given index.
     *       | result == recipes.get(index)
     *
     */
    @Basic
    public Recipe getRecipeAt(int index) {
        return recipes.get(index);
    }

    /**
     * Return the number of recipes in this recipe book.
     *
     * @return The number of recipes in this recipe book.
     *       | result == recipes.size()
     */
    @Basic
    public int getNbRecipes() {
        return recipes.size();
    }
}
