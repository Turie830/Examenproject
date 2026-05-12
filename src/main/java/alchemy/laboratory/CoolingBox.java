package alchemy.laboratory;

import alchemy.Temperature;
import alchemy.ingredients.AlchemicIngredient;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class for cooling boxes
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public class CoolingBox extends SingleContainerDevice implements TemperatureDevice {

    /**
     * The temperature target of the cooling box
     */
    private Temperature temperatureTarget;


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
    public CoolingBox(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     * Execute this cooling box.
     *
     * @post If the target temperature is hotter than the ingredient temperature,
     *       the ingredient temperature does not change.
     * @post If the target temperature is colder than or equal to the ingredient temperature,
     *       the ingredient is cooled to the target temperature.
     * @throws IllegalStateException The device must have an ingredient to cool
     *      | TODO getDeviceContent is protected so what expression?
     * @throws IllegalStateException The device must have a target temperature
     *      | getTemperatureTarget() == null
     */
    @Override
    public void execute() throws IllegalStateException {
        AlchemicIngredient ingredient = this.getActualDeviceContent();

        if (ingredient == null) {
            throw new IllegalStateException("The device should have ingredients in it.");
        }
        if (getTemperatureTarget() == null) {
            throw new IllegalStateException("The temperature target should not be null.");
        }

        long ingredientColdness = ingredient.getColdness();
        long ingredientHotness = ingredient.getHotness();
        Temperature ingredientTemp = new Temperature(ingredientColdness, ingredientHotness);


        // Check the ingredient needs to be cooled
        if (ingredientTemp.isHotterThan(temperatureTarget)) {
            // calculate cooling amount
            long coolAmount = ingredientTemp.difference(temperatureTarget);

            // cool the ingredient by the amount
            ingredient.cool(coolAmount);
            return;
        }


        // can't throw since we put 1 container in so we get the same amount out
        createResultContainer(ingredient);
        emptyDeviceContent();
    }

    /**
     * Gets the temperature target
     *
     * @return the temperature this coolbox will reach when executed
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
