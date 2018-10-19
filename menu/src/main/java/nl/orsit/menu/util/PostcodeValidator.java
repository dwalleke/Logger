package nl.orsit.menu.util;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import java.util.regex.Pattern;

public class PostcodeValidator extends BaseValidator {

    public static final Pattern POSTCODE = Pattern.compile("[0-9][0-9][0-9][0-9][a-zA-Z][a-zA-Z]");


    public PostcodeValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = "Postcode: 1234AB";
        mEmptyMessage = "Graag postcode invullen";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return POSTCODE.matcher(charSequence).matches();
    }
}