package alchemy;

import alchemy.ingredients.AlchemicIngredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {

    @Test
    public void isValidName_ValidSingleWordNames() {
        assertTrue(AlchemicIngredient.isValidName("Water"));
        assertTrue(AlchemicIngredient.isValidName("Milk"));
        assertTrue(AlchemicIngredient.isValidName("Salt"));
        assertTrue(AlchemicIngredient.isValidName("Eye"));
    }

    @Test
    public void isValidName_ValidMultipleWordNames() {
        assertTrue(AlchemicIngredient.isValidName("Lizard's Tale"));
        assertTrue(AlchemicIngredient.isValidName("Rat's Eye Fluid"));
        assertTrue(AlchemicIngredient.isValidName("Red Mushroom Gas"));
        assertTrue(AlchemicIngredient.isValidName("(Red) Mushroom"));
    }

    @Test
    public void isValidName_NullOrBlankNames() {
        assertFalse(AlchemicIngredient.isValidName(null));
        assertFalse(AlchemicIngredient.isValidName(""));
        assertFalse(AlchemicIngredient.isValidName(" "));
        assertFalse(AlchemicIngredient.isValidName("   "));
    }

    @Test
    public void isValidName_InvalidSpaces() {
        assertFalse(AlchemicIngredient.isValidName(" Water"));
        assertFalse(AlchemicIngredient.isValidName("Water "));
        assertFalse(AlchemicIngredient.isValidName("Red  Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red   Mushroom"));
    }

    @Test
    public void isValidName_InvalidCharacters() {
        assertFalse(AlchemicIngredient.isValidName("Water2"));
        assertFalse(AlchemicIngredient.isValidName("Red-Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red_Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red.Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red!"));
        assertFalse(AlchemicIngredient.isValidName("Red@Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red[Mushroom]"));
    }

    @Test
    public void isValidName_InvalidWordLengthSingleWord() {
        assertFalse(AlchemicIngredient.isValidName("Ox"));     // one word must have at least 3 letters
        assertFalse(AlchemicIngredient.isValidName("A"));      // one word must have at least 3 letters
        assertFalse(AlchemicIngredient.isValidName("(Ox)"));   // only 2 letters
        assertFalse(AlchemicIngredient.isValidName("()"));     // 0 letters
        assertFalse(AlchemicIngredient.isValidName("(e"));     // 1 letter
    }

    @Test
    public void isValidName_InvalidWordLengthMultipleWords() {
        assertFalse(AlchemicIngredient.isValidName("Red A"));
        assertFalse(AlchemicIngredient.isValidName("A Red"));
        assertFalse(AlchemicIngredient.isValidName("Red (A)"));
        assertTrue(AlchemicIngredient.isValidName("Ox Eye"));  // multiple words: each word at least 2 letters
    }

    @Test
    public void isValidName_InvalidCapitalization() {
        assertFalse(AlchemicIngredient.isValidName("water"));
        assertFalse(AlchemicIngredient.isValidName("red Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red mushroom"));
        assertFalse(AlchemicIngredient.isValidName("REd Mushroom"));
        assertFalse(AlchemicIngredient.isValidName("Red MUshroom"));
        assertFalse(AlchemicIngredient.isValidName("(red) Mushroom"));
    }

    @Test
    public void isValidName_MixedAndWithNotAllowedInRegularNames() {
        assertFalse(AlchemicIngredient.isValidName("mixed"));
        assertFalse(AlchemicIngredient.isValidName("with"));
        assertFalse(AlchemicIngredient.isValidName("Beer mixed with Coke"));
        assertFalse(AlchemicIngredient.isValidName("Beer Mixed With Coke"));
    }

    @Test
    public void isValidMixtureName_MixedAndWithAllowed() {
        assertTrue(AlchemicIngredient.isValidMixtureName("Beer mixed with Coke"));
        assertTrue(AlchemicIngredient.isValidMixtureName("Water mixed with Salt"));
    }

    @Test
    public void isValidMixtureName_MixedAndWithMustBeLowercase() {
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer Mixed with Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer mixed With Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer Mixed With Coke"));
    }

    @Test
    public void isValidName_ForbiddenSimpleNameWords() {
        assertFalse(AlchemicIngredient.isValidName("Heated"));
        assertFalse(AlchemicIngredient.isValidName("Cooled"));
        assertFalse(AlchemicIngredient.isValidName("Heated Water"));
        assertFalse(AlchemicIngredient.isValidName("Cooled Salt"));
    }

    @Test
    public void constructor_ValidName() {
        Name name = new Name("Red Mushroom Gas");

        assertEquals("Red Mushroom Gas", name.getName());
    }

    @Test
    public void constructor_InvalidName() {
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


    //ToDO: dit klopt niet doordat isValidMixtureName() niet klopt
    @Test
    public void createMixtureName_InvalidMixtureName() {
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName(null));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName(""));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Water"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer with Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer mixed Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer Mixed with Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer mixed With Coke"));
        assertThrows(IllegalArgumentException.class, () -> Name.createMixtureName("Beer2 mixed with Coke"));
    }

    @Test
    public void isValidMixtureName_MustContainMixedWithStructure() {
        assertFalse(AlchemicIngredient.isValidMixtureName("Water"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer with Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer mixed Coke"));
        assertFalse(AlchemicIngredient.isValidMixtureName("Beer mixed"));
        assertFalse(AlchemicIngredient.isValidMixtureName("mixed with Coke"));
    }





}
