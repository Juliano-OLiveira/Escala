/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReseultTableModel;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.Box;
import javax.swing.JButton;
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
    public static final String DEFAULT_QUERY = "SELECT * from escala";
    public static JScrollPane scrollPane;
    private static JButton filterButton;
    private static JPanel teste;
    private static JButton deletar, relatorio;
    private String saida;

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
            JButton filterButton = new JButton("Aplicar filtro");
            filterButton.addActionListener(new FilterQueryHandler());
            sorter = new TableRowSorter<>(tableModel);
            resultTable.setRowSorter(sorter);

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
                deletarEscala();
            } else if (arg0.getSource() == relatorio) {
                try {
                    GerarRelatorio();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DisplayQueryResults.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        private void deletarEscala() {
            Object[] options = {"Sim", "Não"};

            int respost = JOptionPane.showOptionDialog(null, "Deseja realmente apagar a Escala?", "Informação", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if (respost == JOptionPane.NO_OPTION) {
                // System.out.println("Não");
            } else {
                try {
                    ConexaoBD.Conexao.iniciarConexao();
                    PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement("delete from escala");
                    pst.executeUpdate();

                    pst.close();
                    ConexaoBD.Conexao.disconect();
                    DisplayQueryResults.atualizarTableModel();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

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
                    sorter.setRowFilter(RowFilter.regexFilter(text));
                } catch (PatternSyntaxException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Bad regex pattern", "Bad regex pattern",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
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

    public static void atualizarTableModel() {

        try {

            tableModel.atualizarDados();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void GerarRelatorio() throws FileNotFoundException {
        try {
//            ConexaoBD.Conexao.iniciarConexao();
//            PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement(DEFAULT_QUERY);
//            ResultSet rs = pst.executeQuery();
         
//
//            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
//            System.out.println("Result set: " + jrRS.toString());
//            // caminho do reltório

//            FileInputStream caminhoRelatorio = new FileInputStream(cherry);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(caminhoRelatorio, new HashMap<>(), jrRS);
//
//            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/oli/NetBeansProjects/Escala/rel/Cherry.pdf");
            String src = "/home/oli/NetBeansProjects/Escala/src/relatorios/Cherry.jasper";
            JasperPrint jasperPrint = JasperFillManager.fillReport(src, null, ConexaoBD.Conexao.getC());
            
            JasperViewer view = new JasperViewer(jasperPrint, false);
           view.setTitle("Escala");
           view.setVisible(true);
   //         JasperViewer.viewReport(jasperPrint, false);
            

//            //     abrir o relatório
//            File file = new File("/home/oli/NetBeansProjects/Escala/rel/Escala.pdf");
//            try {
//                java.awt.Desktop.getDesktop().open(file);
//            } catch (IOException e) {
//                JOptionPane.showConfirmDialog(null, e);
//            }
//            file.deleteOnExit();
//            String impressora = "";
//            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//            PrintService psSelected = null;
//            for (PrintService ps : services) {
//                if (ps.getName().equals(impressora)) {
//                    psSelected = ps;
//                    break;
//                }
//            }
//            if (psSelected != null) {
//                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//                PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//                printServiceAttributeSet.add(new PrinterName(impressora, null));
//                printRequestAttributeSet.add(new Copies(1));
//                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
//                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, psSelected);
//                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.TRUE);
//                exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
//                exporter.exportReport();
//
//            }
            
            

        } catch (JRException e) {
            System.out.println(e.getMessage());
        }

    }

}
