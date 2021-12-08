package Views;

import Controller.EscalaCotroller;
import DateFormat.TextDataChooser;
import Models.Funcionario;
import ReseultTableModel.DisplayQueryResults;
import groovy.xml.Entity;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Escala extends JPanel {

    private static JPanel resultTableModel;
    public static Escala escala;
    private JLabel funciJLabel, periodoLabel, dataInicial, dataFinal, labelFeriado, vazio, vazio2;
    public static JComboBox listarFunc, jtComboFeriado;
    private String[] FE = {"Não", "Sim"};
    public static JTextField dataIn, dataFin, feridaoField;
    private JButton adiconar;
    private Funcionario func;
    private static JPanel esquerdo;

    public Escala() {
        setLayout(new BorderLayout());
        // setBackground(Color.red);
        btHandler handler = new btHandler();

        esquerdo = new JPanel(new GridLayout(1, 2));
        funciJLabel = new JLabel("Funcionários: ");
        funciJLabel.setHorizontalAlignment(JLabel.RIGHT);

//        periodoLabel = new JLabel("Período: ");
//         periodoLabel.setHorizontalAlignment(JLabel.RIGHT);
        listarFunc = new JComboBox();
        EscalaCotroller.preencherCombo();

        adiconar = new JButton();
        adiconar.setIcon(new ImageIcon(getClass().getResource("/icones/add.png")));
        adiconar.setPreferredSize(new Dimension(40, 40));
        adiconar.setBackground(null);
        adiconar.setOpaque(false);

        adiconar.setBorderPainted(false);

        jtComboFeriado = new JComboBox(FE);

        esquerdo.add(funciJLabel);
        esquerdo.add(listarFunc);
        // esquerdo.add(periodoLabel);
        //esquerdo.add(perido);

        add(esquerdo, BorderLayout.NORTH);

        JPanel direito = new JPanel(new BorderLayout());

        dataInicial = new JLabel("Data Inicio: ");
        dataInicial.setHorizontalAlignment(JLabel.RIGHT);
        dataFinal = new JLabel("Data Final: ");
        dataFinal.setHorizontalAlignment(JLabel.RIGHT);

        EscalaCotroller.txtData = new TextDataChooser();
        EscalaCotroller.txtData.setColumns(10);
        EscalaCotroller.txtFinal = new TextDataChooser();
        EscalaCotroller.txtFinal.setColumns(10);

        labelFeriado = new JLabel("Feriado: ");
        feridaoField = new JTextField(15);
        feridaoField.setText("Exemplo: 1,2");
        feridaoField.setForeground(Color.GRAY);
        feridaoField.setEditable(false);
        feridaoField.setHorizontalAlignment(JLabel.RIGHT);

        esquerdo.add(dataInicial);
        esquerdo.add(  EscalaCotroller.txtData);
        esquerdo.add(dataFinal);
        esquerdo.add(  EscalaCotroller.txtFinal);
        esquerdo.add(labelFeriado);
        esquerdo.add(jtComboFeriado);
        esquerdo.add(feridaoField);

        jtComboFeriado.addActionListener(handler);

        vazio = new JLabel(" ");

        esquerdo.add(vazio);

        direito.add(adiconar);

        esquerdo.add(direito);

        resultTableModel = new JPanel();

        add(  EscalaCotroller.instanciarTableEscala1());

        jtComboFeriado.setSelectedIndex(0);

        adiconar.addActionListener(handler);

       
        feridaoField.addFocusListener(EscalaCotroller.placeHolder);

    }

    public class btHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == adiconar) {
                try {
                    EscalaCotroller.inserindoDatas();
                } catch (ParseException ex) {
                    Logger.getLogger(Escala.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (e.getSource() == jtComboFeriado) {
                String texto = (String) jtComboFeriado.getSelectedItem();

                if (texto.equalsIgnoreCase("sim")) {
                    feridaoField.setText("");
                    feridaoField.setEditable(true);

                } else {
                    //  jtComboFeriado.setSelectedIndex(0);
                    feridaoField.setEditable(false);

                    // esquerdo.remove(feridaoField);
                }

            }

        }
    }

}
