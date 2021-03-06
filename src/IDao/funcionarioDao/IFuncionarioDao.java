package IDao.funcionarioDao;

import IDao.IDao;
import Models.Funcionario;
import java.util.List;

public interface IFuncionarioDao extends IDao {

    public Funcionario buscarPorId(Integer id);

    public List buscarPorNome(String nome);
}
