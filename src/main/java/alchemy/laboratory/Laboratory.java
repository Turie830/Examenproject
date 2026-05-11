package alchemy.laboratory;

import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import alchemy.ingredients.Quantity;
import alchemy.ingredients.State;
import be.kuleuven.cs.som.annotate.Basic;
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
 * @invar The storerooms capacity of every laboratory is positive.
 *      | getStorerooms() >= 0
 *
 * @invar Each laboratory has maximum one device of each type.
 *      | for each type in Class<? extends Device>:     // ToDo ? invullen
 *      |     count(d in getDevices() | d.getClass() == type) <= 1
 *
 * @invar Every device in a laboratory references that laboratory back (bidirectional link).
 *      | for each d in getDevices():
 *      |     d.getLaboratory() == this
 *
 * @invar No two different ingredients in a laboratory share the same simple name.
 *      Same-name ingredients are merged on storage, so they always appear as one.
 *      | for any two distinct ingredients a and b in getIngredients():
 *      |     a.getSimpleName() is not equal to b.getSimpleName()
 *
 * @invar The total stored amount per state never exceeds the capacity for that state.
 *      | for each state in State.values():
 *      |     getUsedAmountInLowestUnit(state) <= getCapacityInLowestUnit(state)
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 *
 * @version 1.0
 */

public class Laboratory {

    /**
     * constructors
     */

    /**
     * Initialize this new laboratory with the given storerooms capacity,
     * with no ingredients and no devices.
     *
     * @param storerooms
     *        The number of storerooms of capacity for this laboratory (>=0).
     *
     * @post The number of storerooms of this new laboratory equals the given amount.
     *     | new.getStorerooms() == storerooms
     *
     * @post This new laboratory has no ingredients.
     *     | new.getNbIngredients() == 0
     *
     * @post This new laboratory has no devices.
     *     | new.getNbDevices() == 0
     *
     * @throws IllegalArgumentException
     *         The given number of storerooms is negative.
     *       | storerooms < 0
     */
    @Raw
    public Laboratory(int storerooms) throws IllegalArgumentException {
        if (storerooms < 0) {
            throw new IllegalArgumentException("Storerooms cannot be negative");
        }
        this.storerooms = storerooms;
    }

    /**
     * Capacity             ToDo: contoleer op totaal enzo
     */

    /**
     * Variable storing the storerooms capacity of this laboratory.
     * Final means it is set once in the constructor and never changes.
     */
    private final int storerooms;


    /**
     * Return the storerooms capacity of this laboratory.
     */
    @Basic
    public int getStorerooms() {
        return storerooms;
    }


    /**
     * Return the total capacity of this laboratory in the lowest unit
     * for the given state (liquids - drops, powders - pinches).
     *
     * @param state
     *        The state we want the capacity in (= LIQUID or POWDER).
     *
     * @return The number of storerooms multiplied by how many lowest-units fit in one storeroom.
     *       | result = getStorerooms() * Unit.STOREROOM.getFactorToBaseUnit(state)
     *
     * @throws IllegalArgumentException         ToDo: moet dit of is da er heel over?
     *         The given state is not effective.
     *       | state == null
     */
    public long getCapacityInLowestUnit(State state) throws IllegalArgumentException {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        return (long) storerooms * Unit.STOREROOM.getFactorToBaseUnit(state);
    }


    /**
     *
     * @param state
     *        The state we want the total in (= LIQUID or POWDER).
     *
     * @return The sum of the amounts of every ingredient
     *         in this laboratory, in the lowest unit.
     *         | result ==
     *         |   sum of ing.getAmountInLowestUnit() for each ing in getIngredients()
     *
     *  ToDo: moet er een throw? illegalargument?
     */
    public long getUsedAmountInLowestUnit(State state) {
        long total = 0L;
        for (AlchemicIngredient ing : ingredients) {
            if (ing.getType().getStandardState() == state) {
                total += ing.getAmountInLowestUnit();
            }
        }
        return total;
    }


