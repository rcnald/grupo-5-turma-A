package lib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lib.interfaces.Interacao;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class InteracaoWrapper {
  private List<Interacao> interacoes;

  public List<Interacao> getInteracoes() {
    return interacoes;
  }
}

public class ObterInteracoes {
  public static List<Interacao> obter(String caminhoDoArquivo) {
    try (FileReader reader = new FileReader(caminhoDoArquivo)) {
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(Interacao.class, new InteracaoDeserializer())
          .create();

      InteracaoWrapper wrapper = gson.fromJson(reader, InteracaoWrapper.class);

      return wrapper.getInteracoes();

    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
