package net.myacxy.agsm.utils;

import android.util.Patterns;

import com.rengwuxian.materialedittext.validation.METValidator;

public class IpAddressAndDomainValidator extends METValidator
{
    public IpAddressAndDomainValidator(String errorMessage)
    {
        super(errorMessage);
    }

    @Override
    public boolean isValid(CharSequence text, boolean isEmpty)
    {
        if(!Patterns.IP_ADDRESS.matcher(text.toString().trim()).matches())
        {
            if(!Patterns.DOMAIN_NAME.matcher(text.toString().trim()).matches())
            {
                return false;
            }
        }
        return true;
    } // isValid
} // IpAddressAndDomainValidator
