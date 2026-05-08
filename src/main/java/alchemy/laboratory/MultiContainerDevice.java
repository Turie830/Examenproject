package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for devices that can contain multiple ingredient containers.
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
    protected MultiContainerDevice(Laboratory laboratory) {
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
    }
}
