package demo.timeapp.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dhval on 2/2/16.
 */
@XmlRootElement
public class ListItem {

    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ListItem() {}

    public ListItem(String label, String value) {
        this.label = label;
        this.value = value;
    }
}