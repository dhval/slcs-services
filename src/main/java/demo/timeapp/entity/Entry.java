package demo.timeapp.entity;

import demo.timeapp.dto.ChargeCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dhval on 2/1/16.
 */
@Entity
@Table
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entry {

    public Entry() {}

    public Entry(ChargeCode chargeCode) {
        code = chargeCode;
        sequence = chargeCode.getValue();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private int sequence;

    @Enumerated(EnumType.STRING)
    private ChargeCode code;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float mon = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float tue = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float wed = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float thu = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float fri = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float sat = 0.00f;

    @Column(columnDefinition="Decimal(4,2) default '0.00'")
    float sun = 0.00f;

    @SuppressWarnings("unused")
    @PrePersist
    private void onInsert() {
        calculate();
    }

    @SuppressWarnings("unused")
    @PreUpdate
    private void onUpdate() {
       calculate();
    }

    public void calculate() {
        List<Float> list = Arrays.asList(mon, tue, wed, thu, fri, sat, sun);
        for (Float item : list) {
            if (item <0 || item >24) item = 0f;
            item = Math.round(item*4)/4f;
        }
    }

    public Long getId() {
        return id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public ChargeCode getCode() {
        return code;
    }

    public void setCode(ChargeCode code) {
        this.code = code;
    }

    public float getMon() {
        return mon;
    }

    public void setMon(float mon) {
        this.mon = mon;
    }

    public float getTue() {
        return tue;
    }

    public void setTue(float tue) {
        this.tue = tue;
    }

    public float getWed() {
        return wed;
    }

    public void setWed(float wed) {
        this.wed = wed;
    }

    public float getThu() {
        return thu;
    }

    public void setThu(float thu) {
        this.thu = thu;
    }

    public float getFri() {
        return fri;
    }

    public void setFri(float fri) {
        this.fri = fri;
    }

    public float getSat() {
        return sat;
    }

    public void setSat(float sat) {
        this.sat = sat;
    }

    public float getSun() {
        return sun;
    }

    public void setSun(float sun) {
        this.sun = sun;
    }

    public float getTotal() {
        return getMon() + getTue() + getWed() + getThu() + getFri() + getSat() + getSun();
    }
}
