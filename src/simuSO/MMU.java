package simuSO;

import memoria.*;

import java.util.Random;
import clock.*;

public class MMU implements Memoria, IClockListener {
    private int tempoAtual = 0;
	private MemoriaVirtual memVirtual = null;
    private MemoriaFisica memFisica = null;
    private MemoriaHD memoriaHD = null;
    
    public MMU(int pTamanhoMemoriaFisica, int pTamanhoMemoriaVirtual, MemoriaVirtual pMemoriaVirtual, MemoriaFisica pMemoriaFisica, MemoriaHD pMemoriaHD){
    	this.memFisica = pMemoriaFisica;
    	this.memVirtual = pMemoriaVirtual;
    	this.memoriaHD = pMemoriaHD;
    }

    @Override
	public void escrever(int pIndiceVirtual, int idProcesso) {

    	if(this.memVirtual.getPagina(pIndiceVirtual).presente() == false) {
    		Integer indiceLivre = this.memFisica.getIndiceLivre(this.memFisica.getMemoria());
    		
    		if(indiceLivre == null){
    					
				this.WS();
				
				Integer indiceLivreTemp = this.memFisica.getIndiceLivre(this.memFisica.getMemoria());
				this.memVirtual.getPagina(pIndiceVirtual).presenca(true);
	    		this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
	    		this.memVirtual.getPagina(pIndiceVirtual).modificar(true);
	    		this.memVirtual.getPagina(pIndiceVirtual).setMolduraPagina(indiceLivreTemp);
	    		this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(this.tempoAtual);
	    		
	    		Random r = new Random();
	    		Integer novoValor = r.nextInt(100);
	    		System.out.println("1-Valor escrito: " + novoValor);
	    		this.memFisica.setValor(indiceLivreTemp, novoValor);	
					
			} else {
				
				Integer indiceLivreTemp = this.memFisica.getIndiceLivre(this.memFisica.getMemoria());
				this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
    			this.memVirtual.getPagina(pIndiceVirtual).modificar(true);
    			this.memVirtual.getPagina(pIndiceVirtual).presenca(true);

    			this.memVirtual.getPagina(pIndiceVirtual).setMolduraPagina(indiceLivreTemp);
    			this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(tempoAtual);
    			Random r = new Random();
    			Integer valor = r.nextInt(100);
    			System.out.println("2-Valor Escrito: " + valor);
    			this.memFisica.setValor(indiceLivreTemp, valor);	
			}			
    		
		} else {	
					
			Integer molduraPagina = memVirtual.getPagina(pIndiceVirtual).getMolduraPagina();
			
			Random r = new Random();		
			Integer novoValor = r.nextInt(100);
			System.out.println("3-Valor escrito: " + novoValor);
			this.memFisica.setValor(molduraPagina, novoValor);
			this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
			this.memVirtual.getPagina(pIndiceVirtual).modificar(true);
			this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(this.tempoAtual);
		}
	}

    @Override
	public Integer ler(int pIndiceVirtual, int IDProcesso) {
    	Integer valorDoHD = new Integer(0);
    	
    	try {
    		if(this.memVirtual.getPagina(pIndiceVirtual).presente() == false){   		
	    		Integer indiceLivre = this.memFisica.getIndiceLivre(this.memFisica.getMemoria());
	
	    		if(indiceLivre != null){
	    			this.memVirtual.getPagina(pIndiceVirtual).presenca(true);
	    			this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
	    			this.memVirtual.getPagina(pIndiceVirtual).modificar(false);
	    			this.memVirtual.getPagina(pIndiceVirtual).setMolduraPagina(indiceLivre);
	    			this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(this.tempoAtual);
	    			valorDoHD = this.memoriaHD.getValorHD(pIndiceVirtual);
	    			this.memFisica.setValor(indiceLivre, valorDoHD);
	    			
	    		} else {
	    			
					this.WS();
					Integer molduraPagina = this.memFisica.getIndiceLivre(this.memFisica.getMemoria());
					this.memVirtual.getPagina(pIndiceVirtual).presenca(true);
		    		this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
		    		this.memVirtual.getPagina(pIndiceVirtual).setMolduraPagina(molduraPagina);
		    		this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(this.tempoAtual);
		    		valorDoHD = this.memoriaHD.getValorHD(pIndiceVirtual);
		    		this.memFisica.setValor(molduraPagina, valorDoHD);
	    		}
	    		
			} else {
	    		this.memVirtual.getPagina(pIndiceVirtual).presenca(true);
				this.memVirtual.getPagina(pIndiceVirtual).referenciar(true);
	   			this.memVirtual.getPagina(pIndiceVirtual).setTempoVirtualAtual(this.tempoAtual);
	   			valorDoHD = this.memFisica.getValor(this.memVirtual.getPagina(pIndiceVirtual).getMolduraPagina());				
			}
    	} catch (Exception e) {
			// TODO: handle exception
		}
		
    	return valorDoHD;
	}   
    
