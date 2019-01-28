package com.table6.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.table6.activity.R;
import com.table6.object.RecipeContent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_PREP_TIME = "prepTime";
    private static final String ARG_COOK_TIME = "cookTime";
    private static final String ARG_SERVING_SIZE = "servingSize";

    private String title;
    private String prepTime;
    private String cookTime;
    private String servingSize;

    private OnFragmentInteractionListener mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String title, String prepTime, String cookTime, String servingSize) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PREP_TIME, prepTime);
        args.putString(ARG_COOK_TIME, cookTime);
        args.putString(ARG_SERVING_SIZE, servingSize);
        fragment.setArguments(args);

        return fragment;
    }

    public static RecipeDetailFragment newInstance(RecipeContent.Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, recipe.title);
        args.putString(ARG_PREP_TIME, recipe.prepTime);
        args.putString(ARG_COOK_TIME, recipe.cookTime);
        args.putString(ARG_SERVING_SIZE, recipe.servingSize);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.prepTime = getArguments().getString(ARG_PREP_TIME);
            this.cookTime = getArguments().getString(ARG_COOK_TIME);
            this.servingSize = getArguments().getString(ARG_SERVING_SIZE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView recipeTitleTxt = (TextView) view.findViewById(R.id.recipeDetailTitle);
        recipeTitleTxt.setText(this.title);

        // Breaks here because there are several of these.
        TextView recipeServingSizeTxt = (TextView) view.findViewById(R.id.recipeDetailServingSize);
        recipeServingSizeTxt.setText(this.servingSize);

        TextView recipePrepTimeTxt = (TextView) view.findViewById(R.id.recipeDetailPrepTime);
        recipePrepTimeTxt.setText(this.prepTime);

        TextView recipeCookTimeTxt = (TextView) view.findViewById(R.id.recipeDetailCookTime);
        recipeCookTimeTxt.setText(this.cookTime);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
