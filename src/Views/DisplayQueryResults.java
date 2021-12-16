/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Views;

import Controller.EscalaCotroller;
import Factory.EscalaDaoFactory;
import Factory.FuncionarioDaoFactory;
import IDao.escalaDao.EscalaDaoImpl;
import IDao.funcionarioDao.IFuncionarioDao;
import Models.Escala;
import ReseultTableModel.ResultSetTableModel;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sun.awt.AWTAccessor;

/**
 *
 * @author santos
 */
public class DisplayQueryResults extends JInternalFrame {

    private static JTextArea queryArea;
    private static JTextField filterText;
    public static ResultSetTableModel tableModel;
    private static TableRowSorter<TableModel> sorter;
    public static JTable resultTable;
    public static final String DEFAULT_QUERY = "SELECT * from escala order by data ASC";
    public static JScrollPane scrollPane;
    private static JButton filterButton;
    private static JPanel teste;
    private static JButton deletar, relatorio;
    private String saida;
    private InputStream src;

    public static JButton salvar;

    public static hanbleButton haButton = new hanbleButton();

    public DisplayQueryResults() {
        // super("Visualização de resultados de consulta de banco de dados");

        iniciarComponentes();

    }

    private void iniciarComponentes() {
        try {

            addWindowListener(new WindowHandler());

            tableModel = new ResultSetTableModel(DEFAULT_QUERY);
//            queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
//            queryArea.setWrapStyleWord(true);
//            queryArea.setLineWrap(true);
            scrollPane = new JScrollPane(queryArea,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            // configura o JButton para enviar consulta
            deletar = new JButton("Deletar");
            deletar.addActionListener(new SubmitQueryHandler());
            relatorio = new JButton("Relatório");
            relatorio.addActionListener(new SubmitQueryHandler());
//            submitButton.addActionListener(new SubmitQueryHandler());
            /**
             * Define um painel do tipo Box para armazenar a área de texto do
             * script e o botão de envio
             */
            Box boxNorth = Box.createHorizontalBox();
            boxNorth.add(relatorio);
            boxNorth.add(deletar);

            /**
             * Tabela que apresentará o resultado da execução da consulta no
             * banco de dados
             */
            resultTable = new JTable(tableModel);
            JScrollPane scrollTable = new JScrollPane(resultTable);

            filterText = new JTextField();
            filterText.addActionListener(new FilterQueryHandler());
            JButton filterButton = new JButton("Aplicar filtro");
            filterButton.addActionListener(new FilterQueryHandler());
            
            sorter = new TableRowSorter<>(tableModel);
            resultTable.setRowSorter(sorter);
            altTable atTable = new altTable();
            resultTable.addMouseListener(atTable);

            /**
             * Painel utilizado para permitir a aplicação de filtros na tabela
             */
            Box boxSouth = Box.createHorizontalBox();
            boxSouth.add(new JLabel("Filtro: "));
            boxSouth.add(filterText);
            boxSouth.add(filterButton);

            add(boxNorth, BorderLayout.NORTH);
            add(scrollTable, BorderLayout.CENTER);
            add(boxSouth, BorderLayout.SOUTH);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
            tableModel.disconnect();
            // Finalização com erro
            System.exit(1);
        }
    }

    private class SubmitQueryHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            try {
                tableModel.setQuery(DEFAULT_QUERY);

            } catch (SQLException sqlException2) {
                JOptionPane.showMessageDialog(null,
                        sqlException2.getMessage(), "Database error",
                        JOptionPane.ERROR_MESSAGE);
                // assegura que a conexão de banco de dados está fechada
                tableModel.disconnect();
                // Finalização com erro
                System.exit(1); // termina o aplicativo
            }

            if (arg0.getSource() == deletar) {
                EscalaCotroller.deletarEscala();
            } else if (arg0.getSource() == relatorio) {
                GerarRelatorio();

            }

        }

        public void GerarRelatorio() {
            try {

                src = this.getClass().getClassLoader().getResourceAsStream("relatorios/Escala.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(src, null, ConexaoBD.Conexao.getC());

                JasperViewer view = new JasperViewer(jasperPrint, false);
                view.setTitle("Escala");
                view.setVisible(true);

            } catch (JRException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    private class FilterQueryHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            String text = filterText.getText();

            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)^" + text));
                    
                } catch (PatternSyntaxException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Bad regex pattern", "Bad regex pattern",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private class altTable implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            EscalaCotroller.re = resultTable.getSelectedRow();
            if (EscalaCotroller.re != -1) {
                EscalaCotroller.montarTelaEdicaoEscala();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            return;
        }

    }

    private class WindowHandler extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent e) {
            tableModel.disconnect();
            // Finalização com sucesso
            System.exit(0);
        }

    }

    public static class hanbleButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == salvar) {
                EscalaCotroller.btnSalvar();

            }
        }

    }

   
    
    
}
