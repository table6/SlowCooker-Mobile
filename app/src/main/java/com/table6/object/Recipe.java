package com.table6.object;

public class Recipe {
    private static String title;
    private static String prepTime;
    private static String cookTime;
    private static String servingSize;

    public Recipe () {
        this.title = "Example Recipe";
        this.prepTime = "00:30";
        this.cookTime = "02:00";
        this.servingSize = "9001";
    }

    public Recipe (String title, String prepTimeMinutes, String cookTimeMinutes, String servingSize) {
        this.title = title;
        this.prepTime = prepTimeMinutes;
        this.cookTime = cookTimeMinutes;
        this.servingSize = servingSize;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String x) {
        this.title = x;
    }

    public String getPrepTime() {
        return this.prepTime;
    }

    public String getCookTime() {
        return this.cookTime;
    }

    public String getServingSize() {
        return this.servingSize;
    }
}
