package alchemy.lab;

import alchemy.Temperature;
import alchemy.Unit;
import alchemy.recipes.IngredientRecipeStep;
import alchemy.recipes.Operation;
import alchemy.recipes.Recipe;
import alchemy.recipes.RecipeStep;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for laboratories.
 *
 * A laboratory has a fixed capacity (in storerooms),
 * stores alchemic ingredients, and has a maximum of one
 * device of every kind. Devices are linked bidirectionally to their
 * laboratory (a device registers itself in the laboratory at construction).
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 * @invar The storerooms capacity of every laboratory is positive.
 * | getStorerooms() >= 0
 * @invar Each laboratory has maximum one device of each type.
 * | for each type in Class<? extends Device>:
 * |     count(d in devices | d.getClass() == type) <= 1
 * @invar Every device in a laboratory references that laboratory back (bidirectional link).
 * | for each d in devices:
 * |     d.getLaboratory() == this
 * @invar No two different ingredients in a laboratory share the same simple name.
 * Same-name ingredients are merged on storage, so they always appear as one.
 * | for any two distinct ingredients a and b in getIngredients():
 * |     a.getSimpleName() is not equal to b.getSimpleName()
 * @invar The total stored amount per state never exceeds the capacity for that state.
 * | for each state in State.values():
 * |     getUsedAmountInLowestUnit(state) <= getCapacityInLowestUnit(state)
 */

public class Laboratory {

    /**
     * Variable storing the storerooms capacity of this laboratory.
     * Final means it is set once in the constructor and never changes.
     */
    private final int storerooms;

    /**********************************
     * CAPACITY
     **********************************/

    /**
     * The list of ingredients stored in this laboratory.
     *
     * Per simple name there is a maximum of one ingredient in this list.
     * (Because two ingredients with the same name merge into one).
     *
     * @invar No two ingredients share the same simple name.
     * @invar Every ingredient is effective (not null).
     */
    private final List<AlchemicIngredient> ingredients = new ArrayList<>();


    /**
     * Return the storerooms capacity of this laboratory.
     */
    @Basic
    public int getStorerooms() {
        return storerooms;
    }


    /**
     * The devices registered in this laboratory.
     *
     * @invar Every device is effective
     * | for each device in devices:
     * |     device != null
     * @invar Every device references this laboratory.
     * | for each device in devices:
     * |     device.getLaboratory() == this
     * @invar There is at most one device of each concrete class.
     * | for each device1, device2 in devices:
     * |     device1 == device2 || device1.getClass() != device2.getClass()
     */
    private final List<Device> devices = new ArrayList<>();


    /**
     * Initialize this new laboratory with the given storerooms capacity,
     * with no ingredients and no devices.
     *
     * @param storerooms The number of storerooms of capacity for this laboratory (>=0).
     * @throws IllegalArgumentException The given number of storerooms is negative.
     *                                  | storerooms < 0
     * @post The number of storerooms of this new laboratory equals the given amount.
     * | new.getStorerooms() == storerooms
     * @post This new laboratory has no ingredients.
     * | new.getNbOfIngredients() == 0
     * @post This new laboratory has no devices.
     * | new.getNbDevices() == 0
     */
    @Raw
    public Laboratory(int storerooms) throws IllegalArgumentException {
        if (storerooms < 0) {
            throw new IllegalArgumentException("Storerooms cannot be negative");
        }
        this.storerooms = storerooms;
    }


    /**
     * find the smallest container unit for the state of which
     * the unit capacity is large enough to hold the requested amount.
     *
     * @return the smallest container unit for the given state
     * or null if the largest container unit is still too small.
     *
     */
    private static Unit smallestContainerUnitFor(long amountInLowest, State state) {
        Unit best = null;
        for (Unit u : Unit.values()) {
            if (!u.isValidFor(state)) continue;
            if (!IngredientContainer.isValidCapacityUnit(u)) continue;
            long cap = u.getFactorToBaseUnit(state);
            if (cap < amountInLowest) continue;
            if (best == null || cap < best.getFactorToBaseUnit(state)) {
                best = u;
            }
        }
        return best;
    }


    /**
     * Ingredients
     */

