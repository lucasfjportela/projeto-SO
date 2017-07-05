package simuSO;

import memoria.*;
import clock.*;

public class MMU implements Memoria, IClockListener {
    private int tempoAtual = 0;
	private MemoriaVirtual memVirtual;
    private MemoriaFisica memFisica;
    
    public MMU(int pTamanhoMemoriaFisica, int pTamanhoMemoriaVirtual){
    	this.memFisica = new MemoriaFisica(pTamanhoMemoriaFisica);
    	this.memVirtual = new MemoriaVirtual(pTamanhoMemoriaVirtual);
    }

    @Override
	public void escrever(int pIndice, String pTipo) { // Aplicar o algoritmo WS nesse m�todo pra saber quem vai sair ou ser substituido
    	
		boolean testePresenca = this.memVirtual.getPagina(pIndice).presente();
		
		if (testePresenca == false) {
			PaginaVirtual pagina = memVirtual.getPagina(pIndice);
            int valor = 666;
            memFisica.setValor(pIndice, valor);
            pagina.modificar(true);
            pagina.referenciar(true);
            System.out.println("Escrevendo valor " + valor + " no indice " + pagina.getIndice() + " da memoria fisica!");
		} else {
			System.out.println("n�o t� na memoria fisica");
			int indiceLivre = memFisica.getIndice();
            System.out.println("P�gina livre : " + indiceLivre);
		}
	}

    @Override
	public void ler(int pIndice, String pTipo) {
    	try {
	    	if(this.memVirtual.getPagina(pIndice) == null) {
	    		return;
			} else {
				boolean t = this.memVirtual.getPagina(pIndice).presente();
				int pgFisica = this.memFisica.getValor(pIndice);
				
				if (t) {
					System.out.println("valor -> " + pgFisica);
				}
				else {
					System.out.println("n�o t� na memoria fisica");
				}
			}	
    	}catch (Exception e) {
			// TODO: handle exception
		}
	}   
    
    
    
    // Para zerar os bits de referenciado das p�ginas virtuais
    public void eventoZerarRecebido(int tempoClock){
    	for(int i = 0; i < memVirtual.getTamanho(); ++i){
    		memVirtual.getPagina(i).referenciar(false);
    	}
    	
    	this.tempoAtual = tempoClock;
    }    
}