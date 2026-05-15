package alchemy.lab;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class IngredientContainerTest {

    // A liquid ingredient: 1 vial of Water (fits in a BOTTLE)
    private AlchemicIngredient liquidIngredient;

    // A powder ingredient: 1 sachet of Salt (fits in a BOX)
    private AlchemicIngredient powderIngredient;

    @BeforeEach
    public void setUp() {
        IngredientType waterType = new IngredientType(Name.WATER);
        liquidIngredient = new AlchemicIngredient(waterType, new Quantity(1L, Unit.VIAL));

        IngredientType saltType = new IngredientType(
                new Name("Salt"), State.POWDER, new Temperature(0, 20), false);
        powderIngredient = new AlchemicIngredient(saltType, new Quantity(1L, Unit.SACHET));
    }

    /**
     * isValidCapacityUnit
     */

    @Test
    public void isValidCapacityUnit_Null_ReturnsFalse() {
        assertFalse(IngredientContainer.isValidCapacityUnit(null));
    }

    @Test
    public void isValidCapacityUnit_Drop_ReturnsFalse() {
        assertFalse(IngredientContainer.isValidCapacityUnit(Unit.DROP));
    }


    @Test
    public void isValidCapacityUnit_Pinch_ReturnsFalse() {
        assertFalse(IngredientContainer.isValidCapacityUnit(Unit.PINCH));
    }


    @Test
    public void isValidCapacityUnit_StoreroomLiquid_ReturnsFalse() {
        assertFalse(IngredientContainer.isValidCapacityUnit(Unit.STOREROOM));
    }


    @Test
    public void isValidCapacityUnit_LiquidIntermediates_ReturnTrue() {
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.SPOON));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.VIAL));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.BOTTLE));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.JUG));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.BARREL));
    }


    @Test
    public void isValidCapacityUnit_PowderIntermediates_ReturnTrue() {
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.SPOON));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.SACHET));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.BOX));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.SACK));
        assertTrue(IngredientContainer.isValidCapacityUnit(Unit.CHEST));
    }


    /**
     * Constructor - empty
     */


    @Test
    public void constructor_EmptyContainer_IsEmpty() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE);

        assertTrue(container.isEmpty());
        assertNull(container.getIngredient());
        assertEquals(Unit.BOTTLE, container.getCapacityUnit());
    }


    /**
     * Constructor - has contents
     */

    @Test
    public void constructor_WithIngredient_StoresIngredient() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, liquidIngredient);

        assertFalse(container.isEmpty());
        assertEquals(liquidIngredient, container.getIngredient());
        assertEquals(Unit.BOTTLE, container.getCapacityUnit());
    }


    @Test
    public void constructor_NullIngredient_IsEmpty() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, null);

        assertTrue(container.isEmpty());
    }


    @Test
    public void constructor_IngredientFillsContainerCorrectly() {
        // 1 bottle of water exactly fills a BOTTLE container (120 drops == 120 drops)
        AlchemicIngredient fullBottle
                = new AlchemicIngredient(IngredientType.DEFAULT, new Quantity(1L, Unit.BOTTLE));

        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, fullBottle);
        assertFalse(container.isEmpty());
    }


    @Test
    public void constructor_WrongStateIngredient_ThrowsException() {
        // Powder ingredient in a liquid container
        assertThrows(IllegalArgumentException.class,
                () -> new IngredientContainer(Unit.BOTTLE, powderIngredient));
    }


    @Test
    public void constructor_PartiallyFilled_Accepted() {
        // checks the possibility that a partially filled container exists
        // 4 pinches of salt in a SACK (capacity 3*6*7*6 = 756 pinches)
        AlchemicIngredient smallAmount
                = new AlchemicIngredient( powderIngredient.getType(), new Quantity(4L, Unit.PINCH));

        IngredientContainer container = new IngredientContainer(Unit.SACK, smallAmount);
        assertFalse(container.isEmpty());
    }


    /**
     * canContain
     */

    @Test
    public void canContain_ReturnsTrue() {
        assertTrue(IngredientContainer.canContain(Unit.BOTTLE, liquidIngredient));
    }


    @Test
    public void cannotContain_ReturnsFalse() {
        AlchemicIngredient tooBig
                = new AlchemicIngredient(IngredientType.DEFAULT, new Quantity(2L, Unit.BOTTLE));

        assertFalse(IngredientContainer.canContain(Unit.BOTTLE, tooBig));
    }


    @Test
    public void canContain_WrongState_ReturnsFalse() {
        assertFalse(IngredientContainer.canContain(Unit.BOTTLE, powderIngredient));
    }


    @Test
    public void canContain_NullUnit_ReturnsFalse() {
        assertFalse(IngredientContainer.canContain(null, liquidIngredient));
    }


    @Test
    public void canContain_NullIngredient_ReturnsFalse() {
        assertFalse(IngredientContainer.canContain(Unit.BOTTLE, null));
    }


    /**
     * empty
     */

    @Test
    public void empty_ContainerWithIngredient_Empty() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, liquidIngredient);

        container.empty();

        assertTrue(container.isEmpty());
        assertNull(container.getIngredient());
    }

    @Test
    public void empty_EmptyContainer_RemainsEmpty() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE);

        container.empty();

        assertTrue(container.isEmpty());
    }

}