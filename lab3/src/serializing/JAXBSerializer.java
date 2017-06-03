package serializing;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Alexander on 03/06/2017.
 */
public class JAXBSerializer {

        public Object getObject(File file, Class c) throws JAXBException {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object object = unmarshaller.unmarshal(file);
            return object;
        }

        public void saveObject(File file, Object o) throws JAXBException {
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            System.out.println("marshalling object");
            marshaller.marshal(o, file);
        }

}
