package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Alexander on 07/06/2017.
 */
@XmlRootElement(name="user")
public class User implements Serializable {
    private String name;
    private String type;

    public User() {}

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.hashCode() == obj.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