    private void WS() {
    	int t = 15000;
    	boolean marcada = false;
    	int idadePaginaAtual = 0;
    	int idadePaginaTemp = 0;
    	int iTemp = 0;
    	
    	for(int countPaginas = 0; countPaginas < this.memVirtual.getTamanho(); countPaginas++) {
    		
    		if(this.memVirtual.getPagina(countPaginas).presente() == false){
    			continue;
    		}
    		 
    		if(this.memVirtual.getPagina(countPaginas).referenciada() == true){
    			
    			this.memVirtual.getPagina(countPaginas).setTempoVirtualAtual(this.tempoAtual);
    			idadePaginaAtual = (this.tempoAtual - this.memVirtual.getPagina(countPaginas).getTempoVirtualAtual());
    			idadePaginaTemp = (this.tempoAtual - this.memVirtual.getPagina(iTemp).getTempoVirtualAtual());
    			
    			if(idadePaginaAtual >= idadePaginaTemp){
    				iTemp = countPaginas;	
    				marcada = true;
    			}
    			continue;
    		}
    		
    		if(this.memVirtual.getPagina(countPaginas).referenciada() == false && (this.tempoAtual - this.memVirtual.getPagina(countPaginas).getTempoVirtualAtual()) > t){
    			Integer moldura = this.memVirtual.getPagina(countPaginas).getMolduraPagina();
    			
    			if(this.memVirtual.getPagina(countPaginas).modificada() == true){
        			this.memoriaHD.swap(this.memFisica.getValor(moldura), countPaginas);
    			}
     
    			this.memFisica.setValor(this.memVirtual.getPagina(countPaginas).getMolduraPagina(), null);
    			this.memVirtual.getPagina(countPaginas).descartarPagina();
    			marcada = false;
    			break;
    		}
    		
    		if(this.memVirtual.getPagina(countPaginas).referenciada() == false && (this.tempoAtual - this.memVirtual.getPagina(countPaginas).getTempoVirtualAtual()) <= t){
    			idadePaginaAtual = (this.tempoAtual - this.memVirtual.getPagina(countPaginas).getTempoVirtualAtual());
    			idadePaginaTemp = (this.tempoAtual - this.memVirtual.getPagina(iTemp).getTempoVirtualAtual());
    			
    			if(idadePaginaAtual >= idadePaginaTemp){
    				iTemp = countPaginas;
    				marcada = true;
    			}
    			continue;
    		}
    		
    		if(marcada == true){
    			if(this.memVirtual.getPagina(iTemp).modificada() == true){
    				Integer moldura2Swap = this.memVirtual.getPagina(countPaginas).getMolduraPagina();
    				Integer valor = this.memFisica.getValor(moldura2Swap);
    				this.memoriaHD.swap(valor, countPaginas);
    			}
    			 
    			this.memFisica.setValor(this.memVirtual.getPagina(iTemp).getMolduraPagina(), null);
    			this.memVirtual.getPagina(countPaginas).descartarPagina();
    		}
    	}
    }    
   
    public void eventoZerarRecebido(int tempoClock){
    	this.tempoAtual = tempoClock;
    	for(int i = 0; i < this.memVirtual.getTamanho(); i++){
    		this.memVirtual.getPagina(i).referenciar(false);
    	}
    	System.out.println("- Tick de clock -");
    }    
}