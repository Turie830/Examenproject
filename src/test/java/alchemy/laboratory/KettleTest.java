package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KettleTest {

    private Kettle kettle;

    private static IngredientContainer container(AlchemicIngredient ingredient) {
        for (Unit capacityUnit : Unit.values()) {
            if (IngredientContainer.isValidCapacityUnit(capacityUnit)
                    && IngredientContainer.canContain(capacityUnit, ingredient)) {
                return new IngredientContainer(capacityUnit, ingredient);
            }
        }
        throw new IllegalArgumentException("Ingredient does not fit in a valid test container");
    }

    private static AlchemicIngredient ingredient(String name, State state, Temperature standardTemperature,
                                                 long amount, Unit unit) {
        IngredientType type = new IngredientType(new Name(name), state, standardTemperature);
        return new AlchemicIngredient(type, new Quantity(amount, unit));
    }

    @BeforeEach
    public void setUp() {
        kettle = new Kettle(new Laboratory(1));
    }

    @Test
    public void add_FilledContainer_EmptiesOriginalContainer() {
        AlchemicIngredient water = ingredient("Water", State.LIQUID, new Temperature(0, 20), 1L, Unit.VIAL);
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, water);

        kettle.add(container);

        assertTrue(container.isEmpty());
    }

    @Test
    public void execute_EmptyKettle_ThrowsException() {
        assertThrows(IllegalStateException.class, () -> kettle.execute());
    }

    @Test
    public void execute_SameSimpleName_KeepsRegularName() {
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 20), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 20), 2L, Unit.SPOON)));

        kettle.execute();

        AlchemicIngredient result = kettle.getResult().getIngredient();
        assertEquals("Water", result.getSimpleName());
        assertFalse(result.getType().isMixed());
    }

    @Test
    public void execute_DifferentSimpleNames_CreatesAlphabeticalMixedName() {
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 20), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Garlic", State.POWDER, new Temperature(0, 30), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Imp Gas", State.LIQUID, new Temperature(0, 40), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Mercurial Acid", State.LIQUID, new Temperature(0, 50), 1L, Unit.SPOON)));

        kettle.execute();

        AlchemicIngredient result = kettle.getResult().getIngredient();
        assertEquals("Garlic mixed with Imp Gas, Mercurial Acid and Water", result.getSimpleName());
        assertTrue(result.getType().isMixed());
    }

    @Test
    public void execute_StandardStateTie_ChoosesLiquid() {
        kettle.add(container(ingredient("Salt", State.POWDER, new Temperature(0, 10), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 30), 1L, Unit.SPOON)));

        kettle.execute();

        assertEquals(State.LIQUID, kettle.getResult().getIngredient().getType().getStandardState());
    }

    @Test
    public void execute_StandardTemperatureTie_ChoosesWarmest() {
        kettle.add(container(ingredient("Salt", State.POWDER, new Temperature(0, 10), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 30), 1L, Unit.SPOON)));

        kettle.execute();

        assertArrayEquals(new long[]{0, 30}, kettle.getResult().getIngredient().getType().getStandardTemperature());
    }

    @Test
    public void execute_ChangedStateFractions_AreRoundedDownTogetherToWholeSpoons() {
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 20), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Salt", State.POWDER, new Temperature(0, 20), 4L, Unit.PINCH)));
        kettle.add(container(ingredient("Sugar", State.POWDER, new Temperature(0, 20), 5L, Unit.PINCH)));

        kettle.execute();

        AlchemicIngredient result = kettle.getResult().getIngredient();
        assertEquals(State.LIQUID, result.getType().getStandardState());
        assertEquals(Unit.DROP, result.getUnit());
        assertEquals(16L, result.getAmount());
    }

    @Test
    public void execute_SameStateFractions_ArePreserved() {
        kettle.add(container(ingredient("Water", State.LIQUID, new Temperature(0, 20), 1L, Unit.SPOON)));
        kettle.add(container(ingredient("Mercury", State.LIQUID, new Temperature(0, 30), 3L, Unit.DROP)));
        kettle.add(container(ingredient("Salt", State.POWDER, new Temperature(0, 40), 5L, Unit.PINCH)));

        kettle.execute();

        AlchemicIngredient result = kettle.getResult().getIngredient();
        assertEquals(State.LIQUID, result.getType().getStandardState());
        assertEquals(Unit.DROP, result.getUnit());
        assertEquals(11L, result.getAmount());
    }

    @Test
    public void execute_WeightedTemperature_UsesIngredientQuantities() {
        AlchemicIngredient hotWater = ingredient("Water", State.LIQUID, new Temperature(0, 20), 25L, Unit.SPOON);
        hotWater.heat(80);
        AlchemicIngredient coldWater = ingredient("Water", State.LIQUID, new Temperature(0, 20), 75L, Unit.SPOON);
        coldWater.cool(120);

        kettle.add(container(hotWater));
        kettle.add(container(coldWater));

        kettle.execute();

        assertArrayEquals(new long[]{50, 0}, kettle.getResult().getIngredient().getTemperature());
    }
}
