package br.com.alura.screenmatch.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        // Substitua pela sua chave de API válida da OpenAI
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .prompt("Traduza para o português o texto: " + texto)
                .maxTokens(100)
                .temperature(0.5)
                .build();


        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText().trim();
    }
}
