package com.ifpb.sorrisus.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class WhatsAppService {

    @Value("${zapi.instance}")
    private String instance;

    @Value("${zapi.token}")
    private String token;

    @Value("${zapi.client-token}")
    private String clientToken;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(30))
            .build();

    public void sendMessage(String phone, String message) {

        MediaType mediaType = MediaType.parse("application/json");

        String json = String.format(
                "{\"phone\":\"%s\",\"message\":\"%s\"}",
                phone,
                message.replace("\"", "\\\"") // evita erro se a mensagem tiver aspas
        );

        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url("https://api.z-api.io/instances/" + instance + "/token/" + token + "/send-text")
                .post(body)
                .addHeader("client-token", clientToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sem corpo de erro";
                throw new RuntimeException("Erro ao enviar WhatsApp: " + errorBody);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro de comunicação com a Z-API", e);
        }
    }
}