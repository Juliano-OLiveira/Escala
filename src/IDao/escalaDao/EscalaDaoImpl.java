package IDao.escalaDao;

import ConexaoBD.Conexao;
import IDao.IDao;
import IDao.funcionarioDao.FuncionarioDaoImpl;
import Models.Funcionario;
import Models.Escala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EscalaDaoImpl implements IDao {

    private Conexao conexao;
    private Connection conn;

    public EscalaDaoImpl(Conexao conexao) {
        this.conexao = conexao;
        this.conn = conexao.getC();
    }

    public EscalaDaoImpl() {
    }
    
    

    @Override
    public Object inserir(Object object) {
      return null;
    }

    @Override
    public Object alterar(Object object) {
         Escala funci = (Escala) object;
        String sql = "UPDATE escala SET manha = ?, tarde=? WHERE idescala = ? ";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, funci.getManha());
            pst.setString(2, funci.getTarde());
            pst.setInt(3, funci.getId());
            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Funionarios failed, no rows affected.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return funci;
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
    public Object inserirEscala(Date a, String b, String c) {
         Date dat = (Date) a;
        String manha =   b;
       String tarde =  c;
        String sql = "insert into escala (idEscala,data,manha,tarde) values (default,?,?,?)";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(dat.getTime()));
            pst.setString(2, manha );
            pst.setString(3, tarde );
            

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Escala failed, no rows affected.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public void deletarEscala() {
        PreparedStatement pst;
        try {
            pst = ConexaoBD.Conexao.getC().prepareStatement("delete from escala");
                pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EscalaDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
