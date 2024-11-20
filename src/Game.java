
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
    Execucao desafio1 = new Desafio1();
    Execucao desafio2 = new Desafio2();

    // tutorial.executar();
    // desafio1.executar();
    tutorial2.executar();
    desafio2.executar();
  }
}
