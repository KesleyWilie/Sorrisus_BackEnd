package com.ifpb.sorrisus.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Teste Unitário - JWTUtil")
class JWTUtilTest {

    private JWTUtil jwtUtil;
    private String validToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil();
        validToken = jwtUtil.generateToken("maria@sorrisus.com", "ROLE_RECEPCIONISTA");
        invalidToken = "token.invalido.fake";
    }

    @Test
    @DisplayName("Deve gerar token JWT válido")
    void deveGerarTokenValido() {
        String token = jwtUtil.generateToken("joao@sorrisus.com", "ROLE_PACIENTE");

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Deve extrair username do token válido")
    void deveExtrairUsernameDoToken() {
        String username = jwtUtil.getUsernameFromToken(validToken);

        assertThat(username).isEqualTo("maria@sorrisus.com");
    }

    @Test
    @DisplayName("Deve validar token válido")
    void deveValidarTokenValido() {
        boolean valido = jwtUtil.validateToken(validToken);

        assertThat(valido).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false ao validar token inválido")
    void deveRetornarFalsoAoValidarTokenInvalido() {
        boolean valido = jwtUtil.validateToken(invalidToken);

        assertThat(valido).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false ao validar token nulo")
    void deveRetornarFalsoAoValidarTokenNulo() {
        boolean valido = jwtUtil.validateToken(null);

        assertThat(valido).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false ao validar token vazio")
    void deveRetornarFalsoAoValidarTokenVazio() {
        boolean valido = jwtUtil.validateToken("");

        assertThat(valido).isFalse();
    }

    @Test
    @DisplayName("Deve extrair role do token válido")
    void deveExtrairRoleDoToken() {
        String role = jwtUtil.getRoleFromToken(validToken);

        assertThat(role).isEqualTo("ROLE_RECEPCIONISTA");
    }

    @Test
    @DisplayName("Deve lançar exceção ao extrair username de token inválido")
    void deveLancarExcecaoAoExtrairUsernameDeTokenInvalido() {
        assertThatThrownBy(() -> jwtUtil.getUsernameFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao extrair role de token inválido")
    void deveLancarExcecaoAoExtrairRoleDeTokenInvalido() {
        assertThatThrownBy(() -> jwtUtil.getRoleFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("Deve gerar token com diferentes roles")
    void deveGerarTokenComDiferentesRoles() {
        String[] roles = {"ROLE_DENTISTA", "ROLE_RECEPCIONISTA", "ROLE_PACIENTE"};

        for (String role : roles) {
            String token = jwtUtil.generateToken("user@sorrisus.com", role);
            String extractedRole = jwtUtil.getRoleFromToken(token);

            assertThat(extractedRole).isEqualTo(role);
        }
    }

    @Test
    @DisplayName("Deve gerar tokens diferentes para usuários diferentes")
    void deveGerarTokensDiferentesParaUsuariosDiferentes() {
        String token1 = jwtUtil.generateToken("user1@sorrisus.com", "ROLE_RECEPCIONISTA");
        String token2 = jwtUtil.generateToken("user2@sorrisus.com", "ROLE_RECEPCIONISTA");

        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtUtil.getUsernameFromToken(token1)).isEqualTo("user1@sorrisus.com");
        assertThat(jwtUtil.getUsernameFromToken(token2)).isEqualTo("user2@sorrisus.com");
    }

    @Test
    @DisplayName("Deve validar tokens com timings diferentes")
    void deveValidarTokensComTimingsDiferentes() throws InterruptedException {
        String token = jwtUtil.generateToken("user@sorrisus.com", "ROLE_PACIENTE");
        
        assertThat(jwtUtil.validateToken(token)).isTrue();
        
        Thread.sleep(100);
        
        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("Deve gerar tokens únicos para mesma role")
    void deveGerarTokensUnicosParaMesmaRole() {
        String token1 = jwtUtil.generateToken("user@sorrisus.com", "ROLE_DENTISTA");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // Ignorar
        }

        String token2 = jwtUtil.generateToken("user@sorrisus.com", "ROLE_DENTISTA");

        assertThat(token1).isNotEqualTo(token2);
        assertThat(jwtUtil.validateToken(token1)).isTrue();
        assertThat(jwtUtil.validateToken(token2)).isTrue();
    }

    @Test
    @DisplayName("Deve retornar null ao extrair role de token sem claim")
    void deveRetornarNullAoExtrairRoleSemClaim() {
        String token = jwtUtil.generateToken("user@sorrisus.com", null);
        String role = jwtUtil.getRoleFromToken(token);

        assertThat(role).isNull();
    }

    @Test
    @DisplayName("Deve validar token com caracteres especiais no username")
    void deveValidarTokenComCaracteresEspeciais() {
        String token = jwtUtil.generateToken("user+test@sorrisus.com", "ROLE_PACIENTE");
        
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("user+test@sorrisus.com");
    }

    @Test
    @DisplayName("Deve retornar false ao validar token vazio após Bearer")
    void deveRetornarFalsoAoValidarTokenVazioAposBearerR() {
        String tokenVazio = "";
        
        assertThat(jwtUtil.validateToken(tokenVazio)).isFalse();
    }
}