package alchemy.laboratory;

import alchemy.ingredients.IngredientContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeviceTest {

    @Test
    public void constructor_EffectiveLaboratory_StoresLaboratory() {
        Laboratory laboratory = new Laboratory();
        TestDevice device = new TestDevice(laboratory);

        assertSame(laboratory, device.getLaboratory());
    }

    @Test
    public void constructor_NullLaboratory_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new TestDevice(null));
    }

    private static class TestDevice extends Device {

        private TestDevice(Laboratory laboratory) {
            super(laboratory);
        }

        @Override
        public void add(IngredientContainer container) {
        }

        @Override
        public void execute() {
        }
    }
}
