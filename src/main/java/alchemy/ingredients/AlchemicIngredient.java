package alchemy.ingredients;

public class AlchemicIngredient {
    public String name;


    public static boolean isValidName(String name) {

        //ToDO: die 2 dingen die checken naar word.char in 1 fct schrijven die regex gebruikt

        if (!name.matches("[a-zA-Z'() ]+"))
            return false;

        String[] words = name.split(" ");

        if (words.length == 1) {
            return words[0].length() >= 3;
        }
        for (String word : words) {
            if (word.length() <= 2) {
                return false;
            }
        }

        for (String word : words) {
            if (word.equals("mixed") || word.equals("with")) {
                continue;
            }
            // if 'mixed' or 'with' is written with capital letters, return false
            if (word.equals("Mixed") || word.equals("With")) {
                return false;
            }
            if (!Character.isUpperCase(word.charAt(0))
                    && word.charAt(0) != '\''
                    && word.charAt(0) != '('
                    && word.charAt(0) != ')') {
                return false;
            }
            for (int i = 1; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (!Character.isLowerCase(letter)
                        && letter != '('
                        && letter != ')'
                        && letter != '\'') {
                    return false;
                }
            }
        }



        return true;
    }










}
