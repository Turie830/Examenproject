package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;

/**
 * An interface for devices that can contain at most one ingredient container
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public abstract class SingleContainerDevice extends Device {

    /**
     * The ingredient in the device
     */
    private AlchemicIngredient deviceContent;


    /**
     * Initialise a new single container device
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException The given laboratory must be effective
     *                                  | laboratory == null
     * @post The laboratory of this device is set to the given laboratory
     * | new.getLaboratory() == laboratory
     */
    public SingleContainerDevice(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     *
     * @param container The container to add
     *                  //TODO do we need the @throws here?
     */
    @Override
    public void add(IngredientContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container cannot be empty");
        }

        deviceContents = container.getIngredient();
    }

    /**
     * Gets the content of this device
     *
     * @return the content of this device
     */
    protected AlchemicIngredient getDeviceContent() {
        return deviceContent;
    }
}