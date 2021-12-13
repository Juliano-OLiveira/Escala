package IDao.escalaDao;

import ConexaoBD.Conexao;
import IDao.IDao;
import IDao.funcionarioDao.FuncionarioDaoImpl;
import Models.Funcionario;
import Views.Escala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.Data;

public class IEscalaDaoImpl implements IDao {

    private Conexao conexao;
    private Connection conn;

    public IEscalaDaoImpl(Conexao conexao) {
        this.conexao = conexao;
        this.conn = conexao.getC();
    }

    public IEscalaDaoImpl() {
    }
    
    

    @Override
    public Object inserir(Object object) {
      return null;
    }

    @Override
    public Object alterar(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void excluir(Object object) {
           
       Funcionario funci = (Funcionario) object;
        String sql = " DELETE FROM funcionarios WHERE nome = ?";

        try {
            PreparedStatement pst = ConexaoBD.Conexao.getC().prepareStatement(sql);
            pst.setString(1, funci.getNome());
           pst.execute();

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List listar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object inserirEscala(Date a, List b, List c) {
         Date dat = (Date) a;
        List<String> manha = (ArrayList<String>) b;
        List<String> tarde =  (ArrayList<String>)c;
        String sql = "insert into escala (idEscala,data,manha,tarde) values (default,?,?,?)";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(dat.getTime()));
            pst.setString(2, manha.get(0) );
            pst.setString(3, tarde.get(0) );
            System.out.println("ESCALA  : " + a+b+c);

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Escala failed, no rows affected.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
