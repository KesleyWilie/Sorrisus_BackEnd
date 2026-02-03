package com.ifpb.sorrisus.security.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - CustomAuthenticationEntryPoint")
class CustomAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private CustomAuthenticationEntryPoint entryPoint;

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private DelegatingServletOutputStream servletOutputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        entryPoint = new CustomAuthenticationEntryPoint();

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        servletOutputStream = new DelegatingServletOutputStream();

        when(response.getWriter()).thenReturn(printWriter);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    private String collectedOutput() {
        printWriter.flush();
        String fromWriter = stringWriter.toString();
        String fromStream = new String(servletOutputStream.toByteArray(), StandardCharsets.UTF_8);
        return fromWriter + fromStream;
    }

    @Test
    @DisplayName("Deve retornar status 401 quando não autenticado")
    void deveRetornarStatus401() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/pacientes");
        AuthenticationException exception = new AuthenticationException("Não autenticado") {};

        entryPoint.commence(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertThat(collectedOutput()).contains("/api/pacientes");
    }

    @Test
    @DisplayName("Deve incluir URI na resposta de erro")
    void deveIncluirUriNaResposta() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/dentistas/1");
        AuthenticationException exception = new AuthenticationException("Token inválido") {};

        entryPoint.commence(request, response, exception);

        assertThat(collectedOutput()).contains("/api/dentistas/1");
    }

    @Test
    @DisplayName("Deve incluir mensagem de erro na resposta")
    void deveIncluirMensagemDeErro() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/recepcionistas");
        AuthenticationException exception = new AuthenticationException("Credenciais inválidas") {};

        entryPoint.commence(request, response, exception);

        assertThat(collectedOutput()).contains("Token ausente, inválido ou expirado");
    }

    @Test
    @DisplayName("Deve definir content type como JSON")
    void deveDefinirContentTypeComoJson() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/usuarios");
        AuthenticationException exception = new AuthenticationException("Erro") {};

        entryPoint.commence(request, response, exception);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private static class DelegatingServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        @Override
        public void write(int b) throws IOException {
            buffer.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) { /* no-op */ }

        public byte[] toByteArray() {
            return buffer.toByteArray();
        }
    }
}