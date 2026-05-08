package alchemy.ingredients;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.exceptions.IllegalNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MixedIngredientTypeTest {

    @Test
    public void constructor_ValidMixedIngredientType() {
        Name name = Name.createMixtureName("Beer mixed with Coke");
        Temperature temperature = new Temperature(0, 30);

        IngredientType type = new MixedIngredientType(name, State.LIQUID, temperature);

        assertEquals("Beer mixed with Coke", name.getName());
        assertEquals("Beer mixed with Coke", type.getSimpleName());
        assertEquals(State.LIQUID, type.getStandardState());
        assertArrayEquals(new long[] {0, 30}, type.getStandardTemperature());
        assertTrue(type.isMixed());
    }

    @Test
    public void constructor_RegularNameForMixedType() {
        Name regularName = new Name("Water");
        assertThrows(IllegalNameException.class, () -> {
            new MixedIngredientType(regularName, State.LIQUID, new Temperature(0, 20));
        });
    }

    @Test
    public void constructor_MixtureNameForRegularType() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");
        assertThrows(IllegalNameException.class, () -> {
            new IngredientType(mixtureName, State.LIQUID, new Temperature(0, 20));
        });
    }

    @Test
    public void setSpecialName_ValidSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName,
                State.LIQUID, new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");

        assertTrue(type.hasSpecialName());
        assertEquals("Mazout", type.getSpecialName());
    }

    @Test
    public void setSpecialName_NullSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(
                mixtureName, State.LIQUID, new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");
        type.setSpecialName(null);

        assertFalse(type.hasSpecialName());
        assertNull(type.getSpecialName());
    }

    @Test
    public void setSpecialName_InvalidSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName,
                State.LIQUID, new Temperature(0, 20)
        );

        assertThrows(IllegalNameException.class, () -> {
            type.setSpecialName("mazout");
        });

        assertThrows(IllegalNameException.class, () -> {
            type.setSpecialName("Beer mixed with Coke");
        });

        assertThrows(IllegalNameException.class, () -> {
            type.setSpecialName("Mazout2");
        });
    }

    @Test
    public void alchemicIngredient_FullName_WithSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(
                mixtureName, State.LIQUID, new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");

        AlchemicIngredient ingredient = new AlchemicIngredient(type);

        assertEquals("Beer mixed with Coke", ingredient.getSimpleName());
        assertEquals("Mazout (Beer mixed with Coke)", ingredient.getFullName());
    }

    @Test
    public void alchemicIngredient_FullName_WithSpecialNameAndHeatedPrefix() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName,
                State.LIQUID, new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        ingredient.heat(50);

        assertEquals("Mazout (Heated Beer mixed with Coke)", ingredient.getFullName());
    }

    @Test
    public void alchemicIngredient_FullName_WithSpecialNameAndCooledPrefix() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName,
                State.LIQUID, new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        ingredient.cool(50);

        assertEquals("Mazout (Cooled Beer mixed with Coke)", ingredient.getFullName());
    }
}
