import java.util.Scanner;

public class Game {
  private String resposta;

  Scanner entrada = new Scanner(System.in);

  public void menu() {
    boolean respostaExiste = false;

    do {
      System.out.println("Bem vindo ao Cyberlife");
      System.out.println("1) - Instrucoes");
      System.out.println("2) - Jogar");
      System.out.println("3) - Créditos");
      System.out.println("4) - Sair");

      System.out.print("Escolha uma opcao: ");
      resposta = entrada.next();

      switch (resposta) {
        case "1":
          respostaExiste = true;
          this.instructions();
          break;
        case "2":
          respostaExiste = true;
          System.out.println("Iniciando o jogo...");
          break;
        case "3":
          respostaExiste = true;
          System.out.println("Créditos do jogo...");
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


  public void obterRespostaDoJogador() {
    System.out.println("Digite sua resposta: (Digite 'menu' para voltar ao menu.)");
    resposta = entrada.next();

    this.verificarSeRespostaForMenu();
  }

  public void verificarSeRespostaForMenu() {
    if (resposta.equalsIgnoreCase("menu")) {
      System.out.print("Tem certeza que deseja voltar ao menu principal? (s/n): ");
      String confirmacao = entrada.next();

      if (confirmacao.equalsIgnoreCase("s")) {
        this.menu();
      } else {
        this.obterRespostaDoJogador();
      }
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

    
    this.obterRespostaDoJogador();
    this.verificarSeRespostaForMenu();
  }

  public void start() {

  }
}