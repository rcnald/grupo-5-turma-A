
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
  private boolean acertoDesafioUm;
  private boolean acertoDesafioDois;
  private boolean acertoDesafioTres;
  private boolean acertoDesafioQuatro;
  private boolean acertoDesafioCinco;

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

  public class TutorialDesafio1 implements Execucao {

    @Override
    public void executar() {
      Scanner entrada = new Scanner(System.in);

      System.out.println("\n--- Tutorial: Revisando Memórias do Passado ---");
      System.out.println("""
          Contexto: No Desafio 1, você precisa acessar memórias armazenadas em um array chamado 'memoriaPassado'.
          Essas memórias devem ser revisadas utilizando o laço de repetição mais adequado.
          """);

      boolean continuar = true;

      while (continuar) {
        System.out.println("Escolha uma alternativa para aprender mais:");
        System.out.println("1. for-each");
        System.out.println("2. while");
        System.out.println("3. do-while");
        System.out.println("4. Sair do tutorial");
        System.out.print("Opção: ");
        int escolha = entrada.nextInt();

        switch (escolha) {
          case 1:
            explicarForEach();
            break;

          case 2:
            explicarWhile();
            break;

          case 3:
            explicarDoWhile();
            break;
          case 4:
            System.out.println("\nSaindo do tutorial. Boa sorte no desafio!");
            continuar = false;
            break;
          default:
            System.out.println("\nOpção inválida. Tente novamente.");
        }

        if (continuar) {
          System.out.println("\n--- Fim da explicação ---\n");
        }
      }
    }

    private void explicarForEach() {
      System.out.println("\n--- Alternativa 1: for-each ---");
      System.out.println(
          """
              O 'for-each' é uma forma simplificada de iterar por todos os elementos de uma coleção (como um array ou lista).
              Ele é ideal quando você não precisa acessar os índices diretamente e só quer processar cada elemento.

              Sintaxe:
              for (Tipo elemento : Colecao) {
                  // Operações com elemento
              }
              """);

      String[] memoriaPassado = { "Brincando com meus pais", "Um acidente", "Uma explosão" };
      System.out.println("\nExemplo no contexto do desafio:");
      System.out.println("for (String memoria : memoriaPassado) {");
      System.out.println("    System.out.println(memoria);");
      System.out.println("}\n");
      System.out.println("Resultado:");
      for (String memoria : memoriaPassado) {
        System.out.println("Memória encontrada: " + memoria);
      }
    }

    private void explicarWhile() {
      System.out.println("\n--- Alternativa 2: while ---");
      System.out.println("""
          O 'while' é usado quando não sabemos a quantidade exata de iterações, mas sabemos a condição para parar.
          Ele verifica a condição antes de executar o bloco de código, e só continua enquanto a condição for verdadeira.

          Sintaxe:
          while (condicao) {
              // Operações
          }
          """);

      System.out.println(
          "No contexto do desafio, o 'while' não é a melhor escolha, porque você já conhece o tamanho do array.");
      System.out.println("""
          Exemplo inadequado:
          while (memoriaPassado.length > 0) {
              // O código acima está errado porque 'memoriaPassado.length' não muda.
          }
          """);
      System.out.println("Dica: Use o 'while' quando estiver lidando com condições dinâmicas e desconhecidas.");
    }

    private void explicarDoWhile() {
      System.out.println("\n--- Alternativa 3: do-while ---");
      System.out.println("""
          O 'do-while' é semelhante ao 'while', mas a condição é verificada após a execução do bloco.
          Isso significa que o código será executado pelo menos uma vez, independentemente da condição.

          Sintaxe:
          do {
              // Operações
          } while (condicao);
          """);

      System.out.println(
          "No contexto do desafio, o 'do-while' não é adequado porque você não precisa garantir uma execução inicial.");
      System.out.println("""
          Exemplo inadequado:
          do {
              System.out.println(memoriaPassado);
          } while (false);
          """);
      System.out.println(
          "Dica: Use o 'do-while' quando quiser garantir ao menos uma execução antes de verificar a condição.");
    }
  }

  class Desafio1 implements Execucao {
    private final Scanner entrada = new Scanner(System.in);

    @Override
    public void executar() {
      LimparTerminal.limpar();

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

      int tentativasMaximas = calcularTentativas();
      int tentativas = 0;
      boolean acertou = false;

      while (tentativas < tentativasMaximas && !acertou) {
        System.out.println("\nNível de confiança: " + nivelDeConfianca);
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) {
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe?");
        int escolha = entrada.nextInt();

        if (escolha == 1) {
          acertou = true;
          acertoDesafioUm = true;
          System.out.println("\nCorreto! Você usou um 'for-each' para explorar as memórias.");
          System.out.println("Explicação: O 'for-each' é ideal para percorrer coleções, como arrays ou listas.");

          String[] memoriaPassado = { "Brincando com meus pais", "Um acidente", "Uma explosão" };
          System.out.println("\nRevisando as memórias:");
          for (String memoria : memoriaPassado) {
            System.out.println("Memória encontrada: " + memoria);
          }
        } else {
          tentativas++;
          fornecerFeedback(escolha, tentativasMaximas - tentativas);

          if (tentativas == tentativasMaximas) {
            System.out.println("\nVocê atingiu o limite de tentativas! O desafio falhou.");
          }
        }
      }

      if (acertou) {
        System.out.println("\nParabéns! Você concluiu o desafio com sucesso.");
      }
    }

    private int calcularTentativas() {
      if (nivelDeConfianca >= 7) {
        System.out.println("\nVocê está muito confiante! Receberá 3 tentativas.");
        return 3;
      } else if (nivelDeConfianca >= 5) {
        System.out.println("\nConfiança estável. Receberá 2 tentativas.");
        return 2;
      } else {
        System.out.println("\nConfiança baixa! Você terá apenas 1 tentativa1.");
        return 1;
      }
    }

    private void fornecerFeedback(int escolha, int tentativasRestantes) {
      if (escolha == 2) {
        System.out.println("\nErrado! 'while' é útil para condições genéricas, mas não é ideal aqui.");
      } else if (escolha == 3) {
        System.out.println("\nErrado! 'do-while' executa ao menos uma vez, mas não é prático para este caso.");
      }

      if (nivelDeConfianca >= 7) {
        System.out.println("Dica adicional: Pense em um laço que percorre todos os elementos de forma automática.");
      } else if (nivelDeConfianca >= 5) {
        System.out.println("Dica: Qual laço percorre elementos sem precisar de índices?");
      } else {
        System.out.println("Sem dicas disponíveis. Boa sorte!");
      }

      System.out.println("Tentativas restantes: " + tentativasRestantes);
    }
  }

  public class TutorialDesafio2 implements Execucao {

    @Override
    public void executar() {
      Scanner entrada = new Scanner(System.in);

      System.out.println("\n--- Tutorial: Simulando a Descriptografia de Dados ---");
      System.out.println("""
          Contexto: No Desafio 2, você precisa simular o processo de descriptografia de um arquivo.
          Para isso, é necessário escolher o laço de repetição mais adequado para tentar descriptografar
          até alcançar o resultado esperado ou atingir um limite de tentativas.
          """);

      boolean continuar = true;

      while (continuar) {
        System.out.println("Escolha uma alternativa para aprender mais:");
        System.out.println("1. for-each");
        System.out.println("2. while");
        System.out.println("3. do-while");
        System.out.println("4. Sair do tutorial");
        System.out.print("Opção: ");
        int escolha = entrada.nextInt();

        switch (escolha) {
          case 1:
            explicarForEach();
            break;

          case 2:
            explicarWhile();
            break;

          case 3:
            explicarDoWhile();
            break;

          case 4:
            System.out.println("\nSaindo do tutorial. Boa sorte no desafio!");
            continuar = false;
            break;

          default:
            System.out.println("\nOpção inválida. Tente novamente.");
        }

        if (continuar) {
          System.out.println("\n--- Fim da explicação ---\n");
        }
      }
    }

    private void explicarForEach() {
      System.out.println("\n--- Alternativa 1: for-each ---");
      System.out.println("""
          O 'for-each' é usado para iterar sobre elementos de uma coleção, como arrays ou listas.
          No entanto, ele não permite alterar variáveis de controle ou interromper a execução de forma dinâmica.

          Sintaxe:
          for (Tipo elemento : Colecao) {
              // Operações com elemento
          }
          """);

      System.out.println("""
          No contexto do desafio, o 'for-each' não é ideal porque você precisa de controle
          total sobre as tentativas de descriptografia.
          """);

      System.out.println("Dica: Use 'for-each' apenas para processar elementos conhecidos e estáticos.");
    }

    private void explicarWhile() {
      System.out.println("\n--- Alternativa 2: while ---");
      System.out.println("""
          O 'while' é usado para executar um bloco de código enquanto uma condição for verdadeira.
          Ele é adequado para situações onde você não sabe quantas vezes o laço precisará ser executado.

          Sintaxe:
          while (condicao) {
              // Operações
          }
          """);

      System.out.println("Exemplo no contexto do desafio:");
      System.out.println("""
          boolean arquivoDescriptografado = false;
          int tentativa = 0;

          while (!arquivoDescriptografado && tentativa < 5) {
              System.out.println("Tentativa " + (tentativa + 1) + ": Tentando descriptografar...");
              tentativa++;
          }
          """);

      System.out.println("Resultado:");
      boolean arquivoDescriptografado = false;
      int tentativa = 0;

      while (!arquivoDescriptografado && tentativa < 5) {
        System.out.println("Tentativa " + (tentativa + 1) + ": Tentando descriptografar...");
        tentativa++;
      }

      System.out.println("\nVantagem: O 'while' permite controlar a execução com base em condições dinâmicas!");
    }

    private void explicarDoWhile() {
      System.out.println("\n--- Alternativa 3: do-while ---");
      System.out.println("""
          O 'do-while' é semelhante ao 'while', mas a condição é verificada após a execução do bloco.
          Isso significa que o código será executado pelo menos uma vez, mesmo que a condição inicial seja falsa.

          Sintaxe:
          do {
              // Operações
          } while (condicao);
          """);

      System.out.println("Exemplo no contexto do desafio:");
      System.out.println("""
          boolean arquivoDescriptografado = false;
          int tentativa = 0;

          do {
              System.out.println("Tentativa " + (tentativa + 1) + ": Tentando descriptografar...");
              tentativa++;
          } while (!arquivoDescriptografado && tentativa < 5);
          """);

      boolean arquivoDescriptografado = false;
      int tentativa = 0;

      do {
        System.out.println("Tentativa " + (tentativa + 1) + ": Tentando descriptografar...");
        tentativa++;
      } while (!arquivoDescriptografado && tentativa < 5);

      System.out.println("\nVantagem: O 'do-while' garante pelo menos uma execução inicial!");
    }

  }

  class Desafio2 implements Execucao {
    private final Scanner entrada = new Scanner(System.in);

    @Override
    public void executar() {
      LimparTerminal.limpar();

      System.out.println("\nDesafio 2: Descriptografar o arquivo com o nome dos seus pais.");
      System.out.println("""
          Contexto: Você encontrou um arquivo criptografado com informações sobre seus pais.
          Para descriptografar, é necessário garantir que o processo seja executado pelo menos uma vez,
          mesmo que a condição inicial ainda não seja atendida.
          """);
      System.out.println("Dica inicial: Escolha o laço que garante execução ao menos uma vez.");

      String[] alternativas = {
          "1. while (!arquivoDescriptografado) { System.out.println(\"Tentativa de descriptografia...\"); }",
          "2. do { System.out.println(\"Tentativa de descriptografia...\"); } while (!arquivoDescriptografado);",
          "3. for (int i = 0; i < 3; i++) { System.out.println(\"Tentativa de descriptografia...\"); }"
      };

      int tentativasMaximas = calcularTentativas();
      int tentativas = 0;
      boolean acertou = false;

      while (tentativas < tentativasMaximas && !acertou) {
        System.out.println("\nNível de confiança: " + nivelDeConfianca);
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) {
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe?: ");
        int escolha = entrada.nextInt();

        if (escolha == 2) {
          acertou = true;
          acertoDesafioDois = true;
          simularDescriptografia();
          System.out.println("\nCorreto! O laço 'do/while' garantiu que o código fosse executado pelo menos uma vez.");
          System.out.println("Arquivo descriptografado com sucesso!");
          System.out.println("Você encontra os nomes: Sophia e Elijah.");
        } else {
          tentativas++;
          fornecerFeedback(escolha, tentativasMaximas - tentativas);

          if (tentativas == tentativasMaximas) {
            System.out.println("\nVocê atingiu o limite de tentativas! O desafio falhou.");
            System.out.println("O arquivo permanece criptografado. Talvez haja outra oportunidade no futuro.");
          }
        }
      }

      if (acertou) {
        System.out.println("\nParabéns! Você concluiu o desafio com sucesso.");
      }
    }

    private int calcularTentativas() {
      if (nivelDeConfianca >= 7) {
        System.out.println("\nVocê está muito confiante! Receberá 3 tentativas.");
        return 3;
      } else if (nivelDeConfianca >= 5) {
        System.out.println("\nConfiança estável. Receberá 2 tentativas.");
        return 2;
      } else {
        System.out.println("\nConfiança baixa! Você terá apenas 1 tentativas.");
        return 1;
      }
    }

    private void fornecerFeedback(int escolha, int tentativasRestantes) {
      if (escolha == 1) {
        System.out.println(
            "\nErrado! O 'while' verifica a condição antes de executar, o que não garante a execução inicial.");
      } else if (escolha == 3) {
        System.out.println("\nErrado! O 'for' é usado para iterações de contagem fixa, mas não é ideal neste caso.");
      }

      if (nivelDeConfianca >= 7) {
        System.out
            .println("Dica adicional: Pense em um laço que sempre executa o bloco antes de verificar a condição.");
      } else if (nivelDeConfianca >= 5) {
        System.out.println("Dica: Qual laço garante execução pelo menos uma vez?");
      } else {
        System.out.println("Sem dicas disponíveis. Confie na sua lógica.");
      }

      System.out.println("Tentativas restantes: " + tentativasRestantes);
    }

    private void simularDescriptografia() {
      System.out.println("\nTentativa de descriptografar...");
      for (int i = 0; i < 10; i++) {
        try {
          Thread.sleep(300);
          System.out.print(".");
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          System.out.println("\nErro na simulação de descriptografia.");
        }
      }
      System.out.println("\nArquivo descriptografado!");
    }
  }

  public class TutorialDesafio3 implements Execucao {

    @Override
    public void executar() {
      Scanner entrada = new Scanner(System.in);

      System.out.println("\n--- Tutorial: Estruturando Múltiplas Escolhas ---");
      System.out.println("""
          Contexto: No Desafio 3, você precisa estruturar múltiplas opções de maneira clara e eficiente.
          Para isso, é necessário escolher a estrutura de controle ideal para gerenciar várias alternativas,
          garantindo que cada escolha execute o bloco de código correto.
          """);

      boolean continuar = true;

      while (continuar) {
        System.out.println("Escolha uma alternativa para aprender mais:");
        System.out.println("1. if-else");
        System.out.println("2. switch");
        System.out.println("3. for");
        System.out.println("4. Sair do tutorial");
        System.out.print("Opção: ");
        int escolha = entrada.nextInt();

        switch (escolha) {
          case 1:
            explicarIfElse();
            break;

          case 2:
            explicarSwitch();
            break;

          case 3:
            explicarFor();
            break;

          case 4:
            System.out.println("\nSaindo do tutorial. Boa sorte no desafio!");
            continuar = false;
            break;

          default:
            System.out.println("\nOpção inválida. Tente novamente.");
        }

        if (continuar) {
          System.out.println("\n--- Fim da explicação ---\n");
        }
      }
    }

    private void explicarIfElse() {
      System.out.println("\n--- Alternativa 1: if-else ---");
      System.out.println("""
          A estrutura 'if-else' é usada para avaliar condições booleanas e executar diferentes blocos de código
          com base no resultado. Embora seja versátil, pode ficar confusa ao lidar com muitas condições.

          Sintaxe:
          if (condicao) {
              // Código se a condição for verdadeira
          } else if (outraCondicao) {
              // Código se outra condição for verdadeira
          } else {
              // Código se nenhuma condição for verdadeira
          }
          """);

      System.out.println("Exemplo no contexto do desafio:");
      System.out.println("""
          if (escolha == 1) {
              System.out.println("Você escolheu a opção 1.");
          } else if (escolha == 2) {
              System.out.println("Você escolheu a opção 2.");
          } else {
              System.out.println("Opção inválida.");
          }
          """);

      System.out.println("Limitação: O 'if-else' pode se tornar difícil de manter quando há muitas condições.");
    }

    private void explicarSwitch() {
      System.out.println("\n--- Alternativa 2: switch ---");
      System.out.println("""
          A estrutura 'switch' é ideal para lidar com múltiplas condições baseadas em valores fixos.
          É mais legível e organizada que 'if-else' em cenários com muitas opções.

          Sintaxe:
          switch (variavel) {
              case valor1:
                  // Código para valor1
                  break;
              case valor2:
                  // Código para valor2
                  break;
              default:
                  // Código se nenhum caso corresponder
          }
          """);

      System.out.println("Exemplo no contexto do desafio:");
      System.out.println("""
          switch (escolha) {
              case 1:
                  System.out.println("Você escolheu a opção 1.");
                  break;
              case 2:
                  System.out.println("Você escolheu a opção 2.");
                  break;
              default:
                  System.out.println("Opção inválida.");
          }
          """);

      System.out.println("Vantagem: O 'switch' organiza melhor múltiplas condições, especialmente com valores fixos.");
    }

    private void explicarFor() {
      System.out.println("\n--- Alternativa 3: for ---");
      System.out.println("""
          O 'for' é usado para iterar sobre um conjunto de elementos ou executar um bloco de código
          um número fixo de vezes. É útil para iterações controladas, mas não é ideal para escolhas dinâmicas.

          Sintaxe:
          for (inicializacao; condicao; incremento) {
              // Código a ser executado
          }
          """);

      System.out.println("Exemplo no contexto do desafio:");
      System.out.println("""
          for (int i = 0; i < 3; i++) {
              System.out.println("Opção " + (i + 1) + ": Alguma explicação.");
          }
          """);

      System.out.println("Limitação: O 'for' não é adequado para tomar decisões baseadas em valores específicos.");
    }
  }

  class Desafio3 implements Execucao { // A classe implementa a interface 'Execucao'.
    private final Scanner entrada = new Scanner(System.in); // Scanner para entrada de dados do usuário.

    @Override
    public void executar() { // Método principal que executa o desafio.
      LimparTerminal.limpar(); // Limpa o terminal (implementação externa).

      System.out.println("\nDesafio 3: Perguntar a Taka sobre o acidente."); // Introdução do desafio.
      System.out.println("""
          Contexto: Você decide confrontar Taka sobre o acidente que tirou a vida dos seus pais.
          Para isso, é necessário usar o código ideal para estruturar múltiplas opções de forma clara e eficiente.
          """);
      System.out.println("Dica inicial: Qual estrutura de controle é mais adequada para múltiplas escolhas?");

      // Lista de alternativas possíveis.
      String[] alternativas = {
          "1. if (escolha == 1) { ... } else if (escolha == 2) { ... }",
          "2. switch (escolha) { case 1: ...; break; }",
          "3. for (int i = 0; i < perguntas.length; i++) { ... }"
      };

      // Calcula o número máximo de tentativas baseado no nível de confiança.
      int tentativasMaximas = calcularTentativas();
      int tentativas = 0; // Contador de tentativas.
      boolean acertou = false; // Flag para verificar se o usuário acertou.

      while (tentativas < tentativasMaximas && !acertou) { // Loop enquanto houver tentativas e o usuário não acertar.
        System.out.println("\nNível de confiança: " + nivelDeConfianca); // Mostra o nível de confiança.
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) { // Exibe todas as alternativas.
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe?: "); // Solicita a escolha do usuário.
        int escolha = entrada.nextInt(); // Lê a escolha do usuário.

        if (escolha == 2) { // Verifica se a escolha está correta.
          acertou = true;
          acertoDesafioTres = true; // Marca o desafio como concluído com sucesso.
          System.out.println("\nCorreto! O 'switch' permite estruturar opções de forma clara e eficiente.");
          System.out.println("Taka hesita, mas responde: 'Não sei de nada sobre o acidente.'");
          nivelDeConfianca += 2; // Incrementa o nível de confiança por acertar.
        } else { // Caso a escolha esteja errada.
          tentativas++; // Incrementa o número de tentativas.
          fornecerFeedback(escolha, tentativasMaximas - tentativas); // Dá feedback ao usuário.

          if (tentativas == tentativasMaximas) { // Se o limite de tentativas for atingido.
            System.out.println("\nVocê atingiu o limite de tentativas! O desafio falhou.");
            System.out.println("Taka permanece em silêncio. Você perdeu a chance de obter informações.");
            nivelDeConfianca -= 1; // Reduz o nível de confiança por falhar.
          }
        }
      }

      if (acertou) { // Mensagem de conclusão ao acertar.
        System.out.println("\nParabéns! Você concluiu o desafio com sucesso.");
      }
    }

    private int calcularTentativas() { // Calcula o número de tentativas baseado no nível de confiança.
      if (nivelDeConfianca >= 7) { // Confiança alta.
        System.out.println("\nVocê está muito confiante! Receberá 3 tentativas.");
        return 3;
      } else if (nivelDeConfianca >= 5) { // Confiança média.
        System.out.println("\nConfiança estável. Receberá 2 tentativas.");
        return 2;
      } else { // Confiança baixa.
        System.out.println("\nConfiança baixa! Você terá apenas 1 tentativa.");
        return 1;
      }
    }

    private void fornecerFeedback(int escolha, int tentativasRestantes) { // Fornece feedback baseado na escolha.
      if (escolha == 1) { // Feedback para escolha 1.
        System.out
            .println("\nErrado! O 'if/else' pode ser usado, mas não é a solução mais clara para múltiplas escolhas.");
      } else if (escolha == 3) { // Feedback para escolha 3.
        System.out.println("\nErrado! O 'for' é usado para iterações fixas, mas não é ideal para este caso.");
      }

      if (nivelDeConfianca >= 7) { // Dica adicional para alta confiança.
        System.out.println("Dica adicional: Use uma estrutura que organiza bem várias opções.");
      } else if (nivelDeConfianca >= 5) { // Dica para confiança média.
        System.out.println("Dica: Qual estrutura é mais eficiente para múltiplas escolhas?");
      } else { // Sem dicas para confiança baixa.
        System.out.println("Sem dicas disponíveis. Confie na sua lógica.");
      }

      System.out.println("Tentativas restantes: " + tentativasRestantes); // Mostra o número de tentativas restantes.
    }
  }

  public class TutorialDesafio4 implements Execucao {

    @Override
    public void executar() {
      Scanner entrada = new Scanner(System.in);

      System.out.println("\n--- Tutorial: Validando uma Senha com Laços de Repetição ---");
      System.out.println("""
          Contexto: No Desafio 4, você precisa validar uma senha de maneira eficiente.
          Este tutorial explica diferentes abordagens com laços de repetição e como usá-los para
          resolver o problema proposto de forma correta.
          """);

      boolean continuar = true;

      while (continuar) {
        System.out.println("Escolha um tópico para explorar:");
        System.out.println("1. Estrutura 'for'");
        System.out.println("2. Estrutura 'while'");
        System.out.println("3. Estrutura 'do-while'");
        System.out.println("4. Sair do tutorial");
        System.out.print("Opção: ");
        int escolha = entrada.nextInt();

        switch (escolha) {
          case 1:
            explicarFor();
            break;

          case 2:
            explicarWhile();
            break;

          case 3:
            explicarDoWhile();
            break;

          case 4:
            System.out.println("\nSaindo do tutorial. Boa sorte no desafio!");
            continuar = false;
            break;

          default:
            System.out.println("\nOpção inválida. Tente novamente.");
        }

        if (continuar) {
          System.out.println("\n--- Fim da explicação ---\n");
        }
      }
    }

    private void explicarFor() {
      System.out.println("\n--- Estrutura 1: for ---");
      System.out.println("""
          A estrutura 'for' é ideal para cenários onde o número de iterações é conhecido antecipadamente.
          No entanto, não é adequada para verificar uma condição que depende de um valor dinâmico (como uma senha).

          Sintaxe:
          for (inicializacao; condicao; incremento) {
              // Código a ser executado
          }
          """);

      System.out.println("Exemplo inadequado no contexto de validação de senha:");
      System.out.println("""
          for (int i = 0; i < senha.length(); i++) {
              if (senha.equals("1234")) {
                  return true; // Isso pode gerar comportamentos imprevisíveis.
              }
          }
          """);

      System.out.println("Limitação: 'for' não verifica condições dinâmicas entre iterações, como verificar a senha.");
    }

    private void explicarWhile() {
      System.out.println("\n--- Estrutura 2: while ---");
      System.out.println("""
          A estrutura 'while' é ideal para validar condições dinâmicas antes de cada iteração.
          É uma escolha eficiente para verificar a senha repetidamente até que a condição seja atendida.

          Sintaxe:
          while (condicao) {
              // Código a ser executado enquanto a condição for verdadeira
          }
          """);

      System.out.println("Exemplo correto para validação de senha:");
      System.out.println("""
          while (!senha.equals("1234")) {
              System.out.println("Senha incorreta. Tente novamente.");
              senha = pedirNovaSenha();
          }
          """);

      System.out.println("Vantagem: 'while' verifica a condição antes de executar o bloco de código.");
    }

    private void explicarDoWhile() {
      System.out.println("\n--- Estrutura 3: do-while ---");
      System.out.println(
          """
              A estrutura 'do-while' é útil quando é necessário garantir que o bloco de código seja executado pelo menos uma vez,
              mesmo que a condição seja inicialmente falsa.

              Sintaxe:
              do {
                  // Código a ser executado
              } while (condicao);
              """);

      System.out.println("Exemplo correto para validação de senha:");
      System.out.println("""
          do {
              System.out.println("Digite a senha:");
              senha = pedirNovaSenha();
          } while (!senha.equals("1234"));
          """);

      System.out
          .println("Vantagem: 'do-while' garante a execução do bloco ao menos uma vez antes de verificar a condição.");
    }
  }

  class Desafio4 implements Execucao {
    private final Scanner entrada = new Scanner(System.in);

    @Override
    public void executar() {
      LimparTerminal.limpar();

      System.out.println("\nDesafio 4: Hackear as informações do robô.");
      System.out.println(
          """
              Contexto: Taka, o robô, protege informações confidenciais sobre o acidente que matou seus pais.
              Seu objetivo é encontrar a senha correta para acessar esses dados. Escolha o loop mais adequado para implementar a lógica de tentativa e erro.
              """);

      String[] alternativas = {
          "1. for (int i = 0; i < senha.length(); i++) { if (senha.equals(\"1234\")) return true; }",
          "2. while (!senha.equals(\"1234\")) { pedirNovaSenha(); }",
          "3. do { pedirNovaSenha(); } while (senha != \"1234\");"
      };

      int tentativasMaximas = calcularTentativas();
      int tentativas = 0;
      boolean acertou = false;

      while (tentativas < tentativasMaximas && !acertou) {
        System.out.println("\nNível de confiança: " + nivelDeConfianca);
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) {
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe? ");
        int escolha = entrada.nextInt();

        if (escolha == 2) {
          acertou = true;
          acertoDesafioQuatro = true;
          System.out.println("""
              \nCorreto! O loop 'while (!senha.equals(\"1234\"))' é ideal para verificar a condição
              antes de executar o código, garantindo que a senha correta seja encontrada.
              Hack bem-sucedido! Você acessou informações confidenciais de Taka.
              """);
          mostrarInformacoesHackeadas();
        } else {
          tentativas++;
          fornecerFeedback(escolha, tentativasMaximas - tentativas);

          if (tentativas == tentativasMaximas) {
            System.out.println("\nVocê atingiu o limite de tentativas! Hack falhou.");
          }
        }
      }

      if (acertou) {
        System.out.println("\nParabéns! Você concluiu o desafio com sucesso.");
      }
    }

    private int calcularTentativas() {
      if (nivelDeConfianca >= 7) {
        System.out.println("\nVocê está muito confiante! Receberá 3 tentativas.");
        return 3;
      } else if (nivelDeConfianca >= 5) {
        System.out.println("\nConfiança estável. Receberá 2 tentativas.");
        return 2;
      } else {
        System.out.println("\nConfiança baixa! Você terá apenas 1 tentativa.");
        return 1;
      }
    }

    private void fornecerFeedback(int escolha, int tentativasRestantes) {
      if (escolha == 1) {
        System.out.println("\nErrado! 'for' não é ideal para situações em que o número de iterações é desconhecido.");
      } else if (escolha == 3) {
        System.out.println(
            "\nErrado! 'do-while' executa ao menos uma vez, mas não é adequado para garantir a validação da condição inicialmente.");
      }

      if (nivelDeConfianca >= 7) {
        System.out.println("Dica adicional: Qual loop avalia a condição antes de executar o bloco?");
      } else if (nivelDeConfianca >= 5) {
        System.out.println("Dica: Pense em um laço que permite sair ao atingir uma condição específica.");
      } else {
        System.out.println("Sem dicas disponíveis. Boa sorte!");
      }

      System.out.println("Tentativas restantes: " + tentativasRestantes);
    }

    private void mostrarInformacoesHackeadas() {
      System.out.println("\n--- Informações Hackeadas ---");
      System.out.println("Localização do acidente: Laboratório Central, setor 7.");
      System.out.println("Anotações suspeitas: \"Taka não pode saber. Verificar protocolo X.\"");
    }
  }

  public class TutorialDesafio5 implements Execucao {

    @Override
    public void executar() {
      Scanner entrada = new Scanner(System.in);

      System.out.println("\n--- Tutorial: Escolhendo o Loop Correto para Desativar Taka ---");
      System.out.println("""
          Contexto: No Desafio 5, você precisa desativar o sistema de Taka utilizando um laço de repetição.
          Este tutorial explicará diferentes tipos de laços e como eles podem ser usados
          para resolver o problema de forma eficiente.
          """);

      boolean continuar = true;

      while (continuar) {
        System.out.println("Escolha um tópico para explorar:");
        System.out.println("1. Estrutura 'for'");
        System.out.println("2. Estrutura 'while'");
        System.out.println("3. Estrutura 'do-while'");
        System.out.println("4. Sair do tutorial");
        System.out.print("Opção: ");
        int escolha = entrada.nextInt();

        switch (escolha) {
          case 1:
            explicarFor();
            break;

          case 2:
            explicarWhile();
            break;

          case 3:
            explicarDoWhile();
            break;

          case 4:
            System.out.println("\nSaindo do tutorial. Boa sorte no desafio!");
            continuar = false;
            break;

          default:
            System.out.println("\nOpção inválida. Tente novamente.");
        }

        if (continuar) {
          System.out.println("\n--- Fim da explicação ---\n");
        }
      }
    }

    private void explicarFor() {
      System.out.println("\n--- Estrutura 1: for ---");
      System.out.println("""
          A estrutura 'for' é ideal para cenários em que o número de iterações é conhecido antecipadamente.
          No caso de desativar Taka, essa estrutura não é ideal, pois a condição para parar o laço é dinâmica
          e depende de um evento externo (como o código correto ser inserido).

          Sintaxe:
          for (inicializacao; condicao; incremento) {
              // Código a ser executado
          }
          """);

      System.out.println("Exemplo inadequado no contexto de desativar Taka:");
      System.out.println("""
          for (int i = 0; i < 10; i++) {
              tentarDesativar();
          }
          """);

      System.out.println("Limitação: 'for' não verifica condições dinâmicas entre as iterações.");
    }

    private void explicarWhile() {
      System.out.println("\n--- Estrutura 2: while ---");
      System.out.println("""
          A estrutura 'while' é útil para cenários onde a condição é verificada antes de cada iteração.
          No caso de desativar Taka, ela poderia ser usada, mas tem uma limitação: o laço não garante
          que a tentativa de desativação será executada pelo menos uma vez.

          Sintaxe:
          while (condicao) {
              // Código a ser executado enquanto a condição for verdadeira
          }
          """);

      System.out.println("Exemplo para desativar Taka:");
      System.out.println("""
          while (!codigo.equals("EXIT")) {
              tentarDesativar();
          }
          """);

      System.out
          .println("Limitação: O código dentro do 'while' pode nunca ser executado se a condição inicial for falsa.");
    }

    private void explicarDoWhile() {
      System.out.println("\n--- Estrutura 3: do-while ---");
      System.out.println("""
          A estrutura 'do-while' é ideal quando precisamos garantir que o bloco de código seja executado
          pelo menos uma vez, mesmo que a condição inicialmente seja falsa.
          No caso de desativar Taka, ela é a escolha mais apropriada.

          Sintaxe:
          do {
              // Código a ser executado
          } while (condicao);
          """);

      System.out.println("Exemplo correto para desativar Taka:");
      System.out.println("""
          do {
              tentarDesativar();
          } while (!codigo.equals("EXIT"));
          """);

      System.out.println(
          "Vantagem: 'do-while' garante que a tentativa de desativação ocorra pelo menos uma vez antes de verificar a condição.");
    }
  }

  class Desafio5 implements Execucao {
    private final Scanner entrada = new Scanner(System.in);

    @Override
    public void executar() {
      LimparTerminal.limpar();

      System.out.println("\nDesafio 5: Desativar o sistema de Taka.");
      System.out.println(
          """
              Contexto: Taka, o robô, está tentando proteger os sistemas críticos.
              Sua missão é desativar temporariamente o sistema usando o loop mais adequado
              para garantir ao menos uma tentativa de desativação.
              """);

      String[] alternativas = {
          "1. while (!codigo.equals(\"EXIT\")) { tentarDesativar(); }",
          "2. for (int i = 0; i < 10; i++) { tentarDesativar(); }",
          "3. do { tentarDesativar(); } while (!codigo.equals(\"EXIT\"));"
      };

      int tentativasMaximas = calcularTentativas();
      int tentativas = 0;
      boolean acertou = false;

      while (tentativas < tentativasMaximas && !acertou) {
        System.out.println("\nNível de confiança: " + nivelDeConfianca);
        System.out.println("\nAlternativas:");
        for (String alternativa : alternativas) {
          System.out.println(alternativa);
        }

        System.out.print("\nQual código você escolhe? ");
        int escolha = entrada.nextInt();

        if (escolha == 3) {
          acertou = true;
          acertoDesafioCinco = true;
          System.out.println("""
              \nCorreto! O 'do-while' garante que o bloco de código seja executado pelo menos uma vez,
              antes de avaliar a condição. Taka foi temporariamente desativado, e você tem acesso aos sistemas!
              """);
          mostrarAcessoSistemas();
        } else {
          tentativas++;
          fornecerFeedback(escolha, tentativasMaximas - tentativas);

          if (tentativas == tentativasMaximas) {
            System.out.println("\nVocê atingiu o limite de tentativas! O sistema de Taka continua ativo.");
          }
        }
      }

      if (acertou) {
        System.out.println("\nParabéns! Você concluiu o desafio com sucesso.");
      }
    }

    private int calcularTentativas() {
      if (nivelDeConfianca >= 7) {
        System.out.println("\nVocê está muito confiante! Receberá 3 tentativas.");
        return 3;
      } else if (nivelDeConfianca >= 5) {
        System.out.println("\nConfiança estável. Receberá 2 tentativas.");
        return 2;
      } else {
        System.out.println("\nConfiança baixa! Você terá apenas 1 tentativa.");
        return 1;
      }
    }

    private void fornecerFeedback(int escolha, int tentativasRestantes) {
      if (escolha == 1) {
        System.out.println(
            "\nErrado! 'while' só executa se a condição inicial for verdadeira, o que pode não funcionar aqui.");
      } else if (escolha == 2) {
        System.out.println("\nErrado! 'for' não é ideal quando o número de iterações é desconhecido.");
      }

      if (nivelDeConfianca >= 7) {
        System.out.println("Dica adicional: Qual laço sempre executa o bloco pelo menos uma vez?");
      } else if (nivelDeConfianca >= 5) {
        System.out.println("Dica: Considere um laço que garante uma tentativa antes de verificar a condição.");
      } else {
        System.out.println("Sem dicas disponíveis. Boa sorte!");
      }

      System.out.println("Tentativas restantes: " + tentativasRestantes);
    }

    private void mostrarAcessoSistemas() {
      System.out.println("\n--- Sistemas Acessados ---");
      System.out.println("1. Controle de navegação desativado.");
      System.out.println("2. Logs de atividades: \"Protocolo X em andamento. Prioridade máxima.\"");
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

    // primeiraInteracao.executar();
    // segundaInteracao.executar();
    // terceiraInteracao.executar();
    // quartaInteracao.executar();
    // quintaInteracao.executar();

    Execucao tutorial = new TutorialDesafio1();
    Execucao tutorial2 = new TutorialDesafio2();
    Execucao tutorial3 = new TutorialDesafio3();
    Execucao tutorial4 = new TutorialDesafio4();
    Execucao tutorial5 = new TutorialDesafio5();
    Execucao desafio1 = new Desafio1();
    Execucao desafio2 = new Desafio2();
    Execucao desafio3 = new Desafio3();
    Execucao desafio4 = new Desafio4();
    Execucao desafio5 = new Desafio5();

    // tutorial.executar();
    // desafio1.executar();

    // tutorial2.executar();
    // desafio2.executar();

    // tutorial3.executar();
    // desafio3.executar();

    // tutorial4.executar();
    // desafio4.executar();

    tutorial5.executar();
    desafio5.executar();
  }
}
