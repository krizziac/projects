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
import com.example.foodtracker.model.mealPlan.MealPlanDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class createMealPlanDialog extends DialogFragment {


    /**
     * Interface that passes the range of days selected by user, in order to instantiate new
     * {@link MealPlanDay} objects
     */
    public interface setMPDatesListener{
        void addMP(ArrayList<String> day);
    }

    private setMPDatesListener mpDatesListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mpDatesListener= (setMPDatesListener) context;
    }

    /**
     * Retrieves user input and checks if input is valid
     *
     * @see <a href="https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked">
     *     Preventing dialog exit when value is invalid </a>
     *  Copyright: CC BY-SA 3.0 (Original: Tom Bollwitt, Edit by: Daniel Nugent)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.create_meal_plan_dialog, null);
        DatePicker startDayPicker = view.findViewById(R.id.mealPlanStartDate);
        DatePicker endDayPicker = view.findViewById(R.id.mealPlanEndDate);


        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Create meal plan")
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
                        Calendar mpStart = Calendar.getInstance();
                        mpStart.set(startDayPicker.getYear(), startDayPicker.getMonth(), startDayPicker.getDayOfMonth());

                        Calendar mpEnd = Calendar.getInstance();
                        mpEnd.set(endDayPicker.getYear(), endDayPicker.getMonth(), endDayPicker.getDayOfMonth());

                        if (mpStart.after(mpEnd)){
                            String message = "Start day cannot be after end day";
                            Toast.makeText(getContext(),message, Toast.LENGTH_LONG).show();
                        }
                        else {
                            setDates(mpStart,mpEnd);
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * This function converts the days set by the user into {@link  String} types and adds the dates into an {@link ArrayList} of strings
     * to pass to {@link MealPlanMainScreen}.
     * @see <a href="https://stackoverflow.com/questions/23966950/how-to-get-a-list-of-specific-dates-between-two-datesstart-and-end-in-android">
     *  Retrieve list of dates between a given range </a>
     * Copyright:  CC BY-SA 3.0 (Dave Pile)
     *
     * @param startDay
     * @param endDay
     */
    private void setDates(Calendar startDay, Calendar endDay) {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        ArrayList<String> listDates = new ArrayList<>();

        while(startDay.compareTo(endDay) < 1){
            Date convertDate =  startDay.getTime();
            String strDate = dateFormat.format(convertDate);
            startDay.add(Calendar.DAY_OF_MONTH, 1);
            listDates.add(strDate);
        }
        mpDatesListener.addMP(listDates);
    }


}
