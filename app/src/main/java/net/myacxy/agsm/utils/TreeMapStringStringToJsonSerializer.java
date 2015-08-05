package net.myacxy.agsm.utils;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.TreeMap;

public class TreeMapStringStringToJsonSerializer extends TypeSerializer
{
    private Gson gson;

    public TreeMapStringStringToJsonSerializer()
    {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public Class<?> getDeserializedType() {
        return new TypeToken<TreeMap<String, String>>() {}.getRawType();
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data)
    {
        if(data == null)
        {
            return null;
        }

        return gson.toJson(data);
    }

    @Override
    public TreeMap<String, String> deserialize(Object data)
    {
        if(data == null)
        {
            return null;
        }

        String json = data.toString();
        Type type = new TypeToken<TreeMap<String, String>>() {}.getType();

        return gson.fromJson(json, type);
    }
} // TreeMapStringStringToJsonSerializer
