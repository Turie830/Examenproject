package alchemy.laboratory;

import alchemy.Name;
import alchemy.Temperature;
import alchemy.Unit;
import alchemy.ingredients.*;
import alchemy.recipes.IngredientRecipeStep;
import alchemy.recipes.Operation;
import alchemy.recipes.Recipe;
import alchemy.recipes.SimpleRecipeStep;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void storeEffectiveContainer() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0,20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON));
        IngredientContainer container = new IngredientContainer(Unit.VIAL, water);

        labo.store(container);

        assertTrue(container.isEmpty());
        assertTrue(labo.hasIngredient("Water"));
        assertSame(water, labo.getIngredient("Water"));
    }

    @Test
    public void storeNullContainer() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.store(null));
    }

    @Test
    public void emptyContainerStore() {
        Laboratory labo = new Laboratory(1);
        IngredientContainer container = new IngredientContainer(Unit.VIAL);
        assertThrows(IllegalArgumentException.class, () -> labo.store(container));
    }

    @Test
    public void store_NotEnoughCapacity() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0,20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(1L, Unit.BARREL));
        IngredientContainer container = new IngredientContainer(Unit.BARREL, water);
        labo.store(container);

        IngredientContainer container2 = new IngredientContainer(Unit.BARREL, water);
        labo.store(container2);

        IngredientContainer container3 = new IngredientContainer(Unit.BARREL, water);
        labo.store(container3);

        IngredientContainer container4 = new IngredientContainer(Unit.BARREL, water);
        labo.store(container4);

        IngredientContainer container5 = new IngredientContainer(Unit.BARREL, water);
        labo.store(container5);

        IngredientContainer container6 = new IngredientContainer(Unit.BARREL, water);
        labo.store(container6);

        IngredientContainer container7 = new IngredientContainer(Unit.BARREL, water);


        assertThrows(IllegalStateException.class, () -> labo.store(container7));

        //ToDO: er kan te veel in Mauro'tje!!!
    }



    @Test
    public void request_WithQuantity_RequestedAmount() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        IngredientContainer result = labo.request("Water", new Quantity(2L, Unit.SPOON));

        //checks what you get back.
        assertFalse(result.isEmpty());
        assertEquals("Water", result.getIngredient().getSimpleName());
        assertEquals(2L, result.getIngredient().getAmount());
        assertEquals(Unit.SPOON, result.getIngredient().getUnit());
    }

    @Test
    public void request_WithQuantity_LeavesRemainingAmountInLaboratory() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        labo.request("Water", new Quantity(2L, Unit.SPOON));

        AlchemicIngredient remaining = labo.getIngredient("Water");

        //checks what remains in the laboratory.
        assertEquals(3L, remaining.getAmountInSpoons());
    }

    @Test
    public void request_WithQuantity_AllAvailableAmount() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        labo.request("Water", new Quantity(5L, Unit.SPOON));

        assertFalse(labo.hasIngredient("Water"));
    }

    @Test
    public void request_WithQuantity_NullName() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.request(null, new Quantity(1L, Unit.SPOON)));
    }

    @Test
    public void request_WithQuantity_NullQuantity() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.request("Water", null));
    }

    @Test
    public void request_WithQuantity_UnknownName() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.request("Water", new Quantity(1L, Unit.SPOON)));
    }

    @Test
    public void request_WithQuantity_TooMuchRequested() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        assertThrows(IllegalArgumentException.class, () -> labo.request("Water", new Quantity(6L, Unit.SPOON)));
    }

    @Test
    public void request_WithoutQuantity() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        IngredientContainer result = labo.request("Water");

        assertFalse(result.isEmpty());
        assertEquals("Water", result.getIngredient().getSimpleName());
    }

    @Test
    public void request_WithoutQuantity_RemovesIngredientFromLaboratory() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));

        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        labo.request("Water");

        assertFalse(labo.hasIngredient("Water"));
    }

    @Test
    public void request_WithoutQuantity_NullName() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.request(null));
    }

    @Test
    public void request_WithoutQuantity_UnknownName() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.request("Water"));
    }

    @Test
    public void request_WithoutQuantity_LiquidIngredient_UsesLargestLiquidContainer() {
        Laboratory labo = new Laboratory(2);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        IngredientContainer result = labo.request("Water");

        //it chooses the largest container, and if not everything fits inside it, the remaining amount is lost
        assertEquals(Unit.BARREL, result.getCapacityUnit());
    }

    @Test
    public void request_WithoutQuantity_PowderIngredient_UsesLargestPowderContainer() {
        Laboratory labo = new Laboratory(2);
        IngredientType saltType = new IngredientType(new Name("Salt"), State.POWDER,
                new Temperature(0, 20));
        AlchemicIngredient salt = new AlchemicIngredient(
                saltType, new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.SACHET, salt));

        IngredientContainer result = labo.request("Salt");

        //it chooses the largest container, and if not everything fits inside it, the remaining amount is lost
        assertEquals(Unit.CHEST, result.getCapacityUnit());
    }


    @Test
    public void constructor_NoDevicesRegistered() {
        Laboratory labo = new Laboratory(1);

        assertNull(labo.getCoolingBox());
        assertNull(labo.getOven());
        assertNull(labo.getKettle());
        assertNull(labo.getTransmogrifier());

        assertFalse(labo.hasCoolingBox());
        assertFalse(labo.hasOven());
        assertFalse(labo.hasKettle());
        assertFalse(labo.hasTransmogrifier());
    }

    @Test
    public void constructor_Oven() {
        Laboratory labo = new Laboratory(1);

        Oven oven = new Oven(labo);

        assertSame(oven, labo.getOven());
        assertTrue(labo.hasOven());
        assertSame(labo, oven.getLaboratory());
    }

    @Test
    public void constructor_CoolingBox() {
        Laboratory labo = new Laboratory(1);

        CoolingBox coolingBox = new CoolingBox(labo);

        assertSame(coolingBox, labo.getCoolingBox());
        assertTrue(labo.hasCoolingBox());
        assertSame(labo, coolingBox.getLaboratory());
    }

    @Test
    public void constructor_Kettle() {
        Laboratory labo = new Laboratory(1);

        Kettle kettle = new Kettle(labo);

        assertSame(kettle, labo.getKettle());
        assertTrue(labo.hasKettle());
        assertSame(labo, kettle.getLaboratory());
    }

    @Test
    public void constructor_Transmogrifier() {
        Laboratory labo = new Laboratory(1);

        Transmogrifier transmogrifier = new Transmogrifier(labo);

        assertSame(transmogrifier, labo.getTransmogrifier());
        assertTrue(labo.hasTransmogrifier());
        assertSame(labo, transmogrifier.getLaboratory());
    }

    @Test
    public void execute_NullRecipe() {
        Laboratory labo = new Laboratory(1);

        assertThrows(IllegalArgumentException.class, () -> labo.execute(null, 1));
    }

    @Test
    public void execute_ZeroFactor() {
        Laboratory labo = new Laboratory(1);

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new SimpleRecipeStep(Operation.MIX))));

        assertThrows(IllegalArgumentException.class, () -> labo.execute(recipe, 0));
    }

    @Test
    public void execute_NegativeFactor() {
        Laboratory labo = new Laboratory(1);

        Recipe recipe = new Recipe(List.of(new SimpleRecipeStep(Operation.MIX)));

        assertThrows(IllegalArgumentException.class, () -> labo.execute(recipe, -1));
    }

    @Test
    public void execute_AddStep() {
        Laboratory labo = new Laboratory(1);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                new Name("Water"), new Quantity(2L, Unit.SPOON)))));

        labo.execute(recipe, 1);

        assertTrue(labo.hasIngredient("Water"));
        //stays 5 spoons, because execute takes 2 spoons out of labo and then at the end stores them back.
        assertEquals(5L, labo.getIngredient("Water").getAmountInSpoons());
    }

    @Test
    public void execute_AddStep_WithFactor() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));

        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(10L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.BOTTLE, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                new Name("Water"), new Quantity(2L, Unit.SPOON)))));

        labo.execute(recipe, 3);

        assertTrue(labo.hasIngredient("Water"));
        assertEquals(10L, labo.getIngredient("Water").getAmountInSpoons());
    }

    @Test
    public void execute_HeatStepWithoutOven() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));

        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                        new Name("Water"), new Quantity(2L, Unit.SPOON)),
                        new SimpleRecipeStep(Operation.HEAT))));

        assertThrows(IllegalStateException.class, () -> labo.execute(recipe, 1));
    }

    @Test
    public void execute_HeatStepWithOven_StoresHeatedIngredientBack() {
        Laboratory labo = new Laboratory(1);
        new Oven(labo);
        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                        new Name("Water"), new Quantity(2L, Unit.SPOON)),
                        new SimpleRecipeStep(Operation.HEAT))));

        labo.execute(recipe, 1);

        assertTrue(labo.hasIngredient("Water"));
        assertEquals("Heated Water", labo.getIngredient("Water").getFullName());
    }

    @Test
    public void execute_CoolStepWithoutCoolingBox() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));

        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                        new Name("Water"), new Quantity(2L, Unit.SPOON)),
                        new SimpleRecipeStep(Operation.COOL))));

        assertThrows(IllegalStateException.class, () -> labo.execute(recipe, 1));
    }

    @Test
    public void execute_CoolStepWithCoolingBox_StoresCooledIngredientBack() {
        Laboratory labo = new Laboratory(1);
        new CoolingBox(labo);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        AlchemicIngredient water = new AlchemicIngredient(waterType,
                new Quantity(5L, Unit.SPOON));

        labo.store(new IngredientContainer(Unit.VIAL, water));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(new IngredientRecipeStep(Operation.ADD,
                        new Name("Water"), new Quantity(2L, Unit.SPOON)),
                        new SimpleRecipeStep(Operation.COOL))));

        labo.execute(recipe, 1);

        assertTrue(labo.hasIngredient("Water"));
        assertEquals("Cooled Water", labo.getIngredient("Water").getFullName());
    }

    @Test
    public void execute_MixStepWithoutKettle() {
        Laboratory labo = new Laboratory(1);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));

        IngredientType beerType = new IngredientType(new Name("Beer"), State.LIQUID,
                new Temperature(0, 20));

        labo.store(new IngredientContainer(Unit.VIAL, new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON))));

        labo.store(new IngredientContainer(Unit.VIAL, new AlchemicIngredient(beerType,
                new Quantity(2L, Unit.SPOON))));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(
                new IngredientRecipeStep(Operation.ADD, new Name("Water"), new Quantity(1L, Unit.SPOON)),
                new IngredientRecipeStep(Operation.ADD, new Name("Beer"), new Quantity(1L, Unit.SPOON)),
                new SimpleRecipeStep(Operation.MIX))));

        assertThrows(IllegalStateException.class, () -> labo.execute(recipe, 1));
    }

    @Test
    public void execute_MixStepWithKettle() {
        Laboratory labo = new Laboratory(1);
        new Kettle(labo);

        IngredientType waterType = new IngredientType(new Name("Water"), State.LIQUID,
                new Temperature(0, 20));
        IngredientType beerType = new IngredientType(new Name("Beer"), State.LIQUID,
                new Temperature(0, 20));

        labo.store(new IngredientContainer(Unit.VIAL, new AlchemicIngredient(waterType,
                new Quantity(2L, Unit.SPOON))));

        labo.store(new IngredientContainer(Unit.VIAL, new AlchemicIngredient(beerType,
                new Quantity(2L, Unit.SPOON))));

        Recipe recipe = new Recipe(new ArrayList<>(List.of(
                new IngredientRecipeStep(Operation.ADD, new Name("Water"), new Quantity(1L, Unit.SPOON)),
                new IngredientRecipeStep(Operation.ADD, new Name("Beer"), new Quantity(1L, Unit.SPOON)),
                new SimpleRecipeStep(Operation.MIX))));

        labo.execute(recipe, 1);

        assertTrue(labo.hasIngredient("Beer mixed with Water")
                || labo.hasIngredient("Water mixed with Beer"));
    }




}
