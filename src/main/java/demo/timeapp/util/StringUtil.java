package demo.timeapp.util;

import demo.timeapp.dto.FormUser;
import demo.timeapp.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by dhval on 4/26/16.
 */
public class StringUtil {

    public final static String toJSON(Object object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            return  jpe.getMessage();
        }
    }

    public final static String prettyName(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getFname()).append(" ");
        if (StringUtils.isNotBlank(user.getMname())) {
            sb.append(user.getMname());
        }
        sb.append(" ").append(user.getLname());
        return sb.toString();
    }

    public static String prettyName(FormUser form) {
        StringBuilder sb = new StringBuilder();
        sb.append(form.getFname()).append(" ");
        if (StringUtils.isNotBlank(form.getMname()))
            sb.append(form.getMname());
        return sb.append(" ").append(form.getLname()).toString();
    }

}
