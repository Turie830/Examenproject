package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;

/**
 * A class of temperatures for alchemic ingredients.
 *
 * A temperature is represented by a coldness and a hotness.
 * Both values are between zero and the upper bound.
 * A temperature cannot have both a positive coldness and a positive hotness.
 *
 * Temperatures are implemented totally: invalid values are converted to
 * a valid default temperature.
 *
 * @invar The coldness of each temperature must be valid.
 *      | isValidTemperatureValue(getColdness())
 *
 * @invar The hotness of each temperature must be valid.
 *      | isValidTemperatureValue(getHotness())
 *
 * @invar A temperature cannot have both positive coldness and positive hotness.
 *      | !(getColdness() > 0 && getHotness() > 0)
 *
 * @author Arthur
 * @author Mauro
 * @author Obe
 *
 * @version 1.0
 */
public class Temperature {
    /**
     * The upper bound for coldness and hotness.
     */
    private static final long UPPER_BOUND = 10000;

    /**
     * The default coldness used when invalid values are given.
     */
    private static final long DEFAULT_COLDNESS = 0;

    /**
     * The default hotness used when invalid values are given.
     */
    private static final long DEFAULT_HOTNESS = 20;

    /**
     * Variable storing the coldness of this temperature.
     */
    private long coldness;

    /**
     * Variable storing the hotness of this temperature.
     */
    private long hotness;


    /**
     * Initialize this new temperature with the given coldness and hotness.
     *
     * If the given values do not form a valid temperature, this temperature is
     * initialized as the default temperature [0, 20].
     *
     * @param coldness
     *        The coldness for this new temperature.
     *
     * @param hotness
     *        The hotness for this new temperature.
     *
     * @post If the given coldness and hotness form a valid temperature, the
     *       coldness and hotness of this new temperature are equal to the given values.
     *     | if (isValidTemperature(coldness, hotness)) then
     *     |   new.getColdness() == coldness
     *     |   && new.getHotness() == hotness
     *
     * @post If the given coldness and hotness do not form a valid temperature,
     *       this new temperature is initialized as the default temperature.
     *     | if (!isValidTemperature(coldness, hotness)) then
     *     |   new.getColdness() == DEFAULT_COLDNESS
     *     |   && new.getHotness() == DEFAULT_HOTNESS
     */
    public Temperature(long coldness, long hotness) {
        if (isValidTemperature(coldness, hotness)) {
            this.coldness = coldness;
            this.hotness = hotness
        }
        else {
            this.coldness = DEFAULT_COLDNESS;
            this.hotness = DEFAULT_HOTNESS;
        }
    }


    /**
     * Initialize this new temperature as a copy of the given temperature.
     *
     * If the given temperature is not effective, this temperature is initialized
     * as the default temperature.
     *
     * @param temperature
     *        The temperature to copy.
     *
     * @post If the given temperature is effective, this new temperature has the
     *       same coldness and hotness as the given temperature.
     *     | if (temperature != null) then
     *     |   new.getColdness() == temperature.getColdness()
     *     |   && new.getHotness() == temperature.getHotness()
     *
     * @post If the given temperature is not effective, this new temperature is
     *       initialized as the default temperature.
     *     | if (temperature == null) then
     *     |   new.getColdness() == DEFAULT_COLDNESS
     *     |   && new.getHotness() == DEFAULT_HOTNESS
     *
     *
     * @note This constructor is a copy constructor. It is useful because Temperature
     * objects are mutable: methods such as heat and cool change the object.
     * By copying a temperature instead of sharing the same Temperature object,
     * different ingredients can change their own temperature without accidentally
     * changing the temperature of another ingredient or of an ingredient type.
     * Example:
     *
     * Temperature standardTemperature = new Temperature(0, 20);
     *
     * Temperature temperatureOfIngredient1 = new Temperature(standardTemperature);
     * Temperature temperatureOfIngredient2 = new Temperature(standardTemperature);
     *
     * temperatureOfIngredient1.heat(50);
     *
     * // temperatureOfIngredient1 is now [0, 70]
     * // temperatureOfIngredient2 is still [0, 20]
     * // standardTemperature is still [0, 20]
     *
     */
    public Temperature(Temperature temperature) {
        if (temperature == null) {
            this.coldness = DEFAULT_COLDNESS;
            this.hotness = DEFAULT_HOTNESS;
        } else {
            this.coldness = temperature.getColdness();
            this.hotness = temperature.getHotness();
        }
    }


    /**
     * Return the upper bound for coldness and hotness.
     *
     * @return The upper bound for coldness and hotness.
     *       | result == 10000
     */
    @Basic
    @Immutable
    public static long getUpperBound() {
        return UPPER_BOUND;
    }




    /**
     * Return the coldness of this temperature.
     *
     * @return The coldness of this temperature.
     *       | result == this.coldness
     */
    @Basic
    public long getColdness() {
        return coldness;
    }


