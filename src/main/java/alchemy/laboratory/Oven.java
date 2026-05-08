package alchemy.laboratory;

import alchemy.Temperature;

/**
 * A class for ovens
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public class Oven extends SingleContainerDevice implements TemperatureDevice {

    /**
     * The temperature target of the oven
     */
    private Temperature temperatureTarget;


    /**
     * Initialise a new oven device
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException The given laboratory must be effective
     *                                  | laboratory == null
     * @post The laboratory of this device is set to the given laboratory
     * | new.getLaboratory() == laboratory
     */
    public Oven(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     * Execute this oven.
     *
     * @post If the target temperature is colder than the ingredient temperature,
     *       the ingredient temperature does not change.
     * @post If the target temperature is hotter than or equal to the ingredient temperature,
     *       the ingredient is heated to the target temperature.
     */
    @Override
    public void execute() {

        long ingredientColdness = this.getDeviceContent().getColdness();
        long ingredientHotness = this.getDeviceContent().getHotness();
        Temperature ingredientTemp = new Temperature(ingredientColdness, ingredientHotness);


        // Check if the ingredient is colder or equal to the targetTemperature
        if (ingredientTemp.isHotterThan(temperatureTarget)) {
            // do nothing
            return;
        }

        // calculate cooling amount
        long coolAmount = ingredientTemp.difference(temperatureTarget);

        // cool the ingredient by the amount
        this.getDeviceContent().heat(coolAmount);

        // can't throw since we put 1 container in so we get the same amount out
        createResultContainer(this.getDeviceContent());
        emptyDeviceContent();

    }

    /**
     * Gets the temperature target
     *
     * @return the temperature this oven will reach when executed
     */
    @Override
    public Temperature getTemperatureTarget() {
        if (temperatureTarget == null) {
            return null;
        }
        return new Temperature(temperatureTarget);
    }

    /**
     * Sets the temperature target to the given temperature
     *
     * @param temperature The new temperature target for this device, that the device will reach when executed
     *
     * @throws IllegalArgumentException The given temperature must be effective
     *                                  | temperature == null
     * @post The temperature target of this device has the same coldness and hotness as the given temperature
     *       | new.getTemperatureTarget().getColdness() == temperature.getColdness()
     *       | && new.getTemperatureTarget().getHotness() == temperature.getHotness()
     */
    @Override
    public void setTemperatureTarget(Temperature temperature) throws IllegalArgumentException {
        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        temperatureTarget = new Temperature(temperature);
    }
}
