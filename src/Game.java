
import java.util.List;
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
        //respostas - servem para ver como  o usuario age perante as perguntas feitas, e cada pergunta tem o seu nivel de confiança e de acordo com cada resposrtas eles pode aumentar ou diminuir.


        if (respostaIgualMenu()) {
          confirmarVoltarAoMenu(this);
          return;
        }

        alternativaEstaValida();
        mensagemSeAlternativaForInvalida();
      } while (!respostaDadaPeloUsuarioExiste);

      LimparTerminal.limpar();
      //codigo de confiaca + desafios -  os desafios podem ser mais faceis ou dificeis dependendo do nivel de confianca adquiridos pelo jogador, atraves de cada resposta dada durante o jogo.

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
  }
}
