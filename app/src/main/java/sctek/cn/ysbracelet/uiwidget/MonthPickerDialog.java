package sctek.cn.ysbracelet.uiwidget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * Created by kang on 16-3-11.
 */
public class MonthPickerDialog extends DatePickerDialog{
    public MonthPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        ((ViewGroup)((ViewGroup)(this.getDatePicker().getChildAt(0))).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }

    public MonthPickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
    }
}
