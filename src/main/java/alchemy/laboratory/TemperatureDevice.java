package alchemy.laboratory;

import alchemy.Temperature;

/**
 * An interface for devices that have a configurable temperature
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public interface TemperatureDevice {

    /**
     * Return the configured temperature of this temperature device
     */
    Temperature getTemperatureTarget();

    /**
     * Set the configured temperature of this temperature device
     *
     * @param temperature The new temperature for this device
     *
     * @throws IllegalArgumentException The given temperature must be effective
     *                                  | temperature == null
     *
     * @post The temperature of this device is set to the given temperature
     * | new.getTemperature() == temperature
     */
    void setTemperatureTarget(Temperature temperature) throws IllegalArgumentException;
}