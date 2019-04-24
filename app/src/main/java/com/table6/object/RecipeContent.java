package com.table6.object;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.table6.utility.RecipeXmlParser;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeContent extends Application {
    public static List<Recipe> ITEMS;
    public static Map<String, Recipe> ITEM_MAP;
    public static final String recipeInputFileName = "user_saved_recipes.xml";

    // https://developer.android.com/training/data-storage/files#java
    public static void getUserSavedRecipes(Context context) {
        ITEMS = new ArrayList<>();
        ITEM_MAP = new HashMap<>();

        RecipeXmlParser parser = new RecipeXmlParser();
        List<Recipe> parsedList = null;

        String temp = context.getFilesDir().getAbsolutePath();

        try {
            parsedList = parser.parse(new FileInputStream(new File(context.getFilesDir(), recipeInputFileName)));
        } catch (FileNotFoundException e) {
            Log.e("", e.getMessage());
        } catch (XmlPullParserException e) {
            Log.e("", e.getMessage());
        } catch (IOException e ) {
            Log.e("", e.getMessage());
        }

        if (parsedList != null) {
            for (Recipe r : parsedList) {
                addItem(r);
            }
        } else if (ITEMS.isEmpty()) {
            addItem(new Recipe());
        }
    }

    // https://developer.android.com/training/data-storage/files#java
    public static void setUserSavedRecipes(Context context) {
        FileOutputStream outputStream = null;

        try {
            outputStream = context.openFileOutput(recipeInputFileName, Context.MODE_PRIVATE);

            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);

            serializer.startTag(null, "root");

            for (Recipe recipe : ITEMS) {
                serializer.startTag(null, "entry");

                serializer.startTag(null, "title");
                serializer.text(recipe.title);
                serializer.endTag(null, "title");

                serializer.startTag(null, "prepTime");
                serializer.text(recipe.prepTime);
                serializer.endTag(null, "prepTime");

                serializer.startTag(null, "cookTime");
                serializer.text(recipe.cookTime);
                serializer.endTag(null, "cookTime");

                serializer.startTag(null, "servingSize");
                serializer.text(recipe.servingSize);
                serializer.endTag(null, "servingSize");

                serializer.startTag(null, "directions");
                serializer.text(recipe.directions);
                serializer.endTag(null, "directions");

                serializer.startTag(null, "ingredients");
                for(String ingredient : recipe.ingredients) {
                    serializer.text(ingredient + ";");
                }
                serializer.endTag(null, "ingredients");

                serializer.endTag(null, "entry");
            }

            serializer.endTag(null, "root");

            serializer.endDocument();
            serializer.flush();

            String data = writer.toString();
            outputStream.write(data.getBytes());

            outputStream.close();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    public static void addItem(Recipe item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }

    public static boolean removeItem(String recipeTitle) {
        if (ITEM_MAP.containsKey(recipeTitle)) {
            removeItem(ITEM_MAP.get(recipeTitle));
            return true;
        }

        return false;
    }

    private static void removeItem(Recipe recipe) {
        ITEMS.remove(recipe);
        ITEM_MAP.remove(recipe.title);
    }

    public static boolean editItem(RecipeContent.Recipe recipe) {
        if (ITEM_MAP.containsKey(recipe.title)) {
            removeItem(recipe);
            addItem(recipe);
            return true;
        }

        return false;
    }

    public static class Recipe {
        public final String title;
        public final String prepTime;
        public final String cookTime;
        public final String servingSize;
        public final String directions;
        public final ArrayList<String> ingredients;

        public Recipe() {
            this.title = "Example Recipe";
            this.prepTime = "00:00";
            this.cookTime = "00:00";
            this.servingSize = "0";
            this.directions = "Example directions...";
            this.ingredients = new ArrayList<>();
        }

        public Recipe(String title, String prepTime, String cookTime, String servingSize) {
            this.title = title;
            this.prepTime = prepTime;
            this.cookTime = cookTime;
            this.servingSize = servingSize;
            this.directions = "Example directions...";
            this.ingredients = new ArrayList<>();
        }

        public Recipe(String title, String prepTime, String cookTime, String servingSize, String directions, ArrayList<String> ingredients) {
            this.title = title;
            this.prepTime = prepTime;
            this.cookTime = cookTime;
            this.servingSize = servingSize;
            this.directions = directions;
            this.ingredients = ingredients;
        }

        @Override
        public String toString() {
            String allIngredients = "";
            for(String ingredient : this.ingredients) {
                allIngredients += ", " + ingredient;
            }

            return this.title + "\n\t" + this.prepTime + ", " + this.cookTime + ", " + this.servingSize + "\n\t" + this.directions + "\n\t" + allIngredients;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Recipe)obj).title.equals(this.title);
        }
    }
}
