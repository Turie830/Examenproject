package alchemy.recipes;


/**
 * A class for recipe steps
 *
 * @invar the step must have an Operation that is effective
 *      | getOperation() != null
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 *
 */
public abstract class RecipeStep {

    /**
     * A variable for storing the operation
     */
    private final Operation operation;


    /**
     * Create a recipeStep
     *
     * @param operation
     *      The operation this step is
     *
     * @throws IllegalArgumentException
     *       the operation must be effective
     *       | operation == null
     *
     * @post the operation is set to the given operation
     *      | new.getOperation() == operation
     */
    protected RecipeStep(Operation operation) throws IllegalArgumentException {
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }

        this.operation = operation;
    }

    /**
     * Gets the operation of the step
     *
     * @returns the operation of this step
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Check whether this step is valid to use in a recipe.
     *
     * @return True if this step satisfies its recipe-step invariants.
     *
     * @note This was added so that a Recipe can verify if all the steps are correct (in case a user creates a custom RecipeStep)
     */
    public abstract boolean isValidRecipeStep();
}
