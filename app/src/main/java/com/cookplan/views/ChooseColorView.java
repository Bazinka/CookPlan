package com.cookplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cookplan.R;
import com.cookplan.models.ToDoCategoryColor;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class ChooseColorView extends RelativeLayout {

    private ToDoCategoryColor selectedColor;

    public ChooseColorView(Context context) {
        super(context);
        init();
    }

    public ChooseColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChooseColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.choose_color_view_layout, this);

        ImageView area1ImageView = (ImageView) findViewById(R.id.image_view_area1);
        area1ImageView.setBackgroundResource(ToDoCategoryColor.PURPLE.getColorId());
        View area1View = findViewById(R.id.main_view_area1);
        area1View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.PURPLE;
            }
        });

        ImageView area2ImageView = (ImageView) findViewById(R.id.image_view_area2);
        area2ImageView.setBackgroundResource(ToDoCategoryColor.DARK_BLUE.getColorId());
        View area2View = findViewById(R.id.main_view_area2);
        area2View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.DARK_BLUE;
            }
        });

        ImageView area3ImageView = (ImageView) findViewById(R.id.image_view_area3);
        area3ImageView.setBackgroundResource(ToDoCategoryColor.BLUE.getColorId());
        View area3View = findViewById(R.id.main_view_area3);
        area3View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.BLUE;
            }
        });

        ImageView area4ImageView = (ImageView) findViewById(R.id.image_view_area4);
        area4ImageView.setBackgroundResource(ToDoCategoryColor.CYAN.getColorId());
        View area4View = findViewById(R.id.main_view_area4);
        area4View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.CYAN;
            }
        });

        ImageView area5ImageView = (ImageView) findViewById(R.id.image_view_area5);
        area5ImageView.setBackgroundResource(ToDoCategoryColor.TEAL.getColorId());
        View area5View = findViewById(R.id.main_view_area5);
        area5View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.TEAL;
            }
        });

        ImageView area6ImageView = (ImageView) findViewById(R.id.image_view_area6);
        area6ImageView.setBackgroundResource(ToDoCategoryColor.GREEN.getColorId());
        View area6View = findViewById(R.id.main_view_area6);
        area6View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.GREEN;
            }
        });

        ImageView area7ImageView = (ImageView) findViewById(R.id.image_view_area7);
        area7ImageView.setBackgroundResource(ToDoCategoryColor.YELLOW.getColorId());
        View area7View = findViewById(R.id.main_view_area7);
        area7View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.YELLOW;
            }
        });

        ImageView area8ImageView = (ImageView) findViewById(R.id.image_view_area8);
        area8ImageView.setBackgroundResource(ToDoCategoryColor.ORANGE.getColorId());
        View area8View = findViewById(R.id.main_view_area8);
        area8View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.ORANGE;
            }
        });

        ImageView area9ImageView = (ImageView) findViewById(R.id.image_view_area9);
        area9ImageView.setBackgroundResource(ToDoCategoryColor.RED.getColorId());
        View area9View = findViewById(R.id.main_view_area9);
        area9View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.RED;
            }
        });

        ImageView area10ImageView = (ImageView) findViewById(R.id.image_view_area10);
        area10ImageView.setBackgroundResource(ToDoCategoryColor.BROWN.getColorId());
        View area10View = findViewById(R.id.main_view_area10);
        area10View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.BROWN;
            }
        });

        ImageView area11ImageView = (ImageView) findViewById(R.id.image_view_area11);
        area11ImageView.setBackgroundResource(ToDoCategoryColor.GREY.getColorId());
        View area11View = findViewById(R.id.main_view_area11);
        area11View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.GREY;
            }
        });

        ImageView area12ImageView = (ImageView) findViewById(R.id.image_view_area12);
        area12ImageView.setBackgroundResource(ToDoCategoryColor.BLACK.getColorId());
        View area12View = findViewById(R.id.main_view_area12);
        area12View.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedColor = ToDoCategoryColor.BLACK;
            }
        });
    }

    public ToDoCategoryColor getSelectedColor() {
        return selectedColor;
    }
}
