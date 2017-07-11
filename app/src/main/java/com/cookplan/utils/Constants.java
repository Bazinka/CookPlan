package com.cookplan.utils;

import com.cookplan.R;
import com.cookplan.RApplication;

/**
 * Created by DariaEfimova on 11.05.17.
 */

public class Constants {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public static final float GEOFENCE_DEFAULT_RADIUS_IN_METERS = 50;
    public static final long GEOFENCE_DEFAULT_PERIOD_MILLISECONDS = 10 * 24 * 60 * 60 * 1000;

    public static final long DAYS_TO_MILLISECONDS = 24 * 60 * 60 * 1000;

    public static final int GOOGLE_SEARCH_RESULTS_NUMBER = 10;
    public static final long DELAY_COOKPLAN_NOTIFICATION_JOB = 4 * 1000;

    public enum ObjectType {
        RECIPE(0),
        INGREDIENT(1);

        private int id;

        ObjectType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum TypeOfTime {
        BREAKFAST(8, 0, RApplication.getAppContext().getString(R.string.breakfast_title)),
        LUNCH(12, 30, RApplication.getAppContext().getString(R.string.lunch_title)),
        SNACK(17, 0, RApplication.getAppContext().getString(R.string.snack_title)),
        DINNER(19, 0, RApplication.getAppContext().getString(R.string.dinner_title));
        private int hour;
        private int minute;
        private String name;

        TypeOfTime(int hour, int minute, String name) {
            this.hour = hour;
            this.minute = minute;
            this.name = name;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        @Override
        public String toString() {
            return name;
        }

        public static TypeOfTime getTypeOfTimeByHours(int hour) {
            if (hour == BREAKFAST.getHour()) {
                return BREAKFAST;
            }
            if (hour == LUNCH.getHour()) {
                return LUNCH;
            }
            if (hour == SNACK.getHour()) {
                return SNACK;
            }
            if (hour == DINNER.getHour()) {
                return DINNER;
            }
            return null;
        }
    }
}
