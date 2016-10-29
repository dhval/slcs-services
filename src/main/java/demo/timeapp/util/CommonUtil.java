package demo.timeapp.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Created by dhval on 9/21/16.
 */
public class CommonUtil {

    public static<T> void isNull(Optional<T> optional) {
        optional.isPresent();
    }

    public static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
           return "NA";
        }
    }

}
