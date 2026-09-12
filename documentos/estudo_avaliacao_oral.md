# Guia de Preparação e Domínio Técnico — Arquitetura Clyvo VET
> **Projeto:** Clyvo VET — Aplicação Web com Spring Boot, Thymeleaf, Flyway e Spring Security  
> **Instituição:** FIAP — Challenge 2026  
> **Objetivo:** Fornecer aos integrantes do grupo total segurança, profundidade técnica e capacidade de argumentação para a apresentação técnica em sala de aula.

---

## 🧭 Sumário Rápido de Navegação
1. [Visão Conceitual do Clyvo VET](#1-visão-conceitual-do-clyvo-vet)
2. [Ciclo de Vida da Requisição (A Arquitetura em Camadas)](#2-ciclo-de-vida-da-requisição-a-arquitetura-em-camadas)
3. [Camada de Visualização com Thymeleaf](#3-camada-de-visualização-com-thymeleaf)
4. [Versionamento com Flyway Migrations](#4-versionamento-com-flyway-migrations)
5. [Autenticação e Autorização com Spring Security RBAC](#5-autenticação-e-autorização-com-spring-security-rbac)
6. [Fluxos Clínicos Especializados: Triagem e Atendimento](#6-fluxos-clínicos-especializados-triagem-e-atendimento)
7. [Como Abordar e Defender o Uso da IA perante o Professor](#7-como-abordar-e-defender-o-uso-da-ia-perante-o-professor)
8. [Simulado de Perguntas da Banca (Com Respostas Prontas)](#8-simulado-de-perguntas-da-banca-com-respostas-prontas)

---

## 1. Visão Conceitual do Clyvo VET
* **Problema Real:** A maioria dos sistemas veterinários tradicionais opera de forma reativa: o tutor só leva o animal quando ele já está gravemente doente, e a clínica não tem ferramentas para prever sobrecarga de agenda ou acompanhar o histórico biométrico contínuo do pet.
* **Nossa Solução:** Uma plataforma holística que combina:
  - **Portal Web Clínico** para a equipe veterinária e tutores gerenciarem triagens, prontuários eletrônicos e execução de atendimentos.
  - **API REST interoperável** com o aplicativo mobile React Native desenvolvido na Sprint 2.
  - **Prevenção ativa de saúde** por meio de cronogramas vacinais e monitoramento do peso corporal.

---

## 2. Ciclo de Vida da Requisição (A Arquitetura em Camadas)

Quando o professor perguntar: *"Explique o que acontece quando o usuário clica em Confirmar Agendamento"*, responda com esta sequência precisa:

```text
Browser (HTTP POST /agendamentos/solicitar)
   │
   ▼
[SecurityFilterChain] 
   ├─ 1. Valida se o usuário está autenticado
   ├─ 2. Valida o token CSRF contra ataques de falsificação
   └─ 3. Verifica a permissão (ROLE_TUTOR ou ROLE_VET)
   │
   ▼
[DispatcherServlet] (Front Controller do Spring MVC)
   │
   ▼
[AgendamentoWebController]
   ├─ 1. Converte os parâmetros do formulário no SolicitacaoAgendamentoDTO
   ├─ 2. Executa a validação do Bean Validation (@Valid / JSR-380)
   └─ 3. Se houver erro no formulário, devolve a View com os erros em BindingResult
   │
   ▼
[TriagemAgendamentoService]
   ├─ Executa as REGRAS DE NEGÓCIO (horário comercial, conflito de agenda do vet, antecedência)
   └─ Monta a entidade Agendamento no status CONFIRMADO
   │
   ▼
[AgendamentoRepository (Spring Data JPA)]
   └─ Executa as queries JPQL/SQL no banco de dados
   │
   ▼
[Banco de Dados H2 (Esquema versionado pelo Flyway)]
   │
   ▼
Controller define FlashAttribute ("sucesso") e envia Redirecionamento (HTTP 302 -> /agendamentos)
```

---

## 3. Camada de Visualização com Thymeleaf

### Por que escolhemos o Thymeleaf?
1. **Server-Side Rendering (SSR) Nativo do Spring:** O Thymeleaf processa as páginas HTML no servidor antes de enviá-las ao navegador. Isso garante renderização rápida, sem problemas de tela branca e com suporte imediato a SEO.
2. **Integração com Spring Security Dialect (`thymeleaf-extras-springsecurity6`):** Permite renderizar elementos condicionais com base no perfil do usuário usando atributos como `sec:authorize="hasRole('VET')"`. Um tutor simplesmente não vê links ou botões clínicos restritos.
3. **Form Binding Bidirecional:** O atributo `th:object="${dto}"` e `th:field="*{campo}"` vincula diretamente os dados digitados ao objeto Java, mantendo os dados preenchidos na tela caso ocorra algum erro de validação.
4. **Layout Modular por Fragmentos:** Criamos o `fragments/layout.html` com fragmentos reutilizáveis:
   - `th:fragment="head(pageTitle)"` — Injeta CSS padronizado (paleta Clyvo VET `#0c211b` e `#23c483`).
   - `th:fragment="navbar"` — Barra superior com identificação do usuário e role ativa.
   - `th:fragment="alerts"` — Bloco inteligente de mensagens de sucesso ou erro.
   - `th:fragment="footer"` — Rodapé com links para documentação Swagger e Console do banco.

---

## 4. Versionamento com Flyway Migrations

### Por que usar Flyway em vez do `spring.jpa.hibernate.ddl-auto=update`?
Esta é uma das perguntas mais clássicas de bancas de arquitetura:
* **O perigo do `ddl-auto=update`:** Em ambientes de produção ou equipes concorrentes, o `ddl-auto=update` pode alterar tabelas sem aviso, não remove colunas obsoletas, não aplica renomeações com segurança e não mantém histórico das mudanças.
* **A vantagem profissional do Flyway:**
  1. **Rastreabilidade e Imutabilidade:** Cada script SQL possui número de versão estrito (`V1`, `V2`, `V3`), descrição e cálculo de *checksum*. Se alguém alterar um arquivo já executado, o Flyway acusa inconsistência.
  2. **Tabela de Metadados (`flyway_schema_history`):** O banco mantém o registro de quem rodou cada migração, quando e se houve sucesso.
  3. **Validação estrita no JPA:** No nosso `application.properties`, definimos `spring.jpa.hibernate.ddl-auto=validate`. Isso significa que o Hibernate **não cria tabelas**, apenas confere se as classes Java estão 100% em conformidade com o banco gerado pelo Flyway.

### Nossas Migrações:
* **`V1__create_domain_tables.sql`**: Tabelas do negócio (`t_clyvo_clinica`, `t_clyvo_veterinario`, `t_clyvo_tutor`, `t_clyvo_pet`, `t_clyvo_agendamento`, `t_clyvo_evento_saude`, `t_clyvo_protocolo`) com chaves estrangeiras e índices.
* **`V2__create_security_tables.sql`**: Tabela de segurança (`t_clyvo_usuario`) com e-mail único, hash de senha e perfil (role).
* **`V3__insert_seed_data.sql`**: Carga de dados iniciais de clínicas, veterinários, pets e agendamentos para teste imediato.

---

## 5. Autenticação e Autorização com Spring Security RBAC

### Como a Segurança foi Estruturada?
* **Padrão RBAC (Role-Based Access Control):** Dois perfis de usuários com responsabilidades e privilégios distintos:
  1. **`ROLE_VET` (Médico Veterinário):** Acesso total à fila clínica (`/atendimentos/**`), realização e evolução de consultas e alteração de prontuários.
  2. **`ROLE_TUTOR` (Tutor de Animais):** Acesso aos seus próprios pets, solicitação de agendamentos e acompanhamento do histórico médico. Se tentar acessar `/atendimentos`, é barrado com **HTTP 403 Forbidden**.
  3. **`ROLE_ADMIN`:** Administrador da clínica.

* **Criptografia com BCrypt:** Nenhuma senha é gravada em texto plano. O `BCryptPasswordEncoder` aplica um salt aleatório e função de derivação de chave lenta (função hash segura contra força bruta e ataques de rainbow table).

* **Configuração da `SecurityFilterChain` (`SecurityConfig.java`):**
  - Rotas públicas: `/login`, `/css/**`, `/js/**`, `/images/**`.
  - Rotas da API REST: `/api/**` liberadas (para que o aplicativo móvel React Native continue operando simultaneamente).
  - Rotas restritas:
    - `/atendimentos/**` -> `.hasRole("VET")`
    - `/agendamentos/novo` -> `.hasAnyRole("TUTOR", "VET")`
    - `/dashboard`, `/pets/**` -> `.authenticated()`
  - Proteção CSRF ativa para requisições de formulário web.
  - Tela personalizada de Acesso Negado (`/403`).

---

## 6. Fluxos Clínicos Especializados: Triagem e Atendimento

A plataforma implementa dois fluxos de negócio centrais que coordenam lógicas avançadas de validação temporal, integridade relacional e transações multi-entidades:

### ⚡ 1. Triagem Clínica & Agendamento Inteligente com Prevenção de Conflito
* **Onde está o código:** `TriagemAgendamentoService.java` e `AgendamentoWebController.java`.
* **O que faz:**
  1. Recebe a solicitação via formulário web (`SolicitacaoAgendamentoDTO`).
  2. **Validação de Expediente:** Bloqueia agendamentos fora do horário das 08:00 às 18:00 e rejeita domingos.
  3. **Validação de Antecedência:** Exige antecedência mínima de 1 hora.
  4. **Prevenção Ativa de Conflito de Horário (*Double-Booking*):** Executa uma query JPQL personalizada no banco (`agendamentoRepository.existsConflitoVeterinario(...)`) verificando se o veterinário já possui atendimento agendado no intervalo de 30 minutos em torno do horário desejado. Se sim, rejeita a operação com mensagem amigável.
  5. **Conflito do Pet:** Checa se o mesmo animal não possui outro agendamento no mesmo período.
  6. **Processamento da Triagem:** Formata a classificação de risco e determina recomendações prévias (como jejum de 8 horas).
  7. Salva o agendamento já no status `CONFIRMADO`.

### 🩺 2. Execução Clínica de Atendimento e Atualização Atômica de Prontuário
* **Onde está o código:** `ExecucaoConsultaService.java` e `AtendimentoWebController.java`.
* **O que faz:**
  1. Acessível exclusivamente por `ROLE_VET` na tela de atendimento clínico.
  2. Anotado com **`@Transactional`**: Garante a propriedade **ACID** (Atomicidade, Consistência, Isolamento e Durabilidade). Se qualquer passo falhar, todas as alterações sofrem *rollback*.
  3. **Coordena 4 entidades simultaneamente em uma única ação:**
     - **Passo 1 (`Pet`):** Atualiza o peso aferido na consulta (`pet.setPeso(...)`), mantendo a biometria do animal atualizada.
     - **Passo 2 (`EventoSaude`):** Cria um registro permanente no prontuário histórico, gravando a anamnese, hipótese diagnóstica, exame físico e prescrição farmacológica.
     - **Passo 3 (`Protocolo`):** Se for assinalada atualização preventiva, valida e sincroniza o cronograma vacinal do animal.
     - **Passo 4 (`Agendamento`):** Altera o status do agendamento de `CONFIRMADO` para `CONCLUIDO`, registrando a data/hora do término e o médico responsável.
  4. Redireciona o veterinário para o prontuário completo do pet recém-atualizado.

---

## 7. Como Abordar e Defender o Uso da IA perante o Professor

Se o professor perguntar: *"Vocês usaram inteligência artificial para fazer este projeto?"*, mantenha uma postura **madura, transparente e técnica**:

### ✅ O que responder:
> *"Sim, professor. Utilizamos ferramentas de IA generativa de ponta como acelerador de produtividade de engenharia de software, exatamente como o mercado de tecnologia moderno opera hoje. No entanto, a IA foi utilizada como nossa parceira de pair-programming: nós definimos toda a arquitetura em camadas, a modelagem das entidades, as regras de negócio dos fluxos clínicos e as políticas de segurança RBAC. Todas as classes geradas foram minuciosamente inspecionadas, testadas e validadas por nós através de testes unitários e de integração."*

### ❌ O que NÃO fazer:
- Não diga: *"A IA fez tudo sozinha"*.
- Não tente esconder ou mentir caso perguntem. O segredo para tirar 10 é **demonstrar domínio absoluto de cada linha de código**.

---

## 8. Simulado de Perguntas da Banca (Com Respostas Prontas)

### P1: *"Onde está configurado o controle de acesso de rotas do Spring Security?"*
* **Resposta:** *"Na classe `SecurityConfig.java`, dentro do método `@Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)`. Lá utilizamos o método `.requestMatchers(...)` encadeado com `.hasRole("VET")` para rotas restritas como `/atendimentos/**`, `.hasAnyRole("TUTOR", "VET")` para agendamentos e `.authenticated()` para o dashboard e catálogo de pets."*

### P2: *"Como o sistema sabe se a senha digitada no login bate com a senha do banco?"*
* **Resposta:** *"Utilizamos o `DaoAuthenticationProvider` integrado com a interface `UserDetailsService` (implementada na classe `UsuarioDetailsService.java`) e o bean `PasswordEncoder` configurado com `BCryptPasswordEncoder`. O Spring Security busca o usuário pelo e-mail no banco e o BCrypt compara a senha fornecida com o hash saltado persistido na coluna `ds_senha`."*

### P3: *"Por que o método de atendimento clínico possui a anotação `@Transactional`?"*
* **Resposta:** *"Porque a finalização da consulta envolve uma operação de negócio atômica que altera quatro tabelas distintas: atualiza o peso na tabela `t_clyvo_pet`, insere um novo registro na tabela `t_clyvo_evento_saude`, sincroniza o protocolo e atualiza o status do agendamento para CONCLUIDO na tabela `t_clyvo_agendamento`. Se houver qualquer falha ou queda de conexão no meio da operação, o `@Transactional` garante o rollback automático, evitando que o banco fique em estado inconsistente."*

### P4: *"Qual a diferença entre o `spring.jpa.hibernate.ddl-auto=update` e o uso do Flyway?"*
* **Resposta:** *"O `ddl-auto=update` tenta inferir as alterações de esquema automaticamente em tempo de execução, o que é inseguro em produção pois não tem controle de versão, não remove campos e pode gerar travamentos de tabela. Já o Flyway executa migrações SQL versionadas e imutáveis (`V1`, `V2`, `V3`), armazenando o histórico e checksums na tabela `flyway_schema_history`. No nosso projeto, o Flyway é o único responsável pelo DDL e configuramos o JPA com `ddl-auto=validate` apenas para validação."*

### P5: *"Por que as rotas `/api/**` continuam funcionando sem CSRF e sem login web?"*
* **Resposta:** *"Porque a arquitetura do nosso ecossistema foi projetada para manter interoperabilidade com o aplicativo móvel React Native desenvolvido na Sprint 2. No `SecurityConfig`, configuramos `.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))` e liberamos o acesso aos endpoints REST para que a comunicação mobile cliente-servidor continue operando perfeitamente."*
