
package Models;

import java.util.Date;


public class Escala {
    private int id;
    private Date data;
    private String manha;
    private String tarde;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getManha() {
        return manha;
    }

    public void setManha(String manha) {
        this.manha = manha;
    }

    public String getTarde() {
        return tarde;
    }

    public void setTarde(String tarde) {
        this.tarde = tarde;
    }
    
     
}
