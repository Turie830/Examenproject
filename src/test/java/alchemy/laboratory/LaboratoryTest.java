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




}
