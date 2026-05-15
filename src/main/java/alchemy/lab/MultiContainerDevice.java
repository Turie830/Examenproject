package alchemy.lab;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for devices that can contain multiple ingredient containers.
 *
 *
 * @note There are no functions to get what's inside the multiContainerDevice
 *     cause it wouldn't be logical if you could take stuff out of a kettle
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public abstract class MultiContainerDevice extends Device {

    /**
     * The ingredients inside the device.
     *
     * @note This list is internal to the device. Subclasses decide how the
     *       ingredients are processed when the device is executed.
     */
    private List<AlchemicIngredient> deviceContents = new ArrayList<AlchemicIngredient>();


    /**
     * Initialise a new multi container device
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException The given laboratory must be effective
     *                                  | laboratory == null
     * @post The laboratory of this device is set to the given laboratory
     * | new.getLaboratory() == laboratory
     */
    @Raw
    public MultiContainerDevice(Laboratory laboratory) {
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
     */
    @Override
    public void add(IngredientContainer container) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container cannot be empty");
        }

        deviceContents.add(container.getIngredient());
        container.empty();
    }

    /**
     * Gets the contents of this device
     *
     * @return a copy of the list with the current ingredients
     */
    @Basic
    protected List<AlchemicIngredient> getActualDeviceContents() {
        return new ArrayList<AlchemicIngredient>(deviceContents);
    }

    /**
     * Empties the device of any inputs
     *
     * @post the device contents are empty
     * | getActualDeviceContents().isEmpty()
     */
    protected void emptyDeviceContents() {
        deviceContents.clear();
    }
}
