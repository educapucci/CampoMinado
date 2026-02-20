package dominio;


public class Celula {
    private int id;
    private boolean revelada;
    private boolean marcacao;
    private boolean temBomba;
    private int minasAdjacentes;

    public Celula() {
    }

    public Celula(int id, boolean revelada, boolean marcacao, boolean temBomba, int minasAdjacentes) {
        this.id = id;
        this.revelada = revelada;
        this.marcacao = marcacao;
        this.temBomba = temBomba;
        this.minasAdjacentes = minasAdjacentes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isRevelada() {
        return revelada;
    }

    public void revelar() {
        this.revelada = true;
    }

    public boolean estaMarcada() {
        return marcacao;
    }

    public void setMarcacao(boolean marcacao) {
        this.marcacao = marcacao;
    }

    public boolean temBomba() {
        return temBomba;
    }

    public void setBomba(boolean bomba) {
        this.temBomba = bomba;
    }

    public int getMinasAdjacentes() {
        return minasAdjacentes;
    }

    public void setMinasAdjacentes(int minasAdjacentes) {
        this.minasAdjacentes = minasAdjacentes;
    }

    @Override
    public String toString() {
        return "Celula{" + "id=" + id + ", revelada=" + revelada + ", marcacao=" + marcacao + ", temBomba=" + temBomba + ", minasAdjacentes=" + minasAdjacentes + '}';
    }
}

