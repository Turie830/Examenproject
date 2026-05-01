package alchemy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureTest {

    @Test
    public void constructor_ValidTemperature_HotTemperature() {
        Temperature temperature = new Temperature(0, 20);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void constructor_ValidTemperature_ColdTemperature() {
        Temperature temperature = new Temperature(20, 0);
        assertEquals(20, temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void constructor_ValidTemperature_NeutralTemperature() {
        Temperature temperature = new Temperature(0, 0);
        assertEquals(0, temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void constructor_InvalidTemperature_ColdAndHot() {
        Temperature temperature = new Temperature(10, 30);
        assertEquals(0, temperature.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, temperature.getHotness()); //set to DEFAULT_HOTNESS
    }

    @Test
    public void constructor_InvalidTemperature_NegativeColdness() {
        Temperature temperature = new Temperature(-1, 30);
        assertEquals(0, temperature.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, temperature.getHotness()); //set to DEFAULT_HOTNESS
    }


    @Test
    public void constructor_InvalidTemperature_NegativeHotness() {
        Temperature temperature = new Temperature(10, -1);
        assertEquals(0, temperature.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, temperature.getHotness()); //set to DEFAULT_HOTNESS
    }

    @Test
    public void constructor_InvalidTemperature_ColdnessAboveUpperBound() {
        Temperature temperature = new Temperature(Temperature.getUpperBound() + 1, 0);
        assertEquals(0, temperature.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, temperature.getHotness()); //set to DEFAULT_HOTNESS
    }


    @Test
    public void constructor_InvalidTemperature_HotnessAboveUpperBounde() {
        Temperature temperature = new Temperature(0, Temperature.getUpperBound() + 1);
        assertEquals(0, temperature.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, temperature.getHotness()); //set to DEFAULT_HOTNESS
    }

    @Test
    public void copyConstructor_EffectiveTemperature_CopiesColdnessAndHotness() {
        Temperature original = new Temperature(40, 0);
        Temperature copy = new Temperature(original);
        assertEquals(40, copy.getColdness());
        assertEquals(0, copy.getHotness());
    }

    @Test
    public void copyConstructor_NullTemperature() {
        Temperature copy = new Temperature(null);
        assertEquals(0, copy.getColdness()); //set to DEFAULT_COLDNESS
        assertEquals(20, copy.getHotness()); //set to DEFAULT_HOTNESS
    }

    @Test
    public void copyConstructor_CopyIsIndependentFromOriginal() {
        Temperature original = new Temperature(0, 20);
        Temperature copy = new Temperature(original);

        copy.heat(50);

        assertEquals(0, original.getColdness());
        assertEquals(20, original.getHotness());

        assertEquals(0, copy.getColdness());
        assertEquals(70, copy.getHotness());
    }


    @Test
    public void getUpperBound_Returns10000() {
        assertEquals(10000, Temperature.getUpperBound());
    }

    @Test

    public void getTemperature_ReturnsColdnessFirstAndHotnessSecond() {
        Temperature temperature = new Temperature(30, 0);

        long[] result = temperature.getTemperature();

        assertEquals(2, result.length);
        assertEquals(30, result[0]);
        assertEquals(0, result[1]);
    }

    @Test
    public void getTemperature_ReturnsNewArray() {
        Temperature temperature = new Temperature(0, 20);

        long[] result = temperature.getTemperature();

        result[0] = 999;
        result[1] = 999;

        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void isValidTemperature_ValidTemperatures() {
        assertTrue(Temperature.isValidTemperature(0, 0));
        assertTrue(Temperature.isValidTemperature(0, 20));
        assertTrue(Temperature.isValidTemperature(20, 0));
        assertTrue(Temperature.isValidTemperature(0, Temperature.getUpperBound()));
        assertTrue(Temperature.isValidTemperature(Temperature.getUpperBound(), 0));
    }

    @Test
    public void isValidTemperature_InvalidTemperatures() {
        assertFalse(Temperature.isValidTemperature(-1, 0));
        assertFalse(Temperature.isValidTemperature(0, -1));
        assertFalse(Temperature.isValidTemperature(Temperature.getUpperBound() + 1, 0));
        assertFalse(Temperature.isValidTemperature(0, Temperature.getUpperBound() + 1));
        assertFalse(Temperature.isValidTemperature(1, 1));
        assertFalse(Temperature.isValidTemperature(50, 20));
    }

    @Test
    public void heat_NonPositiveAmount_DoesNotChangeTemperature() {
        Temperature temperature = new Temperature(0, 20);
        temperature.heat(0);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
        temperature.heat(-10);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void heat_HotTemperature_IncreasesHotness() {
        Temperature temperature = new Temperature(0, 20);
        temperature.heat(30);
        assertEquals(0, temperature.getColdness());
        assertEquals(50, temperature.getHotness());
    }

    @Test
    public void heat_ColdTemperature_DecreasesColdnessFirst() {
        Temperature temperature = new Temperature(50, 0);
        temperature.heat(30);
        assertEquals(20, temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void heat_ColdTemperature_WithRemainingHeat_IncreasesHotness() {
        Temperature temperature = new Temperature(50, 0);
        temperature.heat(70);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void heat_DoesNotExceedUpperBound() {
        Temperature temperature = new Temperature(0, 9990);
        temperature.heat(50);
        assertEquals(0, temperature.getColdness());
        assertEquals(Temperature.getUpperBound(), temperature.getHotness());
    }

    @Test
    public void cool_NonPositiveAmount_DoesNotChangeTemperature() {
        Temperature temperature = new Temperature(0, 20);
        temperature.cool(0);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
        temperature.cool(-10);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void cool_ColdTemperature_IncreasesColdness() {
        Temperature temperature = new Temperature(20, 0);
        temperature.cool(30);
        assertEquals(50, temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void cool_HotTemperature_DecreasesHotnessFirst() {
        Temperature temperature = new Temperature(0, 50);
        temperature.cool(30);
        assertEquals(0, temperature.getColdness());
        assertEquals(20, temperature.getHotness());
    }

    @Test
    public void cool_HotTemperature_WithRemainingColdness_IncreasesColdness() {
        Temperature temperature = new Temperature(0, 50);
        temperature.cool(70);
        assertEquals(20, temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void cool_DoesNotExceedUpperBound() {
        Temperature temperature = new Temperature(9990, 0);
        temperature.cool(50);
        assertEquals(Temperature.getUpperBound(), temperature.getColdness());
        assertEquals(0, temperature.getHotness());
    }

    @Test
    public void isHotterThan_OtherTemperatureIsNull() {
        Temperature temperature = new Temperature(0, 20);
        assertFalse(temperature.isHotterThan(null));
    }

    @Test
    public void isHotterThan_ThisTemperatureIsHotter() {
        Temperature temperature = new Temperature(0, 100);
        Temperature other = new Temperature(0, 20);
        assertTrue(temperature.isHotterThan(other));
    }

    @Test
    public void isHotterThan_ThisTemperatureIsNotHotter() {
        Temperature temperature = new Temperature(0, 20);
        Temperature other = new Temperature(0, 100);
        assertFalse(temperature.isHotterThan(other));
    }

    @Test
    public void isColderThan_OtherTemperatureIsNull() {
        Temperature temperature = new Temperature(0, 20);
        assertFalse(temperature.isColderThan(null));
    }

    @Test
    public void isColderThan_ThisTemperatureIsColder() {
        Temperature temperature = new Temperature(50, 0);
        Temperature other = new Temperature(0, 20);
        assertTrue(temperature.isColderThan(other));
    }

    @Test
    public void isColderThan_ThisTemperatureIsNotColder() {
        Temperature temperature = new Temperature(0, 20);
        Temperature other = new Temperature(50, 0);
        assertFalse(temperature.isColderThan(other));
    }

    @Test
    public void difference_OtherTemperature() {
        Temperature temperature = new Temperature(0, 100);
        Temperature other = new Temperature(50, 0);
        assertEquals(150, temperature.difference(other));
    }

    @Test
    public void difference_NullOtherTemperature_UsesDefaultTemperature() {
        Temperature temperature = new Temperature(0, 100);
        assertEquals(80, temperature.difference(null));
    }

}
