package ReseultTableModel;

import ConexaoBD.Conexao;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author santos
 */
public class ResultSetTableModel extends AbstractTableModel {

    
    private final Statement statement;
    private boolean connectedToDatabase;
    public static ResultSet resultset;
    private static ResultSetMetaData metaData;
    private static int numberOfRows;

    public ResultSetTableModel(String query) throws SQLException {
        Conexao.iniciarConexao();
        statement = ConexaoBD.Conexao.getC().createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        
        connectedToDatabase = true;
        setQuery(query);
       
    }

    public void setQuery(String query) throws SQLException, 
            IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Não está conectado ao banco de dados");
        }
        resultset = statement.executeQuery(query);
        // obtém informações do cabeçado da query
        metaData = resultset.getMetaData();
        resultset.last();
        // determina o número de elementos do resultado da query
        numberOfRows = resultset.getRow();
        // informa que uma nova consulta foi gerada, portanto deve atualizar os dados
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Não está conectado ao banco de dados");
        }
        return numberOfRows;
    }

    @Override
    public int getColumnCount() throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Não está conectado ao banco de dados");
        }
        try {
            return metaData.getColumnCount();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // retorna 0 em caso de falha
        return 0;
    }

    @Override
    public Object getValueAt(int row, int col) throws IllegalStateException {
        if (!connectedToDatabase) {
            throw new IllegalStateException("Não está conectado ao banco de dados");
        }
        try {
            resultset.absolute(row + 1);
            return resultset.getObject(col + 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // se ocorrer falha, envia vazio
        return "";
    }

    /**
     * Retorna a classe que representa a coluna informada
     * 
     * @param columnIndex
     * @return 
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        try {
            if (!connectedToDatabase) {
                throw new IllegalStateException("Não está conectado ao banco de dados");
            }
            String className = metaData.getColumnClassName(columnIndex + 1);
            
            return Class.forName(className);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // se ocorrer falhar, retorna o padrão Object
        return Object.class;
    }

    @Override
    public String getColumnName(int column) {
        try {
            if (!connectedToDatabase) {
                throw new IllegalStateException("Não está conectado ao banco de dados");
            }
            return metaData.getColumnName(column + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    public void disconnect() {
        if (connectedToDatabase) {
            try {
                resultset.close();
                statement.close();
                ConexaoBD.Conexao.disconect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                connectedToDatabase = false;
            }
        }
    }

    public void atualizarDados() throws SQLException{
      resultset = statement.executeQuery("select  * from escala order by data ASC");
        // obtém informações do cabeçado da query
        metaData = resultset.getMetaData();
        resultset.last();
        // determina o número de elementos do resultado da query
        numberOfRows = resultset.getRow();
        // informa que uma nova consulta foi gerada, portanto deve atualizar os dados
        fireTableStructureChanged();
    
    
    }
    
}
