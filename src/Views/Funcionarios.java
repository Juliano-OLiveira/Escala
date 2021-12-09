package Views;

import Controller.ContollerFuncionarios;
import Factory.FuncionarioDaoFactory;
import IDao.funcionarioDao.IFuncionarioDao;
import ViewPrincipal.TelaPrincipal;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Funcionarios extends JPanel {
    
    private String[] cabecalho;
    public static DefaultTableModel dados;
    public static JTable tabela;
    protected static JButton btIncluir, btExcluir, btAlterar, btFechar;
     
    private static Connection con = null;
    private FuncionarioDaoFactory factory;
    private IFuncionarioDao dao;
    
    public Funcionarios() {
        setLayout(new FlowLayout());
        factory = new FuncionarioDaoFactory();
        this.dao = (IFuncionarioDao) factory.createObject();
        cabecalho = new String[3];
        
        cabecalho[0] = "ID";
        cabecalho[1] = "Nome";
        cabecalho[2] = "Ações";
        
        btIncluir = new JButton("Incluir");
        btExcluir = new JButton("Exluir");
        btFechar = new JButton("Fechar");
        btAlterar = new JButton("ALterar");
        
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.CENTER);
        JPanel paineBotoes = new JPanel(layout);
        paineBotoes.add(btIncluir);
        paineBotoes.add(btFechar);
        paineBotoes.add(btExcluir);
        paineBotoes.add(btAlterar);
        
        add(paineBotoes, BorderLayout.SOUTH);
        
        dados = new DefaultTableModel();
        dados.addColumn(cabecalho[0]);
        dados.addColumn(cabecalho[1]);
        dados.addColumn(cabecalho[2]);
        
        tabela = new JTable(dados);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane rolagem = new JScrollPane(tabela);
        add(rolagem, BorderLayout.CENTER);
        
        btnHandre handler = new btnHandre();
        btFechar.addActionListener(handler);
        btIncluir.addActionListener(handler);
        btAlterar.addActionListener(handler);
        btExcluir.addActionListener(handler);

       
        dao.listar();
    }

    private class btnHandre implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Object oriem = e.getSource();
            if (oriem == btFechar) {
                System.exit(1);
            } else if (oriem == btIncluir) {
                
                TelaPrincipal.selectAba(0);
                
            } else if (oriem == btAlterar) {
                ContollerFuncionarios.preencherCamposCadastro();
                
            } else if (oriem == btExcluir) {
                ContollerFuncionarios.excluirBD();
                
            }
        }
        
        
    }
    
}
