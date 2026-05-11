package alchemy.laboratory;

import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import be.kuleuven.cs.som.annotate.Basic;

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
     * The result container after executing
     */
    private IngredientContainer result;


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
     * Create a result container for the given ingredient
     *
     * @param resultIngredient The ingredient for which to create a result container
     * @throws IllegalArgumentException The given result ingredient must be effective
     *                                  | resultIngredient == null
     * @throws IllegalArgumentException The given result ingredient must fit in a valid container
     */
    // TODO this is shared with singleContainerDevice (move to Device)
    protected void createResultContainer(AlchemicIngredient resultIngredient) {
        if (resultIngredient == null) {
            throw new IllegalArgumentException("Result ingredient cannot be null");
        }

        Unit resultUnit = resultIngredient.getQuantity().getUnit();

        if (IngredientContainer.isValidCapacityUnit(resultUnit)
                && IngredientContainer.canContain(resultUnit, resultIngredient)) {
            result = new IngredientContainer(resultUnit, resultIngredient);
            return;
        }

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
