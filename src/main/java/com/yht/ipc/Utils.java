package com.yht.ipc;

import android.databinding.BindingConversion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yangHaiTao on 2018/9/18.
 * E-mail: yanghaitao@ren001.com
 */
public class Utils {
    @BindingConversion
    public static String convertDate(Date date) {
        return new SimpleDateFormat("MM-dd", Locale.getDefault()).format(date);
    }
}
