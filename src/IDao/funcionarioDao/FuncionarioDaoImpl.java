package IDao.funcionarioDao;

import ConexaoBD.Conexao;
import Models.Funcionario;
import Views.Cadastro;
import static Views.Funcionarios.dados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncionarioDaoImpl implements IFuncionarioDao {

    private static final String FUNCIONARIO = "select idfunc, nome from funcionarios";
    private Conexao conexao;
    private Connection conn;

    public FuncionarioDaoImpl(Conexao conexao) {
        this.conexao = conexao;
        this.conn = conexao.getC();
    }

    @Override
    public Object inserir(Object object) {
        Funcionario funci = (Funcionario) object;
        String sql = "insert into funcionarios (nome) values(?)";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, funci.getNome());
            System.out.println("Fucnionario: " + object);

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
        String sql = " DELETE FROM funcionarios WHERE idfunc = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, funci.getId());
            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Funionarios failed, no rows affected.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List listar() {
        ResultSet rs = null;
        List<Funcionario> resultList = null;
        try {
            dados.setNumRows(0);

            Funcionario func = Cadastro.getFunc();
            String sql = "SELECT * FROM funcionarios WHERE idfunc<1000 ORDER BY idfunc ASC";
            PreparedStatement stm = conn.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                func.setId(rs.getInt("idFunc"));
                func.setNome(rs.getString("nome"));
                dados.addRow(func.toVetor());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public Object alterar(Object object) {
        Funcionario funci = (Funcionario) object;
        String sql = "UPDATE funcionarios SET nome = ? WHERE idfunc = ? ";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, funci.getNome());
            pst.setInt(2, funci.getId());
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
    public Funcionario buscarPorId(Integer id) {
        ResultSet rs = null;
        Funcionario funcionario = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(FUNCIONARIO
                    + " WHERE id = ? ");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                funcionario = new Funcionario();
                funcionario.setId(rs.getInt(1));
                funcionario.setNome(rs.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return funcionario;
    }

    @Override
    public List buscarPorNome(String nome) {
        ResultSet rs = null;
        List<Funcionario> resultList = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(FUNCIONARIO
                    + " WHERE nome LIKE ? ");
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            resultList = new ArrayList();
            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt(1));
                funcionario.setNome(rs.getString(2));
                resultList.add(funcionario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public Object inserirEscala(Date a, String b,String c) {
       return null;
    }

    @Override
    public void deletarEscala() {
    }

   

}
