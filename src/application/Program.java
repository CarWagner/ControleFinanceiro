package application;

import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import dao.ContaDAO;
import entities.Conta;
import entities.Contas;
import entities.Despesa;
import entities.Receita;
import entities.enums.TipoConta;
import entities.enums.TipoDespesa;
import entities.enums.TipoReceita;

/**
 * @author Carlos Wagner Barreto de Oliveira Gomes
 *
 */
public class Program {

	
	private static Scanner sc = new Scanner(System.in);
	private static String opcao = "";
	private static int opcaoEscolhida = -1;
	private static Contas contas = new Contas();
	
	private static String nomeUsuario = "";
	private static String idadeUsuario = "";
	private static int idadeUsu = 0;
	private static String cpfUsuario = ""; 
	
	private static Conta novaConta = new Conta(); 
	private static TipoConta tipoConta = null; 
	private static TipoReceita tipoReceita = null;
	private static TipoDespesa tipoDespesa = null;
	private static String instituicaoFinanceira = ""; 
	private static String descricaoTransferencia = "";
	private static String custo;
	private static double custoTransferencia = -1;
	private static Double valorTransferencia = null;
	private static boolean saldoNegativo = false; 
	private static String cpf1;
	private static String cpf2;
	private static long eNumero;
	private static ContaDAO contaDao = new ContaDAO();
	
	/**
	 * Página de inicio
	 */
	private static void paginaInicial() {
		do {
			
			System.out.println();
			System.out.println("---------------------");
			System.out.println("   Sessão Iniciada");
			System.out.println("---------------------");
			System.out.println();
	
			System.out.println("1. Cadastrar uma nova conta.");
			System.out.println("2. Editar conta.");
			System.out.println("3. Listar contas.");
			System.out.println("4. Transferir saldo entre contas.");
			System.out.println("0. Sair do Programa.");
			
			do {
				System.out.println();
				System.out.print("Insira o número da operação: ");
				opcao = sc.nextLine();
				
				try {
					// Transferir o valor que o usuario adicionou para um inteiro
					opcaoEscolhida = Integer.parseInt(opcao);
				} catch(Exception e) {
					System.out.println("Escolha uma opção "
							+ "para dar andamento a operação!");
				}
				// Dá continuidade ao processo caso ocorra algum erro
			} while(opcaoEscolhida < 0 || opcaoEscolhida > 4 || opcao.isEmpty());
			
			switch (opcaoEscolhida) {
				case 1: // Cria uma nova conta
					if(introduzirConta()) {
						
						// Criar uma conta;
						novaConta.setNome(nomeUsuario);
						novaConta.setIdade(idadeUsu);
						novaConta.setTipoConta(tipoConta);
						novaConta.setInstituicaoFinanceira(instituicaoFinanceira);
						contaDao.inserir(novaConta);
						
						// Adicionar a um array de contas
						contas.addConta(novaConta);
						
						menuUsuario();
						
					}	
					break;
				case 2: // Editar uma conta
					paginaEditarConta();
					break;
				case 3: // Listar as contas
					if(contas.getContas().isEmpty()) {
						System.out.println("Conta inexistente.");
					}
					for(Conta conta : contas.getContas()) {
						System.out.println(conta.toString());
					}
					break;
				case 4: // Transferir saldo entre contas
					if(contas.getContas().isEmpty() || contas.getContas().size() < 2) {
						System.out.println("Conta inexistente.");
					} else {
						do {
							System.out.print("Insira o CPF de origem: ");
							cpf1 = sc.nextLine();
						} while(String.valueOf(cpf1).toString().length() != 11);
						do {
							System.out.print("Insira o CPF de destino: ");
							cpf2 = sc.nextLine();
						} while(String.valueOf(cpf2).toString().length() != 11);
						do {
							System.out.print("Insira o valor desejado: ");
							try {
								
								valorTransferencia = sc.nextDouble();
								
							} catch(Exception e) {
								
								System.out.print("O valor da tranferência só pode ser número. ");
								
							}
						} while(valorTransferencia == null);
						if(cpf1.equals(cpf2)) {
							System.out.print("O CPF de origem deve ser diferente do CPF de destino.");
							break;
						}
						
						for(Conta conta : contas.getContas()) {
							if(conta.getCpf().equals(cpf1)) {
								conta.addDespesa("Transferência de" + cpf2, valorTransferencia, new Date(), TipoDespesa.OUTROS);
							}
							if(conta.getCpf().equals(cpf2)) {
								conta.addReceita("Transferência de" + cpf1, valorTransferencia, new Date(), TipoReceita.OUTROS);
							}
						}
					}
					break;
				case 0: // Finalizar o Programa
					System.out.println("Sessão Finalizada\nAgradecemos a Preferência!");
					break;	
			}
			
		} while(opcaoEscolhida != 0);
	}
	
