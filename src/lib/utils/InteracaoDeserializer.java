package lib.utils;

import com.google.gson.*;
import lib.interfaces.Interacao;
import lib.interfaces.RespostaCondicional;

import java.lang.reflect.Type;


public class InteracaoDeserializer implements JsonDeserializer<Interacao> {

    @Override
    public Interacao deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        String pergunta = obj.get("pergunta").getAsString();
        JsonArray alternativasJson = obj.getAsJsonArray("alternativas");
        String[] alternativas = new String[alternativasJson.size()];
        int index = 0;
        for (JsonElement alternativaElement : alternativasJson) {
            alternativas[index++] = alternativaElement.getAsString();
        }

        JsonArray respostasJson = obj.getAsJsonArray("respostas");
        Object[] respostas = new Object[respostasJson.size()];
        int respostaIndex = 0;
        for (JsonElement respostaElement : respostasJson) {
            if (respostaElement.isJsonObject() && respostaElement.getAsJsonObject().has("condicional")) {
                JsonObject condicionalJson = respostaElement.getAsJsonObject().getAsJsonObject("condicional");
                String condicionalPergunta = condicionalJson.get("pergunta").getAsString();
                JsonArray condicionalAlternativasJson = condicionalJson.getAsJsonArray("alternativas");
                String[] condicionalAlternativas = new String[condicionalAlternativasJson.size()];
                int condAlternativaIndex = 0;
                for (JsonElement alternativaElement : condicionalAlternativasJson) {
                    condicionalAlternativas[condAlternativaIndex++] = alternativaElement.getAsString();
                }
                int nivelDeConfianca = condicionalJson.get("nivelDeConfianca").getAsInt();


                respostas[respostaIndex++] = new RespostaCondicional(condicionalPergunta, condicionalAlternativas,
                        nivelDeConfianca);
            } else {
                respostas[respostaIndex++] = respostaElement.getAsString();
            }
        }

        JsonArray efeitosConfiancaJson = obj.getAsJsonArray("efeitosConfianca");
        int[] efeitosConfianca = new int[efeitosConfiancaJson.size()];
        int efeitoIndex = 0;
        
        for (JsonElement efeitoElement : efeitosConfiancaJson) {
            efeitosConfianca[efeitoIndex++] = efeitoElement.getAsInt();
        }

        Interacao interacao = new Interacao();
        interacao.pergunta = pergunta;
        interacao.alternativas = alternativas;
        interacao.respostas = respostas;
        interacao.efeitosConfianca = efeitosConfianca;

        return interacao;
    }
}