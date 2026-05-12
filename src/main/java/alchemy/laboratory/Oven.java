package alchemy.laboratory;

import alchemy.Temperature;
import alchemy.ingredients.AlchemicIngredient;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Random;

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
    @Raw
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

        // Check the ingredient needs to be heated
        if (ingredientTemp.isColderThan(temperatureTarget)) {
            // calculate heating amount
            long heatAmount = ingredientTemp.difference(temperatureTarget);

            // can be 5 of (either direction)
            Random rand = new Random();
            int n = rand.nextInt(11) - 5; // random between 0 and 10 (inclusive) then shift 5 left

            // heat the ingredient by the amount
            ingredient.heat(heatAmount + n);
            createResultContainer(ingredient);
        }

        // can't throw since we put 1 container in so we get the same amount out
        createResultContainer(ingredient);
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
