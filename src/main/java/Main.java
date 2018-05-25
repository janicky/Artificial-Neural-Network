import javax.swing.*;

public class Main {

    private JPanel panelMain;
    private JTextField globalError;
    private JCheckBox bias;
    private JCheckBox inputRotation;
    private JTextField learningFactor;
    private JRadioButton modeLearning;
    private JRadioButton modeTesting;
    private JButton wczytajSiećButton;
    private JButton rozpocznijButton;
    private JTextArea textArea1;
    private JTextField momentum;
    private JTextField error;
    private JTextField epochs;
    private JRadioButton conditionError;
    private JRadioButton conditionEpochs;

    public Main() {

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Main().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setTitle("Multi-Layer Neural Network");
        frame.setVisible(true);
    }
}
