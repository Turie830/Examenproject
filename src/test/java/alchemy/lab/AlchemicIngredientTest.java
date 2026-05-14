package alchemy.lab;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @note A lot of the following tests already exist in other test classes, but they are here for completeness.
 */
public class AlchemicIngredientTest {

    @Test
    public void constructor_validTypeAndQuantity() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID, new Temperature(0,20));

        Quantity quantity = new Quantity(5L, Unit.SPOON);

        AlchemicIngredient ingredient = new AlchemicIngredient(type, quantity);

        assertSame(type, ingredient.getType());
        assertSame(quantity, ingredient.getQuantity());
        assertEquals("Water", ingredient.getSimpleName());
        assertArrayEquals(new long[] {0, 20}, ingredient.getTemperature());

    }

    @Test
    public void constructor_NullType() {
        Quantity quantity = new Quantity(5L, Unit.SPOON);
        AlchemicIngredient ingredient = new AlchemicIngredient(null, quantity);

        assertSame(IngredientType.DEFAULT, ingredient.getType());
        assertEquals("Water", ingredient.getSimpleName());
        assertArrayEquals(IngredientType.DEFAULT.getStandardTemperature(), ingredient.getTemperature());
    }

    @Test
    public void constructor_NullQuantity() {
        IngredientType type = new IngredientType(new Name("Water"),
                State.LIQUID, new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type, null);

        assertNotNull(ingredient.getQuantity());
        assertEquals(1L, ingredient.getAmount());
        assertEquals(Unit.SPOON, ingredient.getUnit());
    }

    @Test
    public void constructor_OnlyType() {
        IngredientType type = new IngredientType(new Name("Salt"),
                State.POWDER, new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);

        assertSame(type, ingredient.getType());
        assertEquals(1L, ingredient.getAmount());
        assertEquals(Unit.SPOON, ingredient.getUnit());
    }

    @Test
    public void constructor_NoArguments() {
        AlchemicIngredient ingredient = new AlchemicIngredient();

        assertSame(IngredientType.DEFAULT, ingredient.getType());
        assertEquals("Water", ingredient.getSimpleName());
        assertEquals(1L, ingredient.getAmount());
        assertEquals(Unit.SPOON, ingredient.getUnit());
    }

    @Test
    public void getAmount_ReturnsQuantityAmount() {
        Quantity quantity = new Quantity(7L, Unit.SPOON);
        AlchemicIngredient ingredient = new AlchemicIngredient(IngredientType.DEFAULT, quantity);

        assertEquals(7L, ingredient.getAmount());
    }

    @Test
    public void getUnit_ReturnsQuantityUnit() {
        Quantity quantity = new Quantity(7L, Unit.SPOON);
        AlchemicIngredient ingredient = new AlchemicIngredient(IngredientType.DEFAULT, quantity);

        assertEquals(Unit.SPOON, ingredient.getUnit());
    }

    @Test
    public void getAmountInLowestUnit_LiquidQuantity() {
        Quantity quantity = new Quantity(2L, Unit.SPOON);
        AlchemicIngredient ingredient = new AlchemicIngredient(IngredientType.DEFAULT, quantity);

        assertEquals(16L, ingredient.getAmountInLowestUnit());
    }

    @Test
    public void getAmountInSpoons_LiquidQuantity() {
        Quantity quantity = new Quantity(1L, Unit.VIAL);
        AlchemicIngredient ingredient = new AlchemicIngredient(IngredientType.DEFAULT, quantity);

        assertEquals(5L, ingredient.getAmountInSpoons());
    }

    @Test
    public void getSimpleName_RegularIngredient() {
        IngredientType type = new IngredientType(new Name("Red Mushroom"), State.POWDER,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        assertEquals("Red Mushroom", ingredient.getSimpleName());

    }

    @Test
    public void getFullName_StandardTemperature_NoPrefix() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);

        assertEquals("Water", ingredient.getFullName());
    }

    @Test
    public void getFullName_HeatedIngredient_HasHeatedPrefix() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        ingredient.heat(30);

        assertEquals("Heated Water", ingredient.getFullName());
    }

    @Test
    public void getFullName_CooledIngredient_HasCooledPrefix() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        ingredient.cool(30);

        assertEquals("Cooled Water", ingredient.getFullName());
    }

    @Test
    public void getFullName_MixedIngredientWithoutSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        assertEquals("Beer mixed with Coke", ingredient.getFullName());
    }

    @Test
    public void getFullName_MixedIngredientWithSpecialName() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");
        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        assertEquals("Mazout (Beer mixed with Coke)", ingredient.getFullName());

    }

    @Test
    public void getFullName_MixedIngredientWithSpecialNameAndHeatedPrefix() {
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType type = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20)
        );

        type.setSpecialName("Mazout");
        AlchemicIngredient ingredient = new AlchemicIngredient(type);
        ingredient.heat(50);

        assertEquals("Mazout (Heated Beer mixed with Coke)", ingredient.getFullName());
    }

    @Test
    public void getTemperature_ReturnsColdnessFirstAndHotnessSecond() {
        IngredientType type = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20)
        );

        AlchemicIngredient ingredient = new AlchemicIngredient(type);

        assertArrayEquals(new long[]{0, 20}, ingredient.getTemperature());
    }

    @Test
    public void heat_ChangesIngredientTemperature() {
        AlchemicIngredient ingredient = new AlchemicIngredient();

        ingredient.heat(50);

        assertArrayEquals(new long[] {0, 70}, ingredient.getTemperature());
        assertEquals(0L, ingredient.getColdness());
        assertEquals(70L, ingredient.getHotness());
    }

    @Test
    public void cool_ChangesIngredientTemperature() {
        AlchemicIngredient ingredient = new AlchemicIngredient();

        ingredient.cool(50);

        assertArrayEquals(new long[] {30, 0}, ingredient.getTemperature());
        assertEquals(30L, ingredient.getColdness());
        assertEquals(0L, ingredient.getHotness());
    }

    @Test
    public void getTemperatureNewArray() {
        AlchemicIngredient ingredient = new AlchemicIngredient();
        long[] temperature = ingredient.getTemperature();

        temperature[0] = 999;
        temperature[1] = 999;

        assertArrayEquals(new long[] {0, 20}, ingredient.getTemperature());
    }

    @Test
    public void isValidName_ValidRegularNames() {
        assertTrue(AlchemicIngredient.isValidName("Water"));
        assertTrue(AlchemicIngredient.isValidName("Red Mushroom"));
        assertTrue(AlchemicIngredient.isValidName("Rat's Eye"));
    }

    @Test
    public void isValidName_MixedAndWithNotAllowedForRegularNames() {
        assertFalse(AlchemicIngredient.isValidName("Beer mixed with Coke"));
        assertFalse(AlchemicIngredient.isValidName("mixed"));
        assertFalse(AlchemicIngredient.isValidName("with"));
    }

    @Test
    public void isValidMixtureName_ValidMixtureNames() {
        assertTrue(AlchemicIngredient.isValidMixtureName("Beer mixed with Coke"));
        assertTrue(AlchemicIngredient.isValidMixtureName("Water mixed with Salt"));
    }

    @Test
    public void isValidMixtureName_RegularNameIsNotMixtureName() {
        assertFalse(AlchemicIngredient.isValidMixtureName("Water"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Red Mushroom"));
    }

    @Test
    public void isValidMixtureName_MustContainMixedWithSequence() {
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer mixed Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer with Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("mixed with Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer mixed with"));
    }

    @Test
    public void changeState_nullState_throws() {
        AlchemicIngredient ing = new AlchemicIngredient();
        assertThrows(IllegalArgumentException.class, () -> ing.changeState(null));
    }

    @Test
    public void changeState_sameState_DoesNothing() {
        AlchemicIngredient ing = new AlchemicIngredient();
        Quantity before = ing.getQuantity();
        ing.changeState(State.LIQUID);

        assertEquals(State.LIQUID, ing.getState());
        assertEquals(before, ing.getQuantity());
    }

    @Test
    public void changeState_Success() {
        AlchemicIngredient ing = new AlchemicIngredient();
        Quantity before = ing.getQuantity();
        ing.changeState(State.POWDER);

        assertEquals(State.POWDER, ing.getState());
        assertEquals(1L, ing.getQuantity().getAmount());
        assertEquals(Unit.SPOON, ing.getQuantity().getUnit());
    }



}
