package alchemy.recipes;


import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class for recipe steps without ingredients
 *
 * @invar The operation for this step must not need an ingredient
 *      | operation.requiresIngredient() == false
 *
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public class SimpleRecipeStep extends RecipeStep {

    /**
     * Create a recipe step without ingredients
     *
     * @param operation The operation
     *
     * @throws IllegalArgumentException
     *      The operation for this step must not need an ingredient
     *      | operation.requiresIngredient() == false
     */
    @Raw
    public SimpleRecipeStep(Operation operation) throws IllegalArgumentException {
        // We must do this one again, cause if it's null the operation.requiresIngredient Will fail for nullpointer even thoug it's already in RecipeStep
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }
        if (operation.requiresIngredient()) {
            throw new IllegalArgumentException("Operation must not require ingredient");
        }

        super(operation);
    }

    @Override
    public boolean isValidRecipeStep() {
        return !getOperation().requiresIngredient();
    }

}
