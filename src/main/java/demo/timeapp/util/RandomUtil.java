package demo.timeapp.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dhval on 3/21/16.
 */
public class RandomUtil {
    private static final Pattern CONSECUTIVE_NUM = Pattern.compile("(\\d)\\1");
    private static final int RANDON_MAX = 1000000;
    private static final int RANDON_MIN = 100000;

    private static boolean isValid(int number) {
        String num = String.valueOf(number);
        Matcher matcher = CONSECUTIVE_NUM.matcher(num);
        return !matcher.find();
    }

    public static String generateToken() {
        int num = ThreadLocalRandom.current().nextInt(RANDON_MIN, RANDON_MAX);
        while (!isValid(num)) {
            num = ThreadLocalRandom.current().nextInt(RANDON_MIN, RANDON_MAX);
        }
        return Integer.toString(num);
    }

}
