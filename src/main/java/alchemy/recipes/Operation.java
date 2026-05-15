package alchemy.recipes;


import be.kuleuven.cs.som.annotate.Basic;

/**
 * An enum for the operations possible in a recipe
 *
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */
public enum Operation {
    ADD, HEAT, COOL, MIX;

    /**
     * Checks if the operation requires an ingredient in the step
     *
     * @return if the operation needs an ingredient
     *      | this == ADD
     */
    @Basic
    public boolean requiresIngredient() {
        return this == ADD;
    }

}
