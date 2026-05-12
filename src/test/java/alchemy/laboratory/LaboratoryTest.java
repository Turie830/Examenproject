package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LaboratoryTest {

    @Test
    public void isValidAmountOfStorerooms(){
        assertThrows(IllegalArgumentException.class, () -> new Laboratory(-1));
        assertThrows(IllegalArgumentException.class, () -> new Laboratory(-100000));

        Laboratory laboVanMauro = new Laboratory(9);
        assertEquals(9, laboVanMauro.getStorerooms());

        Laboratory laboVanArthur = new Laboratory(0);
        assertEquals(0, laboVanArthur.getStorerooms());
    }

    @Test
    public void getCapacityInLowestUnit_LiquidState() {
        Laboratory labo = new Laboratory(2);
        assertEquals(2L * Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID),
                labo.getCapacityInLowestUnit(State.LIQUID));

        Laboratory laboVanObe = new Laboratory(67);
        assertEquals(67L * Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID),
                laboVanObe.getCapacityInLowestUnit(State.LIQUID));
    }

    @Test
    public void getCapacityInLowestUnit_PowderState() {
        Laboratory labo = new Laboratory(2);
        assertEquals(2L * Unit.STOREROOM.getFactorToBaseUnit(State.POWDER),
                labo.getCapacityInLowestUnit(State.POWDER));

        Laboratory labo2 = new Laboratory(67);
        assertEquals(67L * Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID),
                labo2.getCapacityInLowestUnit(State.LIQUID));
    }

    @Test
    public void getCapacityInLowestUnit_nullState() {
        Laboratory labo = new Laboratory(0);
        assertEquals(0L * Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID),
                labo.getCapacityInLowestUnit(State.LIQUID));

        Laboratory labo2 = new Laboratory(0);
        assertEquals(0L * Unit.STOREROOM.getFactorToBaseUnit(State.LIQUID),
                labo2.getCapacityInLowestUnit(State.LIQUID));
    }

    @Test
    public void getUsedAmountInLowestUnit_LiquidState() {
        Laboratory labo = new Laboratory(3);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        IngredientType saltType = new IngredientType(new Name("Salt"), State.POWDER,
                new Temperature(0,20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));
        AlchemicIngredient salt = new AlchemicIngredient(saltType,
                new Quantity(3L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));
        labo.store(new IngredientContainer(Unit.SACHET, salt));

        assertEquals(
                water.getAmountInLowestUnit(),
                labo.getUsedAmountInLowestUnit(State.LIQUID)
        );
    }

    @Test
    public void getUsedAmountInLowestUnit_PowderState() {
        Laboratory labo = new Laboratory(3);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        IngredientType saltType = new IngredientType(new Name("Salt"), State.POWDER,
                new Temperature(0,20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));
        AlchemicIngredient salt = new AlchemicIngredient(saltType,
                new Quantity(3L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));
        labo.store(new IngredientContainer(Unit.SACHET, salt));

        assertEquals(
                salt.getAmountInLowestUnit(),
                labo.getUsedAmountInLowestUnit(State.POWDER)
        );
    }

    @Test
    public void getUsedAmountInLowestUnit_nullState() {
        //zie ToDo
    }

    @Test
    public void hasRoomFor_IngredientFitsInLaboratory() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient ingredient = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        assertTrue(labo.hasRoomFor(ingredient));
    }

    @Test
    public void hasRoomFor_IngredientDoesNotFitInLaboratory() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient ingredient = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.STOREROOM));

        assertFalse(labo.hasRoomFor(ingredient));
    }


    @Test
    public void hasRoomFor_NullIngredient() {
        Laboratory labo = new Laboratory(1);

        assertFalse(labo.hasRoomFor(null));
    }

    @Test
    public void getIngredient_SimpleName() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertSame(water, labo.getIngredient("Water"));
    }

    @Test
    public void getIngredient_SpecialName() {
        Laboratory labo = new Laboratory(1);
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");
        MixedIngredientType mixedType = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20));

        mixedType.setSpecialName("Mazout");

        AlchemicIngredient mazout = new AlchemicIngredient(mixedType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, mazout));

        assertSame(mazout, labo.getIngredient("Mazout"));
    }

    @Test
    public void getIngredient_MixedSimpleName() {
        Laboratory labo = new Laboratory(1);
        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");
        MixedIngredientType mixedType = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20));

        mixedType.setSpecialName("Mazout");

        AlchemicIngredient ingredient = new AlchemicIngredient(mixedType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, ingredient));

        assertSame(ingredient, labo.getIngredient("Beer mixed with Coke"));
    }

    @Test
    public void getIngredient_NullName() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.getIngredient(null));
    }

    @Test
    public void getIngredient_UnknownName() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertThrows(IllegalArgumentException.class, () -> labo.getIngredient("Salt"));
    }

    @Test
    public void getIngredient_SpecialNameOfRegularIngredient() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertThrows(IllegalArgumentException.class, () -> labo.getIngredient("Mazout"));
    }

    @Test
    public void hasIngredient_SimpleName() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertTrue(labo.hasIngredient("Water"));
    }

    @Test
    public void hasIngredient_SpecialName() {
        Laboratory labo = new Laboratory(1);

        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType mixedType = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20));
        mixedType.setSpecialName("Mazout");

        AlchemicIngredient ingredient = new AlchemicIngredient(mixedType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, ingredient));

        assertTrue(labo.hasIngredient("Mazout"));
    }

    @Test
    public void hasIngredient_MixedSimpleName() {
        Laboratory labo = new Laboratory(1);

        Name mixtureName = Name.createMixtureName("Beer mixed with Coke");

        MixedIngredientType mixedType = new MixedIngredientType(mixtureName, State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient ingredient = new AlchemicIngredient(mixedType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, ingredient));

        assertTrue(labo.hasIngredient("Beer mixed with Coke"));
    }

    @Test
    public void hasIngredient_UnknownName() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertFalse(labo.hasIngredient("Salt"));
    }

    @Test
    public void hasIngredient_NullName() {
        Laboratory labo = new Laboratory(1);

        assertFalse(labo.hasIngredient(null));
    }


//ToDO: alles vanaf store()







}
