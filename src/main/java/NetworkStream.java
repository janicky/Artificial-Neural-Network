import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkStream {

    private Perceptron perceptron;

    public NetworkStream(Perceptron perceptron) {
        this.perceptron = perceptron;
    }

    void saveNetwork() throws XMLStreamException, IOException {
        try (FileOutputStream fos = new FileOutputStream("test.xml")){
            XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = xmlOutFact.createXMLStreamWriter(fos);
            writer.writeStartDocument();
            writer.writeStartElement("test");
            // write stuff
            writer.writeEndElement();
        }
    }
}
