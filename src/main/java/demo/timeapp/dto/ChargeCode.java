package demo.timeapp.dto;

import javax.persistence.*;

/**
 * Created by dhval on 6/5/16.
 */

public enum ChargeCode {
        REGULAR(0), OVERTIME(1), PAID_TIMEOFF(2), UNPAID_TIMEOFF(3);
        int value;
        ChargeCode(int v) {
            value = v;
        }

    public int getValue() {
        return value;
    }
}
