
package Models;

import Views.Funcionarios;

 
public class Funcionario {
    private   Integer id;
    private   String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
   

   

   

    public String getNome() {
        return nome;
    }

    public  void setNome(String  nome) {
        this.nome = nome;
    }
    public Object[] toVetor(){
    Object[] v = new Object[2];
    v[0] = this.id;
    v[1] = this.nome;
    return v;
    
    }

    @Override
    public String toString() {
        return "Funcionario{" + "id=" + id + ", nome=" + nome + '}';
    }
 
    
   
    
}