    /**
     * Check if this laboratory has enough capacity to add
     * the given ingredient or not.
     *
     * An ingredient can be liquid or powder. hasRoomFor looks at how much is already stored    ToDo: is dit te 'engelse' uitleg?
     * in that state, add the new amount and check that it stays below the capacity.
     *
     * @param ingredient
     *        The ingredient check room for.
     *
     * @return False if the given ingredient is not effective.
     *       | if (ingredient == null) then result == false
     *
     * @return True if (currently used) + (new amount) is less then or equal to the capacity.
     *         everything is calculated in the lowest unit for the ingredient's state.
     *       | result ==
     *       |   (getUsedAmountInLowestUnit(ingredient.getType().getStandardState())
     *       |    + ingredient.getAmountInLowestUnit())
     *       |   <= getCapacityInLowestUnit(ingredient.getType().getStandardState())
     */
    public boolean hasRoomFor(AlchemicIngredient ingredient) {
        if (ingredient == null) {
            return false;
        }
        State state = ingredient.getType().getStandardState();

        long used = getUsedAmountInLowestUnit(state);
        long extra = ingredient.getAmountInLowestUnit();
        boolean ret = used + extra <= getCapacityInLowestUnit(state);

        return ret;
    }


    /**
     * Ingredients
     */


    /**
     * The list of ingredients stored in this laboratory.
     *
     * Per simple name there is a maximum of one ingredient in this list.
     * (Because two ingredients with the same name merge into one).
     *
     * @invar No two ingredients share the same simple name.
     *
     * @invar Every ingredient is effective (not null).
     */
    private final List<AlchemicIngredient> ingredients = new ArrayList<>();


    // ToDo: is public List<AlchemicIngredient> getIngredients() nodig?


    /**
     * Return the ingredient in this laboratory whose simple name or
     * special name (mixed ingredient) matches the given name.
     *
     * @param name
     *        The name to look up.
     *
     * @throws IllegalArgumentException
     *         The given name is not effective, or no ingredient with that
     *         name is in this laboratory.
     *       | name == null || !hasIngredient(name)
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
        throw new IllegalArgumentException("No ingredient that name");
    }


    /**
     * Check if an ingredient with the given name exists in this laboratory.
     * The name can be a simple name or a special name.
     *
     * @param name
     *        The name to look up.
     *
     * @return False if the given name is null or if no ingredient in this
     *         laboratory has the given name. True otherwise.
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
     * After this call the container is empty ('container wordt vernietigd'). ToDo: dit juiste interpreattie van vernietigen?
     *
     *
     * The ingredient gets brought to its standard temperaturen using Oven or CoolingBox
     * If an ingredient with the same simple name already exists in this laboratory, merge the two via the Kettle.
     *
     * @param container
     *        The container whose contents should be stored.
     *
     * @post After this call, the given container is empty.
     *     | container.isEmpty()
     *
     * @post The ingredient that was in the container is now stored in this laboratory.
     *
     * @throws IllegalArgumentException
     *         The given container is not effective.
     *       | container == null
     *
     * @throws IllegalArgumentException
     *         The given container is empty.
     *       | container.isEmpty()
     *
     * @throws IllegalStateException
     *         There is not enough remaining capacity for the ingredient.
     *       | !hasRoomFor(container.getIngredient())
     */
    public void store(IngredientContainer container)                    // ToDo: check deze pls, ingewikkeld
            throws IllegalArgumentException, IllegalStateException {
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
        // ToDo: bring `toStore` back to its standard temperature here, using
        //       getDevice(Oven.class) or getDevice(CoolingBox.class).
        String simpleName = toStore.getSimpleName();
        if (!hasIngredient(simpleName)) {
            ingredients.add(toStore);
        } else {
            AlchemicIngredient existing = getIngredient(simpleName);
            // TODO: mix `existing` and `toStore` via getDevice(Kettle.class) and replace
            //       `existing` in the list with the resulting merged ingredient.
            container.empty();
        }
    }


