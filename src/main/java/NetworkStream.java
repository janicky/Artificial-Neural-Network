import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkStream {

    private String file_name = "network.xml";
    private Perceptron perceptron;
    private FileOutputStream fos;
    private XMLStreamWriter writer;

    public NetworkStream(Perceptron perceptron) {
        this.perceptron = perceptron;
    }

    void saveNetwork() throws XMLStreamException, IOException {
        fos = new FileOutputStream(file_name);
        XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
        writer = xmlOutFact.createXMLStreamWriter(fos);
        writer.writeStartDocument();
        writer.writeStartElement("perceptron");
        __saveParemeters();
        __saveWeights();
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();
    }

    private void __saveParemeters() throws XMLStreamException, IOException {
        writer.writeStartElement("parameters");
        Configurator cfg = perceptron.getCfg();
        __saveParemeter("inputCount", Integer.toString(cfg.getInputCount()));
        __saveParemeter("learningFactor", Double.toString(cfg.getLearningFactor()));
        __saveParemeter("momentum", Double.toString(cfg.getMomentum()));
        __saveParemeter("bias", Boolean.toString(cfg.isBias()));
        __saveParemeter("inputRotation", Boolean.toString(cfg.isInputRotation()));
        __saveParemeter("error", Double.toString(cfg.getError()));
        writer.writeEndElement();
    }

    private void __saveParemeter(String name, String value) throws XMLStreamException, IOException {
        writer.writeStartElement("parameter");
        writer.writeAttribute("name", name);
        writer.writeAttribute("value", value);
        writer.writeEndElement();
    }

    private void __saveWeights() throws XMLStreamException {
        writer.writeStartElement("weights");
        int l = 0;
        for (double[][] layer : perceptron.getWeights()) {
            writer.writeStartElement("layer");
            writer.writeAttribute("id", Integer.toString(l++));
            int n = 0;
            for (double[] neurone : layer) {
                writer.writeStartElement("neurone");
                writer.writeAttribute("id", Integer.toString(n++));
                int w = 0;
                    for (double weight : neurone) {
                        writer.writeStartElement("weight");
                        writer.writeAttribute("id", Integer.toString(w++));
                        writer.writeCharacters(Double.toString(weight));
                        writer.writeEndElement();
                    }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}
