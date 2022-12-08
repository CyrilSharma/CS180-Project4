import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FilterPanel implements Runnable {
    private JFrame board;
    private Container content;
    private JButton addButton;
    private JButton removeButton;
    private JButton enableButton;
    private JTextField txtField;
    private JButton backButton;
    private JLabel label;
    private JPanel pan;
    private JPanel mainPan;
    private JPanel rightPan;
    private Translator tr;
    private boolean enabled;
    private ArrayList<String> words;

    public FilterPanel(JFrame frame) {
        frame.setSize(600,400);
        board = frame;
        this.words = new ArrayList<>();
        tr = new Translator();
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public void show() {
        board.setContentPane(new Container());
        run();
        board.revalidate();
        board.repaint();
    }

    //not tested method
    public boolean checkSpecialChar(String word) {
        boolean result = false;
        for (int i = 0; i < word.length(); i++) {
            if (!(word.charAt(i) > 64 && word.charAt(i) <= 122)) {
                return true;
            }
        }
        return result;
    }
    public void createAndAdd() {
        content = board.getContentPane();
        content.setLayout(new BorderLayout());
        rightPan = new JPanel();
        mainPan = new JPanel();
        pan = new JPanel();
        pan.setSize(600,400);
        addButton = new JButton("Add Word");
        removeButton = new JButton("Remove Word");
        enableButton = new JButton("Enable Filter");
        backButton = new JButton("Back");
        txtField = new JTextField();
        label = new JLabel();
        label.setText("Filtered Words: ");
        mainPan.add(label);
        mainPan.add(txtField);
        rightPan.add(enableButton);
        rightPan.add(addButton);
        rightPan.add(removeButton);
        rightPan.add(backButton);
        pan.add(mainPan);
        pan.add(rightPan);
        content.add(pan);
    }
    public void setFrame() {
        pan.setLayout(null);
        rightPan.setLayout(null);
        mainPan.setLayout(null);
        txtField.setBounds(30,50,250,30);
        label.setBounds(30,80,250,100);
        rightPan.setBounds(400, 0, 200, 400);
        mainPan.setBackground(Color.white);
        mainPan.setBounds(0,0,400,400);
        enableButton.setBounds(25, 30, 150, 30);
        addButton.setBounds(25,75,150,30);
        removeButton.setBounds(25, 120, 150, 30);
        backButton.setBounds(25,330,150,30);
        label.setBackground(Color.green);
    }
    public void updateLabel() {
        String str = "Filtered Words: ";
        for (int i = 0; i < words.size(); i++) {
            str += words.get(i);
            if (i != words.size() - 1) {
                str += ", ";
            }
        }
        if (words.size() == 0) {
            str += "No word";
        }
        label.setText(str);
        label.updateUI();
    }
    public void addActionListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Check if special character exists
                String word = txtField.getText();
                if (checkSpecialChar(word)) {
                    words.add(word);
                    updateLabel();
                } else {
                    JOptionPane.showMessageDialog(null, "Enter a word without special character!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String word = txtField.getText();
                    if (checkSpecialChar(word)) {
                        words.remove(word);
                        updateLabel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a word without special character!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception er) {
                    JOptionPane.showMessageDialog(null, "Enter a valid word!",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
                updateLabel();
            }
        });
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enabled = !enabled;
                if (enabled) {
                    enableButton.setText("Disable Filter");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: FIX THIS CONSTRUCTOR
                AccountManagerGUI gui = new AccountManagerGUI(board, null);
            }
        });

    }

    public void run() {
        createAndAdd();
        setFrame();
        addActionListeners();
    }
}