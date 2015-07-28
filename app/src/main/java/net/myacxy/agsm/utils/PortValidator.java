package net.myacxy.agsm.utils;

import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class PortValidator extends RegexpValidator
{
    private static final String PORT_PATTERN = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    public PortValidator(String errorMessage)
    {
        super(errorMessage, PORT_PATTERN);
    }
}
