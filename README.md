# BookManager — API de Gerenciamento de Livros

Back-end do **BookManager**, sistema para organizar o cadastro e a consulta de livros, autores e empréstimos de um pequeno acervo (biblioteca pequena, sala de leitura ou acervo pessoal).

Trabalho final da disciplina **Desenvolvimento Back-end II** — TSI-4AN.

- **Aluna:** Gabriela Lima
- **Professora:** Maria Eduarda Carvalho

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.2 |
| Arquitetura | Controller, Service e Repository |
| Persistência | Spring Data JPA e Hibernate |
| Banco de dados | PostgreSQL |
| Validação | Jakarta Bean Validation |
| Documentação | springdoc-openapi (Swagger UI) |
| Build | Maven |
| Comunicação | API REST com troca de dados em JSON |

## Funcionalidades

- **Autores:** cadastrar, listar, consultar, atualizar e excluir.
- **Livros:** cadastrar, listar, consultar, atualizar e excluir, sempre vinculados a um autor.
- **Livros de um autor:** consultar todos os livros associados a um autor.
- **Empréstimos:** registrar empréstimo, listar (com filtro por status) e registrar devolução.
- **Pesquisa:** localizar livros por título, autor ou gênero (busca parcial, sem diferenciar maiúsculas de minúsculas).
- **Exportação:** baixar o arquivo `livros.json` com a listagem de livros cadastrados.

## Regras de negócio

- Não é possível registrar um empréstimo quando a quantidade disponível do livro é zero.
- Ao registrar um empréstimo, a quantidade disponível é reduzida em 1.
- Ao registrar a devolução, a quantidade disponível é aumentada em 1.
- Um empréstimo já devolvido não pode ser devolvido novamente.
- A data prevista de devolução não pode ser anterior à data de hoje.
- No cadastro de um livro, a quantidade disponível é igual à quantidade total.
- Ao alterar a quantidade total de um livro, a diferença é aplicada à quantidade disponível. Não é possível reduzir o total abaixo do número de exemplares emprestados.
- Não é possível excluir um autor que possui livros cadastrados.
- Não é possível excluir um livro que possui empréstimos registrados.

## Modelo de dados

```
Autor (1) ──── (N) Livro (1) ──── (N) Emprestimo
```

| Entidade | Atributos |
|---|---|
| **Autor** | id, nome, nacionalidade |
| **Livro** | id, titulo, isbn, anoPublicacao, genero, quantidadeTotal, quantidadeDisponivel, autor |
| **Emprestimo** | id, dataEmprestimo, dataPrevistaDevolucao, dataDevolucao, status (`ATIVO` ou `DEVOLVIDO`), livro |

## Estrutura do projeto

```
src/main/java/com/gabriela/bookmanager/
├── config/        # CORS e configuração do Swagger
├── controller/    # Endpoints REST
├── dto/           # Objetos de entrada e saída da API
├── exception/     # Exceções e tratamento global de erros
├── model/         # Entidades JPA
├── repository/    # Acesso ao banco (Spring Data JPA)
└── service/       # Regras de negócio
```

## Como executar

### Pré-requisitos

- JDK 21
- Maven (ou use o wrapper `mvnw` do projeto)
- PostgreSQL

### 1. Criar o banco de dados

```sql
CREATE DATABASE bookmanager;
```

As tabelas são criadas automaticamente pelo Hibernate na primeira execução.

### 2. Configurar a conexão

Em `src/main/resources/application.properties`, ajuste usuário e senha do seu PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmanager
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

### 3. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

No Windows (PowerShell ou CMD): `mvnw.cmd spring-boot:run`. Também é possível executar a classe `BookmanagerApplication` pela IDE.

A API fica disponível em `http://localhost:8080`.

## Documentação (Swagger)

Com a aplicação rodando, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI (JSON):** http://localhost:8080/v3/api-docs

## Endpoints

### Autores

| Método | URL | Descrição |
|---|---|---|
| GET | `/autores` | Lista os autores |
| GET | `/autores/{id}` | Consulta um autor |
| GET | `/autores/{id}/livros` | Lista os livros do autor |
| POST | `/autores` | Cadastra um autor |
| PUT | `/autores/{id}` | Atualiza um autor |
| DELETE | `/autores/{id}` | Exclui um autor |

### Livros

| Método | URL | Descrição |
|---|---|---|
| GET | `/livros` | Lista os livros. Filtros opcionais: `titulo`, `autor`, `genero` |
| GET | `/livros/{id}` | Consulta um livro |
| POST | `/livros` | Cadastra um livro |
| PUT | `/livros/{id}` | Atualiza um livro |
| DELETE | `/livros/{id}` | Exclui um livro |
| GET | `/livros/exportar` | Baixa o arquivo `livros.json` |

### Empréstimos

| Método | URL | Descrição |
|---|---|---|
| GET | `/emprestimos` | Lista os empréstimos. Filtro opcional: `status` (`ATIVO` ou `DEVOLVIDO`) |
| POST | `/emprestimos` | Registra um empréstimo |
| PUT | `/emprestimos/{id}/devolucao` | Registra a devolução |

## Exemplos de requisição

**Cadastrar autor** — `POST /autores`

```json
{
  "nome": "Machado de Assis",
  "nacionalidade": "Brasileira"
}
```

**Cadastrar livro** — `POST /livros`

```json
{
  "titulo": "Dom Casmurro",
  "isbn": "9788535911664",
  "anoPublicacao": 1899,
  "genero": "Romance",
  "quantidadeTotal": 3,
  "autorId": 1
}
```

**Registrar empréstimo** — `POST /emprestimos`

```json
{
  "livroId": 1,
  "dataPrevistaDevolucao": "2026-11-27"
}
```

**Pesquisar livros**

```
GET /livros?titulo=dom
GET /livros?autor=machado
GET /livros?genero=romance
```

## Exportação de dados (JSON)

O endpoint `GET /livros/exportar` faz o download do arquivo `livros.json`, com título, ISBN, ano de publicação, gênero, quantidades (total e disponível) e nome do autor de cada livro. Para baixar o arquivo, abra a URL diretamente no navegador: http://localhost:8080/livros/exportar

## Tratamento de erros

Os erros são devolvidos em JSON, com o código HTTP correspondente:

```json
{
  "timestamp": "2026-11-13T00:18:25.321",
  "status": 400,
  "erro": "Não há exemplares disponíveis deste livro para empréstimo"
}
```

| Código | Quando acontece |
|---|---|
| 400 | Dados inválidos ou violação de regra de negócio |
| 404 | Autor, livro ou empréstimo não encontrado |

## Integração com o front-end

O CORS está liberado para `http://localhost:3000`, endereço padrão do front-end em Next.js durante o desenvolvimento. Para usar outra origem, ajuste a classe `config/WebConfig.java`.
