package demo.timeapp.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DOBSerializer extends JsonSerializer<Date> {

    private static final Logger LOG = LoggerFactory.getLogger(DOBSerializer.class);

    private final static String DATE_DOB_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        SimpleDateFormat formatter = new SimpleDateFormat(
                DATE_DOB_FORMAT);
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }

}
