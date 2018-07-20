package cheemala.reqgeofence.com.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CheemalaCh on 7/20/2018.
 */

public class ShowMsg {

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
