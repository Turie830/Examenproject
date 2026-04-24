package alchemy.ingredients;

public class AlchemicIngredient {
    public String name;


    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z'()]+"));
    }










}
