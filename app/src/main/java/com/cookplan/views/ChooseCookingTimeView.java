package com.cookplan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;

import com.cookplan.R;
import com.cookplan.utils.Constants;

import org.joda.time.DateTime;

import static com.cookplan.utils.Constants.TypeOfTime.BREAKFAST;
import static com.cookplan.utils.Constants.TypeOfTime.DINNER;
import static com.cookplan.utils.Constants.TypeOfTime.LUNCH;
import static com.cookplan.utils.Constants.TypeOfTime.SNACK;

/**
 * Created by DariaEfimova on 10.05.17.
 */

public class ChooseCookingTimeView extends RelativeLayout {

    private Constants.TypeOfTime selectedTime;
    private DateTime selectedDate;


    public ChooseCookingTimeView(Context context) {
        super(context);
        init();
    }

    public ChooseCookingTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChooseCookingTimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.choose_cooking_time_view_layout, this);

        ViewGroup breakfastLayout = (ViewGroup) findViewById(R.id.breakfast_layout);
        breakfastLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedTime = BREAKFAST;
            }
        });

        ViewGroup snackLayout = (ViewGroup) findViewById(R.id.snack_layout);
        snackLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedTime = SNACK;
            }
        });

        ViewGroup lunchLayout = (ViewGroup) findViewById(R.id.lunch_layout);
        lunchLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedTime = LUNCH;
            }
        });

        ViewGroup dinnerLayout = (ViewGroup) findViewById(R.id.dinner_layout);
        dinnerLayout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectedTime = DINNER;
            }
        });

        CalendarView calendarView = (CalendarView) findViewById(R.id.cooking_time_calendar_view);

        selectedDate = new DateTime();
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            DateTime dt = new DateTime();
            selectedDate = dt.withDate(year, month + 1, dayOfMonth);
        });
    }

    public Constants.TypeOfTime getSelectedTime() {
        return selectedTime;
    }

    public DateTime getSelectedDate() {
        return selectedDate;
    }
}
