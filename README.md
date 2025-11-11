# Sorrisus - Sistema Odontológico

## Sobre o projeto

O **Sorrisus** é um sistema odontológico desenvolvido como parte de um projeto acadêmico, com o objetivo de auxiliar na gestão de clínicas odontológicas. Ele permite o gerenciamento de informações relacionadas a **usuários**, **pacientes**, **dentistas** e **recepcionistas**, centralizando os dados em um único sistema.

O sistema está sendo desenvolvido em **Java com Spring Boot**, utilizando **MySQL** como banco de dados relacional, e segue uma **arquitetura em camadas** (Controller → Service → Repository → Model).

## Funcionalidades atuais

Atualmente, o backend do sistema conta com:

* **Entidades modeladas:**

  * `Usuario`
  * `Paciente`
  * `Dentista`
  * `Recepcionista`
  * `Role` (enum de papéis de usuário)
* **CRUD completo (Create, Read, Update, Delete)** implementado para todas as entidades principais.
* **Banco de dados MySQL configurado** com criação automática das tabelas via JPA/Hibernate.
* **Variáveis de ambiente** utilizadas para armazenar URL, usuário e senha do banco de dados.
* **Projeto configurado com Lombok** (para reduzir boilerplate) e **Spring DevTools** (para recarregamento automático durante o desenvolvimento).

## Tecnologias utilizadas

* Java 17
* Spring Boot 3.5.6
* Spring Data JPA
* MySQL
* Lombok
* Maven

## Configuração do Projeto

### 1. Criar banco de dados MySQL

Antes de iniciar o projeto, crie um banco de dados com o nome `sorrisus_db`:

```sql
CREATE DATABASE sorrisus_db;
```

### 2. Configurar variáveis de ambiente

O sistema usa variáveis de ambiente para dados sensíveis:

**Windows (CMD):**
```cmd
set DB_URL=jdbc:mysql://localhost:3306/sorrisus_db?useSSL=false&serverTimezone=UTC
set DB_USER=seu_usuario
set DB_PASS=sua_senha
```

**Linux/Mac (bash):**
```bash
export DB_URL=jdbc:postgresql://localhost:5432/divulgafacil
export DB_USER=seu_usuario
export DB_PASS=sua_senha
```

Ou configure diretamente em `src/main/resources/application.properties`.

## Como subir via Docker

Siga estes passos para executar a aplicação e o banco de dados com Docker Compose sem expor credenciais no repositório.

#### 1. Criar arquivo .env (variáveis sensíveis)
Na raiz do projeto crie um arquivo chamado `.env` e defina as variáveis abaixo:

```
# .env 
MYSQL_ROOT_PASSWORD=seu_mysql_root_password
DB_USER=seu_usuario_app
DB_PASS=sua_senha_app
DB_URL=jdbc:mysql://db_sorrisus:3306/sorrisus_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

Variáveis necessárias:
- MYSQL_ROOT_PASSWORD
- DB_USER
- DB_PASS
- DB_URL (opcional se sua aplicação obtém a URL de outra fonte, mas recomendado)


#### 2. Subir os containers 
Com a Engine do Docker rodando. Abra o terminal na raiz do projeto e execute:

```
docker compose up --build
```

Para rodar em background:

```
docker compose up -d --build
```

#### 3. Verificar status e logs
Verifique containers em execução:

```
docker compose ps
```

Acompanhe logs (aplicação ou banco):

```
docker compose logs -f sorrisus_app
docker compose logs -f db_sorrisus
```

#### 4. Parar e remover containers
Parar sem remover:

```
docker compose stop
```

Parar e remover volumes/containers:

```
docker compose down -v
```

#### 5. Observações importantes
- Use o host `db_sorrisus` na URL JDBC (nome do serviço definido no docker-compose).      

## Estrutura do projeto

* `model/` → classes de domínio e entidades JPA
* `repository/` → interfaces de persistência (Spring Data JPA)
* `service/` → regras de negócio e comunicação entre controller e repository
* `controller/` → endpoints REST expostos para o Postman ou frontend
* `resources/application.yml` → configurações do banco de dados e variáveis de ambiente

## Testes

O sistema pode ser testado via **Postman**, utilizando os endpoints REST disponíveis para cada entidade. O json da collection se encontra em `collection\Sorrisus_API_Collection.json.json`. 
Cada entidade possui operações para:

* Criar (`POST`)
* Listar (`GET`)
* Buscar por ID (`GET /{id}`)
* Atualizar (`PUT /{id}`)
* Deletar (`DELETE /{id}`)

## Próximos passos

* Implementar autenticação e controle de acesso (Spring Security)
* Criar relacionamento entre entidades (ex: Paciente ↔ Dentista)
* Desenvolver o frontend 
* Criar testes automatizados e documentação de API (Swagger)

---

## 👥 Contribuidores
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/estertrvs" title="GitHub">
        <img src="https://avatars.githubusercontent.com/u/141650957?v=4" width="100px;" alt="Foto de Ester"/><br>
        <sub>
          <b>Ester Trevisan</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/analiciafsoares" title="GitHub">
        <img src="https://avatars.githubusercontent.com/u/144076062?v=4" width="100px;" alt="Foto de Ana"/><br>
        <sub>
          <b>Ana Licia Soares</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Joaopaulomedeirosdesouza" title="GitHub">
        <img src="https://avatars.githubusercontent.com/u/148402008?v=4" width="100px;" alt="Foto de João Paulo"/><br>
        <sub>
          <b>João Paulo Medeiros</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/KesleyWilie" title="GitHub">
        <img src="https://avatars.githubusercontent.com/u/144160126?v=4" width="100px;" alt="Foto de Kesley"/><br>
        <sub>
          <b>Kesley Wilie</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/GeorgeAdOliveira" title="GitHub">
        <img src="https://avatars.githubusercontent.com/u/143577407?v=4" width="100px;" alt="Foto de George"/><br>
        <sub>
          <b>George Oliveira</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

---

**Instituto Federal da Paraíba** - Disciplina de **Projeto II**.