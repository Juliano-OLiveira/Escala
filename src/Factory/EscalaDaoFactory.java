 
package Factory;
 
import ConexaoBD.Conexao;
import IDao.escalaDao.EscalaDaoImpl;
import java.sql.SQLException;

public class EscalaDaoFactory implements IDaoFactory{

    @Override
    public Object createObject() {
        try {
            return new EscalaDaoImpl(Conexao.getInstance());
        } catch (SQLException ex) {
            System.out.println("Conexao: "+ex);
        }
        return null;
        
    }
    
}
