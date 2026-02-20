package tools;

public class Cronometro {
    
    private long inicio;
    private long fim;        // tempo quando o jogo foi parado
    private boolean rodando; //indica se o cronometro esta ativo
    
    private Thread thread;   //executa algo em paralelo
    //private interfaceCronometro inter; //quem vai receber o tempo
    
    public Cronometro(){
        this.rodando = false;
        iniciar();
    }
    
    public boolean estaRodando(){
        return rodando;
    }
    
    public void iniciar(){
        if(rodando) return;
        
        if(!rodando){
            inicio = System.nanoTime(); //inicia o cronometro
            rodando = true;
        }
        
        thread = new Thread(() -> {
            while(rodando){
                //inter.aCadaSegundo(getTempoEmSegundos()); // apaga esse trem
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    break;
                }
            }
        });
        
        thread.start();
    }
    
    public void parar(){
        if(!rodando) return; //se nao estiver rodando, nao tem o que parar
        
        fim = System.nanoTime();
        rodando = false;
        thread.interrupt();
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
