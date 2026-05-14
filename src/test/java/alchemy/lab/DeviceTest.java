package alchemy.lab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceTest {

    @Test
    public void constructor_EffectiveLaboratory_StoresLaboratory() {
        Laboratory laboratory = new Laboratory(1);
        TestDevice device = new TestDevice(laboratory);

        assertSame(laboratory, device.getLaboratory());
    }

    @Test
    public void constructor_EffectiveLaboratory_RegistersDeviceInLaboratory() {
        Laboratory laboratory = new Laboratory(1);
        TestDevice device = new TestDevice(laboratory);

        assertSame(laboratory, device.getLaboratory());
        assertEquals(1, laboratory.getNbDevices());
        assertTrue(laboratory.hasAsDevice(device));
    }

    @Test
    public void constructor_DuplicateConcreteDeviceClass_ThrowsException() {
        Laboratory laboratory = new Laboratory(1);

        new TestDevice(laboratory);

        assertThrows(IllegalStateException.class, () -> new TestDevice(laboratory));
    }

    @Test
    public void constructor_SameConcreteDeviceClassInDifferentLaboratories_IsAllowed() {
        Laboratory firstLaboratory = new Laboratory(1);
        Laboratory secondLaboratory = new Laboratory(1);

        TestDevice firstDevice = new TestDevice(firstLaboratory);
        TestDevice secondDevice = new TestDevice(secondLaboratory);

        assertSame(firstLaboratory, firstDevice.getLaboratory());
        assertSame(secondLaboratory, secondDevice.getLaboratory());
        assertTrue(firstLaboratory.hasAsDevice(firstDevice));
        assertTrue(secondLaboratory.hasAsDevice(secondDevice));
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
