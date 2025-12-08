# Sorrisus - Sistema Odontológico

## Sobre o projeto

O **Sorrisus** é um sistema odontológico desenvolvido como parte de um projeto acadêmico, com o objetivo de auxiliar na gestão de clínicas odontológicas. Ele permite o gerenciamento de informações relacionadas a **usuários**, **pacientes**, **dentistas** e **recepcionistas**, centralizando os dados em um único sistema.

O sistema está sendo desenvolvido em **Java com Spring Boot**, utilizando **MySQL** como banco de dados relacional, e segue uma **arquitetura em camadas** (Controller → Service → Repository → Model).

Com a evolução do projeto, o backend agora possui **autenticação completa com JWT**, **rotas protegidas**, **controle de acesso por papéis (roles)** e **middleware de segurança** usando Spring Security.


## 🛡️ Autenticação JWT (nova funcionalidade)

O sistema agora implementa um fluxo completo de autenticação baseado em JWT (**JSON Web Token**).

### Como funciona?

1. O usuário faz login enviando **email + senha** para `/api/auth/login`.
2. O backend valida as credenciais.
3. Se válidas, gera um **token JWT**, assinado com chave secreta.
4. O token precisa ser enviado em todas as requisições protegidas no header:

```

Authorization: Bearer \<token\>

```

### Rotas públicas (sem autenticação)

- `POST /api/auth/login`
- `POST /api/usuarios/cadastro`
- `POST /api/dentistas/cadastro`
- `POST /api/pacientes/cadastro`
- `POST /api/recepcionistas/cadastro`

Todas as demais rotas agora **exigem autenticação**.

### Middleware de segurança

- Filtro JWT (`JwtAuthenticationFilter`) validando tokens a cada request.
- Classe de usuários customizada (`CustomUserDetailsService`).
- Configuração de segurança centralizada (`SecurityConfig`).
- Controle de sessão 100% stateless.

## 🌐 CORS configurado para comunicação com o front-end

O CORS foi configurado globalmente para permitir que o front-end (porta 5173, Vite + React) possa consumir a API:

Origem permitida:
```

http://localhost:5173

```

Métodos liberados:
```

GET, POST, PUT, DELETE, OPTIONS

```

Headers permitidos:
```

Authorization, Content-Type

````

Credenciais habilitadas (para envio de token).

## Funcionalidades atuais

Atualmente, o backend do sistema conta com:

* **Entidades modeladas:**
  * `Usuario`
  * `Paciente`
  * `Dentista`
  * `Recepcionista`
  * `Role` (enum de papéis de usuário)

* **CRUD completo** (Create, Read, Update, Delete) para todas as entidades.
* **Autenticação JWT** com filtro, validação e roles.
* **Rotas protegidas por segurança** via Spring Security.
* **Tratamento global de exceções**.
* **Banco MySQL configurado** com criação automática via JPA/Hibernate.
* **Uso de variáveis de ambiente** para dados sensíveis.
* **Projeto configurado com Lombok** e **Spring DevTools**.

## 📅 Fluxo de Agendamento, Consulta e Prontuário (Novo)

O sistema implementa as regras de negócio para **Manter Agenda** e **Manter Prontuário Eletrônico**. Abaixo está o fluxo correto para realizar um atendimento clínico dentro da API:

### 1. Criar um Agendamento
O processo inicia com a reserva de um horário na agenda. O sistema valida se o horário está disponível para o dentista selecionado.

* **Endpoint:** `POST /api/agendamentos`
* **Ação:** Cria uma reserva provisória. O status inicial é "não confirmado".

### 2. Confirmar o Agendamento
Para que o agendamento se torne efetivo, ele deve ser confirmado. Ao confirmar, o sistema **automaticamente gera uma Consulta**.

* **Endpoint:** `POST /api/agendamentos/{id}/confirmar`
* **Regra de Negócio:** O sistema verifica novamente conflitos de horário. Se válido, muda o status do agendamento para `CONFIRMADO` e cria um registro na tabela `Consulta`.

### 3. Realizar a Consulta e Atualizar Prontuário
A consulta criada automaticamente no passo anterior é onde o atendimento clínico ocorre. O dentista pode atualizar o status da consulta e inserir as anotações no prontuário.

* **Endpoint:** `PUT /api/consultas/{id}`
* **Funcionalidade:** Permite atualizar o status (ex: `REALIZADA`), adicionar observações e preencher o objeto `prontuario` vinculado.
* **Exemplo de Payload:**
    ```json
    {
      "status": "REALIZADA",
      "observacao": "Paciente relatou dor no dente 16",
      "prontuario": {
        "observacoes": "Realizado procedimento de restauração."
      }
    }
    ```

### Resumo dos Endpoints Principais

| Entidade | Ação | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **Agendamento** | Agendar | `POST /api/agendamentos` | Reserva o horário. |
| **Agendamento** | Confirmar | `POST .../{id}/confirmar` | Valida e cria a Consulta. |
| **Consulta** | Atualizar | `PUT /api/consultas/{id}` | Edita status e prontuário. |
| **Consulta** | Cancelar | `DELETE /api/consultas/{id}` | Cancela o atendimento. |
| **Prontuário** | Consultar | `GET /api/prontuarios/{id}` | Acessa histórico clínico. |

## Tecnologias utilizadas

* Java 17
* Spring Boot 3.5.6
* Spring Security
* JWT (jjwt-api / jjwt-impl / jjwt-jackson)
* Spring Data JPA
* MySQL
* Lombok
* Maven

## Configuração do Projeto

### 1. Criar banco de dados MySQL

```sql
CREATE DATABASE sorrisus_db;
````

