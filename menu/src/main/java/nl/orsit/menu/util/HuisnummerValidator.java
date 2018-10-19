package nl.orsit.menu.util;

import android.support.design.widget.TextInputLayout;

import java.util.regex.Pattern;

public class HuisnummerValidator extends BaseValidator {

    public HuisnummerValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = "Huisnummer mag niet leeg zijn.";
        mEmptyMessage = "Huisnummer mag niet leeg zijn.";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }
}