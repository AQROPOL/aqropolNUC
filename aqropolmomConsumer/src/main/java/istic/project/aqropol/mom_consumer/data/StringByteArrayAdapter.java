package istic.project.aqropol.mom_consumer.data;

import com.google.gson.*;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Type;

/**
 * @author VinYarD
 * created : 04/04/2018, 02:19
 */


public class StringByteArrayAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {


    // Using Android's base64 libraries. This can be replaced with any base64 library.
    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return DatatypeConverter.parseBase64Binary((json.getAsString()));
    }

    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DatatypeConverter.printBase64Binary(src));
    }
}