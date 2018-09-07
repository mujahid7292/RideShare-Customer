package com.sand_corporation.www.uthao.DatePicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.sand_corporation.www.uthao.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetDate implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;

    public SetDate(EditText editText, Context ctx){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.ctx = ctx;
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        // this.editText.setText();

        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            //Below this one will create calender type date picker
//            DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, this, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                datePickerDialog.getDatePicker().setLayoutMode(1);
//            }
//            datePickerDialog.show();

            //Below this one will create spinner type date picker
            DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                    R.style.PickerDialogCustom, this,myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            //As API <21 does not support spinner mode, so we have to add this below line,
            //this below command is ignored for API >=21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                datePickerDialog.getDatePicker().setLayoutMode(1);
            }
            datePickerDialog.show();


        }
    }

}

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            new DatePickerDialog(ctx, this, myCalendar
//            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//        }
//        return false;
//    }


