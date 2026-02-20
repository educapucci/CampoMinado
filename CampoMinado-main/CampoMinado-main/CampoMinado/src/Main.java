import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import tools.Cronometro;
import dominio.*; // importa TUDO do package "dominio"


public class Main {
    
    static Scanner s = new Scanner(System.in);
    static ArrayList<Jogador> jogadores = new ArrayList<>();
    static ArrayList<Recorde> rankingTop10 = new ArrayList<>();
    

    static final String ArquivoBanco = "saves.dat"; //nome do arquivo onde tudo sera salvo
    static final String ArquivoRanking = "ranking.dat";

    public static void main(String args[]){
        carregarDados();
        carregarRanking();
        
        int escolha = 0;
        
        System.out.println("--- BEM VINDO AO CAMPO MINADO ---");
        do{
            System.out.println("------------------");
            System.out.println("1- Criar novo usuario");
            System.out.println("2- Fazer login");
            System.out.println("3- Ver Ranking (Top 10)");
            System.out.println("4- Sair do programa");
            System.out.println("------------------");
            
            try{
                escolha  = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Digite uma opcao valida: ");
                continue;
            }
            
            switch(escolha){
                case 1 -> {
                    cadastrarUsuario();
                }
                case 2 -> {
                    try{
                        Jogador jogadorLogado = realizarLogin();
                        menuUsuario(jogadorLogado);
                    } catch (Exception e){
                        System.out.println("Falha no login! " + e.getMessage());
                    }
                }
                case 3 -> {
                    top10();
                }
                case 4 -> {
                    System.out.println("Encerrando programa...");
                }
                default -> {
                    System.out.println("Opcao invalida!");
                }
            }
        }while(escolha!=4);
    }