    /**
     * Return the total capacity of this laboratory in spoons
     *
     * @return The number of storerooms multiplied by how many lowest-units fit in one storeroom.
     * | result = getStorerooms() * Unit.STOREROOM.getFactorToBaseUnit(State.POWDER) / Unit.SPOON.getFactorToBaseUnit(State.POWDER)
     *
     */
    public double getCapacityInSpoonFractions() throws IllegalArgumentException {

        // should never be a fraction since storeroom is larger than spoon and we always have a natural number of storerooms
        // this is state independent, that's why we chose State.POWDER
        return (double) (storerooms * Unit.STOREROOM.getFactorToBaseUnit(State.POWDER))
                / Unit.SPOON.getFactorToBaseUnit(State.POWDER);
    }


    /**
     * Get the number of ingredients in the laboratory
     *
     * @return The number of ingredients in the laboratory
     */
    public int getNbOfIngredients() {
        return ingredients.size();
    }


    /**
     * Get the amount in spoon fractions
     *
     * @param state The state we want the total in (= LIQUID or POWDER).
     *
     * @return The sum of the amounts of every ingredient
     * in this laboratory, in the lowest unit.
     * | result ==
     * |   sum of ing.getAmountInLowestUnit() for each ing in getIngredients()
     *
     * @throws IllegalArgumentException the state must be effective
     *                                  | state == null
     *
     */
    public double getUsedAmountInSpoonFractions(State state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

        double total = 0L;
        for (AlchemicIngredient ing : ingredients) {
            if (ing.getState() == state) {
                total += ing.getAmountInSpoonFractions();
            }
        }
        return total;
    }

    /**
     * Check if this laboratory has enough capacity to add
     * the given ingredient or not.
     *
     * An ingredient can be liquid or powder. hasRoomFor looks at how much is already stored
     * in that state, add the new amount and check that it stays below the capacity.
     *
     * @param ingredient The ingredient check room for.
     * @return False if the given ingredient is not effective.
     * | if (ingredient == null) then result == false
     * @return True if (currently used) + (new amount) is less then or equal to the capacity.
     * everything is calculated in the lowest unit for the ingredient's state.
     * | result ==
     * |   (getUsedAmountInLowestUnit(ingredient.getState())
     * |    + ingredient.getAmountInLowestUnit())
     * |   <= getCapacityInLowestUnit(ingredient.getState())
     */
    public boolean hasRoomFor(AlchemicIngredient ingredient) {
        if (ingredient == null) {
            return false;
        }
        State state = ingredient.getState();

        double used = getUsedAmountInSpoonFractions(state);
        double extra = ingredient.getAmountInSpoonFractions();

        return used + extra <= getCapacityInSpoonFractions();
    }

    /**
     * Check if an ingredient with the given name exists in this laboratory.
     * The name can be a simple name or a special name.
     *
     * @param name The name to look up.
     * @return False if the given name is null or if no ingredient in this
     * laboratory has the given name. True otherwise.
     */
    public boolean hasIngredient(String name) {
        if (name == null) {
            return false;
        }
        try {
            getIngredient(name);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Store the contents of the given ingredient container in this laboratory.
     * After this call the container is empty ('container wordt vernietigd').
     *
     * The ingredient gets brought to its standard temperaturen using Oven or CoolingBox
     * If an ingredient with the same simple name already exists in this laboratory, merge the two via the Kettle.
     *
     * @param container The container whose contents should be stored.
     * @throws IllegalArgumentException The given container is not effective.
     *                                  | container == null
     * @throws IllegalArgumentException The given container is empty.
     *                                  | container.isEmpty()
     * @throws IllegalStateException    There is not enough remaining capacity for the ingredient.
     *                                  | !hasRoomFor(container.getIngredient())
     * @post After this call, the given container is empty.
     * | container.isEmpty()
     * @post The ingredient that was in the container is now stored in this laboratory.
     *
     * @post the ingredient container is destroyed
     *      | container.isDestroyed()
     */
    public void store(IngredientContainer container) throws IllegalArgumentException, IllegalStateException {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container is empty");
        }

        AlchemicIngredient toStore = container.getIngredient();
        if (!hasRoomFor(toStore)) {
            throw new IllegalStateException("Not enough capacity in laboratory");
        }

        toStore = bringToStandardTemperature(toStore);

        storeNoTempChange(new IngredientContainer(container.getCapacityUnit(), toStore));
        container.emptyDestroy();
    }

    /**
     * Store the contents of the given ingredient container in this laboratory.
     * After this call the container is empty ('container wordt vernietigd').
     * <p>
     * If an ingredient with the same simple name already exists in this laboratory, merge the two via the Kettle.
     *
     * @param container The container whose contents should be stored.
     * @throws IllegalArgumentException The given container is not effective.
     *                                  | container == null
     * @throws IllegalArgumentException The given container is empty.
     *                                  | container.isEmpty()
     * @throws IllegalStateException    There is not enough remaining capacity for the ingredient.
     *                                  | !hasRoomFor(container.getIngredient())
     * @post After this call, the given container is empty.
     * | container.isEmpty()
     * @post The ingredient that was in the container is now stored in this laboratory.
     */
    private void storeNoTempChange(IngredientContainer container) throws IllegalArgumentException, IllegalStateException {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container is empty");
        }

        AlchemicIngredient toStore = container.getIngredient();
        if (!hasRoomFor(toStore)) {
            throw new IllegalStateException("Not enough capacity in laboratory");
        }

        String simpleName = toStore.getSimpleName();
        if (!hasIngredient(simpleName)) {
            ingredients.add(toStore);
            container.empty();
        } else {
            // An ingredient with the same name already exists: merge them via the Kettle.
            AlchemicIngredient existing = getIngredient(simpleName);
            ingredients.remove(existing);
            List<AlchemicIngredient> toMerge = new ArrayList<>();
            toMerge.add(existing);
            toMerge.add(toStore);
            AlchemicIngredient merged = mixAll(toMerge);
            ingredients.add(merged);
        }
    }


