package com.table6.object;

public class Recipe {
    private static String title;
    private static int prepTimeMinutes;
    private static int cookTimeMinutes;
    private static int servingSize;

    public Recipe () {
        this.title = "Example Recipe";
        this.prepTimeMinutes = 30;
        this.cookTimeMinutes = 120;
        this.servingSize = -1;
    }

    public Recipe (String title, int prepTimeMinutes, int cookTimeMinutes, int servingSize) {
        this.title = title;
        this.prepTimeMinutes = prepTimeMinutes;
        this.cookTimeMinutes = cookTimeMinutes;
        this.servingSize = servingSize;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPrepTimeMinutes() {
        return this.prepTimeMinutes;
    }

    public int getCookTimeMinutes() {
        return this.cookTimeMinutes;
    }

    public int getServingSize() {
        return this.servingSize;
    }
}
