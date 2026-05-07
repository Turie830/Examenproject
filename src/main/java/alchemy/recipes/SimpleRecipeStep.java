package alchemy.recipes;


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
    public SimpleRecipeStep(Operation operation) throws IllegalArgumentException {
        if (operation.requiresIngredient()) {
            throw new IllegalArgumentException("Operation must not require ingredient");
        }

        super(operation);
    }
}