    /**
     * Return a boolean if the ingredient needs cooling
     *
     * @param ingredient the ingredient to check if it needs cooling
     * @return true if the ingredient is hotter than the standard temperature
     * @pre ingredient is effective
     *
     */
    @Model
    private boolean needsCooling(AlchemicIngredient ingredient) {
        long coldness = ingredient.getTemperature()[0];
        long hotness = ingredient.getTemperature()[1];
        Temperature temp = new Temperature(coldness, hotness);

        long standardColdness = ingredient.getType().getStandardTemperature()[0];
        long standardHotness = ingredient.getType().getStandardTemperature()[1];
        Temperature standardTemp = new Temperature(standardColdness, standardHotness);

        return temp.isHotterThan(standardTemp);
    }

    /**
     * Return a boolean if the ingredient needs heating
     *
     * @param ingredient the ingredient to check if it needs heating
     * @return true if the ingredient is colder than the standard temperature
     * @pre the ingredient is effective
     */
    @Model
    private boolean needsHeating(AlchemicIngredient ingredient) {
        long coldness = ingredient.getTemperature()[0];
        long hotness = ingredient.getTemperature()[1];
        Temperature temp = new Temperature(coldness, hotness);

        long standardColdness = ingredient.getType().getStandardTemperature()[0];
        long standardHotness = ingredient.getType().getStandardTemperature()[1];
        Temperature standardTemp = new Temperature(standardColdness, standardHotness);

        return temp.isColderThan(standardTemp);
    }


    /**
     * Bring the ingredient to the standard temperature using the Oven or CoolingBox
     *
     * @param ingredient The ingredient to bring to standard temperature
     * @return the ingredient brought to the standard temperature using the cooling box, oven
     * or nothing if the ingredient is already at standard temperature
     * @throws IllegalArgumentException the ingredient should be effective
     *                                  | ingredient == null
     * @throws IllegalStateException    if the ingredient has to be cooled it must have the cooling box device for it
     *                                  | (needsCooling(ingredient) && !hasDevice(CoolingBox.class))
     * @throws IllegalStateException    if the ingredient has to be  heated it must have the oven device for it
     *                                  | (needsHeating(ingredient) && !hadDevice(Oven.class))
     */
    private AlchemicIngredient bringToStandardTemperature(AlchemicIngredient ingredient) throws IllegalArgumentException, IllegalStateException {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }

        long coldness = ingredient.getTemperature()[0];
        long hotness = ingredient.getTemperature()[1];
        Temperature temp = new Temperature(coldness, hotness);

        long standardColdness = ingredient.getType().getStandardTemperature()[0];
        long standardHotness = ingredient.getType().getStandardTemperature()[1];
        Temperature standardTemp = new Temperature(standardColdness, standardHotness);

