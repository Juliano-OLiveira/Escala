/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package IDao.escalaDao;

import Models.Funcionario;
import java.util.List;

/**
 *
 * @author oli
 */
public interface IEscalaDao  extends IDao.IDao{
      public Funcionario buscarPorId(Integer id);

    public List buscarPorNome(String nome);
    
}
