package ViewPrincipal;

import Views.Cadastro;
import Views.Escala;
import Views.Funcionarios;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class TelaPrincipal extends JFrame {

    private static JTabbedPane tabbedpane;
    private JTabbedPane Apoio;

    public TelaPrincipal() {
        setTitle("Gerador de Escala");
        setBackground(Color.decode("#ffffff"));

        tabbedpane = new JTabbedPane();
        tabbedpane.setPreferredSize(new Dimension(1080, 500));
        add(tabbedpane, BorderLayout.CENTER);

        Cadastro cadastro = new Cadastro();
        cadastro.setBackground(Color.decode("#ffffff"));
        tabbedpane.add("Cadastro", cadastro);

        Funcionarios funcionarios = new Funcionarios();
        tabbedpane.add("Funcionários", funcionarios);

        Escala escala = new Escala();
        tabbedpane.add("Escala", escala);

       

    }

    public static void selectAba(int aba) {
        tabbedpane.setSelectedIndex(aba);
    }

    

}
