import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkStream {

    private String file_name = "network.xml";
    private Perceptron perceptron;
    private XMLStreamWriter writer;
    private XMLStreamReader reader;

    public NetworkStream(Perceptron perceptron) {
        this.perceptron = perceptron;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }

    void saveNetwork() throws XMLStreamException, IOException {
        FileOutputStream fos = new FileOutputStream(file_name);
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

    void loadNetwork() throws XMLStreamException, IOException {
        FileInputStream fis = new FileInputStream(file_name);
        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
        reader = xmlInFact.createXMLStreamReader(fis);
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.equals("parameter")) {
                        __readParameter();
                        break;
                    }
            }
        }
    }

    private void __readParameter() throws XMLStreamException, IOException {
        String name = reader.getAttributeValue(0);
        String value = reader.getAttributeValue(1);
        Configurator cfg = perceptron.getCfg();

        switch (name) {
            case "inputCount":
                cfg.setInputCount(Integer.parseInt(value));
                break;
            case "learningFactor":
                cfg.setLearningFactor(Double.parseDouble(value));
                break;
            case "momentum":
                cfg.setMomentum(Double.parseDouble(value));
                break;
            case "bias":
                cfg.setBias(Boolean.parseBoolean(value));
                break;
            case "inputRotation":
                cfg.setInputRotation(Boolean.parseBoolean(value));
                break;
            case "error":
                cfg.setError(Double.parseDouble(value));
                break;
        }
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
