# OdontoSystem — API Backend

> Sistema de gestão para clínicas odontológicas. API RESTful desenvolvida com **Java 21** e **Spring Boot 3**, com autenticação JWT, controle de acesso por perfil, migrations automatizadas e integração com armazenamento em nuvem (AWS S3) e notificações via WhatsApp.

---

## Índice

- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Configuração das variáveis de ambiente](#configuração-das-variáveis-de-ambiente)
- [Executando com Docker](#executando-com-docker)
- [Executando localmente](#executando-localmente)
- [Endpoints da API](#endpoints-da-api)
- [Perfis de acesso](#perfis-de-acesso)
- [Banco de dados e migrações](#banco-de-dados-e-migrações)
- [Documentação interativa (Swagger)](#documentação-interativa-swagger)

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5 |
| Segurança | Spring Security + JWT (jjwt 0.12) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 16 |
| Migrações | Flyway |
| Armazenamento | AWS S3 (SDK v2) |
| Notificações | Evolution API (WhatsApp) |
| Documentação | SpringDoc OpenAPI / Swagger UI |
| Build | Maven Wrapper |
| Containerização | Docker + Docker Compose |
| Testes | JUnit 5 + Testcontainers |
| Utilitários | Lombok |

---

## Funcionalidades

- **Autenticação e autorização** com JWT e controle de acesso baseado em perfis (ADMIN, DENTIST, RECEPTIONIST)
- **Gestão de pacientes** com busca por nome e CPF, validação de CPF e paginação
- **Agendamentos** com verificação de conflito de horários, bloqueio de agenda e consulta de horários livres
- **Sala de espera** com visualização em tempo real dos pacientes aguardando atendimento
- **Prontuários clínicos** com soft delete e histórico completo por paciente
- **Odontograma digital** com registro do status de cada dente por paciente
- **Catálogo de procedimentos** com vinculação a prontuários
- **Arquivos de exames** com upload para AWS S3 e categorização por tipo (Radiografia, Tomografia, etc.)
- **Notificações automáticas via WhatsApp** com lembretes de consulta (via Evolution API)
- **Gestão de usuários** pelo administrador: criação, edição, ativação/desativação e reset de senha
- **Seed automático** de usuário administrador na primeira execução

---

## Arquitetura

O projeto segue uma organização por **módulos de domínio**, onde cada funcionalidade agrupa seus próprios controllers, services, repositories e DTOs:

```
src/main/java/com/odonto/odonto_system/
├── auth/           # Autenticação JWT e filtros de segurança
├── appointment/    # Agendamentos, sala de espera, bloqueios e slots livres
├── patient/        # Cadastro e gestão de pacientes
├── record/         # Prontuários clínicos
├── tooth/          # Odontograma (registro por dente)
├── procedure/      # Catálogo de procedimentos
├── exam/           # Arquivos de exames (upload S3)
├── user/           # Gestão de usuários e perfis
├── notification/   # Envio de mensagens via WhatsApp (Evolution API)
├── config/         # Seed de admin inicial
└── shared/         # Utilitários, exceções globais, storage e validações
```

---

## Pré-requisitos

- **Java 21+**
- **Maven** (ou use o `./mvnw` incluído no projeto)
- **Docker e Docker Compose** (para o banco de dados)
- Conta na **AWS** com bucket S3 configurado (para upload de exames)
- Instância da **Evolution API** (opcional — para notificações WhatsApp)

---

## Configuração das variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Banco de dados
DB_NAME=odontosystem-db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
DB_PORT=5432

# PgAdmin (interface web do banco)
PGADMIN_EMAIL=admin@email.com
PGADMIN_PASSWORD=sua_senha_pgadmin

# JWT
JWT_SECRET=sua_chave_secreta_jwt
JWT_EXPIRATION=900000

# AWS S3
AWS_ACCESS_KEY_ID=sua_access_key
AWS_SECRET_ACCESS_KEY=sua_secret_key
AWS_REGION=us-east-1
AWS_BUCKET_NAME=nome-do-seu-bucket

# Evolution API (WhatsApp) — opcional
EVOLUTION_API_URL=http://localhost:8080
EVOLUTION_API_KEY=sua_api_key
EVOLUTION_INSTANCE=odontosystem
```

---

## Executando com Docker

Suba o banco de dados e o PgAdmin com Docker Compose:

```bash
docker compose up -d
```

Isso inicia:
- **PostgreSQL 16** na porta configurada em `DB_PORT`
- **PgAdmin 4** em `http://localhost:5050`

Depois, execute a aplicação:

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080/api/v1`.

---

## Executando localmente

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/odonto-system-api.git
cd odonto-system-api

# 2. Suba o banco de dados
docker compose up -d

# 3. Configure as variáveis de ambiente (veja a seção acima)
# Você pode exportá-las no terminal ou usar um plugin de .env na IDE

# 4. Execute a aplicação
./mvnw spring-boot:run
```

### Build para produção

```bash
./mvnw clean package -DskipTests
java -jar target/odonto-system-*.jar
```

### Build com Docker

```bash
docker build -t odonto-system-api .
docker run -p 8080:8080 --env-file .env odonto-system-api
```

---

## Endpoints da API

Base URL: `/api/v1`

### Autenticação — `/auth`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/login` | Realiza login e retorna o token JWT |
| GET | `/auth/me` | Retorna os dados do usuário autenticado |

### Pacientes — `/patients`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/patients` | Cadastra um novo paciente |
| GET | `/patients` | Lista pacientes (filtros: `name`, `cpf`) |
| GET | `/patients/{id}` | Busca paciente por ID |
| PUT | `/patients/{id}` | Atualiza dados do paciente |
| DELETE | `/patients/{id}` | Remove paciente |

### Agendamentos — `/appointments`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/appointments` | Cria um agendamento |
| GET | `/appointments` | Lista agendamentos (paginado) |
| GET | `/appointments/{id}` | Busca agendamento por ID |
| PUT | `/appointments/{id}` | Atualiza agendamento |
| PATCH | `/appointments/{id}/status` | Atualiza status do agendamento |
| GET | `/appointments/waiting-room` | Lista pacientes na sala de espera |
| POST | `/appointments/block` | Bloqueia horário na agenda |
| GET | `/appointments/free-slots` | Retorna slots livres por dentista e data |

**Status disponíveis:** `AGENDADO`, `CONFIRMADO`, `AGUARDANDO`, `EM_ATENDIMENTO`, `CONCLUIDO`, `CANCELADO`, `NAO_COMPARECEU`, `BLOQUEADO`

### Prontuários — `/patients/{patientId}/records` e `/records`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/patients/{patientId}/records` | Cria prontuário para o paciente |
| GET | `/patients/{patientId}/records` | Lista prontuários do paciente |
| GET | `/records/{id}` | Busca prontuário por ID |
| PUT | `/records/{id}` | Atualiza prontuário |
| DELETE | `/records/{id}` | Remove prontuário (soft delete) |

### Odontograma — `/patients/{patientId}/teeth`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/patients/{patientId}/teeth` | Registra status de um dente |
| GET | `/patients/{patientId}/teeth` | Retorna o odontograma completo |
| PUT | `/patients/{patientId}/teeth/{toothNumber}` | Atualiza status de um dente |

### Procedimentos — `/procedures`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/procedures` | Cria procedimento no catálogo |
| GET | `/procedures` | Lista catálogo de procedimentos |
| PUT | `/procedures/{id}` | Atualiza procedimento |

### Exames — `/patients/{patientId}/exams`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/patients/{patientId}/exams` | Faz upload de arquivo de exame (multipart) |
| GET | `/patients/{patientId}/exams` | Lista exames do paciente |
| DELETE | `/exams/{id}` | Remove exame |

**Tipos de exame:** `RADIOGRAFIA`, `TOMOGRAFIA`, `FOTOGRAFIA`, `OUTROS`

### Usuários — `/users`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/users` | ADMIN | Cria usuário |
| GET | `/users` | ADMIN, RECEPTIONIST | Lista usuários (filtros: `role`, `active`) |
| PUT | `/users/{id}` | ADMIN | Atualiza usuário |
| PATCH | `/users/{id}/password` | ADMIN | Redefine senha |
| PATCH | `/users/{id}/deactivate` | ADMIN | Desativa usuário |
| PATCH | `/users/{id}/activate` | ADMIN | Ativa usuário |

---

## Perfis de acesso

| Perfil | Descrição |
|---|---|
| `ADMIN` | Acesso total: gestão de usuários, configurações e todas as funcionalidades |
| `DENTIST` | Acesso a pacientes, prontuários, odontograma, exames e seus agendamentos |
| `RECEPTIONIST` | Acesso a pacientes, agendamentos, sala de espera e lista de usuários |

> Na primeira execução, um usuário administrador é criado automaticamente via `AdminUserSeeder`. Consulte a classe para ver as credenciais padrão e altere-as imediatamente em produção.

---

## Banco de dados e migrações

As migrações são gerenciadas pelo **Flyway** e executadas automaticamente na inicialização. Os scripts estão em `src/main/resources/db/migration/`.

| Versão | Descrição |
|---|---|
| V1 | Criação da tabela de usuários |
| V2 | Criação da tabela de pacientes |
| V3 | Criação da tabela de prontuários |
| V4 | Criação da tabela de agendamentos |
| V5 | Criação da tabela de arquivos de exames |
| V6 | Criação do catálogo de procedimentos |
| V7 | Tabela de procedimentos por prontuário |
| V8 | Seed do catálogo de procedimentos |
| V9 | Criação da tabela de odontograma |
| V10 | Campo `arrived_at` nos agendamentos |
| V11 | Permite paciente nulo em agendamentos (bloqueios) |

---

## Documentação interativa (Swagger)

Com a aplicação em execução, acesse:

```
http://localhost:8080/api/v1/swagger-ui.html
```

A documentação é gerada automaticamente pelo **SpringDoc OpenAPI** e permite testar todos os endpoints diretamente pelo navegador.

---

## Licença

Distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