	/**
	 * Página de edição de contas
	 */
	private static void paginaEditarConta() {
		
		if(contas.getContas().isEmpty()) {
			System.out.println("Conta Inexistente.");
			paginaInicial();
		}
		
		System.out.println();
		System.out.println("--------------------.");
		System.out.println("   Sessão Iniciada.");
		System.out.println("---------------------");
		System.out.println();

		System.out.print("Insira o CPF da conta: ");
		String cpf = sc.nextLine();
		// fazer tratamento de erros
		for(Conta conta : contas.getContas()) {
			if(conta.getCpf().equals(cpf)) {
				System.out.println(conta.getCpf());
				do {
					
					System.out.println();
					System.out.println("----------------------------");
					System.out.println("   Sessão de Edição.");
					System.out.println("----------------------------");
					System.out.println();
					System.out.println("1. Alterar uma Despesa ou Receita da conta.");
					System.out.println("2. Alterar o nome da conta.");
					System.out.println("3. Alterar o tipo da conta.");
					System.out.println("4. Alterar a instituição financeira da conta.");
					System.out.println("5. Alterar a idade do usuário.");
					System.out.println("6. Retornar a pagina inicial.");
					
					do {
						System.out.println();
						System.out.print("Insira o número da operação que desejada: ");
						opcao = sc.nextLine();
						
						try {
							// Passando o valor que o usuario introduzio para um inteiro
							opcaoEscolhida = Integer.parseInt(opcao);
						} catch(Exception e) {
							System.out.println();
							System.out.println("Insira um número válido "
									+ "para dar andamento a operação!");
						}
						// Continua o programa caso ocorra algum erro
					} while(opcaoEscolhida < 0 || opcaoEscolhida > 6 || opcao.isEmpty());
					
					switch (opcaoEscolhida) {
						case 1: // Modifica Despesas e Receita da conta
							menuUsuario();
							break;
						case 2: // Modifica o nome da conta
							System.out.print("Insira o novo nome da conta: ");
							String novoNome = sc.nextLine();
							conta.setNome(novoNome);
							contaDao.atualizarNome(conta); 
							break;
						case 3: // Modifica o tipo da conta
							System.out.print("Insira o novo tipo da conta: ");
							TipoConta novoTipo = TipoConta.valueOf(sc.next().toUpperCase());
							conta.setTipoConta(novoTipo);
							contaDao.atualizarTipoConta(conta); 
							break;
						case 4: // Modifica a instituição financeira da conta
							System.out.print("Insira a nova instituição financeira da conta: ");
							String novaInstituicaoFinanceira = sc.nextLine();
							conta.setInstituicaoFinanceira(novaInstituicaoFinanceira);
							contaDao.atualizarInstituicaoFinanceira(conta); 
							break;
						case 5: // Modifica a idade do usuário
							System.out.print("Insira a nova idade do usuario: ");
							int novaIdade = sc.nextInt();
							conta.setIdade(novaIdade);
							contaDao.atualizarIdade(conta); 
							break;
						case 6: // Volta para a pagina inicial
							paginaInicial();
							break;
						case 0: // Sai do Programa
							System.out.println("Sessão Finalizada\nAgradeço a preferência!");
							break;	
					}
					
				} while(opcaoEscolhida != 0);
			}
		}
		
	}
	/**
	 * Cria uma Conta
	 */
	private static boolean introduzirConta() {
		
		// Pedido do nome do usuário
		do {
			System.out.print("Insira o nome do usuário: ");
			nomeUsuario = sc.nextLine();
		} while(nomeUsuario.isEmpty());
		
		// Pedido da idade do usuário
		do {
			System.out.print("Insira a idade do usuário: ");
			idadeUsuario = sc.nextLine();
			
			try {
				
				idadeUsu = Integer.parseInt(idadeUsuario);
				
			} catch(Exception e) {
				
				System.out.println("Insira um número válido.");
				
			}
			
		} while(idadeUsuario.isEmpty() || idadeUsu <= 0);
		
		// Pedido do cpf do usuário
		do {
			System.out.print("Insira o cpf do usuário: ");
			cpfUsuario = sc.nextLine();
			if(cpfUsuario.length() != 11) {
				System.out.println("O CPF deve conter 11 dígitos.");
			}

			try {
	
				eNumero = Long.parseLong(cpfUsuario);
				
			} catch(Exception e) {
				
				System.out.println("O CPF só pode conter números");
				
			}
		} while(novaConta.setCPF(cpfUsuario) != true);
		
		// Pedido do tipo de conta do usuário
		do {
			System.out.print("Introduza o tipo de conta do usuario(carteira, corrente, poupanca): ");
			
			try {
				
				tipoConta = TipoConta.valueOf(sc.next().toUpperCase());
				sc.nextLine();
				
			} catch(Exception e) {
				
				System.out.println("O tipo de conta tem que ser um dos listados a cima.");

			}
		} while(tipoConta == null);
		
		// Pedido da instituição financeira do usuário
		do {
			System.out.print("Introduza  do instituição financeira usuario: ");
			instituicaoFinanceira = sc.nextLine();
		} while(instituicaoFinanceira == "");
	
		return true;
	}
	
