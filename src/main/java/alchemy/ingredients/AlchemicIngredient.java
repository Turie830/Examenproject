package alchemy;

public class AlchemicIngredient {
    public String name;


    public static boolean isValidName(String name) {

        if (!name.matches("[a-zA-Z'() ]+"))
            return false;

        String[] words = name.split(" ");

        for(int i =0, String word : words; i++) {
            if (word[i][0])
        }

        if (words.length == 1) {
            return words[0].length() >= 3;
        }
        for(String word : words) {
            if (word.length() <= 2) {
                return false;
            }

        }
    }
