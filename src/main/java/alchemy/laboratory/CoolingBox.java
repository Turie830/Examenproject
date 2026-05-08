package alchemy.laboratory;

import alchemy.Temperature;
import alchemy.ingredients.IngredientContainer;

public class CoolingBox extends SingleContainerDevice implements TemperatureDevice {

    /**
     * The temperature target of the cooling box
     */
    private Temperature temperatureTarget;

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
    public CoolingBox(Laboratory laboratory) {
        super(laboratory);
    }

    // TODO verbeter comment

    /**
     * Runs the coolbox
     *
     * @post if the target temperature is hotter than the ingredient temperature,
     * the ingredient temperature does not change, else cool to the targetTemperature
     */
    @Override
    public void execute() {

        long ingredientColdness = this.getDeviceContent().getColdness();
        long ingredientHotness = this.getDeviceContent().getHotness();
        Temperature ingredientTemp = new Temperature(ingredientColdness, ingredientHotness);


        // Check if the ingredient is colder or equal to the targetTemperature
        if (ingredientTemp.isColderThan(temperatureTarget)) {
            // do nothing
            return;
        }

        // calculate cooling amount
        long coolAmount = ingredientTemp.difference(temperatureTarget);

        // cool the ingredient by the amount
        this.getDeviceContent().cool(coolAmount);
    }

    /**
     * Gets the temperature target
     *
     * @return the temperature this coolbox will reach when executed
     */
    @Override
    public Temperature getTemperatureTarget() {
        return temperatureTarget;
    }

    /**
     * Sets the temperature target to the given temperature
     *
     * @param temperature The new temperature target for this device, that the device will reach when executed
     *                    <p>
     *                    TODO Do I need to copy these from the interface?
     * @throws IllegalArgumentException The given temperature must be effective
     *                                  | temperature == null
     * @post The temperature of this device is set to the given temperature
     * | new.getTemperature() == temperature
     */
    @Override
    public void setTemperatureTarget(Temperature temperature) throws IllegalArgumentException {
        if (temperature == null) {
            throw new IllegalArgumentException("Temperature cannot be null");
        }

        temperatureTarget = temperature;
    }
}
