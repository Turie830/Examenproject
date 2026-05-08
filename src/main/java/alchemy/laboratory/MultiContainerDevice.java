package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface for devices that can contain multiple ingredient containers
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public abstract class MultiContainerDevice extends Device {

    /**
     * The ingredients inside the device
     */
    // TODO do we need a way to check what ingredients are in it?
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
     * // TODO do we copy the comments or is it inherited?
     *
     * @param container The container to add
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