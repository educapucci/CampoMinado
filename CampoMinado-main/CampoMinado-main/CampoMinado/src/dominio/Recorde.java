package dominio;

import java.io.Serializable;

public class Recorde implements Serializable, Comparable<Recorde> {

    private String nomeJog;
    private long tempo;

    public Recorde(String nome, long tempo) {
        this.nomeJog = nome;
        this.tempo = tempo;
    }

    public String getNome() { return nomeJog; }
    public long getTempo() { return tempo; }

    @Override
    public String toString() {
        return nomeJog + " - " + tempo + "s";
    }

    @Override
    public int compareTo(Recorde outro) {
        return Long.compare(this.tempo, outro.tempo);
    }
}