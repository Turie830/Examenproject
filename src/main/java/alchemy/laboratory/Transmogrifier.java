package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;

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


    @Override
    public void execute() throws IllegalStateException {
        AlchemicIngredient ingredient = getActualDeviceContent();

        if (ingredient == null) {
            throw new IllegalStateException("No ingredient in device");
        }

        // todo first implement state in IngredientType
//        ingredient.

    }
}