	/**
	 * Metodo que visualiza o menu de opções do usuario
	 */
	private static void menuUsuario() {
		do {
		
			System.out.println();
			System.out.println("-----------------------------------");
			System.out.println("   Sessão Iniciada.");
			System.out.println("-----------------------------------");
			System.out.println();
			System.out.println("1. Inserir uma nova receita.");
			System.out.println("2. Inserir uma nova despesa.");
			System.out.println("3. Exibir receitas.");
			System.out.println("4. Exibir despesas.");
			System.out.println("5. Exibir saldo da conta.");
			System.out.println("6. Exibir sua conta.");
			System.out.println("7. Retornar para a seção de contas.");
			System.out.println("0. Sair.");
			
			do {
				System.out.println();
				System.out.print("Introduza o número da operação que deseja realizar: ");
				opcao = sc.nextLine();
				
				try {
					// Transforma o valor que o usuario adicionou para um inteiro
					opcaoEscolhida = Integer.parseInt(opcao);
				} catch(Exception e) {
					System.out.println("É necessário selecionar uma opção"
							+ "para dar andamento a operação!");
				}
				// Dá continuidade ao processo caso ocorra algum erro
			} while(opcaoEscolhida < 0 || opcaoEscolhida > 7 || opcao.isEmpty());
			
			switch (opcaoEscolhida) {
				case 1: // Incluir uma receita
					introduzirReceita();
					break;	
				case 2: // Incluir uma despesa
					introduzirDespesa();
					break;
				case 3: // Exibir as receitas
					mostrarListaReceita();
					break;				
				case 4: // Exibir as Despesas
					mostrarListaDespesa();
					break;
				case 5: // Exibir saldo da conta
					System.out.println(novaConta.toString());
					break;
				case 6: // Exibir a conta
					System.out.println(novaConta.toStringAll());
					for(int i = 0; i < novaConta.getListaReceita().size(); i++) {
						System.out.println(novaConta.getListaReceita().get(i));
					}
					for(int i = 0; i < novaConta.getListaDespesa().size(); i++) {
						System.out.println(novaConta.getListaDespesa().get(i));
					}
					break;
				case 7: // Retornar para a pagina inicial
					paginaInicial();
					break;
				case 0: // Finalizar o programa
					System.out.println("Sessão finalizada\nAgradeço a preferência!");
					break;	
			}
			
		} while(opcaoEscolhida != 0);
	}