    public static void menuUsuario(Jogador j){
        System.out.println("--------------------");
        System.out.println("Seja bem vindo, " + j.getApelido().toUpperCase() + "!");
        System.out.println("--------------------");
        
        int escolha =0;
        do{
            System.out.println("\n--- Ola, " + j.getApelido().toUpperCase() + " ---");
            System.out.println("1. Jogar Campo Minado");
            System.out.println("2. Ver meu recorde pessoal");
            System.out.println("3. Deslogar");
            System.out.print("Escolha: ");

            try {
                escolha = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) { continue; }

            switch (escolha) {
                case 1 -> {
                    // CORPO DO JOGO
                    iniciarPartida(j);
                } case 2 -> {
                    try {
                        System.out.println("Seu melhor tempo: " +
                            (j.getRecorde().getTempo() == Integer.MAX_VALUE ? "Sem jogos" : j.getRecorde().getTempo() + "s"));
                    } catch (Exception e) {
                        System.err.println("Erro ao carregar melhor tempo: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("Fazendo logout...");
                }
            }
        } while (escolha != 3);
    }


    public static void iniciarPartida(Jogador jogadorLogado) {
        System.out.println("\n--- CONFIGURAR JOGO ---");
        System.out.println("Selecione a dificuldade (1 a 4): ");
        System.out.print("Digite um numero: ");

        int dificuldade = 8; // valor padrão
        try {
            dificuldade = Integer.parseInt(s.nextLine());
        } catch(Exception e) {
            System.out.println("Valor invalido, usando tamanho 8.");
        }

        Jogo jogo = new Jogo(1, jogadorLogado, dificuldade); // ver o id depois
        jogo.getCampo().criarCampo();

        int qntdDeLances = 0;
        boolean condicao = false;
        boolean venceu = false;

        //long tempoInicio = System.currentTimeMillis();
        
        Cronometro cronometro = new Cronometro(); // inicia automaticamente

        do {
            exibirCampo(jogo.getCampo());
            System.out.println("\nTabuleiro [" + jogo.getCampo().getTamanho() + "x" + jogo.getCampo().getTamanho() + "]\n");
            System.out.println("Revelar: [1]\nMarcar: [2]");
            System.out.println("Dar lance: (coordenadas [x, y] e acao [1 ou 2])");

            try {
                int x = (s.nextInt() - 1); // Linha
                int y = (s.nextInt() - 1); // Coluna

                // esse " - 1 " é pra ajustar as variáveis x, y aos indexes do array

                int acao = s.nextInt();  // 1 ou 2
                s.nextLine();
                
                if (qntdDeLances == 0) {
                    jogo.getCampo().colocarBombas(x, y); // primeiro clique está seguro
                    jogo.getCampo().calcularMinasAdjacentes(); // vem primeiro
                }

                if (acao == 1) {
                    condicao = jogo.condicaoDeFinal(jogo.verificarLance(x, y));

                    if (condicao) {
                        System.out.println("BOOM! Voce acertou uma mina.");
                        jogo.getCampo().revelarTodasAsBombas();
                        exibirCampo(jogo.getCampo());
                        venceu = false;
                        cronometro.parar(); // pare o cronometro
                    } else {
                        jogo.lance(x, y, acao);
                        if (jogo.condicaoDeFinal(false)) {
                            condicao = true;
                            venceu = true;
                        }
                    }
                } else {
                    jogo.lance(x, y, acao);
                }
                qntdDeLances++;

            } catch (Exception e) {
                System.out.println("Entrada invalida! Tente numeros.");
            }
        } while (!condicao);

        if (venceu) {
            //COLOCAR O CRONOMETRO!
            cronometro.parar();
            
            Recorde temp = new Recorde(jogadorLogado.getApelido(), cronometro.getTempoEmSegundos()); // tempo do jogo

            System.out.println("\n--------------------------------");
            System.out.println("PARABENS! VOCE VENCEU!");
            System.out.println("Tempo total: " + cronometro.getTempoEmSegundos() + " segundos.");
            System.out.println("--------------------------------\n");            

            // 1. Tenta atualizar recorde pessoal
            try {
                // se for a primeira vez OU bateu o tempo antigo, então...
                if (jogadorLogado.getRecorde() == null || temp.compareTo(jogadorLogado.getRecorde()) < 0 /* retorna -1 se o da equerda for menor que o da direita */) {
                    System.out.println(">>> NOVO RECORDE PESSOAL! <<<");
                    jogadorLogado.setRecorde(temp); // bota o recorde lá
                    salvarDados();
                    
                    if (verificaRecorde(jogadorLogado.getRecorde().getTempo())) // ve se o recorde dá pra entrar no ranking
                        verificaRanking(jogadorLogado.getRecorde());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("\nGAME OVER! Mais sorte na proxima.");
        }
    }

    public static void exibirCampo(Campo c){
        System.out.println();
        int tamanho = c.getTamanho();
        Celula[][] tabuleiro = c.getTabuleiro();

        for(int i=0; i<tamanho; i++){
            if (i >= 0 && i < 9)
                System.out.print("0" + (i+1) + "  "); // Número da linha com 0 à esquerda
            else
                System.out.print((i+1) + "  "); // Número da linha

            for(int j=0; j<tamanho; j++){
                if (!tabuleiro[i][j].isRevelada()) {
                    if (tabuleiro[i][j].estaMarcada()) {
                        System.out.print("P ");
                    } else {
                        System.out.print("# ");
                    }
                } else {
                    if (!tabuleiro[i][j].temBomba())
                        System.out.print(tabuleiro[i][j].getMinasAdjacentes() + " ");
                    else System.out.print("* ");
                }
                if (j != tamanho - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //ARQUIVO
    //pega o arraylist e joga no arquivo
    public static void salvarDados(){
        try(ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(ArquivoBanco))){ // isso aqui tá certo
            obj.writeObject(jogadores);
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e){
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static void carregarDados(){
        File arquivo = new File(ArquivoBanco);

        if(!arquivo.exists()){ //se o arquivo nao existe, nao faz nada
            return;
        }

        try(ObjectInputStream o = new ObjectInputStream(new FileInputStream(arquivo))){
            jogadores = (ArrayList<dominio.Jogador>) o.readObject();
        } catch (Exception e){
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    //USUARIO
    public static void cadastrarUsuario(){
        
        System.out.println("--- Cadastro ---");
        System.out.print("Digite o nome: ");
        String nome = s.nextLine();
        
        if(nomeDisponivel(nome)){
            System.out.print("Digite uma senha: ");
            String senha = s.nextLine();

            jogadores.add(new Jogador(1, nome, senha)); //adiciona na lista de jogadores
            System.out.println("Jogador criado!");

            salvarDados();
        } else{
            System.out.println("ERRO: Este nome ja esta em uso!");
        }
    }
    
    public static Jogador realizarLogin() throws Exception{
        System.out.println("------------------");
        System.out.println("--- Login ---");
        System.out.print("Usuario: ");
        String nome = s.nextLine();
        System.out.print("Senha: ");
        String senha = s.nextLine();
        
        for(Jogador j : jogadores){
            if(j.getApelido().equals(nome)){ //se achar o nome na lista
                if(verificaSenha(nome, senha)){ //se a senha bater
                    return j; //retorna o objeto
                }
                else{
                    throw new Exception ("Senha incorreta!"); //se a senha nao bater
                }
            }
        }
        throw new Exception ("Usuario nao encontrado!");
    }
    
    public static boolean nomeDisponivel(String nome){ //verificar se o jogador existe ou nao
        for(Jogador j : jogadores)
            if(j.getApelido().equals(nome)) 
                return false;//encontrou, nome nao disponivel
        
        return true;  //se nao achou na lista, nome disponivel
    }
    
    public static boolean verificaSenha(String nome, String senha){
        for(Jogador j : jogadores){
            if(j.getApelido().equals(nome)){
                if(j.getSenha().equals(senha)){
                    return true; //senha bateu
                } else {
                    return false; //senha NAO bateu
                }
            }
        }
        return false;
    }

    //RANKING TOP 10

    public static void verificaRanking(Recorde recorde){
        carregarRanking();

        Recorde novoRecorde = recorde;

        rankingTop10.add(novoRecorde);

        Collections.sort(rankingTop10);

        //se a lista estiver com mais de 10 tempos, exclui 1
        while(rankingTop10.size() > 10){
            rankingTop10.remove(rankingTop10.size() -1);
        }

        if(rankingTop10.contains(novoRecorde)){
            System.out.println("Parabens! Voce entrou para o TOP10!");
            salvarRanking();
        }
    }

    public static void salvarRanking(){
        try(ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(ArquivoRanking))){
            o.writeObject(rankingTop10);
        } catch (IOException e){
            System.out.println("Erro ao salvar ranking: " + e.getMessage());
        }
    }

    public static void carregarRanking(){
        File f = new File("ranking.dat");
        if(!f.exists()) return;

        try(ObjectInputStream o = new ObjectInputStream(new FileInputStream(f))){
            rankingTop10 = (ArrayList<Recorde>) o.readObject();
        } catch (Exception e){
            System.out.println("Erro ao carregar o ranking!");
        }
    }
    public static void top10(){
        if(jogadores.isEmpty()){
            System.out.println("Nenhum recorde registrado ainda!");
            return;
        }
        System.out.println("---------------------------------");
        System.out.println("--- RANKING DE MELHORES TEMPOS ---");

        int posicao = 1;
        for(Recorde r : rankingTop10){
            System.out.println(posicao + "º Lugar: " + r.getNome() + " - " + r.getTempo() + " segundos");
            posicao++;
        }
        System.out.println("---------------------------------");
    }

    public static boolean verificaRecorde(long tempo){
        for(Recorde r : rankingTop10){
            if(tempo < r.getTempo()){
                return true; //verdadeiro, o tempo esta dentro do top 10
            }
            else return false;
        }
        return false;
    }
}
