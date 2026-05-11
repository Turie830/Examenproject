package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.State;

/**
 * A class for transmogrifier devices.
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public class Transmogrifier extends SingleContainerDevice {


    /**
     * Initialise a new transmogrifier
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException The given laboratory must be effective
     *                                  | laboratory == null
     * @post The laboratory of this device is set to the given laboratory
     * | new.getLaboratory() == laboratory
     */
    public Transmogrifier(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     * Execute this transmogrifier.
     *
     * @throws IllegalStateException The device must have an ingredient to transmogrify.
     * @post If the ingredient is a powder, its state is changed to liquid
     * @post If the ingredient is not a powder, its state is changed to powder
     * @post The converted ingredient is stored in the result container
     * @post The device content is empty after execution
     */
    @Override
    public void execute() throws IllegalStateException {
        AlchemicIngredient ingredient = getActualDeviceContent();

        if (ingredient == null) {
            throw new IllegalStateException("No ingredient in device");
        }

        State currentState = ingredient.getState();

        if (currentState == State.POWDER) {
            ingredient.changeState(State.LIQUID);
        } else {
            ingredient.changeState(State.POWDER);
        }

        createResultContainer(ingredient);
        emptyDeviceContent();
    }
}
