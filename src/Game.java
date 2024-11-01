import java.util.Scanner;

interface Interacao {
  void executar();
}

// TODO: mostrar nivel de confianca de taka, e "mostrar" ao jogador como isso impacta na historia. e claro toda escolha deve ter uma vantagem e uma desvantagem

public class Game {
  private String resposta = "";
  private int nivelDeConfianca = 5;

  Scanner entrada = new Scanner(System.in);

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

    // this.obterRespostaDoJogador();

  }

  public void obterRespostaDoJogador() {
    System.out.println("\n");
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

  public void confirmarVoltarAoMenu(Interacao interacao) {
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

  public class Introducao {
    public void executar() {
      System.out.println("Bem-vindo, Nys!");
      System.out.println(
          "Desde a perda dos seus pais, você tem vivido isolado. Seu único companheiro  é Taka, seu robô guardião.");
      System.out.println(
          "Mas algo não parece certo. Aos poucos, você começa a desconfiar que Taka  guarda segredos sobre o acidente de seus pais.");

      PrimeiraInteracaoTaka primeiraInteracao = new PrimeiraInteracaoTaka();

      primeiraInteracao.executar();
    }
  }

  public class PrimeiraInteracaoTaka implements Interacao {

    @Override
    public void executar() {
      boolean respostaExiste = false;
      SegundaInteracaoTaka segundaInteracao = new SegundaInteracaoTaka();

      do {

        System.out.println("\nTaka entra na sala e pergunta:");
        System.out.println("\"Tudo bem com você, Nys?\"");
        System.out.println("1 - Responder amigavelmente");
        System.out.println("2 - Responder de forma suspeita");
        System.out.print("Escolha uma opção: ");

        obterRespostaDoJogador();

        if (respostaIgualMenu()) {
          confirmarVoltarAoMenu(this);
          return;
        }

        if (resposta.equalsIgnoreCase("1")) {
          limparTerminal();
          respostaExiste = true;

          System.out.println("Você responde amigavelmente, mas, por dentro, continua desconfiando.");
          nivelDeConfianca += 2;
          segundaInteracao.executar();
        } else if (resposta.equalsIgnoreCase("2")) {
          limparTerminal();
          respostaExiste = true;

          System.out.println("Você responde de forma suspeita. Taka observa, mas nao reage.");
          nivelDeConfianca -= 2;
          segundaInteracao.executar();
        } else {
          respostaExiste = false;

          System.out.println("Opcao inválida. Tente novamente.");
        }
      } while (!respostaExiste);
    }
  }

  public class SegundaInteracaoTaka implements Interacao {

    @Override
    public void executar() {
      boolean respostaExiste = false;
      TerceiraInteracaoTaka terceiraInteracao = new TerceiraInteracaoTaka();

      do {
        System.out.println(
            "\nMais tarde naquela noite, você encontra um arquivo no seu computador com o  nome dos seus pais. O arquivo está encriptado.");
        System.out.println("1 - Tentar hackear o arquivo agora");
        System.out.println("2 - Perguntar a Taka sobre o arquivo");
        System.out.print("Escolha uma opcao: ");

        obterRespostaDoJogador();

        if (respostaIgualMenu()) {
          confirmarVoltarAoMenu(this);
          return;
        }

        if (resposta.equalsIgnoreCase("1")) {
          limparTerminal();
          respostaExiste = true;
          System.out.println(
              "Você tenta hackear o arquivo, mas percebe que o nível de encriptacao é alto demais para suas habilidades atuais.");
          System.out.println("Você precisará estudar mais antes de tentar novamente.");
          terceiraInteracao.executar();
        } else if (resposta.equalsIgnoreCase("2")) {
          limparTerminal();
          respostaExiste = true;
          System.out.println("Você pergunta a Taka sobre o arquivo.");

          if (nivelDeConfianca > 5) {
            System.out.println("Taka hesita, mas responde: \"É apenas um arquivo antigo. Nao se preocupe com isso.\"");
          } else {
            System.out.println(
                "Taka responde friamente: \"Nao tenho informacoes sobre isso.\" Você sente que ele está escondendo algo.");
            nivelDeConfianca -= 1;
          }

          terceiraInteracao.executar();
        } else {
          respostaExiste = false;
          System.out.println("Opcao inválida. Tente novamente.");
        }

      } while (!respostaExiste);
    }
  }

  public class TerceiraInteracaoTaka implements Interacao {

    @Override
    public void executar() {
      boolean respostaExiste = false;

      do {
        System.out.println(
            "\nFrustrado, você decide começar a desenvolver um programa em Java para desativar temporariamente Taka e acessar os arquivos escondidos.");
        System.out.println("1 - Trabalhar no programa durante toda a noite");
        System.out.println("2 - Desistir e esperar outro dia");
        System.out.print("Escolha uma opcao: ");

        obterRespostaDoJogador();

        if (respostaIgualMenu()) {
          confirmarVoltarAoMenu(this);
          return;
        }

        if (resposta.equalsIgnoreCase("1")) {
          limparTerminal();
          respostaExiste = true;
          System.out.println(
              "Você passa a noite escrevendo linhas de código, focado em criar um programa que possa desativar Taka.");
          System.out.println("Sua habilidade de programacao aumenta.");
        } else if (resposta.equalsIgnoreCase("2")) {
          limparTerminal();
          respostaExiste = true;
          System.out.println(
              "Você decide deixar para outro dia. Uma sensacao de incerteza te atormenta enquanto você tenta dormir.");
        } else {
          respostaExiste = false;
          System.out.println("Opção inválida. Tente novamente.");
        }

      } while (!respostaExiste);
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

    // this.obterRespostaDoJogador();
  }

  public void start() {
    Introducao introducao = new Introducao();
    introducao.executar();

    System.out.println(
        "\nEssa é apenas a primeira noite de uma jornada sombria para descobrir a verdade sobre o acidente de seus pais.");
    System.out.println("Escolhas futuras moldarao o destino de Nys e sua relacao com Taka...");
  }
}
