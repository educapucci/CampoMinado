package dominio;

public class Jogo {

    private int id;
    private Campo campo;
    private Jogador jogador;
    private int pontuacao;
    private int dificuldade;

    public Jogo() {
    }

    public Jogo(int id, Jogador jogador, int dificuldade) {
        this.id = id;
        this.campo = new Campo(dificuldade);
        this.dificuldade = dificuldade;
        this.jogador = jogador;
        this.pontuacao = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return "Jogo{" + "id=" + id + ", campo=" + campo + ", jogador=" + jogador + ", pontuacao=" + pontuacao + ", dificuldade=" + dificuldade + '}';
    }
    
    public void lance(int x, int y, int escolha) {
        switch (escolha) {
            case 1 -> {
                // se não tem bandeira
                if (!this.getCampo().getTabuleiro()[x][y].estaMarcada()) {                
                    this.getCampo().abrir(x, y); // revelar e por sorte em cascata
                }
                
            } case 2 -> {
                if (!this.getCampo().getTabuleiro()[x][y].estaMarcada()) // se a célula não está marcada
                    this.getCampo().getTabuleiro()[x][y].setMarcacao(true); // marcar com bandeira
                else 
                    this.getCampo().getTabuleiro()[x][y].setMarcacao(false); // se não, desmarca
            }
        }
    }
    

    public boolean verificarLance(int x, int y) {
        boolean res = false;
        
        if (!this.getCampo().getTabuleiro()[x][y].isRevelada()                  
            && this.getCampo().getTabuleiro()[x][y].temBomba())
                res = true;
        
        return res; // retorna true se a célula escondida tiver bomba
    }
    
    public boolean condicaoDeFinal(boolean condicao) {
        boolean res = false;
        if (condicao) {
            System.out.println("Fim de jogo!");
            res = true;
        } else if (!condicao) {
            
            int tamanho = this.getCampo().getTamanho();
            int bombas = this.getCampo().getQntBombas();
            
            int soma = 0;
            int total = (tamanho * tamanho) - bombas;
            
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    if (!this.getCampo().getTabuleiro()[i][j].temBomba() && this.getCampo().getTabuleiro()[i][j].isRevelada()) {
                        soma++;
                    }
                }
            }
            
            if (soma == total) {
                System.out.println("Vitória");
                res = true;
            }
        }
        return res;
    }

    
}

