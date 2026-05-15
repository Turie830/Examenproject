package alchemy.recipes;

import alchemy.Name;
import alchemy.lab.Quantity;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class for recipe steps that need ingredients
 *
 * @invar the step must have an IngredientName that is effective
 *      | getIngredientName() != null
 *
 * @invar the step must have an effective quantity
 *       | getIngredientQuantity() != null
 *
 * @invar The operation for this step must need an ingredient
 *      | getOperation().requiresIngredient() == true
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public class IngredientRecipeStep extends RecipeStep {

    /**
     * A variable for storing the ingredientName
     */
    private final Name ingredientName;

    /**
     * A variable for storing the quantity of the ingredient
     */
    private final Quantity ingredientQuantity;

    /**
     * Create a new Recipe step that needs ingredients
     *
     * @param operation
     *      The operation of this step
     * @param ingredientName
     *      The name of this ingredient
     * @param quantity
     *      The quantity of this ingredient
     *
     * @post the ingredientName is set to the give ingredientName
     *      | new.getIngredientName() == ingredientName
     *
     * @post the ingredientQuantity is set to the give quantity
     *      | new.getIngredientQuantity() == quantity
     *
     *
     * @throws IllegalArgumentException
     *      The ingredient name must be effective
     *      | ingredientName == null
     * @throws IllegalArgumentException
     *      The quantity must be effective
     *      | quantity == null
     * @throws IllegalArgumentException
     *      The operation must require an ingredient
     *      | !operation.reqruiresIngredient()
     */
    @Raw
    public IngredientRecipeStep(Operation operation, Name ingredientName, Quantity quantity) throws IllegalArgumentException {
        // We must do this one again, cause if it's null the operation.requiresIngredient Will fail for nullpointer even thoug it's already in RecipeStep
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }
        if (!operation.requiresIngredient()) {
            throw new IllegalArgumentException("Operation must require ingredient");
        }

        if (ingredientName == null) {
            throw new IllegalArgumentException("Ingredient name cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        super(operation);
        this.ingredientName = ingredientName;
        this.ingredientQuantity = quantity;
    }


    /**
     * Gets the ingredient name needed in this step
     *
     * @return the ingredient name need for this step
     */
    @Basic
    @Immutable
    public Name getIngredientName() {
        return ingredientName;
    }

    /**
     * Gets the ingredient quantity needed in this step
     *
     * @return the ingredient quantity need for this step
     */
    @Basic
    @Immutable
    public Quantity getIngredientQuantity() {
        return ingredientQuantity;
    }

    @Override
    public boolean isValidRecipeStep() {
        return getOperation().requiresIngredient()
                && getIngredientName() != null
                && getIngredientQuantity() != null;
    }

}
