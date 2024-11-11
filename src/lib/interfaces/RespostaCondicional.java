package lib.interfaces;

public class RespostaCondicional {
  public String respostaBase;
  public String[] respostasCondicionais;
  public int nivelDeConfiancaMinimoParaPrimeiraCondicional;

  public RespostaCondicional(String respostaBase, String[] respostasCondicionais,
      int nivelDeConfiancaMinimoParaPrimeiraCondicional) {
    this.respostaBase = respostaBase;
    this.respostasCondicionais = respostasCondicionais;
    this.nivelDeConfiancaMinimoParaPrimeiraCondicional = nivelDeConfiancaMinimoParaPrimeiraCondicional;
  }

  public String exibirRespostaCondicionalUm() {
    return respostasCondicionais[0];
  }

  public String exibirRespostaCondicionalDois() {
    return respostasCondicionais[1];
  }
}
