package IDao;

import java.util.Date;
import java.util.List;

public interface IDao {

    public Object inserir(Object object);
    public Object inserirEscala(Date a,List b, List c);

    public Object alterar(Object object);

    public void excluir(Object object);

    public List listar();
}
