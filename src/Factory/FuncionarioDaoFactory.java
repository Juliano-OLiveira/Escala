
package Factory;

import ConexaoBD.Conexao;
import IDao.funcionarioDao.FuncionarioDaoImpl;

public class FuncionarioDaoFactory implements IDaoFactory{

    @Override
    public Object createObject() {
        try {
            return new FuncionarioDaoImpl(Conexao.getInstance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