	/**
	 * Metodo que introduz receita na conta
	 */
	private static void introduzirReceita() {
		do {
			System.out.println("Insira a descriçao da receita: ");
			descricaoTransferencia = sc.nextLine();
		} while(descricaoTransferencia.isEmpty());
		
		do {
			System.out.println("Insira o valor da receita: ");
			custo = sc.nextLine();
			
			try {
				custoTransferencia = Double.parseDouble(custo);
			} catch(Exception e) {
				System.out.println("Insira um valor decimal");
			}
		} while(custo.isEmpty() || custoTransferencia < 0);
		
		do {
			System.out.println("Insira o tipo da receita(salario, presente, premio, outros): ");
			
			try {
				
				tipoReceita = TipoReceita.valueOf(sc.next().toUpperCase());
				sc.nextLine();
				
			} catch(Exception e) {
				
				System.out.println("Insira um tipo de receita listado.");

			}
		} while(tipoReceita == null);
		
		for(Conta conta : contas.getContas()) {
			if(conta.getCpf() == novaConta.getCpf()) {
				String descricao = descricaoTransferencia;
				conta.addReceita(descricaoTransferencia, custoTransferencia, new Date(), tipoReceita);
				System.out.println("Receita registrada com sucesso.");
				for (Receita receita : conta.getListaReceita()) {
					if(descricao.equals(receita.getDescricao())){
						contaDao.inserirReceita(receita);
						
					}
				}
			} else {
				System.out.print("Ocorreu uma falha no registro");
			}
		}
	}
	
	/*
	 * Método que adiciona uma despesa na conta
	 */
	private static void introduzirDespesa() {
		do {
			System.out.println("Insira a descriçao da despesa: ");
			descricaoTransferencia = sc.nextLine();
		} while(descricaoTransferencia.isEmpty());
		
		do {
			System.out.println("Insira o custo da despesa: ");
			custo = sc.nextLine();
			
			try {
				custoTransferencia = Double.parseDouble(custo);
			} catch(Exception e) {
				System.out.println("Insira um valor decimal");
			}
			if(novaConta.getSaldo() < 0) {
				System.out.println("É necessário possuir receita para realizar o pagamento desta despesa");
				saldoNegativo = true;
				break;
			} else {
				saldoNegativo = false;
			}
		} while(custo.isEmpty() || custoTransferencia < 0 || saldoNegativo == true);
		
		do {
			System.out.println("Insira o tipo da despesa(alimentacao, educacao, lazer, moradia, roupa, saude, transporte, outros): ");
			
			try {
				
				tipoDespesa = TipoDespesa.valueOf(sc.next().toUpperCase());
				sc.nextLine();
				
			} catch(Exception e) {
				
				System.out.println("Selecione um tipo de despesa listado.");

			}
		} while(tipoDespesa == null);
		
		if(saldoNegativo == false) {
			// No caso do saldo ser negativo 
			novaConta.addDespesa(descricaoTransferencia, custoTransferencia, new Date(), tipoDespesa);
			for(Conta conta : contas.getContas()) {
				if(conta.getCpf() == novaConta.getCpf()) {
					String descricao = descricaoTransferencia;
					for (Despesa despesa : conta.getListaDespesa()) {
						if(descricao.equals(despesa.getDescricao())){
							contaDao.inserirDespesa(despesa);
							
						}
					}
				}
			}
		}
	}
	
	/*
	 * Método que exibirá a lista de receitas
	 */
	
	private static void mostrarListaReceita() {
		for(Conta conta : contas.getContas()) {
			if(conta.getCpf() == novaConta.getCpf()) {
				for(int i = 0; i < conta.getListaReceita().size(); i++) {
					System.out.println(conta.getListaReceita().get(i));
				}
			}
		}
	}
	
	/*
	 * Metodo que mostrará a lista de despesas
	 */
	
	private static void mostrarListaDespesa() {
		for(Conta conta : contas.getContas()) {
			if(conta.getCpf() == novaConta.getCpf()) {
				for(int i = 0; i < conta.getListaDespesa().size(); i++) {
					System.out.println(conta.getListaDespesa().get(i));
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);

		// Inicia o programa dando opções do programa ao usuario 
		paginaInicial();
		
	}
}