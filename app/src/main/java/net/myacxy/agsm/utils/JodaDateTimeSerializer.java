package net.myacxy.agsm.utils;

import com.activeandroid.serializer.TypeSerializer;

import org.joda.time.DateTime;

public class JodaDateTimeSerializer extends TypeSerializer
{
    @Override
    public Class<?> getDeserializedType() {
        return DateTime.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return Long.class;
    }

    @Override
    public Long serialize(Object data)
    {
        if(data == null)
        {
            return null;
        }

        return ((DateTime) data).toDate().getTime();
    }

    @Override
    public DateTime deserialize(Object data)
    {
        if(data == null)
        {
            return null;
        }

        return new DateTime((long) data);
    }
} // TreeMapStringStringToJsonSerializer
