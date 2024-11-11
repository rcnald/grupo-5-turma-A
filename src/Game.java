import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

interface Execucao {
  void executar();
}

// TODO: Fazer desafios, refatorar metodos de Game para conversar melhor com as
// interacoes e furuta Classe Desafio
// Implementar tutorias

public class Game {
  private String resposta = "";
  private int nivelDeConfianca = 5;

  Gson gson = new Gson();

  Scanner entrada = new Scanner(System.in);

  public class Interacao {
    public String pergunta;
    public String[] alternativas;
    public Object[] respostas; 
    public int[] efeitosConfianca;
  }

  public void menu() {
    boolean respostaExiste = false;

    do {
      this.limparTerminal();
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

  public void limparTerminal() {
    String os = System.getProperty("os.name").toLowerCase();

    try {
      if (os.contains("windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        new ProcessBuilder("clear").inheritIO().start().waitFor();
      }
    } catch (Exception e) {
      System.out.println("Erro ao limpar a tela: " + e.getMessage());
    }
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
          this.limparTerminal();

          respostaExiste = true;
          resposta = "";

          this.menu();
        } else if (respostaNegativa(confirmacao)) {
          this.limparTerminal();

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

  public class RespostaCondicional {
    private String respostaBase;
    private String[] respostasCondicionais;
    private int nivelDeConfiancaMinimoParaPrimeiraCondicional;

    public RespostaCondicional(String respostaBase, String[] respostasCondicionais,
        int nivelDeConfiancaMinimoParaPrimeiraCondicional) {
      this.respostaBase = respostaBase;
      this.respostasCondicionais = respostasCondicionais;
      this.nivelDeConfiancaMinimoParaPrimeiraCondicional = nivelDeConfiancaMinimoParaPrimeiraCondicional;
    }

    public String exibirRespostaCondicional() {
      int indice = nivelDeConfianca > nivelDeConfiancaMinimoParaPrimeiraCondicional ? 0 : 1;
      return (respostasCondicionais != null && respostasCondicionais.length > indice) ? respostasCondicionais[indice]
          : respostaBase;
    }
  }

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

      limparTerminal();

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
        limparTerminal();
        System.out.println("Opcao inválida. Tente novamente.");
      }
    }

    public void exibirRespostas() {
      int respostaInteiraIndice = Integer.parseInt(resposta) - 1;
      Object repostaParaExibir = respostas[respostaInteiraIndice];

      if (repostaParaExibir instanceof RespostaCondicional) {
        System.out.print(((RespostaCondicional) repostaParaExibir).exibirRespostaCondicional());
      } else if (repostaParaExibir instanceof String) {
        System.out.println(repostaParaExibir);
      }
    }

    public void aplicarEfeitosColateraisDeConfianca() {
      int respostaInteiraIndice = Integer.parseInt(resposta) - 1;

      nivelDeConfianca += efeitoColateralDeConfianca[respostaInteiraIndice];
    }
  }

  public static List<Interacao> carregarInteracoesDeJSON(String caminhoDoArquivo) {
    try (FileReader reader = new FileReader(caminhoDoArquivo)) {
      Gson gson = new Gson();
      // A estrutura do JSON é um objeto que tem um campo "interacoes" que é uma lista
      TipoInteracoes tipoInteracoes = gson.fromJson(reader, TipoInteracoes.class);
      return tipoInteracoes.getInteracoes();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static class TipoInteracoes {
    private List<Interacao> interacoes;

    public List<Interacao> getInteracoes() {
      return interacoes;
    }
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

    List<Interacao> interacoes = carregarInteracoesDeJSON("data/interacoes.json");

    Interacao interacao1 = interacoes.get(0);
    Interacao interacao2 = interacoes.get(1);

    InteracaoPadrao primeiraInteracao = new InteracaoPadrao(interacao1);
    InteracaoPadrao segundaInteracao = new InteracaoPadrao(interacao2);

    primeiraInteracao.executar();
    segundaInteracao.executar();
  }
}