    /**
     * Take the requested quantity of the ingredient with the given name out of
     * this laboratory and return it inside a new container.
     *
     * The container we return uses the smallest container unit (for the ingredient)
     * that the amount of ingredient still fits into.
     *
     * @param name
     *        The (simple or special) name of the ingredient to put in a container.
     *
     * @param quantity
     *        How much of it should be put in a container.
     *
     * @return A new container holding an ingredient of the same type as the one
     *         that was in the laboratory, with the requested quantity.
     *
     * @post The total stored amount of the matched ingredient
     *       has decreased by the requested amount.
     *
     * @throws IllegalArgumentException
     *         The given name or quantity is not effective, no ingredient with the
     *         given name is in this laboratory, the requested unit does
     *         not match the ingredient's state, or the requested amount is greater than
     *         the available amount (in the Laboratory).
     */
    public IngredientContainer request(String name, Quantity quantity)          // ToDo: controle deze functie: is ie te (overbodig) ingewikkeld?
            throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        // getIngredient throws IllegalArgumentException if there is no ingredient as in request()
        AlchemicIngredient existing = getIngredient(name);

        State state = existing.getType().getStandardState();
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
        } // ToDo: controle

    /**
     * Take the stored amount of the ingredient with the given name out of
     * this laboratory and return it inside the largest container unit that is valid for
     * the standard state of the ingredient.
     *
     * If the laboratory holds more than the new container can hold, the
     * amount that can't fit is gone.
     *
     * @param name
     *        The name of the ingredient to retrieve, simple or special.
     *
     * @return A new container holding the ingredient.
     *
     * @post After this call, no ingredient with the same name is still in this lab.
     *     | !new.hasIngredient(name)
     *
     * @throws IllegalArgumentException
     *         The given name is not effective or there is no ingredient with that name.
     */
    public IngredientContainer request(String name) throws IllegalArgumentException{

        AlchemicIngredient existing = getIngredient(name);

        State state = existing.getType().getStandardState();
        Unit largest = largestContainerUnitFor(state);
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


    // ToDo: hulpfunctie: moet hier documentatie bij (zie komende 3 fct) ? --> zo ja, vervolledigen ToDo

    /**
     * Replace the old ingrediënt with a new version that only now contains the remaining amount.
     * If none of the ingredient is left, remove the ingredient.
     *
     * @param existing
     * @param remainingInLowest
     * @param state
     */
    private void replaceWithRemaining(AlchemicIngredient existing, long remainingInLowest, State state) {
        ingredients.remove(existing);
        if (remainingInLowest <= 0) {
            return;
        }
        Quantity remainingQty = new Quantity(remainingInLowest, Unit.getBaseUnit(state));
        ingredients.add(new AlchemicIngredient(existing.getType(), remainingQty));
    }


    /**
     * find the smallest container unit for the state of which
     * the unit capacity is large enough to hold the requested amount.
     * Return null if the largest container unit is still too small.
     */
    private static Unit smallestContainerUnitFor(long amountInLowest, State state) {
        Unit best = null;
        for (Unit u : Unit.values()) {
            if (!u.isValidFor(state)) continue;
            if (!IngredientContainer.isValidCapacityUnit(u)) continue;
            long cap = u.getFactorToBaseUnit(state);
            if (cap < amountInLowest) continue;
            // ToDo: iteratielogica? stap is wel juist maar weet niet of na || overbodig is
            if (best == null || cap < best.getFactorToBaseUnit(state)) {
                best = u;
            }
        }
        return best;
    }


    /**
     * Return the largest valid container unit for the given state.
     * = BARREL for liquids and CHEST for powders.
     */
    private static Unit largestContainerUnitFor(State state) {
        Unit best = null;
        for (Unit u : Unit.values()) {
            if (!u.isValidFor(state)) continue;
            if (!IngredientContainer.isValidCapacityUnit(u)) continue;
            if (best == null || u.getFactorToBaseUnit(state) > best.getFactorToBaseUnit(state)) {
                best = u;
            }
        }
        return best;
    }


    /**
     * Devices --> bidirectional
     */

    /**
     * The CoolingBox currently in this laboratory, null if there is no CoolingBox in this Laboratory.
     *
     * @invar If not null, the CoolingBox references this laboratory.
     *      | coolingBox == null || coolingBox.getLaboratory() == this
     */
    private CoolingBox coolingBox;

    /**
     * The Oven currently in this laboratory, null if there is no Oven in this Laboratory.
     *
     * @invar If not null, the Oven references this laboratory.
     *      | oven == null || oven.getLaboratory() == this
     */
    private Oven oven;

    /**
     * The Kettle currently in this laboratory, null if there is no Kettle in this Laboratory.
     *
     * @invar If not null, the Kettle references this laboratory.
     *      | kettle == null || kettle.getLaboratory() == this
     */
    private Kettle kettle;

    /**
     * The Transmogrifier currently in this laboratory, null if there is no Transmogrifier in this Laboratory.
     *
     * @invar If not null, the Transmogrifier references this laboratory.
     *      | transmogrifier == null || transmogrifier.getLaboratory() == this
     */
    private Transmogrifier transmogrifier;


    /**
     * Register this device with this laboratory.
     * This method is called automatically from the Device constructor, so there is a
     * bidirectional link.
     *
     * Only callable from alchemy.laboratory ToDo: correct? laten staan of weg?
     *
     * @param device
     *        The device to be registered.
     *
     * @post The given device is now registered as the device of its kind in this laboratory.
     *
     * @throws IllegalArgumentException
     *         The given device is not effective, it does not reference this laboratory
     *         or its concrete type is not a valid device type.
     *
     * @throws IllegalStateException
     *         A device of the same kind is already registered in this laboratory.
     */
    void registerDevice(Device device)
            throws IllegalArgumentException, IllegalStateException {
        if (device == null) {
            throw new IllegalArgumentException("Device can't be null");
        }
        if (device.getLaboratory() != this) {
            throw new IllegalArgumentException("Device is not referencing this laboratory");
        }

        if (device instanceof CoolingBox) {
            if (coolingBox != null) {
                throw new IllegalStateException("A CoolingBox is already present in this laboratory");
            }
            coolingBox = (CoolingBox) device;
        }
        else if (device instanceof Oven) {
            if (oven != null) {
                throw new IllegalStateException("An Oven is already present in this laboratory");
            }
            oven = (Oven) device;
        }
        else if (device instanceof Kettle) {
            if (kettle != null) {
                throw new IllegalStateException("A Kettle is already present in this laboratory");
            }
            kettle = (Kettle) device;
        }
        else if (device instanceof Transmogrifier) {
            if (transmogrifier != null) {
                throw new IllegalStateException("A Transmogrifier is already present in this laboratory");
            }
            transmogrifier = (Transmogrifier) device;
        }
        else {
            throw new IllegalArgumentException("Unknown device type");
        }
    }


    /**
     * Return the CoolingBox in this laboratory or null if there is none in this Laboratory.
     */
    @Basic
    public CoolingBox getCoolingBox() {
        return coolingBox;
    }

    /**
     * Return the Oven in this laboratory or null if there is none in this Laboratory.
     */
    @Basic
    public Oven getOven() {
        return oven;
    }

    /**
     * Return the Kettle in this laboratory or null if there is none in this Laboratory.
     */
    @Basic
    public Kettle getKettle() {
        return kettle;
    }

    /**
     * Return the Transmogrifier in this laboratory or null if there is none in this Laboratory.
     */
    @Basic
    public Transmogrifier getTransmogrifier() {
        return transmogrifier;
    }


    /**
     * Check if this laboratory has a CoolingBox.
     *
     * @return True if a CoolingBox is registered in this laboratory.
     *       | result == (getCoolingBox() != null)
     */
    public boolean hasCoolingBox() {
        return coolingBox != null;
    }

    /**
     * Check if this laboratory has an Oven.
     *
     * @return True if an Oven is registered in this laboratory.
     *       | result == (getOven() != null)
     */
    public boolean hasOven() {
        return oven != null;
    }

    /**
     * Check if this laboratory has a Kettle.
     *
     * @return True if a Kettle is registered in this laboratory.
     *       | result == (getKettle() != null)
     */
    public boolean hasKettle() {
        return kettle != null;
    }

    /**
     * Check if this laboratory contains a Transmogrifier.
     *
     * @return True if a Transmogrifier is registered in this laboratory.
     *       | result == (getTransmogrifier() != null)
     */
    public boolean hasTransmogrifier() {
        return transmogrifier != null;
    }



    /**
     * Receipies (wrs??)
     */









}