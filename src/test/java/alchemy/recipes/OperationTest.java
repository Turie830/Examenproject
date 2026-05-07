package alchemy.recipes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static alchemy.recipes.Operation.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OperationTest {

    static List<Operation> operations_no_ingredient = new ArrayList<Operation>(List.of(MIX, COOL, HEAT));
    static List<Operation> operations_ingredient = new ArrayList<Operation>(List.of(ADD));

    @Test
    public void testRequiresIngredient() {
        for (Operation op : operations_no_ingredient) {
            assertFalse(op.requiresIngredient());
        }
        for (Operation op : operations_ingredient) {
            assertTrue(op.requiresIngredient());
        }

    }

}
