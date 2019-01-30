package com.table6.utility;

import android.util.Xml;

import com.table6.object.RecipeContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// https://developer.android.com/training/basics/network-ops/xml
public class RecipeXmlParser {

    // We don't use namespaces
    private static final String ns = null;

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

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private RecipeContent.Recipe readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String prepTime = null;
        String cookTime = null;
        String servingSize = null;

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
            } else {
                skip(parser);
            }
        }

        return new RecipeContent.Recipe(title, prepTime, cookTime, servingSize);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");

        return title;
    }

    // Processes title tags in the feed.
    private String readPrepTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "prepTime");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "prepTime");

        return title;
    }

    // Processes title tags in the feed.
    private String readCookTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "cookTime");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cookTime");

        return title;
    }

    // Processes summary tags in the feed.
    private String readServingSize(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "servingSize");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "servingSize");

        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

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
