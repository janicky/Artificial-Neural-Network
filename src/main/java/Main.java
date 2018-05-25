import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private JPanel panelMain;
    private JTextField globalError;
    private JCheckBox bias;
    private JCheckBox inputRotation;
    private JTextField learningFactor;
    private JRadioButton modeLearning;
    private JRadioButton modeTesting;
    private JButton buttonLoad;
    private JButton start;
    private JTextArea output;
    private JTextField momentum;
    private JTextField error;
    private JTextField epochs;
    private JRadioButton conditionError;
    private JRadioButton conditionEpochs;
    private JButton buttonSave;
    private JButton stop;
    private JButton loadPatterns;
    private JTextField currentEpoch;

    private int mode = 0; // 0 - learn, 1 - test

    Configurator cfg = new Configurator(4, new int[]{ 2, 4 });
    NetworkManager nm = new NetworkManager(cfg);
    Perceptron perceptron;
    ExecutorService service = Executors.newFixedThreadPool(4);

    public Main() {
        DefaultCaret caret = (DefaultCaret) output.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        perceptron = nm.getPerceptron();
        updateFields();

        learningFactor.addActionListener(e -> {
            try {
                cfg.setLearningFactor(Double.parseDouble(learningFactor.getText()));
                log("Współczynnik nauki ustawiony na " + learningFactor.getText());
            } catch (Exception ex) {
                learningFactor.setText(Double.toString(cfg.getLearningFactor()));
                invalidValue();
            }
        });

        momentum.addActionListener(e -> {
            try {
                cfg.setMomentum(Double.parseDouble(momentum.getText()));
                log("Momentum ustawione na " + momentum.getText());
            } catch (Exception ex) {
                momentum.setText(Double.toString(cfg.getMomentum()));
                invalidValue();
            }
        });

        bias.addActionListener(e -> {
            cfg.setBias(bias.isSelected());
            log("Ustawienia biasu zostały zmienione");
        });

        inputRotation.addActionListener(e -> {
            cfg.setInputRotation(inputRotation.isSelected());
            log("Ustawienia losowej prezentacji wzorców zostały zmienione");
        });

        error.addActionListener(e -> {
            try {
                cfg.setError(Double.parseDouble(error.getText()));
                log("Oczekiwany błąd ustawiony na " + error.getText());
            } catch (Exception ex) {
                error.setText(Double.toString(cfg.getError()));
                invalidValue();
            }
        });

        epochs.addActionListener(e -> {
            try {
                cfg.setEpochs(Integer.parseInt(epochs.getText()));
                log("Oczekiwany błąd ustawiony na " + epochs.getText());
            } catch (Exception ex) {
                epochs.setText(Integer.toString(cfg.getEpochs()));
                invalidValue();
            }
        });

        conditionError.addActionListener(e -> updateNetworkCondition());
        conditionEpochs.addActionListener(e -> updateNetworkCondition());
        modeLearning.addActionListener(e -> updateMode());
        modeTesting.addActionListener(e -> updateMode());

        buttonSave.addActionListener(e -> {
            nm.saveNetwork();
            log("Sieć została zapisana do pliku " + nm.getFileName());
        });

        buttonLoad.addActionListener(e -> {
            nm.loadNetwork();
            updateFields();
            log("Sieć została wczytana z pliku " + nm.getFileName());
        });

        loadPatterns.addActionListener(e -> {
            loadPatterns();
        });

        start.addActionListener(e -> {
            loadPatterns();
            if (!nm.patternsLoaded()) {
                log("Wzorce nie zostały wczytane");
            }
            if (mode == 0) {
                log("Nauka została rozpoczęta");
                cfg.setStop(false);
                service.shutdownNow();
                service = Executors.newFixedThreadPool(4);
                perceptron.initialize();
                service.submit(() -> {
                    nm.learn(globalError, currentEpoch, output);
                });
            } else {
                nm.test(output);
                log("Testowanie zostało rozpoczęte");
            }
        });
        stop.addActionListener(e -> {
            cfg.setStop(true);
            service.shutdownNow();

            log("Zatrzymano proces");
        });
    }

    private void loadPatterns() {
        try {
            nm.loadPatterns(cfg.getPatternsPath());
            log("Wzorce zostały wczytane z pliku " + cfg.getPatternsPath());
        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }

    private void updateNetworkCondition() {
        if (conditionError.isSelected()) {
            cfg.setCondition(NetworkManager.ConditionMode.ERROR);
        } else if (conditionEpochs.isSelected()) {
            cfg.setCondition(NetworkManager.ConditionMode.EPOCHS);
        }
        log("Warunek zakończenia nauki został zmieniony");
    }

    private void updateMode() {
        mode = (modeLearning.isSelected() ? 0 : 1);
        log("Tryb został zmieniony");
    }

    private void invalidValue() {
        log("Wprowadzona wartość jest niepoprawna");
    }

    private void log(String msg) {
        output.append(" " + msg + "\n");
    }

    private void updateFields() {
//        Set fields
        learningFactor.setText(Double.toString(cfg.getLearningFactor()));
        momentum.setText(Double.toString(cfg.getMomentum()));
        bias.setSelected(cfg.isBias());
        inputRotation.setSelected(cfg.isInputRotation());
        error.setText(Double.toString(cfg.getError()));
        epochs.setText(Integer.toString(cfg.getEpochs()));

        if (cfg.getCondition() == NetworkManager.ConditionMode.ERROR) {
            conditionError.setSelected(true);
        } else if (cfg.getCondition() == NetworkManager.ConditionMode.EPOCHS) {
            conditionEpochs.setSelected(true);
        }

        if (mode == 0) {
            modeLearning.setSelected(true);
        } else {
            modeTesting.setSelected(true);
        }
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
