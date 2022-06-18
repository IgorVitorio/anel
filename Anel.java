package anel;

import java.util.ArrayList;
import java.util.Random;

public class Anel {

	private final int ADICIONA = 3000;
	private final int REQUISICAO = 2500;

	public static ArrayList<Processo> processosAtivos;
	private final Object lock = new Object();

	public Anel() {
		processosAtivos = new ArrayList<Processo>();
	}

	public void criaProcessos () {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (processosAtivos.size() < 5 ) {
					synchronized (lock) {
						if (processosAtivos.isEmpty()) {
							processosAtivos.add(new Processo(1, false));
						} else if (processosAtivos.size() < 5 ){
							processosAtivos.add(new Processo(
									processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
						}
						System.out.println("Servidor " + processosAtivos.get(processosAtivos.size() - 1).getPid() + " criado.");
					}

					try {
						Thread.sleep(ADICIONA);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void fazRequisicoes () {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(REQUISICAO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					synchronized (lock) {
						if (processosAtivos.size() > 3) {
							int indexProcessoAleatorio = new Random().nextInt(processosAtivos.size());
														
							Processo processoRequisita = processosAtivos.get(indexProcessoAleatorio);
							System.out.println("Servidor " + processoRequisita.getPid() + " faz requisicao.");
							processoRequisita.enviarRequisicao();
						}
					}
				}
			}
		}).start();
	}
}
