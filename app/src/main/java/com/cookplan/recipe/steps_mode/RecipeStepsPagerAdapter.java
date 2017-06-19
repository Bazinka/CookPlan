package com.cookplan.recipe.steps_mode;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookplan.R;

import java.util.List;

/**
 * Created by DariaEfimova on 13.04.17.
 */

public class RecipeStepsPagerAdapter extends PagerAdapter {

    private List<String> steps;
    private Context mContext;

    public RecipeStepsPagerAdapter(List<String> steps, Context context) {
        this.steps = steps;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        String step = steps.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recipe_step_pager_item_layout, collection, false);

        TextView stepDescTextView = (TextView) layout.findViewById(R.id.step_textview);
        stepDescTextView.setText(step);

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}