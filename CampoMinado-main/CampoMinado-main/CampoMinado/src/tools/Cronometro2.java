package tools;

public class Cronometro2 {
    //funciona, desde que: o jogo nao trave, rode sem esperar uma resposta do usuario
    //exige loop ativo, e que o codigo rode constantemente
    
    private long inicio;
    private long fim;        // tempo quando o jogo foi parado
    private boolean rodando; //indica se o cronometro esta ativo
    
    private long ultimoSegundoImpresso; //controle do intervalo de 1 segundo
    
    public boolean estaRodando(){
        return rodando;
    }
    
    public void iniciar(){
        if(rodando) return;
        
        inicio = System.nanoTime(); //inicia o cronometro
        rodando = true;
        ultimoSegundoImpresso = inicio;
        
    }
    
    public boolean passouUmSegundo(){
        if(!rodando) return false;
        
        long agora = System.nanoTime();
        
        //se a diferenca entre o tempo de agora e o ultimo seg for maior ou igual a
        //1bi nanosseguno = 1 segundo -> atualiza
        if(agora - ultimoSegundoImpresso >= 1_000_000_000){
            ultimoSegundoImpresso = agora;
            return true;
        }
        
        return false;
    }
    
    public void parar(){
        if(!rodando) return; //se nao estiver rodando, nao tem o que parar
        
        fim = System.nanoTime();
        rodando = false;

    }
    
    public long getTempoEmSegundos(){
        long tempoFinal = System.nanoTime();
        if(rodando) tempoFinal = System.nanoTime();
        else tempoFinal = fim;
        
        return (tempoFinal - inicio)/1_000_000_000;
    }
    //o metodo acima precisa ser dividido por 1bi pois System.nanoTime() trabalha com nanossegundos
    
    
    public String getTempo(){
        long totalSeg = getTempoEmSegundos();
        long min = totalSeg / 60;
        long segundos = totalSeg % 60;
        
        return String.format("%02d:%02d", min, segundos);
    }
    
    public void getContaSeg(){
        
    }
    
    public void resetar(){
        rodando = false;
        inicio = 0;
        fim = 0;
    }
}



/*
Como sera usado na pratica:
if (cronometro.passouUmSegundo()) {
    System.out.println(cronometro.getTempo());
}
*/