package model.message;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by Alexander on 03/06/2017.
 */
public enum Command {
    @XmlEnumValue(value = "message")
    TEXT_MESSAGE
}
