package com.table6.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeContent {
    public static final List<Recipe> ITEMS = new ArrayList<>();
    public static final Map<String, Recipe> ITEM_MAP = new HashMap<>();

    static {
        addItem(new Recipe("Chili", "00:15", "08:00", "8"));
        addItem(new Recipe("Chicken Barbecue Potatoes", "00:10", "04:00", "6"));
        addItem(new Recipe("Beef Stew", "00:30", "04:00", "6"));
        addItem(new Recipe("Steel-cut Oats", "00:15", "06:00", "6"));

    }

    private static void addItem(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }

    private static Recipe createDummyItem(int position) {
        return new Recipe(String.valueOf(position), "prep time", "cook time", "serving size");
    }

    public static class Recipe {
        public final String title;
        public final String prepTime;
        public final String cookTime;
        public final String servingSize;

        public Recipe(String title, String prepTime, String cookTime, String servingSize) {
            this.title = title;
            this.prepTime = prepTime;
            this.cookTime = cookTime;
            this.servingSize = servingSize;
        }
    }
}
