package Views;

import Models.Funcionario;
import Controller.ContollerFuncionarios;
import Controller.EscalaCotroller;
import ViewPrincipal.TelaPrincipal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Cadastro extends JPanel {

    public static Funcionario getFunc() {
        return Cadastro.funcionario;
    }

    private JLabel nome, idLabel;
    public static JTextField nomeFuncionario, codigo;
    private JButton salvar, novo;
    public static Funcionario funcionario;
    private String status;

    public static void limparCampos() {
        codigo.setText("");
        nomeFuncionario.setText("");

    }

    public Cadastro() {

        setLayout(new GridLayout(5, 2));
        JPanel west = new JPanel();
        JPanel east = new JPanel();
        JPanel souht = new JPanel(new FlowLayout());
//        add(west,BorderLayout.WEST);
        // add(east,BorderLayout.NORTH);

        funcionario = new Funcionario();
        JPanel center = new JPanel();

        idLabel = new JLabel("ID: ");

        codigo = new JTextField(5);
        codigo.setEnabled(false);
        nome = new JLabel("Nome: ");

        //nome.setHorizontalAlignment(JLabel.LEFT);
        nome.setForeground(Color.decode("#57c4b9"));
        nome.setFont(new Font("Times new Roman", Font.BOLD, 20));

        nomeFuncionario = new JTextField(15);
        nomeFuncionario.setBorder(new LineBorder(Color.decode("#9eadba"), 2));
        nomeFuncionario.setBorder(BorderFactory.createCompoundBorder(
                nomeFuncionario.getBorder(),
                BorderFactory.createEmptyBorder(0, 5, 0, 0)
        ));

        salvar = new JButton("Salvar");
        salvar.setFont(new Font("Arial", Font.BOLD, 20));
        salvar.setForeground(Color.white);

        salvar.setBackground(Color.decode("#6558f5"));

        novo = new JButton("Novo");
        novo.setFont(new Font("Arial", Font.BOLD, 20));
        novo.setForeground(Color.white);
        novo.setBackground(Color.decode("#7FFFD4"));

        center.add(idLabel);
        center.add(codigo);
        center.add(nome);
        center.add(nomeFuncionario);
        center.setBackground(Color.decode("#ffffff"));

        souht.setBackground(Color.decode("#ffffff"));
        souht.add(salvar);
        souht.add(novo);

        add(center, BorderLayout.CENTER);
        add(souht);

        salvarHandler handler = new salvarHandler();
        nomeFuncionario.addActionListener(handler);
        salvar.addActionListener(handler);
        novo.addActionListener(handler);

    }

    static void setFuncionario(Funcionario funcionario) {
        Cadastro.funcionario = funcionario;
        codigo.setText(funcionario.getId().toString());
        nomeFuncionario.setText(funcionario.getNome());
    }

    public class salvarHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (nomeFuncionario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Cadastro.this, "Digite o Nome do Funcionário", "Aviso", JOptionPane.ERROR_MESSAGE);

            } else if (e.getSource() == salvar) {

                ContollerFuncionarios.addPessoa_altPessoa();
                atualizarComboFuncionarios();
                limparCampos();
                TelaPrincipal.selectAba(1);

            } else if (e.getSource() == novo) {
                limparCampos();
            }
        }

    }

    public static void atualizarComboFuncionarios() {
        Escala.listarFunc.removeAllItems();
        EscalaCotroller.preencherCombo();

    }
}
