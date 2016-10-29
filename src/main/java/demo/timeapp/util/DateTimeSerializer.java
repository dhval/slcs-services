package demo.timeapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dhval on 9/22/16.
 */
public class DateTimeSerializer extends JsonSerializer<Date> {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeSerializer.class);

    private final static String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm a";

    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {

        SimpleDateFormat formatter = new SimpleDateFormat(
                DATE_TIME_FORMAT);
        String formattedDate = formatter.format(value);

        gen.writeString(formattedDate);

    }

}