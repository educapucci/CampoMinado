package dominio;

import java.io.Serializable; // import do serializable
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable {
    private int id;
    private String apelido;
    private String senha;
    private List<Jogo> jogos;
    private Recorde recorde;
    
    public Jogador() {
    }

    public Jogador(int id, String apelido, String senha) {
        this.id = id;
        this.apelido = apelido;
        this.senha = senha;
        this.jogos = new ArrayList<>(); //criando lista//
    }

    public int getId(){ 
        return id; 
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getApelido(){ 
        return apelido; 
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
    
    public String getSenha(){ 
        return senha; 
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void adicionarJogo(Jogo jogo){
        if (jogo != null) jogos.add(jogo);
    }

    public List<Jogo> getJogos(){
        //return List.copyOf(jogos);   //você retorna uma cópia imutável//
        return jogos;
    }

    public Recorde getRecorde() {
        return recorde;
    }

    public void setRecorde(Recorde recorde) {
        this.recorde = recorde;
    }

}