    /**
     * Return the hotness of this temperature.
     *
     * @return The hotness of this temperature.
     *       | result == this.hotness
     */
    @Basic
    public long getHotness() {
        return hotness;
    }

    /**
     * Return this temperature as an array.
     *
     * The first element is the coldness, the second element is the hotness.
     *
     * @return A new array containing the coldness and hotness of this temperature.
     *       | result.length == 2
     *       | && result[0] == getColdness()
     *       | && result[1] == getHotness()
     */
    public long[] getTemperature() {
        return new long[] {
                getColdness(), getHotness()
        };
    }


    /**
     * Check whether the given coldness and hotness form a valid temperature.
     *
     * A temperature is valid if and only if both coldness and hotness are between
     * zero and the upper bound, and they are not both strictly positive.
     *
     * @param coldness
     *        The coldness to check.
     *
     * @param hotness
     *        The hotness to check.
     *
     * @return True if and only if the given coldness and hotness form a valid temperature.
     *       | result ==
     *       |   coldness >= 0
     *       |   && coldness <= getUpperBound()
     *       |   && hotness >= 0
     *       |   && hotness <= getUpperBound()
     *       |   && !(coldness > 0 && hotness > 0)
     */
    public static boolean isValidTemperature(long coldness, long hotness) {
        return coldness >= 0
                && coldness <= getUpperBound()
                && hotness >= 0
                && hotness <= getUpperBound()
                && !(coldness > 0 && hotness > 0);
    }


    /**
     * Heat this temperature with the given amount.
     *
     * If this temperature is cold, heating first decreases the coldness.
     * If the coldness reaches zero and heat remains, the hotness increases.
     * The hotness can never exceed the upper bound.
     *
     * If the given amount is not positive, this temperature is not changed.
     *
     * @param amount
     *        The amount of heat to add.
     */
    public void heat(long amount) {
        if (amount <= 0) {
            return;
        }
        if (getColdness() > 0) {
            if (amount <= getColdness()) {
                coldness = getColdness() - amount;
            } else {
                long remainingHeat = amount - getColdness();
                coldness = 0;
                hotness = Math.min(remainingHeat, getUpperBound());
            }
        } else {
            hotness = Math.min(getHotness() + amount, getUpperBound());
        }
    }


    /**
     * Cool this temperature with the given amount.
     *
     * If this temperature is hot, cooling first decreases the hotness.
     * If the hotness reaches zero and coldness remains, the coldness increases.
     * The coldness can never exceed the upper bound.
     *
     * If the given amount is not positive, this temperature is not changed.
     *
     * @param amount
     *        The amount of coldness to add.
     */
    public void cool(long amount) {
        if (amount <= 0) {
            return;
        }
        if (getHotness() > 0) {
            if (amount <= getHotness()) {
                hotness = getHotness() - amount;
            } else {
                long remainingColdness = amount - getHotness();
                hotness = 0;
                coldness = Math.min(remainingColdness, getUpperBound());
            }
        } else {
            coldness = Math.min(getColdness() + amount, getUpperBound());
        }
    }


    /**
     * Check whether this temperature is hotter than the given temperature.
     *
     * @param other
     *        The other temperature.
     *
     * @return False if the given temperature is not effective.
     *       | if (other == null) then result == false
     *
     * @return True if and only if this temperature has a higher net temperature
     *         than the given temperature.
     *       | if (other != null) then
     *       |   result == (getNetTemperature() > other.getNetTemperature())
     */
    public boolean isHotterThan(Temperature other) {
        if (other == null) {
            return false;
        }
        return getNetTemperature() > other.getNetTemperature();
    }


    /**
     * Check whether this temperature is colder than the given temperature.
     *
     * @param other
     *        The other temperature.
     *
     * @return False if the given temperature is not effective.
     *       | if (other == null) then result == false
     *
     * @return True if and only if this temperature has a lower net temperature
     *         than the given temperature.
     *       | if (other != null) then
     *       |   result == (getNetTemperature() < other.getNetTemperature())
     */
    public boolean isColderThan(Temperature other) {
        if (other == null) {
            return false;
        }
        return getNetTemperature() < other.getNetTemperature();
    }


    /**
     * Return the absolute difference between this temperature and the given temperature.
     *
     * If the given temperature is not effective, the default temperature is used.
     *
     * @param other
     *        The other temperature.
     *
     * @return The absolute difference between this temperature and the given temperature.
     */
    public long difference(Temperature other) {
        if (other == null) {
            other = new Temperature(DEFAULT_COLDNESS, DEFAULT_HOTNESS);
        }

        return Math.abs(getNetTemperature() - other.getNetTemperature());
    }


    /**
     * Return the signed net temperature value.
     *
     * A positive value represents hotness.
     * A negative value represents coldness.
     *
     * @return The hotness minus the coldness of this temperature.
     *       | result == getHotness() - getColdness()
     */
    @Model
    private long getNetTemperature() {
        return getHotness() - getColdness();
    }


}
