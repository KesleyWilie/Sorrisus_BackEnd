package com.ifpb.sorrisus.security.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

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

@DisplayName("Teste Unitário - CustomAccessDeniedHandler")
class CustomAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private CustomAccessDeniedHandler accessDeniedHandler;

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private DelegatingServletOutputStream servletOutputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        accessDeniedHandler = new CustomAccessDeniedHandler();

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
    @DisplayName("Deve retornar status 403 quando acesso negado")
    void deveRetornarStatus403() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/admin/usuarios");
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        accessDeniedHandler.handle(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertThat(collectedOutput()).contains("/api/admin/usuarios");
    }

    @Test
    @DisplayName("Deve incluir URI na resposta de acesso negado")
    void deveIncluirUriNaResposta() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/admin/dashboard");
        AccessDeniedException exception = new AccessDeniedException("Sem permissão");

        accessDeniedHandler.handle(request, response, exception);

        assertThat(collectedOutput()).contains("/api/admin/dashboard");
    }

    @Test
    @DisplayName("Deve incluir mensagem de erro na resposta")
    void deveIncluirMensagemDeErro() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/admin/config");
        AccessDeniedException exception = new AccessDeniedException("Você não tem permissão para acessar este recurso");

        accessDeniedHandler.handle(request, response, exception);

        assertThat(collectedOutput()).contains("Você não tem permissão para acessar este recurso");
    }

    @Test
    @DisplayName("Deve definir content type como JSON")
    void deveDefinirContentTypeComoJson() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/admin");
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        accessDeniedHandler.handle(request, response, exception);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    @DisplayName("Deve lidar com diferentes URIs de acesso negado")
    void deveAceitarDiferentesUris() throws ServletException, IOException {
        String[] uris = {"/api/admin/users", "/api/admin/config", "/api/admin/reports"};

        for (String uri : uris) {
            when(request.getRequestURI()).thenReturn(uri);
            AccessDeniedException exception = new AccessDeniedException("Acesso negado");

            accessDeniedHandler.handle(request, response, exception);

            assertThat(collectedOutput()).contains(uri);
            // limpar buffers entre iterações
            stringWriter.getBuffer().setLength(0);
            servletOutputStream.reset();
        }
    }

    // mesma auxiliar usada no outro teste
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

        public void reset() {
            buffer.reset();
        }
    }
}