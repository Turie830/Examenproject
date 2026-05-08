package alchemy.laboratory;

import alchemy.Unit;
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
     * The result container after executing
     */
    private IngredientContainer result;


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

        deviceContent = container.getIngredient();
    }

    /**
     * Gets the content of this device
     *
     * @return the content of this device
     */
    protected AlchemicIngredient getDeviceContent() {
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

    // TODO comment

    /**
     *
     */
    // todo can be moved to device I think
    protected void createResultContainer(AlchemicIngredient resultIngredient) {
        if (resultIngredient == null) {
            throw new IllegalArgumentException("Result ingredient cannot be null");
        }

        Unit resultUnit = resultIngredient.getQuantity().getUnit();

        // checks if we can use the same unit as the ingredient
        if (IngredientContainer.isValidCapacityUnit(resultUnit)
                && IngredientContainer.canContain(resultUnit, resultIngredient)) {
            result = new IngredientContainer(resultUnit, resultIngredient);
            return;
        }

        // else we try and find the
        for (Unit capacityUnit : Unit.values()) {
            if (IngredientContainer.isValidCapacityUnit(capacityUnit)
                    && IngredientContainer.canContain(capacityUnit, resultIngredient)) {
                result = new IngredientContainer(capacityUnit, resultIngredient);
                return;
            }
        }

        // this can't happen since these are single container devices
        // todo should we then take the largest amount possible (when we make it so that it's in device aswell)
        throw new IllegalArgumentException("Result ingredient does not fit in a valid container");
    }
}
