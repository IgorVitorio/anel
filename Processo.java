package anel;

import java.util.LinkedList;
/**
 * Classe que trata os servidores simulados
 */
public class Processo {
	
	private int pid;
	private boolean ehCoordenador;
	
	public Processo (int pid){
		setPid(pid);
	}
	
	public Processo (int pid, boolean ehCoordenador){
		setPid(pid);
		setEhCoordenador(ehCoordenador);
	}
	
	public boolean isEhCoordenador () {
		return ehCoordenador;
	}

	public void setEhCoordenador (boolean ehCoordenador) {
		this.ehCoordenador = ehCoordenador;
	}

	public int getPid () {
		return pid;
	}
	
	public void setPid (int pid) {
		this.pid = pid;
	}
	
	public boolean enviarRequisicao () {
		boolean resultadoRequisicao = false;
		for (Processo p : Anel.processosAtivos) {
			if (p.isEhCoordenador())
				resultadoRequisicao = p.receberRequisicao(this.pid);
		}
		
		// Se nao existe um coordenador.
		if (!resultadoRequisicao)
			this.realizarEleicao();
		
		System.out.println("Fim da requisicao.");
		return resultadoRequisicao;
	}
	
	private boolean receberRequisicao (int pidOrigemRequisicao) {
		
		/* TRATAMENTO DA REQUISICAO AQUI... */
		
		System.out.println("Requisicao do servidor " + pidOrigemRequisicao + " recebida com sucesso.");
		return true;
	}
	
	private void realizarEleicao () {
		
		System.out.println("Processo de eleicao iniciado");
		
		// Primeiro consulta cada processo, adicionando o id de cada um em uma nova lista.
		
		LinkedList<Integer> idProcessosConsultados = new LinkedList<>();
		for (Processo p : Anel.processosAtivos)
			p.consultarProcesso(idProcessosConsultados);
		
		// Depois percorre a lista de id's procurando pelo maior.
		
		int idNovoCoordenador = this.getPid();
		for (Integer id : idProcessosConsultados) {
			if (id > idNovoCoordenador)
				idNovoCoordenador = id;
		}
		
		// E entao atualiza o novo coordenador.
		
		boolean resultadoAtualizacao = false;
		resultadoAtualizacao = atualizarCoordenador(idNovoCoordenador);
		
		if (resultadoAtualizacao)
			System.out.println("Eleicao concluida com sucesso! O novo líder é: " + idNovoCoordenador + ".");
		else
			System.out.println("A eleicao falhou. Nao foi encontrado um novo líder.");
	}
	
	private void consultarProcesso (LinkedList<Integer> processosConsultados) {
		processosConsultados.add(this.getPid());
	}
	
	private boolean atualizarCoordenador (int idNovoCoordenador) {
		// Garante que nao exista nenhum outro processo cadastrado como coordenador,
		// a nao ser o novo coordenador.
		
		for (Processo p : Anel.processosAtivos) {			
			if (p.getPid() == idNovoCoordenador)
				p.setEhCoordenador(true);
			else
				p.setEhCoordenador(false);
		}
		
		return true;
	}
}