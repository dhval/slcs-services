package demo.timeapp.util;

import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dhval on 7/21/15.
 */
public class DOBDeSerializer extends JsonDeserializer<Date>
{
    private static final Logger LOG = LoggerFactory.getLogger(DOBDeSerializer.class);
    @Override
    public Date deserialize(JsonParser jsonparser,
                            DeserializationContext deserializationcontext) throws IOException {
        if (StringUtils.isEmpty(jsonparser.getText()) || jsonparser.getText().length() != 10)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = jsonparser.getText();
        try {
            LOG.info(date);
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}