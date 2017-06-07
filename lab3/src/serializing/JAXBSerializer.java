package serializing;

import model.Client;
import model.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by Alexander on 03/06/2017.
 */
public class JAXBSerializer {
    private static final Logger log = LogManager.getLogger(Client.class);

    public String messageToXMLString(Message message) {
        try {
            JAXBContext context = JAXBContext.newInstance(message.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(message, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            log.error("JAXB serialising error", e);
        }
        return null;
    }
}
