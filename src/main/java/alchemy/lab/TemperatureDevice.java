package alchemy.lab;

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
     *
     * @return A copy of the configured temperature of this temperature device
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
     * @post The temperature target of this device has the same coldness and hotness as the given temperature
     *       | new.getTemperatureTarget().getColdness() == temperature.getColdness()
     *       | && new.getTemperatureTarget().getHotness() == temperature.getHotness()
     */
    void setTemperatureTarget(Temperature temperature) throws IllegalArgumentException;
}
