package alchemy.laboratory;

import alchemy.ingredients.AlchemicIngredient;

/**
 * An abstract class for devices
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 * @invar TODO
 * @invar The laboratory is effective
 * | getLaboratory() != null
 */
public abstract class Device {

    /**
     * A variable for storing the Laboratory this device sits in
     */
    // TODO final? can this device be moved?
    private Laboratory laboratory;

    /**
     * A variable for storing the result
     */
    private AlchemicIngredient result;

    /**
     * Initialise a new Device
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException laboratory must be effective
     *                                  | laboratory != null
     */
    public Device(Laboratory laboratory) {
        if (laboratory == null) {
            throw new IllegalArgumentException("Laboratory object can't be null");
        }
        // todo set the laboratory (can the device be moved from lab?)
    }
}
