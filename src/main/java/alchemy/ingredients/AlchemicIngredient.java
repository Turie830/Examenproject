package alchemy.ingredients;

public class AlchemicIngredient {
    public String name;


    public static boolean isValidName(String name) {

        if (!name.matches("[a-zA-Z'() ]+"))
            return false;

        String[] words = name.split(" ");

        for (String word : words) {
            if (!Character.isUpperCase(word.charAt(0))
                    && word.charAt(0) != '\''
                    && word.charAt(0) != '('
                    && word.charAt(0) != ')') {
                return false;
            }
        }

        if (words.length == 1) {
            return words[0].length() >= 3;
        }
        for (String word : words) {
            if (word.length() <= 2) {
                return false;
            }
        }
    }
