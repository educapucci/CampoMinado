package dominio;

public class Campo {
    private int escolha;
    private Celula[][] tabuleiro;
    private int tamanho;
    private int qntBombas;
    private int totaldecasas;
    private double percentual;
        
    public Campo(int escolha){
        this.escolha=escolha;

        if(escolha==1){
            tamanho = 9;
            totaldecasas = tamanho*tamanho;
            percentual = 0.10;
            qntBombas = Math.max(1,(int)Math.ceil(totaldecasas*percentual)); 
            //a linha acima exige que o num minimo de bombas seja 1, e arredonda o percentual*qntcasas para cima
            //ex, se essa conta resultar em 0.5, o num de bombas sera = 1.
            //ex2, 49 casas, 12% x 49 = 5.88, arredonda para 6 
        }
        if(escolha==2){
            tamanho = 12;
            totaldecasas = tamanho*tamanho;
            percentual = 0.12;
            qntBombas = Math.max(1,(int)Math.ceil(totaldecasas*percentual)); 
        }
        if(escolha==3){
            tamanho = 15;
            totaldecasas = tamanho*tamanho;
            percentual = 0.14;
            qntBombas = Math.max(1,(int)Math.ceil(totaldecasas*percentual)); 
        }
        if(escolha==4){
            tamanho = 18;
            totaldecasas = tamanho*tamanho;
            percentual = 0.16;
            qntBombas = Math.max(1,(int)Math.ceil(totaldecasas*percentual)); 
        }
    }
    
    public int getTamanho(){
        return tamanho;
    }
    
    public int getQntBombas(){
        return qntBombas;
    }
    
    public int getEscolha(){
        return escolha;
    }
    
    public double getPercentual(){
        return percentual;
    }
    
    public int getTotalDeCasas(){
        return totaldecasas;
    }

    public Celula[][] getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(Celula[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    public boolean condicaoDeExistencia(int x, int y) { // é bem auto explicativo
        return x >= 0 && x < tamanho && y >= 0 && y < tamanho;
    }
    
    public void colocarBombas(int x, int y){ // x e y são o quadrado em branco do inicio
        int bombasColocadas = 0;
        while(bombasColocadas < qntBombas){
            int i = (int) (Math.random() * tamanho); 
            int j = (int) (Math.random() * tamanho); 

            //na logica antiga, nao era permitido colocar bombas na linha e coluna do primeiro click, mas logica do jogo nao deve-se colocar bomba apenas na posicao [x][y] do click
            //se m[i][j] == 0 (nao bomba) && NAO for i==x && j==y 
            
            if (!tabuleiro[i][j].temBomba() && (i!=x || j!=y)) {
                tabuleiro[i][j].setBomba(true);
                bombasColocadas++;
            }
        }
    }
    
    /*
    Math.random = 0.0 a 1.0 (aleatoriamente) * tamanho e depois transforma para int
    a linha acima mostra que uma posicao [i][j] esta sendo escolhida aleatoriamente
    se o num sorteado for:
    i =0.5 * 9(ex de tamanho) = 4.5 = 4
    j = 0.3 * 9 = 2.7 = 2
    posicao [4][2]
    */
    
    
    public void exibirCampo(){
        for(int i=0; i<tamanho;i++){
            for(int j=0; j<tamanho; j++){
                System.out.print(tabuleiro[i][j].getMinasAdjacentes() + " ");
            }
            System.out.println();
        }
    }
    
    public void revelarCelulas(){
        System.out.println("Tabuleiro [i][j]: ");
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<tamanho; j++){
                System.out.print("[" + String.format("%02d", i) +"] [" +
                        String.format("%02d", j) + "]");
            }
        }
    } 
    
    /*String format = monta strings formatadas
    String.format("%02d", i) = %d(inteiro / 2=largura minima / 0= preenche com 0 a esquerda
    ex = String.format("%02d", 3);   // "03"
    ex2 = String.format("%02d", 12);  // "12"
    */
    
    public void criarCampo() {
        tabuleiro = new Celula[tamanho][tamanho];
        int cont = 0; // id de cadacelula
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<tamanho; j++){
                tabuleiro[i][j] = new Celula(cont++, false, false, false, 0);
            }
        }
    }
    
    public void calcularMinasAdjacentes() { 
        for(int i=0; i<tamanho; i++){
            for(int j=0; j<tamanho; j++){
                if (!tabuleiro[i][j].temBomba()) {
                    int numero = 0;
                    // linha superior do lado esquerdo até a linha inferior da coluna mais a direita
                    //
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (condicaoDeExistencia(x, y) // condicao de posicao dentro do tabuleiro
                                && tabuleiro[x][y].temBomba()) { // se for mina, o numero da celula mapeada em questão aumenta
                                numero++; //num de minas
                            }
                        }
                    }
                    
                    tabuleiro[i][j].setMinasAdjacentes(numero); //passa a qnt de minas
                }
            }
        }
    }
    
    public void abrir(int x, int y) {
        //estava aqui
        if (!condicaoDeExistencia(x, y)) return; //validacao da posicao inicial (garante que x e y existem na matriz)
            
        Celula c = this.tabuleiro[x][y]; //troquei de lugar, agora so pega-se a posicao atual do tabuleiro, se for uma posicao valida

        if(c.isRevelada() || c.estaMarcada() || c.temBomba()) return; 
        
        this.tabuleiro[x][y].revelar(); //revela a celula atual uma vez
        
        //se a celula tiver minas adjacentes, para, revelando somente ela
        //se a celula nao tiver minas adjacentes, continua o processo
      
        if(c.getMinasAdjacentes() == 0){
            for(int i = x-1; i <= x+1; i++){
                for(int j = y-1; j <= y+1; j++){
                    if(condicaoDeExistencia(i, j))
                        abrir(i, j); //chamada recursiva (espalha a abertura)
                }        
            }
        }  
    }
    
    /*logica da funcao acima
    passa a posicao x e y do clique
    verifica a condicao de existencia (se a posicao esta dentro da matriz)
    cria uma celula c temporaria que armazena a posicao do clique
    verifica que a celula esta revelada, marcada ou tem bomba, se sim, retorna
    se nao, revela a posicao do clique
    se nao tem mina adjacente -> entra nos for e verifica a condicao das celulas em volta, exemplo:
    11 12 13
    21 22 23
    31 32 33
    se o clique foi na casa 22, os for irao verificar todas as casas em torno daquela posicao
    se tem minas adjacentes, apenas aquela casa sera revelada e encerra.
    verifica se as posicoes testadas estao dentro do tabuleiro (condicao de existencia)
    se atender a tudo, chama recursivamente a propria funcao
    */
    
    public void revelarTodasAsBombas() {
        for(int i=0; i<tamanho; i++)
            for(int j=0; j<tamanho; j++)
                if (this.tabuleiro[i][j].temBomba())
                    this.tabuleiro[i][j].revelar(); // tá auto explicativo
    }
}