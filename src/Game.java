
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import lib.interfaces.Interacao;
import lib.interfaces.RespostaCondicional;
import lib.utils.LimparTerminal;
import lib.utils.ObterInteracoes;

interface Execucao {
  void executar();
}

// TODO: Fazer desafios, refatorar metodos de Game para conversar melhor com as
// interacoes e furuta Classe Desafio
// Implementar tutorias

public class Game {
  private String resposta = "";
  private int nivelDeConfianca = 5;

  Scanner entrada = new Scanner(System.in);

  public class InteracaoPadrao implements Execucao {
    private String pergunta;
    private String[] alternativas;
    private Object[] respostas;
    private int[] efeitoColateralDeConfianca;
    private int alternativasValidas;
    private boolean respostaDadaPeloUsuarioExiste;

    public InteracaoPadrao(Interacao interacao) {
      this.pergunta = interacao.pergunta;
      this.alternativas = interacao.alternativas;
      this.respostas = interacao.respostas;
      this.efeitoColateralDeConfianca = interacao.efeitosConfianca;
      this.alternativasValidas = this.alternativas.length;
    }

    @Override
    public void executar() {

      do {
        exibirPergunta();
        exibirAlternativas();

        obterRespostaDoJogador();

        if (respostaIgualMenu()) {
          confirmarVoltarAoMenu(this);
          return;
        }

        alternativaEstaValida();
        mensagemSeAlternativaForInvalida();
      } while (!respostaDadaPeloUsuarioExiste);

      LimparTerminal.limpar();

      exibirRespostas();
      aplicarEfeitosColateraisDeConfianca();
    }

    public void exibirPergunta() {
      System.out.println(pergunta);
    }

    public void exibirAlternativas() {
      int indice = 1;

      for (String alternativa : alternativas) {
        System.out.println(indice + ") " + alternativa);
        indice++;
      }
    }

    public void alternativaEstaValida() {
      try {
        int respostaInteira = Integer.parseInt(resposta);

        respostaDadaPeloUsuarioExiste = respostaInteira > 0 && respostaInteira <= alternativasValidas;
      } catch (NumberFormatException e) {
        respostaDadaPeloUsuarioExiste = false;
      }
    }

    public void mensagemSeAlternativaForInvalida() {
      if (!respostaDadaPeloUsuarioExiste) {
        LimparTerminal.limpar();
        System.out.println("Opcao inválida. Tente novamente.");
      }
    }

    public void exibirRespostas() {
      int respostaInteiraIndice = Integer.parseInt(resposta) - 1;
      Object repostaParaExibir = respostas[respostaInteiraIndice];

      if (repostaParaExibir instanceof RespostaCondicional) {
        System.out.println(((RespostaCondicional) repostaParaExibir).respostaBase);
        if (nivelDeConfianca > ((RespostaCondicional) repostaParaExibir).nivelDeConfiancaMinimoParaPrimeiraCondicional) {
          System.out.println(((RespostaCondicional) repostaParaExibir).exibirRespostaCondicionalUm());
        } else {
          System.out.println(((RespostaCondicional) repostaParaExibir).exibirRespostaCondicionalDois());
        }
      } else if (repostaParaExibir instanceof String) {
        System.out.println(repostaParaExibir);
      }
    }

    public void aplicarEfeitosColateraisDeConfianca() {
      int respostaInteiraIndice = Integer.parseInt(resposta) - 1;

      nivelDeConfianca += efeitoColateralDeConfianca[respostaInteiraIndice];
    }
  }

  public void menu() {
    boolean respostaExiste = false;

    do {
      LimparTerminal.limpar();
      System.out.println("Bem vindo ao Cyberlife");
      System.out.println("1) - Instrucoes");
      System.out.println("2) - Jogar");
      System.out.println("3) - Créditos");
      System.out.println("4) - Sair");

      System.out.print("Escolha uma opcao: ");

      obterRespostaDoJogadorNoMenu();

      switch (resposta) {
        case "1":
          respostaExiste = true;
          this.instructions();
          break;
        case "2":
          respostaExiste = true;
          this.start();
          break;
        case "3":
          respostaExiste = true;
          this.credits();
          break;
        case "4":
          respostaExiste = true;
          System.out.println("Saindo do jogo...");
          break;
        default:
          respostaExiste = false;
          System.out.println("Opção inválida. Tente novamente.");
          break;
      }
    } while (!respostaExiste);
  }

  public void credits() {
    System.out.println("Créditos do jogo:");
    System.out.println("- Ronaldo");
    System.out.println("- Weliclene");
    System.out.println("- Samira");
    System.out.println("- Vinicius");
    System.out.println("Obrigado por jogar Cyberlife!");

    this.obterRespostaDoJogador();

    if (respostaIgualMenu()) {
      menu();
    }

  }

  public void obterRespostaDoJogador() {
    System.out.println("\n");
    System.out.println("Nivel de confianca " + nivelDeConfianca + " \n");
    System.out.println("Digite sua resposta: (Digite 'menu' para voltar ao menu.)");

    if (!resposta.equalsIgnoreCase("menu")) {
      resposta = entrada.next();
    }
  }

