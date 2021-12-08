package Executavel;

import ViewPrincipal.TelaPrincipal;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DemoEscala {

    public static void main(String[] args) {
        try {
            UIManager.put("nimbusBase", new Color(140, 103, 59));
            UIManager.put("nimbusBlueGrey", new Color(190, 189, 170));
            UIManager.put("control", new Color(221, 223, 212));
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            TelaPrincipal tela = new TelaPrincipal();
           
            tela.setDefaultCloseOperation(new JFrame(). DISPOSE_ON_CLOSE);

            tela.setVisible(true);
            tela.setSize(800, 500);
            tela.setLocationRelativeTo(null);
            tela.pack();
        } catch (UnsupportedLookAndFeelException exc) {
            exc.printStackTrace();
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace();
        } catch (InstantiationException exc) {
            exc.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        }
//
    }

}
