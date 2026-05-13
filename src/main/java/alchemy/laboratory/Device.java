package alchemy.laboratory;

import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import alchemy.ingredients.Quantity;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * An abstract class for devices.
 *
 * @invar The laboratory of each device must be effective.
 *      | getLaboratory() != null
 *
 * @note this class does not contain any ingredient/devicecontent rules/variables
 *      reason: kettle accepts a list, others only accept 1 => liskov
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public abstract class Device {

    /**
     * The laboratory this device is located in.
     */
    private final Laboratory laboratory;

    /**
     * The result container after executing
     */
    private IngredientContainer result;

    /**
     * Initialise a new device
     *
     * @param laboratory
     *      The laboratory this device is located in
     *
     * @post The laboratory of this device is set to the given laboratory.
     *      | new.getLaboratory() == laboratory
     * @post The given laboratory has this device registered.
     *      | laboratory.hasAsDevice(this)
     *
     * @throws IllegalArgumentException
     *      when the laboratory is not effective
     *      | laboratory == null
     */
    @Raw
    public Device(Laboratory laboratory) {
        if (laboratory == null) {
            throw new IllegalArgumentException("Laboratory object can't be null");
        }
        this.laboratory = laboratory;
        // todo: @Raw in laboratory?
        laboratory.registerDevice(this);
    }

    /**
     * Return the laboratory this device is located in
     *
     * @return The laboratory this device is located in
     */
    @Basic
    public Laboratory getLaboratory() {
        return laboratory;
    }

    /**
     * Add the given container to this device
     *
     * @param container The container to add
     *
     * @throws IllegalArgumentException The given container must be effective
     *                                  | container == null
     * @throws IllegalArgumentException The given container must not be empty
     *                                  | container.isEmpty()
     */
    public abstract void add(IngredientContainer container);

    /**
     * Execute the operation of this device.
     */
    public abstract void execute();


    /**
     * Gets the result
     *
     * @return null if the device has not run yet
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

        // largest unit for the ingredientContainer
        Unit largestUnitPossible = IngredientContainer.largestContainerUnit(resultIngredient.getState());

        // get largest quantity for a ingredientContainer
        Quantity largestQuantityPossible = new Quantity(1L,
                largestUnitPossible);

        AlchemicIngredient resultIngredientThatFits = new AlchemicIngredient(resultIngredient.getType(), largestQuantityPossible);
        result = new IngredientContainer(largestUnitPossible, resultIngredientThatFits);
    }

}
