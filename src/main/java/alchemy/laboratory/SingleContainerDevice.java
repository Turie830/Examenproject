package alchemy.laboratory;

import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
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
     *                                  | TODO how to do this getDeviceContent is protected
     */
    // TODO do we want to create a copy or not? (see todo in OvenTest)
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
    public AlchemicIngredient getDeviceContent() {
        if (deviceContent == null) {
            return null;
        }
        //TODO create a copy of ingredient (needs constructor or .copy method) + update test cases
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

    /**
     * Gets the result
     *
     * @return null if the device has not ran yet
     * @return a container with the same capacity and ingredient as the result container
     */
    public IngredientContainer getResult() {
        if (result == null) {
            return null;
        }
        return new IngredientContainer(result.getCapacityUnit(), result.getIngredient());
    }

    /**
     * Create a result container for the given ingredient.
     *
     * The result uses the ingredient's current unit when that unit is a valid
     * container capacity and the ingredient fits in it. Otherwise, the first
     * valid capacity unit that can contain the ingredient is used.
     *
     * @param resultIngredient The ingredient for which to create a result container
     *
     * @throws IllegalArgumentException The given result ingredient must be effective
     *                                  | resultIngredient == null
     * @throws IllegalArgumentException The given result ingredient must fit in a valid container
     */
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

        // Otherwise, try every valid capacity unit until one can contain the result.
        for (Unit capacityUnit : Unit.values()) {
            if (IngredientContainer.isValidCapacityUnit(capacityUnit)
                    && IngredientContainer.canContain(capacityUnit, resultIngredient)) {
                result = new IngredientContainer(capacityUnit, resultIngredient);
                return;
            }
        }

        throw new IllegalArgumentException("Result ingredient does not fit in a valid container");
    }
}
