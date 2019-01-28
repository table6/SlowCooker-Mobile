package com.table6.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.table6.activity.R;
import com.table6.fragment.RecipeListFragment.OnListFragmentInteractionListener;
import com.table6.object.RecipeContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecipeContent.Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyRecipeListRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeListRecyclerViewAdapter.ViewHolder> {

    private final List<RecipeContent.Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRecipeListRecyclerViewAdapter(List<RecipeContent.Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.titleTextView.setText(mValues.get(position).title);
        holder.prepTimeTextView.setText(mValues.get(position).prepTime);
        holder.cookTimeTextView.setText(mValues.get(position).cookTime);
        holder.servingSizeTextView.setText(mValues.get(position).servingSize);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTextView;
        public final TextView prepTimeTextView;
        public final TextView cookTimeTextView;
        public final TextView servingSizeTextView;
        public RecipeContent.Recipe mItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.titleTextView = (TextView) view.findViewById(R.id.recipeTitle);
            this.prepTimeTextView = (TextView) view.findViewById(R.id.recipePrepTime);
            this.cookTimeTextView = (TextView) view.findViewById(R.id.recipeCookTime);
            this.servingSizeTextView = (TextView) view.findViewById(R.id.recipeServingSize);
        }
    }
}
