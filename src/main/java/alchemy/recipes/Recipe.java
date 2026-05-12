package alchemy.recipes;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for recipes
 *
 * @invar A recipe must have at least 1 step
 *      | getNbSteps() > 0
 *
 * @invar Each recipe step must be effective
 *      | for each I in 0..getNbSteps()-1:
 *      |     getStepAt(I) != null
 *
 * @invar Each recipe step must be valid
 *      | for each I in 0..getNbSteps()-1:
 *      |     getStepAt(I).isValidRecipeStep()
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public class Recipe {

    /**
     * A List for storing the steps of this recipe
     */
    private final List<RecipeStep> steps = new ArrayList<RecipeStep>();

    /**
     * Create a recipe with the given steps
     *
     * @param steps A list of steps
     *
     * @post The given steps are set as the steps for this recipe
     *      | getNbSteps() == steps.size()
     *
     * @throws IllegalArgumentException
     *      The given list steps must be effective and not empty
     *      | steps == null || steps.isEmpty()
     * @throws IllegalArgumentException
     *      Each step in the given list must be effective
     * @throws IllegalArgumentException
     *      Each step in the given list must be valid
     */
    @Raw
    public Recipe(List<RecipeStep> steps) throws IllegalArgumentException {
        if  (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Steps cannot be null or empty");
        }

        for (RecipeStep step : steps) {
            if (step == null) {
                throw new IllegalArgumentException("Recipe step cannot be null");
            }
            if (!step.isValidRecipeStep()) {
                throw new IllegalArgumentException("Recipe step is not valid");
            }
        }

        // last step must always be a mix step
        if (steps.getLast().getOperation() != Operation.MIX) {
            RecipeStep mixStep = new SimpleRecipeStep(Operation.MIX);
            steps.add(mixStep);
        }

        // Add the steps to the list, this creates a copy of a step
        this.steps.addAll(steps);


    }


    /**
     * Gets the amount of steps
     *
     * @return the amount of steps in this recipe
     */
    @Basic
    public int getNbSteps() {
        return steps.size();
    }

    /**
     * Gets the step at a certain index
     *
     * @param index
     *      The index of the step to retrieve
     *
     * @pre The index of the requested recipe must be less than the total amount of recipes
     *       | index < getNbRecipes()
     *
     * @return The recipe at the given index.
     *       | result == steps.get(index)
     *
     * @note this does not return a copy since every field in a recipeStep is final
     * TODO verify if this follows liskov
     */
    @Basic
    public RecipeStep getStepAt(int index)  {
        return steps.get(index);
    }
}
