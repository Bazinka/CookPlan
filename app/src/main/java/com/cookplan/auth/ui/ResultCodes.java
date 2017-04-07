package  com.cookplan.auth.ui;

import com.cookplan.auth.ErrorCodes;
import  com.cookplan.auth.AuthUI;

/**
 * Result codes returned when using {@link AuthUI.SignInIntentBuilder#build()} with
 * {@code startActivityForResult}.
 */
@Deprecated
public class ResultCodes {

    /**
     * Sign in failed due to lack of network connection
     *
     * @deprecated Please use {@link ErrorCodes#NO_NETWORK}
     **/
    @Deprecated
    public static final int RESULT_NO_NETWORK = ErrorCodes.NO_NETWORK;

}
