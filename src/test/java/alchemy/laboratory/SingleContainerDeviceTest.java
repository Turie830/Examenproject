package alchemy.laboratory;

import alchemy.Name;
import alchemy.Unit;
import alchemy.ingredients.AlchemicIngredient;
import alchemy.ingredients.IngredientContainer;
import alchemy.ingredients.IngredientType;
import alchemy.ingredients.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleContainerDeviceTest {

    private TestSingleContainerDevice device;
    private AlchemicIngredient ingredient;

    @BeforeEach
    public void setUp() {
        device = new TestSingleContainerDevice(new Laboratory());
        ingredient = new AlchemicIngredient(new IngredientType(Name.WATER), new Quantity(1L, Unit.VIAL));
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
    public void add_FilledContainer_StoresIngredientAsDeviceContent() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, ingredient);

        device.add(container);

        assertSame(ingredient, device.exposedDeviceContent());
    }

    @Test
    public void emptyDeviceContent_AfterAddingIngredient_ClearsDeviceContent() {
        IngredientContainer container = new IngredientContainer(Unit.BOTTLE, ingredient);
        device.add(container);

        device.exposedEmptyDeviceContent();

        assertNull(device.exposedDeviceContent());
    }

    @Test
    public void createResultContainer_NullIngredient_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> device.exposedCreateResultContainer(null));
    }

    @Test
    public void createResultContainer_IngredientWithValidCapacityUnit_DoesNotThrow() {
        assertDoesNotThrow(() -> device.exposedCreateResultContainer(ingredient));
    }

    @Test
    public void getResult_ReturnsCopyOfResultContainer() {
        device.exposedCreateResultContainer(ingredient);

        IngredientContainer result = device.getResult();
        result.empty();

        assertFalse(device.getResult().isEmpty());
        assertSame(ingredient, device.getResult().getIngredient());
    }

    private static class TestSingleContainerDevice extends SingleContainerDevice {

        private TestSingleContainerDevice(Laboratory laboratory) {
            super(laboratory);
        }

        private AlchemicIngredient exposedDeviceContent() {
            return getDeviceContent();
        }

        private void exposedEmptyDeviceContent() {
            emptyDeviceContent();
        }

        private void exposedCreateResultContainer(AlchemicIngredient resultIngredient) {
            createResultContainer(resultIngredient);
        }

        @Override
        public void execute() {
        }
    }
}
