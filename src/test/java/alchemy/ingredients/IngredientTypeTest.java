package alchemy.ingredients;
import alchemy.Name;
import alchemy.Temperature;
import alchemy.exceptions.IllegalNameException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IngredientTypeTest {

    /**
     * @note getName() returns a Name object.
     *       getName().getName() returns the String value of that Name object.
     */
    @Test
    public void defaultIngredientType() {
        IngredientType defaultType = IngredientType.DEFAULT;

        assertEquals("Water", defaultType.getName().getName());
        assertEquals("Water", defaultType.getSimpleName());
        assertEquals(State.LIQUID, defaultType.getStandardState());
        assertArrayEquals(new long[] {0,20}, defaultType.getStandardTemperature());
        assertFalse(defaultType.isMixed());
    }

    @Test
    public void constructor_ValidRegularIngredientType() {
        Name name = new Name("Salt");
        Temperature temperature = new Temperature(0, 50);
        IngredientType type = new IngredientType(name, State.POWDER, temperature, false);

        assertEquals("Salt", name.getName());
        assertEquals("Salt", type.getSimpleName());
        assertEquals(State.POWDER, type.getStandardState());
        assertArrayEquals(new long[] {0, 50}, type.getStandardTemperature());
        assertFalse(type.isMixed());
    }

    @Test
    public void constructor_ValidMixedIngredientType() {
        Name name = Name.createMixtureName("Beer mixed with Coke");
        Temperature temperature = new Temperature(0, 30);
        IngredientType type = new IngredientType(name, State.LIQUID, temperature, true);

        assertEquals("Beer mixed with Coke", name.getName());
        assertEquals("Beer mixed with Coke", type.getSimpleName());
        assertEquals(State.LIQUID, type.getStandardState());
        assertArrayEquals(new long[] {0, 30}, type.getStandardTemperature());
        assertTrue(type.isMixed());
    }

    @Test
    public void constructor_NullName() {
        assertThrows(IllegalNameException.class, () -> {
            new IngredientType(null, State.LIQUID, new Temperature(0, 20), false);
        });
    }

    @Test
    public void constructor_RegularNameForMixedType() {
        Name regularName = new Name("Water");
        assertThrows(IllegalNameException.class, () -> {
            new IngredientType(regularName, State.LIQUID, new Temperature(0, 20), true);
        });
    }

    @Test
    public void constructor_MixtureNameForRegularType() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");
        assertThrows(IllegalNameException.class, () -> {
            new IngredientType(mixtureName, State.LIQUID, new Temperature(0, 20), false);
        });
    }

    @Test
    public void constructor_NullStandardState() {
        IngredientType type = new IngredientType(new Name("Salt"), null,
                new Temperature(0, 50), false);

        assertEquals(IngredientType.DEFAULT.getStandardState(), type.getStandardState());
    }

    @Test
    public void constructor_ValidStandardTemperature() {
        Temperature temperature = new Temperature(0, 75);
        IngredientType type = new IngredientType(new Name("Milk"), State.LIQUID, temperature, false);

        assertArrayEquals(new long[] {0, 75}, type.getStandardTemperature());
    }

    @Test
    public void constructor_NullStandardTemperature() {
        IngredientType type = new IngredientType(new Name("Milk"), State.LIQUID, null, false);

        assertArrayEquals(IngredientType.DEFAULT.getStandardTemperature(), type.getStandardTemperature());
    }

    /**
     * @note IngredientType standardTemperature = moet strikt warmer zijn dan [0,0]
     *       AlchemicIngredient actuele temperature = mag koud zijn, bv. [10,0] (dit wordt getest in AlchemicIngredientTest)
     */
    @Test
    public void constructor_ColdStandardTemperature() {
        IngredientType type = new IngredientType(new Name("Ice"), State.LIQUID,
                new Temperature(10, 0), false);

        assertArrayEquals(IngredientType.DEFAULT.getStandardTemperature(), type.getStandardTemperature());
    }

    @Test
    public void constructor_NeutralStandardTemperature() {
        IngredientType type = new IngredientType(new Name("Stone"), State.POWDER,
                new Temperature(0, 0), false);
        assertArrayEquals(IngredientType.DEFAULT.getStandardTemperature(), type.getStandardTemperature());
    }

    @Test
    public void canHaveAsName_RegularTypeAcceptsRegularName() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20), false);

        assertTrue(type.canHaveAsName(new Name("Salt")));
    }
    @Test
    public void canHaveAsName_RegularTypeRejectsMixtureName() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20), false);

        assertFalse(type.canHaveAsName(Name.createMixtureName("Beer mixed with Coke")));
    }

    @Test
    public void canHaveAsName_MixedTypeAcceptsMixtureName() {
        IngredientType type = new IngredientType(Name.createMixtureName("Beer mixed with Coke"), State.LIQUID,
                new Temperature(0, 20), true);
        assertTrue(type.canHaveAsName(Name.createMixtureName("Water mixed with Salt")));
    }

    @Test
    public void canHaveAsName_MixedTypeRejectsRegularName() {
        IngredientType type = new IngredientType(
                Name.createMixtureName("Beer mixed with Coke"), State.LIQUID,
                new Temperature(0, 20), true);

        assertFalse(type.canHaveAsName(new Name("Water")));
    }

    @Test
    public void canHaveAsName_NullName() {
        IngredientType type = new IngredientType(
                new Name("Water"), State.LIQUID, new Temperature(0, 20), false);

        assertFalse(type.canHaveAsName(null));
    }

    @Test
    public void isValidState_EffectiveState() {
        assertTrue(IngredientType.isValidState(State.LIQUID));
        assertTrue(IngredientType.isValidState(State.POWDER));
    }

    @Test
    public void isValidState_NullState() {
        assertFalse(IngredientType.isValidState(null));
    }

    @Test
    public void canHaveAsStandardTemperature_ValidStandardTemperature() {
        assertTrue(IngredientType.canHaveAsStandardTemperature(new Temperature(0, 20)));
        assertTrue(IngredientType.canHaveAsStandardTemperature(new Temperature(0, 100)));
    }

    @Test
    public void canHaveAsStandardTemperature_NullTemperature() {
        assertFalse(IngredientType.canHaveAsStandardTemperature(null));
    }

    @Test
    public void canHaveAsStandardTemperature_ColdTemperature() {
        assertFalse(IngredientType.canHaveAsStandardTemperature(new Temperature(20, 0)));
    }

    @Test
    public void canHaveAsStandardTemperature_NeutralTemperature() {
        assertFalse(IngredientType.canHaveAsStandardTemperature(new Temperature(0, 0)));
    }

    @Test
    public void getStandardTemperature() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20), false);

        long[] temperature = type.getStandardTemperature();
        temperature[0] = 999;
        temperature[1] = 999;

        assertArrayEquals(new long[] {0, 20}, type.getStandardTemperature());
    }

    @Test
    public void constructor_StandardTemperatureIsCopied() {
        Temperature temperature = new Temperature(0, 20);
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID, temperature, false);

        temperature.heat(50);

        assertArrayEquals(new long[] {0, 20}, type.getStandardTemperature());
        assertArrayEquals(new long[] {0, 70}, temperature.getTemperature());
    }

    @Test
    public void getStandardTemperatureDifference_EffectiveTemperature() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20), false);

        Temperature other = new Temperature(0, 100);

        assertEquals(80, type.getStandardTemperatureDifference(other));
    }

    @Test
    public void getStandardTemperatureDifference_NullTemperature() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 100), false);

        assertEquals(80, type.getStandardTemperatureDifference(null));
    }




}
