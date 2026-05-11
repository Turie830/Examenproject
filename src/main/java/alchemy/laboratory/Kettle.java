package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A class for kettles
 *
 * @author Obe Willaert
 * @author Mauro Devolder
 * @author Arthur Pintelon
 * @version 1.0
 */
public class Kettle extends MultiContainerDevice {


    /**
     * Initialise a new multi container device
     *
     * @param laboratory The laboratory this device is located in
     * @throws IllegalArgumentException when the laboratory is not effective
     *                                  | laboratory == null
     * @post The laboratory of this device is set to the given laboratory
     * | new.getLaboratory() == laboratory
     */
    public Kettle(Laboratory laboratory) {
        super(laboratory);
    }

    /**
     * Mix all ingredients in this kettle into one resulting ingredient
     *
     * @throws IllegalStateException The kettle must contain at least one ingredient
     */
    @Override
    public void execute() {
        List<AlchemicIngredient> ingredients = getActualDeviceContents();

        if (ingredients.isEmpty()) {
            throw new IllegalStateException("The kettle should have ingredients in it");
        }

        IngredientType resultType = createResultType(ingredients);
        Quantity resultQuantity = createResultQuantity(ingredients, resultType.getStandardState());
        AlchemicIngredient result = new AlchemicIngredient(resultType, resultQuantity);
        setTemperature(result, calculateWeightedTemperature(ingredients));

        createResultContainer(result);
        emptyDeviceContents();
    }

    /**
     * Create the type for the resulting ingredient
     *
     * @param ingredients The ingredients that are mixed
     * @return a regular type if all names are equal
     * @return a mixed type if at least two different names occur
     */
    private IngredientType createResultType(List<AlchemicIngredient> ingredients) {
        State resultState = selectResultState(ingredients);
        Temperature resultStandardTemperature = selectResultStandardTemperature(ingredients);
        List<String> simpleNames = getUniqueSimpleNames(ingredients);

        if (simpleNames.size() == 1) {
            return new IngredientType(new Name(simpleNames.getFirst()), resultState, resultStandardTemperature);
        }

        return new MixedIngredientType(
                Name.createMixtureName(createMixedSimpleName(simpleNames)),
                resultState,
                resultStandardTemperature
        );
    }

    /**
     * Select the standard state for the resulting ingredient
     *
     * @param ingredients The ingredients that are mixed
     * @return the state of the type closest to the default temperature
     * @return liquid if liquid and powder are tied
     */
    private State selectResultState(List<AlchemicIngredient> ingredients) {
        long bestDifference = Long.MAX_VALUE;
        State bestState = State.POWDER;

        for (AlchemicIngredient ingredient : ingredients) {
            // null = default (when using difference which getStandardTemperatureDifference does)
            long difference = ingredient.getType().getStandardTemperatureDifference(null);
            State state = ingredient.getType().getStandardState();

            if (difference < bestDifference
                    || (difference == bestDifference && state == State.LIQUID)) {
                bestDifference = difference;
                bestState = state;
            }
        }

        return bestState;
    }

    /**
     * Select the standard temperature for the resulting ingredient
     *
     * @param ingredients The ingredients that are mixed
     * @return the standard temperature closest to the default temperature
     * @return the warmest standard temperature if multiple candidates are tied
     * @pre The list of ingredients is effective
     * @pre every AlchemicIngredient in ingredients is effective
     */
    private Temperature selectResultStandardTemperature(List<AlchemicIngredient> ingredients) {
        Temperature temperatureDefault = new Temperature(0, 20);

        long bestDifference = Long.MAX_VALUE;
        Temperature bestTemperature = null;

        for (AlchemicIngredient ingredient : ingredients) {
            long difference = ingredient.getType().getStandardTemperatureDifference(temperatureDefault);

            long[] candidate = ingredient.getType().getStandardTemperature();
            Temperature candidateTemperature = new Temperature(candidate[0], candidate[1]);

            if (difference < bestDifference) {
                bestDifference = difference;
                bestTemperature = candidateTemperature;

            } else if (difference == bestDifference) {
                if (candidateTemperature.isHotterThan(bestTemperature)) {
                    bestTemperature = candidateTemperature;
                }
            }
        }

        // this will never be null since the list and what's in it is always effective
        // so there will always be 1 temp closer than MAX_VALUE to [0,20]
        return bestTemperature;
    }

    /**
     * Get the different simple names of the given ingredients
     *
     * @param ingredients The ingredients to inspect
     * @return the different simple names in alphabetical order
     */
    private List<String> getUniqueSimpleNames(List<AlchemicIngredient> ingredients) {
        // TreeSet: gesorteerde set
        Set<String> simpleNames = new TreeSet<String>();

        // add every simpleName
        for (AlchemicIngredient ingredient : ingredients) {
            simpleNames.add(ingredient.getSimpleName());
        }

        // convert the now sorted and deduped set to a list
        return new ArrayList<String>(simpleNames);
    }

