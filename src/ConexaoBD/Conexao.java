package ConexaoBD;

import java.sql.*;

public class Conexao {

    private static String local;
    private static String user;
    private static String senha;
    private static Connection c;
    private static Statement statment;
    private static String str_conexao;
    private static String driverjdbc;
    private static Conexao conexao;

    public Conexao(String bd, String local, String porta,
            String banco, String user, String senha) {
        if (bd.equals("PostgreSql")) {
            setStr_conexao("jdbc:postgresql://" + local + ":" + porta + "/" + banco);
            setLocal(local);
            setSenha(senha);
            setUser(user);
            setDriverjdbc("org.postgresql.Driver");
        } else {
            if (bd.equals("MySql")) {
                setStr_conexao("jdbc:mysql://" + local + ":" + porta + "/" + banco);
                setLocal(local);
                setSenha(senha);
                setUser(user);
                setDriverjdbc("com.mysql.jdbc.Driver");
            }
        }
    }

    private Conexao() {
        iniciarConexao();
    }

    public void configUser(String user, String senha) {
        setUser(user);
        setSenha(senha);
    }

    public void configLocal(String banco) {
        setLocal(banco);
    }

    //Conexão com o Banco de Dados
    public static void conect() {
        try {
            Class.forName(getDriverjdbc());
            setC(DriverManager.getConnection(getStr_conexao(), getUser(), getSenha()));
            setStatment(getC().createStatement());
            // System.out.println("Banco conectado!!!");
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static void disconect() {
        try {
            getC().close();
        } catch (SQLException ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        try {
            return getStatment().executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // GETs AND SETs
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public static Connection getC() {
        return c;
    }

    public static void setC(Connection c) {
        Conexao.c = c;
    }

    public Statement getStatment() {
        return statment;
    }

    public static void setStatment(Statement statment) {
        Conexao.statment = statment;
    }

    public static String getStr_conexao() {
        return str_conexao;
    }

    public void setStr_conexao(String str_conexao) {
        this.str_conexao = str_conexao;
    }

    public static String getDriverjdbc() {
        return driverjdbc;
    }

    public static void setDriverjdbc(String driverjdbc) {
        Conexao.driverjdbc = driverjdbc;
    }

    public static void iniciarConexao() {
        Conexao c = new Conexao("PostgreSql", "localhost", "5432", "funcionarios", "postgres", "baguvix");
        c.conect();

    }

    public static void iniciarMysql() {
        Conexao c = new Conexao("MySql", "localhost", "3306", "funcionarios", "root", "toor");
        c.conect();

    }

    public static Conexao getInstance() throws SQLException {
        if (conexao == null) {
            conexao = new Conexao();
        }
        return conexao;
    }
}
