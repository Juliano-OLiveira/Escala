 
package Factory;
 
import ConexaoBD.Conexao;
import IDao.escalaDao.IEscalaDaoImpl;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EscalaDaoFactory implements IDaoFactory{

    @Override
    public Object createObject() {
        try {
            return new IEscalaDaoImpl(Conexao.getInstance());
        } catch (SQLException ex) {
            Logger.getLogger(EscalaDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
}
