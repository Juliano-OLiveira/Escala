package Controller;

import DateFormat.TextDataChooser;
import Factory.EscalaDaoFactory;
import Factory.FuncionarioDaoFactory;
import IDao.escalaDao.EscalaDaoImpl;
import IDao.funcionarioDao.IFuncionarioDao;
import Models.Funcionario;
import Views.Cadastro;
import Views.DisplayQueryResults;
import static Views.DisplayQueryResults.resultTable;
import static Views.DisplayQueryResults.salvar;
import static Views.DisplayQueryResults.tableModel;
import Views.Escala;
import static Views.Escala.jtComboFeriado;
import static Views.Escala.listarFunc;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EscalaCotroller {

    private static PreparedStatement pst;
    private static int subt;
    private static Calendar cal;
    public static TextDataChooser txtData, txtFinal;

    public static Date dat = new Date();
    private static EscalaDaoImpl daoEscala;
    public static FocusListener placeHolder;

    private static EscalaDaoFactory factory;
    private static int dia, diaFinal;
    private static JDialog frame;
    private static JTextField fieldManha = new JTextField(15);
    private static JTextField fieldTarde = new JTextField(15);
    private static JTextField fieldCodigo;
    public static Queue queue = new LinkedList();
    public static int re;
    
    private static FuncionarioDaoFactory factoryFunc;
    private static EscalaDaoFactory factory2;
    private static IFuncionarioDao daoFuncionario;

    public static void ConexaoFactory() {
        factoryFunc = new Factory.FuncionarioDaoFactory();
        daoFuncionario = (IFuncionarioDao) factoryFunc.createObject();
        
        factory2 = new Factory.EscalaDaoFactory();
        daoEscala = (EscalaDaoImpl) factory2.createObject();
        
        factory = new Factory.EscalaDaoFactory();
        daoEscala = (EscalaDaoImpl) factory.createObject();
           
    }

    public static Escala getFunc() {
        return Escala.escala;
    }

  

    public static void insertSabado() throws ParseException {

        try {
            ConexaoBD.Conexao.iniciarConexao();
            PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement("insert into escala (data,manha,tarde) values (default,'  ',' ')");

            pst.executeQuery();
            pst.close();
            ConexaoBD.Conexao.disconect();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }

    public static List<Funcionario> read() {
        String sql = "select idFunc,nome from funcionarios order by idFunc";
        List<Funcionario> lis = new ArrayList();
        ConexaoBD.Conexao.iniciarConexao();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            ps = ConexaoBD.Conexao.getC().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario func = new Funcionario();
                //func.setId(rs.getInt("idFunc"));
                func.setNome(rs.getString("nome"));

                lis.add(func);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lis;

    }

    public static List<Funcionario> pegandoId() {
        String sql = "select idFunc from funcionarios order by idFunc";
        List<Funcionario> lis = new ArrayList();
        ConexaoBD.Conexao.iniciarConexao();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            ps = ConexaoBD.Conexao.getC().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario func = new Funcionario();
                func.setId(rs.getInt("idFunc"));

                lis.add(func);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lis;

    }

    public static void preencherCombo() {

        read().stream().forEach((func) -> {
            listarFunc.addItem(func.getNome());
        });

    }

    public static void removeCombo() throws SQLException {

        int index = listarFunc.getSelectedIndex();
        Object ob = listarFunc.getSelectedItem();
        String sql = " DELETE FROM funcionarios WHERE nome = ?";
        Funcionario funci = new Funcionario();
        funci.setNome((String) ob);

        try {

            Object[] options = {"Sim", "Não"};

            int respost = JOptionPane.showOptionDialog(null, "Deseja realmente Excluir o Funcionario?", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if (respost == JOptionPane.NO_OPTION) {
                // System.out.println("Não");
            } else {

                PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement(sql);
                pst.setString(1, funci.getNome());

                JOptionPane.showMessageDialog(null,
                        "Funcionário " + funci.getNome() + " removido com Sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                pst.execute();

                pst.close();

            }

        } catch (HeadlessException ex) {
            System.out.println("Erro: " + ex);
        }

    }

    public static void inserindoDatas() throws ParseException {

        String sData[] = txtData.getText().split("/");
        String sDataFinal[] = txtFinal.getText().split("/");

        if (txtData.getText().equals("__/__/____")) {
            JOptionPane.showMessageDialog(null, "Insira uma Data de Inicio", "Aviso", JOptionPane.WARNING_MESSAGE);
        } else if (txtFinal.getText().equals("__/__/____")) {
            JOptionPane.showMessageDialog(null, "Insira uma Data Final", "Aviso", JOptionPane.WARNING_MESSAGE);

        }

        List<Integer> feriados = new ArrayList();

        int ano = Integer.parseInt(sData[2]);
        //Subtrai 1 porque os meses iniciam com 0-janeiro e 11-Dezembro
        int mes = Integer.parseInt(sData[1]) - 1;
        dia = Integer.parseInt(sData[0]) - 1;
        diaFinal = Integer.parseInt(sDataFinal[0]);
        subt = diaFinal - dia;

        cal = Calendar.getInstance();
        cal.set(ano, mes, dia);

        String[] testString = Escala.feridaoField.getText().trim().split(",");
        int t0 = 0;

        String nao = (String) jtComboFeriado.getSelectedItem();
        if (nao.equalsIgnoreCase("sim")) {

            for (int l = 0; l < testString.length; l++) {
                t0 = Integer.parseInt(testString[l]);
                feriados.add(t0);
            }

            System.out.println("----" + feriados.toString());
        }

        for (int i = 0; i < subt; i++) {
            cal.add(Calendar.DATE, 1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  ) {
                // System.out.println("Domingo");
                insertDomingo();

            } 
            else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                // System.out.println("Sabado");
                insertSabado();

            } 
            else if (cal.get(Calendar.DATE) == t0 || feriados.contains(cal.get(Calendar.DATE))) {

                System.out.println("Feriados: " + t0);

            } else {

                try {

                    for (Funcionario func2 : read()) {
                        queue.add(func2.getNome());

                    }

                    enviarDados(cal.getTime(), queue);
                } catch (SQLException ex) {
                    Logger.getLogger(EscalaCotroller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
      private static void insertDomingo() {
        try {
            ConexaoBD.Conexao.iniciarConexao();
            PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement("insert into escala (data,manha,tarde) values (default,' ',' ')");
          
            pst.executeQuery();
            pst.close();
            ConexaoBD.Conexao.disconect();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }

    public static DisplayQueryResults instanciarTableEscala1() {
        DisplayQueryResults tela = new DisplayQueryResults();
        tela.setSize(500, 250);

        tela.setVisible(true);

        return tela;
    }

    public static class placeHolder implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (Escala.feridaoField.getText().equals("Ex: 1,2")) {
                Escala.feridaoField.setText("");
                Escala.feridaoField.setForeground(Color.GRAY);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (Escala.feridaoField.getText().isEmpty()) {
                Escala.feridaoField.setForeground(Color.GRAY);
                Escala.feridaoField.setText("Ex: 1,2");

            }
        }

    }

//  
    private static void enviarDados(Date dat, Queue queue) throws SQLException {

        ConexaoFactory();
        String sql = "insert into escala (idEscala,data,manha,tarde) values (default,?,?,?)";
        System.out.println("Array: " + queue.toString());
        String manha = (String) queue.remove();
        String tarde = (String) queue.remove();
        queue.add(manha);
        queue.add(tarde);
        queue.remove(manha);
        queue.remove(tarde);
        daoEscala.inserirEscala(dat, manha, tarde);

        atualizarTableModel();

    }

    public static void deletarEscala() {
        Object[] options = {"Sim", "Não"};

        int respost = JOptionPane.showOptionDialog(null, "Deseja realmente apagar a Escala?", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        if (respost == JOptionPane.NO_OPTION) {
            // System.out.println("Não");
        } else {
            try {
              ConexaoFactory();
                daoEscala.deletarEscala();

                atualizarTableModel();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public static void atualizarTableModel() {

        try {

            tableModel.atualizarDados();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void montarTelaEdicaoEscala() {
        frame = new JDialog();
        frame.setLocationRelativeTo(null);
        //frame.setModal(true);
        frame.setSize(500, 300);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setTitle("Alterar Escala");
        frame.setLayout(new FlowLayout(0, 0, 50));

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2));
        JLabel labelManha = new JLabel("Manha: ");
        labelManha.setHorizontalAlignment(JLabel.RIGHT);
        JLabel labelTarde = new JLabel("Tarde: ");
        labelTarde.setHorizontalAlignment(JLabel.RIGHT);

        JLabel vazio = new JLabel();
        vazio.setHorizontalAlignment(JLabel.RIGHT);
        DisplayQueryResults.salvar = new JButton("Salvar");

        painel.add(labelManha);
        painel.add(fieldManha);
        painel.add(labelTarde);
        painel.add(fieldTarde);
        painel.add(vazio);
        painel.add(DisplayQueryResults.salvar);
        frame.add(painel);

        Models.Escala esc = new Models.Escala();

        esc.setId((Integer) resultTable.getValueAt(re, 0));
        esc.setManha((String) resultTable.getValueAt(re, 2));
        esc.setTarde((String) resultTable.getValueAt(re, 3));

        fieldCodigo = new JTextField();

        fieldCodigo.setText(String.valueOf(esc.getId()));
        fieldManha.setText(esc.getManha());
        fieldTarde.setText(esc.getTarde());

        DisplayQueryResults.hanbleButton haButton = new DisplayQueryResults.hanbleButton();
        salvar.addActionListener(haButton);

    }

    public static void btnSalvar() {
        ConexaoFactory();
        Models.Escala es = new Models.Escala();
        String manha = fieldManha.getText();
        String tarde = fieldTarde.getText();
        Integer codigo = Integer.parseInt(fieldCodigo.getText());
        System.out.println("Teste: " + manha);
        es.setId(codigo);
        es.setManha(manha);
        es.setTarde(tarde);

        daoEscala.alterar(es);
        EscalaCotroller.atualizarTableModel();
        daoFuncionario.listar();
        Cadastro.atualizarComboFuncionarios();
        fieldManha.setText("");
        fieldTarde.setText("");
        JOptionPane.showMessageDialog(null, "Alterado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();

    }

}
