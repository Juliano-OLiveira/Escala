package Views;

import Controller.EscalaCotroller;
import DateFormat.TextDataChooser;
import Factory.EscalaDaoFactory;
import IDao.escalaDao.EscalaDaoImpl;
import Models.Funcionario;
import static Views.Cadastro.atualizarComboFuncionarios;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    private JButton adiconar, remover;
    private Funcionario func;
    private static JPanel esquerdo;
    private static EscalaDaoImpl dao;
    private static EscalaDaoFactory factory;

    public Escala() {
        setLayout(new BorderLayout());

        btHandler handler = new btHandler();

        esquerdo = new JPanel(new GridLayout(1, 2));
        funciJLabel = new JLabel("Funcionários: ");
        funciJLabel.setHorizontalAlignment(JLabel.RIGHT);
        String vaz[] = {""};
        listarFunc = new JComboBox(vaz);

        EscalaCotroller.preencherCombo();
        remover = new JButton("Remover");
        remover.setBorderPainted(true);

        adiconar = new JButton();
        loadIcon();
        adiconar.setPreferredSize(new Dimension(40, 40));
        adiconar.setBackground(null);
        adiconar.setOpaque(false);

        adiconar.setBorderPainted(false);

        jtComboFeriado = new JComboBox(FE);

        esquerdo.add(funciJLabel);
        esquerdo.add(listarFunc);
        esquerdo.add(remover);

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
        feridaoField.setText("Ex: 1,2");
        feridaoField.setForeground(Color.GRAY);
        feridaoField.setEditable(false);
        feridaoField.setHorizontalAlignment(JLabel.RIGHT);

        esquerdo.add(dataInicial);
        esquerdo.add(EscalaCotroller.txtData);
        esquerdo.add(dataFinal);
        esquerdo.add(EscalaCotroller.txtFinal);
        esquerdo.add(labelFeriado);
        esquerdo.add(jtComboFeriado);
        esquerdo.add(feridaoField);

        jtComboFeriado.addActionListener(handler);

        vazio = new JLabel(" ");

        esquerdo.add(vazio);

        direito.add(adiconar);

        esquerdo.add(direito);

        resultTableModel = new JPanel();

        add(EscalaCotroller.instanciarTableEscala1());

        jtComboFeriado.setSelectedIndex(0);

        adiconar.addActionListener(handler);
        remover.addActionListener(handler);

        feridaoField.addFocusListener(EscalaCotroller.placeHolder);

    }

    private void loadIcon() {
        InputStream icon = getClass().getResourceAsStream("/icones/add.png");

        try {

            Image image = ImageIO.read(icon);

            this.adiconar.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

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

                    feridaoField.setEditable(false);
                     feridaoField.setText("Ex: 1,2");

                }

            } else if (e.getSource() == remover) {
                try {
                    EscalaCotroller.removeCombo();
                    atualizarComboFuncionarios();
                    Funcionarios.dao.listar();
                } catch (SQLException ex) {
                    Logger.getLogger(Escala.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

}
