package Controller;

import DateFormat.TextDataChooser;
import Factory.EscalaDaoFactory;
import IDao.funcionarioDao.IFuncionarioDao;
import Models.Funcionario;
import ReseultTableModel.DisplayQueryResults;
import Views.Escala;
import static Views.Escala.jtComboFeriado;
import static Views.Escala.listarFunc;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class EscalaCotroller {

    private static PreparedStatement pst;
    private static int subt;
    private static Calendar cal;
    public static TextDataChooser txtData, txtFinal;
    public static List<String> manha = new ArrayList<>();
    private static List<String> aux = new ArrayList<>();
    public static Date dat = new Date();
    private static final List<String> tarde = new ArrayList<>();
    public static FocusListener placeHolder;
    private static IFuncionarioDao dao;
    private static EscalaDaoFactory factory;
    private static int dia, diaFinal;
    private static List<String> aux2;
    private static  Queue queue = new ArrayDeque();

    public EscalaCotroller() {

    }

    public static Escala getFunc() {
        return Escala.escala;
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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lis;

    }

    public static void preencherCombo() {

        for (Funcionario func : read()) {

            listarFunc.addItem(func.getNome());
            queue.add(func.getNome());

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
        int contador;

        for (int i = 0; i < subt; i++) {
            cal.add(Calendar.DATE, 1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                // System.out.println("Domingo");
                insertDomingo();

            } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                // System.out.println("Sabado");
                insertSabado();

            } else if (cal.get(Calendar.DATE) == t0 || feriados.contains(cal.get(Calendar.DATE))) {

                System.out.println("Feriados: " + t0);

            } else {

                try {
                   
//            }
                    for (Funcionario func2 : read()) {
                        queue.add(func2.getNome());
                    }

                    enviarDados(cal.getTime(),queue);
                } catch (SQLException ex) {
                    Logger.getLogger(EscalaCotroller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

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
            if (Escala.feridaoField.getText().equals("Exemplo: 1,2")) {
                Escala.feridaoField.setText("");
                Escala.feridaoField.setForeground(Color.GRAY);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (Escala.feridaoField.getText().isEmpty()) {
                Escala.feridaoField.setForeground(Color.GRAY);
                Escala.feridaoField.setText("Exemplo: 1,2");

            }
        }

    }

    public static int randInt(int min, int max) {
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

    public static String sorteia() {
        Collections.shuffle(manha);

        int index = randInt(0, manha.size() - 1);
        return manha.remove(index);

    }

    private static void enviarDados(Date dat, Queue queue) throws SQLException {

        try {

//            for (Funcionario func : read()) {
//                manha.add(func.getNome());
//               
//
            String sql = "insert into escala (idEscala,data,manha,tarde) values (default,?,?,?)";
            System.out.println("Array: " + queue.toString());

            String manha = (String) queue.remove();
             
            String tarde = (String) queue.remove();
            queue.add(manha);
            queue.add(tarde);
            
            queue.remove();
            pst = ConexaoBD.Conexao.getC().prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(dat.getTime()));

            pst.setString(2, manha);
            pst.setString(3, tarde);
            pst.execute();
            pst.close();

            DisplayQueryResults.atualizarTableModel();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }

    }

    public static void removeCombo() {

        int index = listarFunc.getSelectedIndex();
        Object ob = listarFunc.getSelectedItem();
        if (ob.equals("")) {

            JOptionPane.showMessageDialog(null,
                    "Você deve selecionar um item na Caixa de Seleção", "Aviso", JOptionPane.WARNING_MESSAGE);

        } else {

            Escala.listarFunc.removeItemAt(index);
            EscalaCotroller.manha.remove(index);

        }

    }

}
