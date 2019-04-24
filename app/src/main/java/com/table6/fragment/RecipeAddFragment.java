package com.table6.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.table6.activity.R;
import com.table6.object.RecipeContent;

import java.util.ArrayList;

public class RecipeAddFragment extends Fragment {

    private TextInputEditText titleInput;
    private TextInputEditText prepTimeInput;
    private TextInputEditText cookTimeInput;
    private TextInputEditText servingSizeInput;
    private TextInputEditText directionsInput;
    private TextInputEditText ingredientTextEdit;
    private ArrayList<TextInputEditText> ingredientTextInputs;

    private OnRecipeAddFragmentInteractionListener mListener;

    public RecipeAddFragment() {
        // Required empty public constructor
    }

    public static RecipeAddFragment newInstance() {
        RecipeAddFragment fragment = new RecipeAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleInput = (TextInputEditText) view.findViewById(R.id.titleTextInput);
        prepTimeInput = (TextInputEditText) view.findViewById(R.id.prepTimeTextInput);
        cookTimeInput = (TextInputEditText) view.findViewById(R.id.cookTimeTextInput);
        servingSizeInput = (TextInputEditText) view.findViewById(R.id.servingSizeTextInput);
        directionsInput = (TextInputEditText) view.findViewById(R.id.directionsTextInput);

        Button addNewRecipeBtn = (Button) view.findViewById(R.id.addNewRecipeBtn);
        addNewRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String prepTime = prepTimeInput.getText().toString();
                String cookTime = cookTimeInput.getText().toString();
                String servingSize = servingSizeInput.getText().toString();
                String directions = directionsInput.getText().toString();

                ArrayList<String> ingredients = new ArrayList<>();
                for(TextInputEditText ingredientInput : ingredientTextInputs) {
                    String ingredient = ingredientInput.getText().toString();
                    ingredients.add(ingredient);
                }

                RecipeContent.Recipe recipe = new RecipeContent.Recipe(title, prepTime, cookTime, servingSize, directions, ingredients);

                // TODO: Check validity of other fields
                if (title.isEmpty()) {
                    Toast.makeText(getActivity(), "Title cannot be empty", Toast.LENGTH_LONG ).show();
                } else if (RecipeContent.ITEMS.contains(recipe)) {
                    Toast.makeText(getActivity(), "A recipe with that title already exists", Toast.LENGTH_LONG).show();
                } else {
                    mListener.onRecipeAddFragmentInteraction(recipe);
                    Toast.makeText(getActivity(), recipe.title + " added to recipe list", Toast.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        ingredientTextInputs = new ArrayList<>();
        ingredientTextEdit = view.findViewById(R.id.ingredientsEditTxt);

        ImageButton addIngredientBtn = (ImageButton) view.findViewById(R.id.addIngredientBtn);
        final LinearLayout ingredientContainer = (LinearLayout) view.findViewById(R.id.ingredientsContainer);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = ingredientTextEdit.getText().toString();
                if(!ingredient.isEmpty()) {
                    // Add a new EditText to the container.
                    TextInputEditText editText = new TextInputEditText(v.getContext());
                    editText.setText(ingredient);
                    ingredientTextEdit.setText("");
                    ingredientContainer.addView(editText);

                    // Add the EditText instead of the contents of the EditText at the time in case the user decides to edit after adding.
                    ingredientTextInputs.add(editText);
                } else {
                    Toast.makeText(getActivity(), "Fill in the ingredient line to add it to the recipe.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_add, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeAddFragmentInteractionListener) {
            mListener = (OnRecipeAddFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRecipeAddFragmentInteractionListener {
        void onRecipeAddFragmentInteraction(RecipeContent.Recipe recipe);
    }
}
