package alchemy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {

    @Test
    public void isValidName_ValidSingleWordNames() {
        assertTrue(Name.isValidName("Water"));
        assertTrue(Name.isValidName("Milk"));
        assertTrue(Name.isValidName("Salt"));
        assertTrue(Name.isValidName("Eye"));
    }

    @Test
    public void isValidName_ValidMultipleWordNames() {
        assertTrue(Name.isValidName("Lizard's Tale"));
        assertTrue(Name.isValidName("Rat's Eye Fluid"));
        assertTrue(Name.isValidName("Red Mushroom Gas"));
        assertTrue(Name.isValidName("(Red) Mushroom"));
    }

    @Test
    public void isValidName_NullOrBlankNames() {
        assertFalse(Name.isValidName(null));
        assertFalse(Name.isValidName(""));
        assertFalse(Name.isValidName(" "));
        assertFalse(Name.isValidName("   "));
    }

    @Test
    public void isValidName_InvalidSpaces() {
        assertFalse(Name.isValidName(" Water"));
        assertFalse(Name.isValidName("Water "));
        assertFalse(Name.isValidName("Red  Mushroom"));
        assertFalse(Name.isValidName("Red   Mushroom"));
    }

    @Test
    public void isValidName_InvalidCharacters() {
        assertFalse(Name.isValidName("Water2"));
        assertFalse(Name.isValidName("Red-Mushroom"));
        assertFalse(Name.isValidName("Red_Mushroom"));
        assertFalse(Name.isValidName("Red.Mushroom"));
        assertFalse(Name.isValidName("Red!"));
        assertFalse(Name.isValidName("Red@Mushroom"));
        assertFalse(Name.isValidName("Red[Mushroom]"));
    }

    @Test
    public void isValidName_InvalidWordLengthSingleWord() {
        assertFalse(Name.isValidName("Ox"));     // one word must have at least 3 letters
        assertFalse(Name.isValidName("A"));      // one word must have at least 3 letters
        assertFalse(Name.isValidName("(Ox)"));   // only 2 letters
        assertFalse(Name.isValidName("()"));     // 0 letters
        assertFalse(Name.isValidName("(e"));     // 1 letter
    }

    @Test
    public void isValidName_InvalidWordLengthMultipleWords() {
        assertFalse(Name.isValidName("Red A"));
        assertFalse(Name.isValidName("A Red"));
        assertFalse(Name.isValidName("Red (A)"));
        assertTrue(Name.isValidName("Ox Eye"));  // multiple words: each word at least 2 letters
    }

    @Test
    public void isValidName_InvalidCapitalization() {
        assertFalse(Name.isValidName("water"));
        assertFalse(Name.isValidName("red Mushroom"));
        assertFalse(Name.isValidName("Red mushroom"));
        assertFalse(Name.isValidName("REd Mushroom"));
        assertFalse(Name.isValidName("Red MUshroom"));
        assertFalse(Name.isValidName("(red) Mushroom"));
    }

    @Test
    public void isValidName_MixedAndWithNotAllowedInRegularNames() {
        assertFalse(Name.isValidName("mixed"));
        assertFalse(Name.isValidName("with"));
        assertFalse(Name.isValidName("Beer mixed with Coke"));
        assertFalse(Name.isValidName("Beer Mixed With Coke"));
    }

    @Test
    public void isValidMixtureName_MixedAndWithAllowed() {
        assertTrue(Name.isValidMixtureName("Beer mixed with Coke"));
        assertTrue(Name.isValidMixtureName("Water mixed with Salt"));
    }

    @Test
    public void isValidMixtureName_MixedAndWithMustBeLowercase() {
        assertFalse(Name.isValidMixtureName("Beer Mixed with Coke"));
        assertFalse(Name.isValidMixtureName("Beer mixed With Coke"));
        assertFalse(Name.isValidMixtureName("Beer Mixed With Coke"));
    }

    @Test
    public void isValidName_ForbiddenSimpleNameWords() {
        assertFalse(Name.isValidName("Heated"));
        assertFalse(Name.isValidName("Cooled"));
        assertFalse(Name.isValidName("Heated Water"));
        assertFalse(Name.isValidName("Cooled Salt"));
    }

    @Test
    public void constructor_ValidName() {
        Name name = new Name("Red Mushroom Gas");

        assertEquals("Red Mushroom Gas", name.getName());
    }

    @Test
    public void constructor_InvalidNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Name(null));
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
        assertThrows(IllegalArgumentException.class, () -> new Name("Water2"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Beer mixed with Coke"));
        assertThrows(IllegalArgumentException.class, () -> new Name("Heated Water"));
    }

    @Test
    public void createMixtureName_ValidMixtureName() {
        Name name = Name.createMixtureName("Beer mixed with Coke");

        assertEquals("Beer mixed with Coke", name.getName());
    }

    @Test
    public void createMixtureName_InvalidMixtureNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName(null));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName(""));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer Mixed with Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer mixed With Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer2 mixed with Coke"));
    }
}