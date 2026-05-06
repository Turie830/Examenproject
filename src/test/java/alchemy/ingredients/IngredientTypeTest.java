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














    //ToDO Arthur: rest van de testen




}
