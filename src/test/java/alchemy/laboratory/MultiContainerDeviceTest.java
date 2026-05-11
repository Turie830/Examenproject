package alchemy.laboratory;

import alchemy.Name;
import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import alchemy.ingredients.IngredientType;
import alchemy.ingredients.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MultiContainerDeviceTest {

    private TestMultiContainerDevice device;

    @BeforeEach
    public void setUp() {
        device = new TestMultiContainerDevice(new Laboratory(1));
    }

    @Test
    public void add_NullContainer_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> device.add(null));
    }

    @Test
    public void add_EmptyContainer_ThrowsException() {
        IngredientContainer emptyContainer = new IngredientContainer(Unit.BOTTLE);

        assertThrows(IllegalArgumentException.class, () -> device.add(emptyContainer));
    }

    @Test
    public void add_FilledContainer_DoesNotThrow() {
        AlchemicIngredient ingredient =
                new AlchemicIngredient(new IngredientType(Name.WATER), new Quantity(1L, Unit.VIAL));
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, ingredient);

        assertDoesNotThrow(() -> device.add(container));
    }

    private static class TestMultiContainerDevice extends MultiContainerDevice {

        private TestMultiContainerDevice(Laboratory laboratory) {
            super(laboratory);
        }

        @Override
        public void execute() {
        }
    }
}
