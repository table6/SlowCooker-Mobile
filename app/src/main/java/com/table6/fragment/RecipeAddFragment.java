package com.table6.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.table6.activity.R;
import com.table6.object.RecipeContent;

import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeAddFragment extends Fragment {

    private TextInputEditText titleInput;
    private TextInputEditText prepTimeInput;
    private TextInputEditText cookTimeInput;
    private TextInputEditText servingSizeInput;

    private OnFragmentInteractionListener mListener;

    public RecipeAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeAddFragment.
     */
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
        prepTimeInput = (TextInputEditText) makeEditTextTime(view.findViewById(R.id.prepTimeTextInput));
        cookTimeInput = (TextInputEditText) makeEditTextTime(view.findViewById(R.id.cookTimeTextInput));
        servingSizeInput = (TextInputEditText) view.findViewById(R.id.servingSizeTextInput);

        Button addNewRecipeBtn = (Button) view.findViewById(R.id.addNewRecipeBtn);
        addNewRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String prepTime = prepTimeInput.getText().toString();
                String cookTime = cookTimeInput.getText().toString();
                String servingSize = servingSizeInput.getText().toString();
                RecipeContent.Recipe recipe = new RecipeContent.Recipe(title, prepTime, cookTime, servingSize);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public <T> T makeEditTextTime(T asset) {
        final TextInputEditText editText = (TextInputEditText) asset;
        if (editText != null) {
            // Make sure the edit text only has enough space for HH:MM.
            int maxLength = 5;
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

            // Set text to 00:00 by default when it gains focus.
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(editText.getText().toString().isEmpty()) {
                        editText.setText("00:00");
                    }
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void afterTextChanged(Editable s) {
                    String colonTime = "HH:mm";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(colonTime);

                    String originalText = editText.getText().toString();
                    formatter.parse(originalText);
                }
            });
        }

        return asset;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRecipeAddFragmentInteraction(RecipeContent.Recipe recipe);
    }
}