    /**
     * Create the simple name for a mixture
     *
     * @param simpleNames The different simple names in alphabetical order
     * @return the simple mixture name
     */
    private String createMixedSimpleName(List<String> simpleNames) {
        List<String> remainingNames = new ArrayList<String>(simpleNames);
        String firstName = remainingNames.removeFirst();

        return firstName + " mixed with " + joinIngredientNames(remainingNames);
    }

    /**
     * Join the names that follow after mixed with
     *
     * @param names The names to join
     * @return one name if only one name is given
     * @return two names separated by and if two names are given
     * @return comma separated names ending with and if more than two names are given
     */
    private String joinIngredientNames(List<String> names) {
        if (names.size() == 1) {
            return names.getFirst();
        }
        if (names.size() == 2) {
            return names.get(0) + " and " + names.get(1);
        }

        // so test fails as expected
        return String.join(", ", names.subList(0, names.size() - 1))
                + " and "
                + names.getLast();
    }

    /**
     * Create the quantity for the resulting ingredient
     *
     * @param ingredients The ingredients that are mixed
     * @param resultState The state of the resulting ingredient
     * @return the resulting quantity expressed in the base unit of the result state
     */
    private Quantity createResultQuantity(List<AlchemicIngredient> ingredients, State resultState) {
        long sameStateResultBaseAmount = 0L;
        long changedStateSpoons = 0L;
        long changedStateRemainderBaseAmount = 0L;
        int changedStateSpoonFactor = 1;

        for (AlchemicIngredient ingredient : ingredients) {
            State ingredientState = ingredient.getType().getStandardState();
            long ingredientBaseAmount = ingredient.getQuantity().toLowestUnit(ingredientState);

            if (ingredientState == resultState) {
                sameStateResultBaseAmount += ingredientBaseAmount;
            } else {
                changedStateSpoonFactor = Unit.SPOON.getFactorToBaseUnit(ingredientState);
                changedStateSpoons += ingredientBaseAmount / changedStateSpoonFactor;
                changedStateRemainderBaseAmount += ingredientBaseAmount % changedStateSpoonFactor;
            }
        }

        changedStateSpoons += changedStateRemainderBaseAmount / changedStateSpoonFactor;

        long resultBaseAmount = sameStateResultBaseAmount
                + changedStateSpoons * Unit.SPOON.getFactorToBaseUnit(resultState);

        return new Quantity(resultBaseAmount, Unit.getBaseUnit(resultState));
    }

    /**
     * Calculate the weighted temperature for the resulting ingredient
     *
     * @param ingredients The ingredients that are mixed
     * @return the weighted average temperature
     */
    private Temperature calculateWeightedTemperature(List<AlchemicIngredient> ingredients) {
        long totalColdness = 0L;
        long totalHotness = 0L;
        double totalWeight = 0.0;

        for (AlchemicIngredient ingredient : ingredients) {
            long[] temp = ingredient.getTemperature();
            long weight = getSpoonWeight(ingredient);

            totalWeight += weight;

            totalColdness += (temp[0] * weight);
            totalHotness += (temp[1] * weight);
        }


        // todo: possibly change quantity so that amount > 0 instead of >=0
        // currently we could have an ingredient with weight 0 so totalWeigt could be 0
        long averageColdness = Math.round(totalColdness / totalWeight);
        long averageHotness = Math.round(totalHotness / totalWeight);

        if (averageColdness > averageHotness) {
            averageColdness -= averageHotness;
            return new Temperature(averageColdness, 0);
        } else {
            return new Temperature(0, averageHotness - averageColdness);
        }

    }


    /**
     * Get the weight of an ingredient in spoons
     *
     * @param ingredient The ingredient to weigh
     * @return the amount expressed as a spoon based value
     */
    private long getSpoonWeight(AlchemicIngredient ingredient) {
        // todo: convert to getState
        State state = ingredient.getType().getStandardState();
        return ingredient.getQuantity().toSpoons(state);
    }


    /**
     * Set the temperature of the given ingredient to the target temperature
     *
     * @param ingredient        The ingredient to heat or cool
     * @param targetTemperature The temperature to reach
     */
    private void setTemperature(AlchemicIngredient ingredient, Temperature targetTemperature) {
        Temperature currentTemperature = new Temperature(ingredient.getColdness(), ingredient.getHotness());
        long difference = currentTemperature.difference(targetTemperature);

        if (targetTemperature.isHotterThan(currentTemperature)) {
            ingredient.heat(difference);
        } else if (targetTemperature.isColderThan(currentTemperature)) {
            ingredient.cool(difference);
        }
    }
}
