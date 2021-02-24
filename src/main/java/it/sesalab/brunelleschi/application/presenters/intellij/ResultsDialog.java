package it.sesalab.brunelleschi.application.presenters.intellij;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Collection;

public class ResultsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private final Collection<ArchitecturalSmell> smells;


    public ResultsDialog(Collection<ArchitecturalSmell> smells) {
        this.smells = smells;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Brunelleschi - Architectural Smell Detection");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        String[] colummns = {"Component Name", "Smell Type"};
        DefaultTableModel model = new DefaultTableModel(colummns, 0);

        for(ArchitecturalSmell smell: smells){
            for(Component component: smell.getAffectedComponents()){
                model.addRow(new String[]{component.getQualifiedName(), smell.getSmellType().toString()});
            }
        }

        table1.setModel(model);


    }

    private void onOK() {
        // add your code here
        this.dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        this.dispose();
    }
}