-----

### 2\. Configurar variáveis de ambiente

Agora o sistema utiliza **4 variáveis principais**:

```
DB_URL=
DB_USER=
DB_PASS=
JWT_SECRET=
JWT_EXPIRATION_MS=
```

### Exemplos

#### **Windows (CMD)**

```cmd
set DB_URL=jdbc:mysql://localhost:3306/sorrisus_db?useSSL=false&serverTimezone=UTC
set DB_USER=seu_usuario
set DB_PASS=sua_senha
set JWT_SECRET=sua_chave_secreta_muito_segura
set JWT_EXPIRATION_MS=3600000
```

#### **Linux/Mac (bash)**

```bash
export DB_URL=jdbc:mysql://localhost:3306/sorrisus_db?useSSL=false&serverTimezone=UTC
export DB_USER=seu_usuario
export DB_PASS=sua_senha
export JWT_SECRET=sua_chave_secreta_muito_segura
export JWT_EXPIRATION_MS=3600000
```

## Como subir via Docker

### 1\. Criar arquivo `.env` na raiz

```
MYSQL_ROOT_PASSWORD=seu_mysql_root_password
DB_USER=seu_usuario_app
DB_PASS=sua_senha_app
DB_URL=jdbc:mysql://db_sorrisus:3306/sorrisus_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

JWT_SECRET=sua_chave_super_secreta
JWT_EXPIRATION_MS=3600000
```

-----

### 2\. Subir containers

```
docker compose up --build
```

Em background:

```
docker compose up -d --build
```

-----

### 3\. Verificar status

```
docker compose ps
```

Logs:

```
docker compose logs -f sorrisus_app
docker compose logs -f db_sorrisus
```

-----

### 4\. Parar containers

```
docker compose stop
```

Remover:

```
docker compose down -v
```

## Estrutura do projeto

  * `model/` → entidades JPA
  * `repository/` → persistência
  * `service/` → regras de negócio
  * `controller/` → endpoints REST
  * `security/` → autenticação e JWT
  * `config/` → configs globais (CORS, Security)
  * `resources/application.yml` → configurações

## Testes

O sistema pode ser testado via **Postman**, utilizando a collection disponível em:

```
collection/Sorrisus_API_Collection.json
```

Cada entidade possui operações:

  * Criar (`POST`)
  * Listar (`GET`)
  * Buscar por ID (`GET`)
  * Atualizar (`PUT`)
  * Deletar (`DELETE`)

## Testes unitários

Os testes podem ser executados com:

```
mvn test
```

Incluem testes de serviços e agora também testes de autenticação JWT.

## Próximos passos

  * Criar relacionamento entre entidades (ex: Paciente ↔ Dentista)
  * Criar dashboard no front-end
  * Implementar auditoria de ações

## 👥 Contribuidores

\<table\>
\<tr\>
\<td align="center"\>
\<a href="https://github.com/estertrvs" title="GitHub"\>
\<img src="https://avatars.githubusercontent.com/u/141650957?v=4" width="100px;" alt="Foto de Ester"/\><br>
\<sub\>
\<b\>Ester Trevisan\</b\>
\</sub\>
\</a\>
\</td\>
\<td align="center"\>
\<a href="https://github.com/analiciafsoares" title="GitHub"\>
\<img src="https://avatars.githubusercontent.com/u/144076062?v=4" width="100px;" alt="Foto de Ana"/\><br>
\<sub\>
\<b\>Ana Licia Soares\</b\>
\</sub\>
\</a\>
\</td\>
\<td align="center"\>
\<a href="https://github.com/Joaopaulomedeirosdesouza" title="GitHub"\>
\<img src="https://avatars.githubusercontent.com/u/148402008?v=4" width="100px;" alt="Foto de João Paulo"/\><br>
\<sub\>
\<b\>João Paulo Medeiros\</b\>
\</sub\>
\</a\>
\</td\>
\<td align="center"\>
\<a href="https://github.com/KesleyWilie" title="GitHub"\>
\<img src="https://avatars.githubusercontent.com/u/144160126?v=4" width="100px;" alt="Foto de Kesley"/\><br>
\<sub\>
\<b\>Kesley Wilie\</b\>
\</sub\>
\</a\>
\</td\>
\<td align="center"\>
\<a href="https://github.com/GeorgeAdOliveira" title="GitHub"\>
\<img src="https://avatars.githubusercontent.com/u/143577407?v=4" width="100px;" alt="Foto de George"/\><br>
\<sub\>
\<b\>George Oliveira\</b\>
\</sub\>
\</a\>
\</td\>
\</tr\>
\</table\>

-----

**Instituto Federal da Paraíba** — Disciplina de **Projeto II**.