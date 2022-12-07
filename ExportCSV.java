import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.*;
//TODO: get some actual organization, change the JScrollPane to a checkbox
public class ExportCSV {
    
    JFrame frame;
    Translator translator;
    Container content;
    JButton export;
    JButton back;
    JScrollPane scrollPane;
    HashMap<String, String> user;
    JList<String> list;
    DefaultListModel<String> listModel;
    String csvDir;

    public ExportCSV(JFrame frame, String userEmail) throws Exception {
        this.frame = frame;
        translator = new Translator();
        user = translator.get("email", userEmail);
        scrollPane = new JScrollPane();
        list = new JList<String>();
        listModel = new DefaultListModel<>();
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        csvDir = PathManager.storeDir + "csv/";
    }

    public void createAndAdd() throws Exception {
        ArrayList<String> conversations = (ArrayList<String>) translator.query(new Query("MessageManager", "listConversations", user.get("id")));
        listModel.addAll(conversations);
        export = new JButton("Export Conversations");
        back = new JButton("Back");
        scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(30,70,370,250);
    }

    public void show() {
        frame.setContentPane(new Container());
        run();
        frame.revalidate();
        frame.repaint();
    }

    public void run() {
        try {
            createAndAdd();
            content = frame.getContentPane();
            content.setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            panel.add(scrollPane);
            panel.setSize(500,400);
            content.add(panel, BorderLayout.WEST);
            JPanel panel2 = new JPanel();
            panel2.add(export);
            panel2.add(back);
            content.add(panel2, BorderLayout.EAST);
            frame.setContentPane(content);
            export.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    List<String> selected = list.getSelectedValuesList();
                    if (selected.size() == 0) {
                        JOptionPane.showMessageDialog(null, "You didn't select anything", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int i = 1;
                    File selectedFile = new File(csvDir + user.get("email") + "-messageHistory.csv");
                    while (selectedFile.isFile()) {
                        selectedFile = new File(csvDir + user.get("email") + "-messageHistory-" + i + ".csv");
                        i++;
                    }
                    JFileChooser fileChooser = new JFileChooser(csvDir);
                    fileChooser.setSelectedFile(selectedFile);
                    int result = fileChooser.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            exportCSV(file, selected);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                
            });
            back.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    AccountManagerGUI accountManagerGUI = new AccountManagerGUI(frame, user.get("email"));
                    accountManagerGUI.show();
                }
                
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean exportCSV(File path, List<String> users) throws Exception {
        String[] userArray = users.toArray(new String[0]);
        for (int i = 0; i < userArray.length; i++) {
            userArray[i] = translator.get("email", (String) userArray[i]).get("id");
        }
        String result = (String) translator.query(new Query("MessageManager", "messagesToCSV", new Object[]{user.get("id"), userArray}));
        File file = path;
        file.createNewFile();
        PrintWriter pw = new PrintWriter(file);
        pw.write(result);
        pw.close();
        return true;
    }
}
