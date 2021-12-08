package Controller;

import ConexaoBD.Conexao;
import Factory.FuncionarioDaoFactory;
import IDao.funcionarioDao.IFuncionarioDao;
import Models.Funcionario;
import ViewPrincipal.TelaPrincipal;
import Views.Cadastro;
import static Views.Cadastro.codigo;
import static Views.Cadastro.funcionario;
import static Views.Funcionarios.dados;
import static Views.Funcionarios.tabela;
import javax.swing.JOptionPane;

public class ContollerFuncionarios {

    private static FuncionarioDaoFactory factory;
    private static IFuncionarioDao dao;

    public static void addPessoa_altPessoa() {
        factory = new Factory.FuncionarioDaoFactory();
        dao = (IFuncionarioDao) factory.createObject();

        try {

            boolean vazio = "".equals(codigo.getText());
            if (vazio == true) {
                Integer id = !vazio ? Integer.parseInt(codigo.getText()) : null;
                funcionario.setId(id);
                funcionario.setNome((String.valueOf(Cadastro.nomeFuncionario.getText())));

                dao.inserir(funcionario);

                JOptionPane.showMessageDialog(null, "Dados Salvo com sucesso", "Dados", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("\nPessoa Adicionada no Banco de Dados\n");
            } else {
                Integer id = Integer.valueOf(Cadastro.codigo.getText());

                funcionario.setId(id);
                funcionario.setNome(Cadastro.nomeFuncionario.getText());
                dao.alterar(funcionario);

                JOptionPane.showMessageDialog(null, "Alterado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            }
            dao.listar();

            //Pronto aqui ele já inseriu no banco.
        } catch (Exception ex) {
            System.out.println("Erro: " + ex);
        }
    }

    public static void excluirBD() {

        try {
            int row = tabela.getSelectedRow();

            if (row != -1) {

                Funcionario funci = new Funcionario();
                Object obj = tabela.getValueAt(row, 0);
                Integer id = (Integer) obj;
                funci.setId(id);
                dao.excluir(funci);
                JOptionPane.showMessageDialog(null, Cadastro.funcionario.getNome() + " excluido com Susseso");

                Conexao.disconect();
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um item na Lista para Excluir", "Aviso", JOptionPane.WARNING_MESSAGE);

            }
            dao.listar();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void preencherCamposCadastro() {
        int row = tabela.getSelectedRow();
        Funcionario funcionario = Cadastro.getFunc();
        if (row != -1) {
            System.out.println("Preenchendo a tabela -->");

            funcionario.setId((Integer) dados.getValueAt(row, 0));
            funcionario.setNome((String) dados.getValueAt(row, 1));

            System.out.println(funcionario.getId() + " " + funcionario.getNome());

            Cadastro.codigo.setText(funcionario.getId().toString());
            Cadastro.nomeFuncionario.setText(funcionario.getNome());

            TelaPrincipal.selectAba(0);

        } else {
            JOptionPane.showMessageDialog(null, "Você de selecionar um item na lista", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

    }

}