  public void obterRespostaDoJogadorNoMenu() {
    resposta = entrada.next();
  }

  public boolean respostaPositiva(String respostas) {
    return respostas.equalsIgnoreCase("s");
  }

  public boolean respostaNegativa(String respostas) {
    return respostas.equalsIgnoreCase("n");
  }

  public void confirmarVoltarAoMenu(Execucao interacao) {
    if (resposta.equalsIgnoreCase("menu")) {
      boolean respostaExiste = false;

      do {
        System.out.print("Tem certeza que deseja voltar ao menu principal? (s/n): ");
        String confirmacao = entrada.next();

        if (respostaPositiva(confirmacao)) {
          LimparTerminal.limpar();

          respostaExiste = true;
          resposta = "";

          this.menu();
        } else if (respostaNegativa(confirmacao)) {
          LimparTerminal.limpar();

          respostaExiste = true;
          resposta = "";

          interacao.executar();
        } else {

          respostaExiste = false;

          System.out.println("Opção inválida. Tente novamente. verificar se for menu");

        }
      } while (!respostaExiste);

    }
  }

  public boolean respostaIgualMenu() {
    return resposta.equalsIgnoreCase("menu");
  }

  public void instructions() {
    System.out.println(
        "Objetivo: Nys está em busca de informações ocultas sobre o acidente que matou seus pais. Para progredir, ele precisa superar uma série de desafios de programação que o ajudarão a desbloquear arquivos encriptados, entender comandos ocultos e acessar memórias bloqueadas de Taka.\n");
    System.out.println(
        "Escolhas e Consequências: Ao longo da história, você tomará decisões que moldam o relacionamento entre Nys e Taka. Suas escolhas afetam a dificuldade e o acesso a informações valiosas. Fique atento ao que cada decisão pode revelar.\n");
    System.out.println(
        "Desafios de Programação: Cada desafio ensina um conceito específico de programação. Para cada tarefa, você deverá escrever pequenos códigos que simulam o hacking que Nys faz para acessar novos arquivos.\n");
    System.out.println(
        "Progresso e Recompensas: A cada desafio concluído, você desbloqueia novos fragmentos de informação sobre o acidente e se aproxima da verdade. Resolva os desafios para avançar e explorar novas partes da história.\n");
  }

  class Desafio1 implements Execucao {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void executar() {
      System.out.println("\nDesafio 1: Tentar se lembrar de seu passado.");
      System.out.println("""
          Contexto: Você está tentando acessar memórias do passado armazenadas em seu subconsciente.
          Seu objetivo é revisar essas memórias usando o laço de repetição correto.
          """);

      String[] alternativas = {
          "1. for (String memoria : memoriaPassado) { System.out.println(memoria); }",
          "2. while (memoriaPassado.length > 0) { System.out.println(memoria); }",
          "3. do { System.out.println(memoriaPassado); } while (false);"
      };

      int tentativas = 0;
      boolean acertou = false;

      while (tentativas < 5 && !acertou) {
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) {
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe? (1/2/3): ");
        int escolha = scanner.nextInt();

        if (escolha == 1) {
          acertou = true;
          System.out.println("\nCorreto! Você usou um 'for-each' para explorar as memórias.");
          System.out.println("Explicação: O 'for-each' é ideal para percorrer coleções, como arrays ou listas.");

          String[] memoriaPassado = { "Brincando com meus pais", "Um acidente", "Uma explosão" };
          System.out.println("\nRevisando as memórias:");
          for (String memoria : memoriaPassado) {
            System.out.println("Memória encontrada: " + memoria);
          }
        } else {
          tentativas++;
          if (escolha == 2) {
            System.out.println("\nErrado! 'while' é útil para condições genéricas, mas não é ideal aqui.");
          } else if (escolha == 3) {
            System.out.println("\nErrado! 'do-while' executa ao menos uma vez, mas não é prático para este caso.");
          }

          if (tentativas < 5) {
            System.out
                .println("Dica: Qual laço percorre cada elemento de um array sem que você precise gerenciar índices?");
          } else {
            System.out.println("\nVocê atingiu o limite de tentativas! O desafio falhou.");
          }
        }
      }
    }
  }

  public void start() {
    List<Interacao> interacoes = ObterInteracoes.obter("data/interacoes.json");

    Interacao interacao1 = interacoes.get(0);
    Interacao interacao2 = interacoes.get(1);
    Interacao interacao3 = interacoes.get(2);
    Interacao interacao4 = interacoes.get(3);
    Interacao interacao5 = interacoes.get(4);

    InteracaoPadrao primeiraInteracao = new InteracaoPadrao(interacao1);
    InteracaoPadrao segundaInteracao = new InteracaoPadrao(interacao2);
    InteracaoPadrao terceiraInteracao = new InteracaoPadrao(interacao3);
    InteracaoPadrao quartaInteracao = new InteracaoPadrao(interacao4);
    InteracaoPadrao quintaInteracao = new InteracaoPadrao(interacao5);

    primeiraInteracao.executar();
    segundaInteracao.executar();
    terceiraInteracao.executar();
    quartaInteracao.executar();
    quintaInteracao.executar();

    Execucao desafio1 = new Desafio1();

    desafio1.executar();
  }
}
