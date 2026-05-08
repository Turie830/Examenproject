package alchemy.laboratory;

import alchemy.ingredients.IngredientContainer;
import be.kuleuven.cs.som.annotate.Basic;

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
     *
     * @note A laboratory currently does not keep track of its devices.
     *       Therefore, this association is only stored from device to laboratory.
     */
    private Laboratory laboratory;

    /**
     * Initialise a new device
     *
     * @param laboratory
     *      The laboratory this device is located in
     *
     * @post The laboratory of this device is set to the given laboratory
     *      | new.getLaboratory() == laboratory
     *
     * @throws IllegalArgumentException
     *      The given laboratory must be effective
     *      | laboratory == null
     */
    public Device(Laboratory laboratory) {
        if (laboratory == null) {
            throw new IllegalArgumentException("Laboratory object can't be null");
        }
        this.laboratory = laboratory;
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

}
