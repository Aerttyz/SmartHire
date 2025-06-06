# SmartHire

A arquitetura do backend do SmartHire é composta por uma API principal desenvolvida em **Spring Boot** que gerencia toda a lógica de negócios, dados de usuários, vagas e candidatos. Para as funcionalidades de Inteligência Artificial, como a análise e extração de informações de currículos, a API Spring se comunica com um **microsserviço em Flask**, que é especializado em Processamento de Linguagem Natural (PLN).

A seguir, estão documentados os principais endpoints da API Spring Boot.

---

## 📦 Recursos da API

### Autenticação (`/auth`)

Endpoints responsáveis pelo registro e login de empresas na plataforma.

-   **Cadastrar Empresa**
    -   **Método:** `POST`
    -   **Rota:** `/auth/cadastrar`
    -   **Descrição:** Registra uma nova empresa no sistema. A senha é criptografada antes de ser salva.
    -   **Corpo da Requisição:** `Empresa`

-   **Login**
    -   **Método:** `POST`
    -   **Rota:** `/auth/login`
    -   **Descrição:** Autentica uma empresa com base em email e senha e, em caso de sucesso, retorna um token JWT para ser usado em requisições subsequentes.
    -   **Corpo da Requisição:** `AuthDto`

---

### Vagas (`/vagas`)

Gerenciamento completo das vagas de emprego.

-   **Listar Vagas da Empresa Autenticada**
    -   **Método:** `GET`
    -   **Rota:** `/vagas/me`
    -   **Descrição:** Retorna todas as vagas pertencentes à empresa que está autenticada via token JWT.

-   **Listar Todas as Vagas (Geral)**
    -   **Método:** `GET`
    -   **Rota:** `/vagas`
    -   **Descrição:** Retorna uma lista de todas as vagas cadastradas no sistema.

-   **Buscar Vaga por ID**
    -   **Método:** `GET`
    -   **Rota:** `/vagas/id/{id}`
    -   **Descrição:** Busca e retorna uma vaga específica pelo seu UUID.

-   **Criar Nova Vaga**
    -   **Método:** `POST`
    -   **Rota:** `/vagas`
    -   **Descrição:** Cria uma nova vaga associada à empresa autenticada. Os pesos das competências (habilidades, experiência, etc.) devem somar 1.0.
    -   **Corpo da Requisição:** `VagaDto`

-   **Atualizar Vaga**
    -   **Método:** `PUT`
    -   **Rota:** `/vagas/{id}`
    -   **Descrição:** Atualiza os dados de uma vaga existente.
    -   **Corpo da Requisição:** `VagaDto`

-   **Deletar Vaga**
    -   **Método:** `DELETE`
    -   **Rota:** `/vagas/{id}`
    -   **Descrição:** Remove uma vaga do sistema pelo seu UUID.

-   **Obter Pontuação dos Candidatos**
    -   **Método:** `GET`
    -   **Rota:** `/vagas/{idVaga}/pontuacoes`
    -   **Descrição:** Endpoint que se comunica com o microsserviço Flask para calcular e retornar a pontuação de compatibilidade de todos os candidatos aplicados a uma vaga específica.

-   **Notificar Candidatos Inaptos**
    -   **Método:** `POST`
    -   **Rota:** `/vagas/{vagaId}/enviar-emails-inaptos`
    -   **Descrição:** Envia um e-mail para todos os candidatos de uma vaga cuja pontuação de compatibilidade foi inferior à pontuação mínima definida para a vaga.

---

### Currículos (`/curriculos`)

Endpoint central para a análise de currículos com IA.

-   **Analisar e Salvar Currículos**
    -   **Método:** `POST`
    -   **Rota:** `/curriculos/analisar-curriculos/{idVaga}`
    -   **Descrição:** Recebe o caminho de uma pasta no servidor contendo arquivos de currículo em PDF. A API envia esses arquivos para o microsserviço Flask, que extrai entidades (nome, email, habilidades, etc.) usando PLN. As informações extraídas são então salvas no banco de dados, criando um novo `Curriculo` e um `Candidato` associado à vaga (`idVaga`).
    -   **Corpo da Requisição:** `{ "path": "caminho/para/pasta" }`

---

### Fases do Processo Seletivo (`/fases`)

Gerenciamento das etapas (fases) de um processo seletivo para uma vaga.

-   **Adicionar Fases a uma Vaga**
    -   **Método:** `POST`
    -   **Rota:** `/fases/{id}/fases` (onde `{id}` é o ID da vaga)
    -   **Descrição:** Cadastra uma ou mais fases (ex: "Triagem", "Entrevista Técnica") para uma vaga específica.
    -   **Corpo da Requisição:** `List<FaseDto>`

-   **Mover Candidatos entre Fases**
    -   **Método:** `POST`
    -   **Rota:** `/fases/{idFase}/candidato`
    -   **Descrição:** Move um ou mais candidatos para uma determinada fase do processo seletivo. Os candidatos são notificados por e-mail sobre o avanço.
    -   **Corpo da Requisição:** `List<UUID>` (lista de IDs dos candidatos)

-   **Listar Fases de uma Vaga**
    -   **Método:** `GET`
    -   **Rota:** `/fases/vaga/{idVaga}`
    -   **Descrição:** Retorna todas as fases de uma vaga específica, ordenadas.

-   **Listar Candidatos em uma Fase**
    -   **Método:** `GET`
    -   **Rota:** `/fases/candidato/fase/{idFase}`
    -   **Descrição:** Retorna todos os candidatos que estão atualmente em uma fase específica.

---

### Empresas (`/empresas`)

Endpoints para o gerenciamento de dados das empresas.

-   **Listar Todas as Empresas**
    -   **Método:** `GET`
    -   **Rota:** `/empresas`
    -   **Descrição:** Retorna uma lista de todas as empresas cadastradas.

-   **Atualizar Empresa Autenticada**
    -   **Método:** `PUT`
    -   **Rota:** `/empresas/me`
    -   **Descrição:** Permite que a empresa autenticada atualize seus próprios dados cadastrais.
    -   **Corpo da Requisição:** `EmpresaRequestDTO`

-   **Deletar Empresa Autenticada**
    -   **Método:** `DELETE`
    -   **Rota:** `/empresas/me`
    -   **Descrição:** Permite que a empresa autenticada exclua seu próprio cadastro do sistema.
