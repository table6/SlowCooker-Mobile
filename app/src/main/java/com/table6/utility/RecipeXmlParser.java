package com.table6.utility;

import android.util.Xml;

import com.table6.object.RecipeContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://developer.android.com/training/basics/network-ops/xml
public class RecipeXmlParser {

    // We don't use namespaces.
    private static final String ns = null;

    /**
     * Parse the given input stream.
     * @param in the input stream to be parsed.
     * @return a list of recipe objects.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<RecipeContent.Recipe> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Read the contents of the given parser.
     * @param parser the parser to be processed.
     * @return a list of recipe objects.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<RecipeContent.Recipe> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RecipeContent.Recipe> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }

        return entries;
    }

    /**
     * Parse the contents of an XML entry.
     * @param parser the parser to be used.
     * @return a populated Recipe object.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private RecipeContent.Recipe readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String prepTime = null;
        String cookTime = null;
        String servingSize = null;
        ArrayList<String> ingredients = null;
        String directions = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("prepTime")) {
                prepTime = readPrepTime(parser);
            } else if (name.equals("cookTime")) {
                cookTime = readCookTime(parser);
            } else if (name.equals("servingSize")) {
                servingSize = readServingSize(parser);
            } else if (name.equals("ingredients")) {
                ingredients = readIngredients(parser);
            } else if (name.equals("directions")) {
                directions = readDirections(parser);
            } else {
                skip(parser);
            }
        }

        return new RecipeContent.Recipe(title, prepTime, cookTime, servingSize, directions, ingredients);
    }

    /**
     * Parse a title tag.
     * @param parser the parser to be used.
     * @return the contents of the title tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");

        return title;
    }

    /**
     * Parse a preparation time tag.
     * @param parser the parser to be used.
     * @return the contents of the preparation time tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readPrepTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "prepTime");
        String prepTime = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "prepTime");

        return prepTime;
    }

    /**
     * Parse a cook time tag.
     * @param parser the parser to be used.
     * @return the contents of the cook time tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readCookTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "cookTime");
        String cookTime = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cookTime");

        return cookTime;
    }

    /**
     * Parse a serving size tag.
     * @param parser the parser to be used.
     * @return the contents of the serving size tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readServingSize(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "servingSize");
        String servingSize = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "servingSize");

        return servingSize;
    }

    /**
     * Parse an ingredients tag.
     * @param parser the parser to be used.
     * @return the contents of the ingredients tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private ArrayList<String> readIngredients(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ingredients");
        String ingredients = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ingredients");

        return new ArrayList<>(Arrays.asList(ingredients.split(";")));
    }

    /**
     * Parse a directions tag.
     * @param parser the parser to be used.
     * @return the contents of the directions tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readDirections(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "directions");
        String directions = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "directions");

        return directions;
    }

    /**
     * Get the text of a closed tag.
     * @param parser the parser to be used.
     * @return The text of the given closed tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    /**
     * Skip the tag. This function is called when a tag is not supported.
     * @param parser the parser to be used.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
