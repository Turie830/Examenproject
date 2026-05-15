package alchemy.lab;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * An abstract class for devices that can contain at most one ingredient container.
 *
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
    @Raw
    public SingleContainerDevice(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     * Add the ingredient in the given container to this device.
     *
     * @param container The container to add
     *
     * @throws IllegalArgumentException The given container must be effective
     *                                  | container == null
     * @throws IllegalArgumentException The given container must not be empty
     *                                  | container.isEmpty()
     * @throws IllegalStateException There can't be anything in the device
     *                                  | getDeviceContents() != null
     */
    @Override
    public void add(IngredientContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container cannot be empty");
        }
        if (deviceContent != null) {
            throw new IllegalStateException("Cannot add more than one ingredient");
        }

        deviceContent = container.getIngredient();
        container.empty();
    }

    /**
     * Gets the content of this device
     *
     * @return the content of this device (could be null)
     */
    protected AlchemicIngredient getActualDeviceContent() {
        return deviceContent;
    }

    /**
     * Gets a copy of the content of this device.
     *
     * @return A copy of the content of this device,
     * or null if this device has no content.
     * | result == null || result != getActualDeviceContent()
     */
    @Basic
    protected AlchemicIngredient getDeviceContent() {
        if (deviceContent == null) {
            return null;
        }
        return deviceContent;
    }

    /**
     * Empties the device of any inputs
     *
     * @post the deviceContent is set to null
     * | getDeviceContent() == null;
     */
    protected void emptyDeviceContent() {
        deviceContent = null;
    }
}