        if (needsCooling(ingredient)) {
            if (!hasDevice(CoolingBox.class)) throw new IllegalStateException("No CoolingBox in this laboratory");
            // cool ingredient
            return coolBy(ingredient, temp.difference(standardTemp));
        }
        if (needsHeating(ingredient)) {
            if (!hasDevice(Oven.class)) throw new IllegalStateException("No Oven in this laboratory");
            // heat ingredient by too much
            heatBy(ingredient, temp.difference(standardTemp) + 5);

            // cool the ingredient back to the standard temperature
            // this way we garantee it's the exact standard temperature
            long coldness2 = ingredient.getTemperature()[0];
            long hotness2 = ingredient.getTemperature()[1];
            Temperature temp2 = new Temperature(coldness, hotness);

            coolBy(ingredient, temp2.difference(standardTemp));
        }
        // does not need to be cooled or heated
        return ingredient;
    }

    /**
     * Take the requested quantity of the ingredient with the given name out of
     * this laboratory and return it inside a new container.
     * <p>
     * The container we return uses the smallest container unit (for the ingredient)
     * that the amount of ingredient still fits into.
     *
     * @param name     The (simple or special) name of the ingredient to put in a container.
     * @param quantity How much of it should be put in a container.
     * @return A new container holding an ingredient of the same type as the one
     * that was in the laboratory, with the requested quantity.
     * @throws IllegalArgumentException The given name or quantity is not effective, no ingredient with the
     *                                  given name is in this laboratory, the requested unit does
     *                                  not match the ingredient's state, or the requested amount is greater than
     *                                  the available amount (in the Laboratory).
     * @post The total stored amount of the matched ingredient
     * has decreased by the requested amount.
     */
    public IngredientContainer request(String name, Quantity quantity) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        // getIngredient throws IllegalArgumentException if there is no ingredient as in request()
        AlchemicIngredient existing = getIngredient(name);

        State state = existing.getState();
        // The requested unit must be valid for the ingredient's state.
        if (!quantity.getUnit().isValidFor(state)) {
            throw new IllegalArgumentException(
                    "Requested quantity unit is not valid for the ingredient's state");
        }

        long requested = quantity.toLowestUnit(state);
        long available = existing.getAmountInLowestUnit();
        if (requested > available) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        Unit containerUnit = smallestContainerUnitFor(requested, state);
        if (containerUnit == null) {
            throw new IllegalArgumentException(
                    "Requested quantity does not fit in any container");
        }

        // Create a new ingredient with the requested quantity and put it in a new container.
        AlchemicIngredient out = new AlchemicIngredient(existing.getType(), quantity);
        IngredientContainer result = new IngredientContainer(containerUnit, out);

        // The amount left stays in the laboratory.
        replaceWithRemaining(existing, available - requested, state);
        return result;
    }


    /**
     * Replace the old ingrediënt with a new version that only now contains the remaining amount.
     * If none of the ingredient is left, remove the ingredient.
     *
     * @param existing the ingredient already existing in the laboratory
     *
     * @param remaining the amount that will remain in the laboratory
     *
     * @param state The state of the existing ingredient
     *
     * @post the old ingredient is replaced with a new version that only now contains the remaining amount
     */
    private void replaceWithRemaining(AlchemicIngredient existing, long remaining, State state) {
        ingredients.remove(existing);
        if (remaining <= 0) {
            return;
        }
        Quantity remainingQty = new Quantity(remaining, Unit.getBaseUnit(state));
        ingredients.add(new AlchemicIngredient(existing.getType(), remainingQty));
    }


    /**
     * Take the stored amount of the ingredient with the given name out of
     * this laboratory and return it inside the largest container unit that is valid for
     * the standard state of the ingredient.
     * <p>
     * If the laboratory holds more than the new container can hold, the
     * amount that can't fit is gone.
     *
     * @param name The name of the ingredient to retrieve, simple or special.
     *
     * @return A new container holding the ingredient.
     *
     * @throws IllegalArgumentException The given name is not effective or there is no ingredient with that name.
     *
     * @post After this call, no ingredient with the same name is still in this lab.
     * | !new.hasIngredient(name)
     */
    public IngredientContainer request(String name) throws IllegalArgumentException {

        AlchemicIngredient existing = getIngredient(name);

        State state = existing.getState();
        Unit largest = IngredientContainer.largestContainerUnit(state);
        long containerCapacity = largest.getFactorToBaseUnit(state);
        long available = existing.getAmountInLowestUnit();
        // Math.min takes as the most amount the container can hold, the rest is lost.
        long taken = Math.min(available, containerCapacity);

        Quantity outQuantity = new Quantity(taken, Unit.getBaseUnit(state));
        AlchemicIngredient outIngred = new AlchemicIngredient(existing.getType(), outQuantity);
        IngredientContainer result = new IngredientContainer(largest, outIngred);

        // remove the ingredient from the laboratory
        ingredients.remove(existing);
        return result;

    }




    /**
     * Devices --> bidirectional
     */

    /**
     * Return the ingredient in this laboratory whose simple name or
     * special name (mixed ingredient) matches the given name.
     *
     * @param name The name to look up.
     * @throws IllegalArgumentException The given name is not effective, or no ingredient with that
     *                                  name is in this laboratory.
     *                                  | name == null || !hasIngredient(name)
     */
    public AlchemicIngredient getIngredient(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        for (AlchemicIngredient ing : ingredients) {
            // simple name
            if (name.equals(ing.getSimpleName())) {
                return ing;
            }
            // special name
            if (ing.getType().isMixed()
                    && ing.getType().getName().hasSpecialName()
                    && name.equals(ing.getType().getName().getSpecialName())) {
                return ing;
            }
        }
        throw new IllegalArgumentException("No ingredient with the name " + name);
    }

    /**
     * Register this device with this laboratory.
     * This method is called automatically from the Device constructor, so there is a
     * bidirectional link.
     * <p>
     * Only callable from alchemy.laboratory.
     *
     * @param device The device to be registered.
     * @throws IllegalArgumentException The given device is not effective or it does not reference this laboratory.
     * @throws IllegalStateException    A device of the same concrete class is already registered in this laboratory.
     * @post The given device is now registered as the device of its kind in this laboratory.
     */
    void registerDevice(@Raw Device device) throws IllegalArgumentException, IllegalStateException {
        if (device == null) {
            throw new IllegalArgumentException("Device can't be null");
        }
        if (device.getLaboratory() != this) {
            throw new IllegalArgumentException("Device is not referencing this laboratory");
        }
        if (hasDeviceOfClass(device.getClass())) {
            throw new IllegalStateException("A device of this class is already present in this laboratory");
        }

        devices.add(device);
    }

    /**
     * Check whether this laboratory already has a device of the given concrete class
     *
     * @param deviceClass The device class to check
     * @return True if a registered device has exactly the given class.
     */
    private boolean hasDeviceOfClass(Class<? extends Device> deviceClass) {
        for (Device registeredDevice : devices) {
            if (registeredDevice.getClass() == deviceClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether the given device is registered in this laboratory.
     *
     * @param device The device to check.
     * @return True if this laboratory contains the exact given device object.
     */
    public boolean hasAsDevice(Device device) {
        return devices.contains(device);
    }

    /**
     * Return the number of devices registered in this laboratory.
     */
    @Basic
    public int getNbDevices() {
        return devices.size();
    }

    /**
     * Return the device of the given concrete class in this laboratory.
     *
     * @param deviceClass The concrete device class to look up.
     * @return The registered device of the given concrete class, or null if there is none.
     */
    @Basic
    public <T extends Device> T getDevice(Class<T> deviceClass) {
        if (deviceClass == null) {
            throw new IllegalArgumentException("Device class cannot be null");
        }
        for (Device device : devices) {
            if (device.getClass() == deviceClass) {
                return deviceClass.cast(device);
            }
        }
        return null;
    }

    /**
     * Check if this laboratory has a device of the given concrete class.
     *
     * @param deviceClass The concrete device class to check.
     * @return True if a device of the given concrete class is registered.
     */
    public boolean hasDevice(Class<? extends Device> deviceClass) {
        if (deviceClass == null) {
            return false;
        }
        return hasDeviceOfClass(deviceClass);
    }


    /**********************************
     * RECIPES
     **********************************/


    /**
     * Execute the given recipe in this laboratory the given number of times.
     *
     * Walk through the recipe step by step, scale every ingredient
     * by the given factor, and use the devices in this laboratory to
     * heat, cool, mix or add ingredients. The final mixture is stored
     * in the laboratory.
     *
     * If at some point there is not enough of an ingredient, the recipe-execution stops.
     * All of the ingredients that were already taken out of the laboratory
     * are converted to standard temperature again and stored back.
     *
     * @param recipe The recipe to execute.
     * @param factor How many times to execute the recipe.
     *               Must be strictly positive.
     * @throws IllegalArgumentException The recipe is not effective or the factor is not strictly positive.
     * @throws IllegalStateException    A required device is not in this laboratory.
     */
    public void execute(Recipe recipe, int factor) throws IllegalArgumentException, IllegalStateException {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe can't be null");
        }
        if (factor <= 0) {
            throw new IllegalArgumentException("Factor must be strictly positive");
        }

        // Set of all ingredients which are not yet mixed.
        // After a MIX, this list contains exactly only the mixture anymore.
        List<AlchemicIngredient> IngredientsSet = new ArrayList<>();

        for (int i = 0; i < recipe.getNbSteps(); i++) {
            RecipeStep step = recipe.getStepAt(i);
            Operation op = step.getOperation();

            try {
                if (op == Operation.ADD) {
                    IngredientRecipeStep addStep = (IngredientRecipeStep) step; // (IngredientRecipeStep) with brackets, to be able to use as IngredientRecipeStep
                    Quantity scaled = new Quantity(addStep.getIngredientQuantity().getAmount() * factor,
                            addStep.getIngredientQuantity().getUnit());
                    IngredientContainer takenFromLab = request(
                            addStep.getIngredientName().getSimpleName(), scaled);
                    IngredientsSet.add(takenFromLab.getIngredient());
                } else if (op == Operation.HEAT) {
                    if (IngredientsSet.isEmpty()) {
                        throw new IllegalStateException("No ingredient to be heated");
                    }
                    if (!hasDevice(Oven.class)) {
                        throw new IllegalStateException("No Oven in this laboratory");
                    }
                    AlchemicIngredient last = IngredientsSet.removeLast();
                    IngredientsSet.add(heatBy(last, 10));   // standard 10

                } else if (op == Operation.COOL) {
                    if (IngredientsSet.isEmpty()) {
                        throw new IllegalStateException("No ingredient to be cooled");
                    }
                    if (!hasDevice(CoolingBox.class)) {
                        throw new IllegalStateException("No CoolingBox in this laboratory");
                    }
                    AlchemicIngredient last = IngredientsSet.removeLast();
                    IngredientsSet.add(coolBy(last, 10));

                } else if (op == Operation.MIX) {
                    if (IngredientsSet.size() >= 2) {
                        if (!hasDevice(Kettle.class)) {
                            throw new IllegalStateException("No Kettle in this laboratory");
                        }
                        AlchemicIngredient mixture = mixAll(IngredientsSet);
                        IngredientsSet.clear();
                        IngredientsSet.add(mixture);
                    }
                    // If there is exactly one ingredient, do nothing.
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                // if fail; for ex. not enough ingredient, restore all to how it was before execute
                // restore the temperature to standard temperature
                storeBackAllRestoreTemp(IngredientsSet);
                return;
            }
        }
        // success
        // don't restore the temperature
        storeBackAll(IngredientsSet);
    }


    /**
     * Use the Oven to heat the given ingredient by the given amount
     * and return the heated ingredient.
     * <p>
     * The oven is set to standard temperature + amount before running, so the
     * ingredient ends up exactly that amount warmer then the standard temperature.
     *
     * @param ingredient The ingredient to heat, not null.
     * @param amount     The number of temperature units to add to the hotness.
     *                   Must be strictly positive.
     * @return The ingredient after it has been heated by the given amount.
     * @pre There is an Oven in this laboratory (hasDevice(Oven.class) == true).
     */
    private AlchemicIngredient heatBy(AlchemicIngredient ingredient, long amount) {
        State state = ingredient.getState();
        // Ingredient in container before putting it in the Oven
        Unit containerUnit = smallestContainerUnitFor(ingredient.getAmountInLowestUnit(), state);
        IngredientContainer container = new IngredientContainer(containerUnit, ingredient);


        long signedTemp = -ingredient.getColdness() + ingredient.getHotness() + amount;

        long targetColdness;
        long targetHotness;

        if (signedTemp < 0) {
            targetColdness = -signedTemp;
            targetHotness = 0;
        } else {
            targetColdness = 0;
            targetHotness = signedTemp;
        }

        Oven oven = getDevice(Oven.class);
        oven.setTemperatureTarget(new Temperature(targetColdness, targetHotness));
        // container in Oven
        oven.add(container);
        // execute oven
        oven.execute();
        // getresult returns a container
        return oven.getResult().getIngredient();
    }


    /**
     * Use the CoolingBox to cool the given ingredient by the given amount
     * and return the cooled ingredient.
     * <p>
     * The CoolingBox is set to standard temperature + amount before running, so the
     * ingredient ends up exactly that amount colder than the standard temperature.
     *
     * @param ingredient The ingredient to cool, not null.
     * @param amount     The number of temperature units to add to the coldness.
     *                   Must be strictly positive.
     * @return The ingredient after it has been cooled by the given amount.
     * @pre There is a CoolingBox in this laboratory (hasDevice(CoolingBox.class) == true).
     */
    private AlchemicIngredient coolBy(AlchemicIngredient ingredient, long amount) {
        State state = ingredient.getState();
        Unit containerUnit = smallestContainerUnitFor(ingredient.getAmountInLowestUnit(), state);
        IngredientContainer container = new IngredientContainer(containerUnit, ingredient);

        long signedTemp = -ingredient.getColdness() + ingredient.getHotness() - amount;

        long targetColdness;
        long targetHotness;

        if (signedTemp < 0) {
            targetColdness = -signedTemp;
            targetHotness = 0;
        } else {
            targetColdness = 0;
            targetHotness = signedTemp;
        }


        CoolingBox coolingBox = getDevice(CoolingBox.class);

        coolingBox.setTemperatureTarget(new Temperature(targetColdness, targetHotness));

        coolingBox.add(container);
        coolingBox.execute();
        return coolingBox.getResult().getIngredient();
    }

    /**
     * Add all ingredients in the given list to the kettle, run the kettle
     * and return the result-mixture.
     *
     * Every ingredient is placed in the smallest container that can fit it
     * before the ingredient is added to the kettle.
     *
     * @param toMix The list of ingredients to mix together. Must have at least 2 elements.
     * @return The mixture that comes out of the kettle. (one thing)
     * @throws IllegalStateException the labo must have a kettle
     *                               | !hasDevice(Kettle.class)
     * @pre toMix contains at least 2 ingredients.
     */
    private AlchemicIngredient mixAll(List<AlchemicIngredient> toMix) {
        if (!hasDevice(Kettle.class)) {
            throw new IllegalStateException("No Kettle in this laboratory");
        }

        Kettle kettle = getDevice(Kettle.class);
        for (AlchemicIngredient ing : toMix) {
            State state = ing.getState();
            Unit containerUnit = smallestContainerUnitFor(ing.getAmountInLowestUnit(), state);
            kettle.add(new IngredientContainer(containerUnit, ing));
        }
        kettle.execute();
        return kettle.getResult().getIngredient();
    }


    /**
     * Store every ingredient that is in the given list
     * back in this laboratory.
     * <p>
     * Used when execute() succeeds or fails anywhere in the function.
     * store()
     * and merges it with the ingredient with the same name if
     * there is already one in the laboratory.
     *
     * @param remaining The list of ingredients to store back.
     *                  If empty, nothing happens.
     */
    private void storeBackAll(List<AlchemicIngredient> remaining) {
        for (AlchemicIngredient ing : remaining) {
            State state = ing.getState();
            Unit containerUnit = smallestContainerUnitFor(ing.getAmountInLowestUnit(), state);
            storeNoTempChange(new IngredientContainer(containerUnit, ing));

        }
    }

    /**
     * Store every ingredient that is in the given list
     * back in this laboratory.
     * <p>
     * Used when execute() succeeds or fails anywhere in the function.
     * store() brings each ingredient back to its standard temperature
     * and merges it with the ingredient with the same name if
     * there is already one in the laboratory.
     *
     * @param remaining The list of ingredients to store back.
     *                  If empty, nothing happens.
     *
     */
    private void storeBackAllRestoreTemp(List<AlchemicIngredient> remaining) {
        for (AlchemicIngredient ing : remaining) {
            State state = ing.getState();
            Unit containerUnit = smallestContainerUnitFor(ing.getAmountInLowestUnit(), state);
            store(new IngredientContainer(containerUnit, ing));
        }
    }


}
