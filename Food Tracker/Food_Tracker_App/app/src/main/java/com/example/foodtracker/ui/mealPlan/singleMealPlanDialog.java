package com.example.foodtracker.ui.mealPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodtracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 *  This dialog allows a user to add one day to their meal plan.
 */

public class singleMealPlanDialog extends DialogFragment {

    public interface setSingleMPDatesListener{
        /**
         * Adds a day to the meal plan, based on selected date in dialog
         * @param day
         */
        void addSingle(String day);

        /**
         * Checks if a given day is in the current meal plan.
         * @param day
         * @return
         */
        boolean isInList(String day);
    }

    setSingleMPDatesListener singleMPDatesListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        singleMPDatesListener = (setSingleMPDatesListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.single_meal_plan_dialog,null);
        DatePicker singleDate = view.findViewById(R.id.mealPlanSingleDate);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add meal plan day")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Calendar singleMPDay= Calendar.getInstance();
                        singleMPDay.set(singleDate.getYear(), singleDate.getMonth(), singleDate.getDayOfMonth());
                        String entryDay = String.format(Locale.CANADA, "%02d-%02d-%d",
                                singleDate.getMonth(),singleDate.getDayOfMonth(), singleDate.getYear());

                        if (singleMPDatesListener.isInList(entryDay) == false){
                            setDay(singleMPDay);
                            dialog.dismiss();
                        }
                        else {
                            String message = "This day is already in the meal plan.";
                            Toast.makeText(getContext(),message, Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
        dialog.show();
        return dialog;

    }


    /**
     * This function converts the date chosen by the user to a {@link String} type
     * and passes the string to {@link MealPlanMainScreen}
     * @param singleDay
     */
    private void setDay(Calendar singleDay) {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date convertDate =  singleDay.getTime();
        String strDate = dateFormat.format(convertDate);

        singleMPDatesListener.addSingle(strDate);
    }



}


