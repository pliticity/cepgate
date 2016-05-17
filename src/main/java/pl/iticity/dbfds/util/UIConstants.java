package pl.iticity.dbfds.util;

import com.google.common.base.Joiner;

/**
 * Created by pmajchrz on 3/29/16.
 */
public class UIConstants {

    public static final String PERCENT_100 = "100%";

    public static final String TOP_BAR_BUTTON = "top-bar-button";
    public static final String WINDOW_FORM_BUTTON ="window-form-button";

    public static String getStyles(String... classes){
        return Joiner.on(" ").join(classes);
    }
}
