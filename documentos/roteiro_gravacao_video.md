# 🎬 Roteiro de Gravação em Vídeo — Clyvo VET (Até 10 Minutos)

> **Projeto:** Clyvo VET — Plataforma Web Clínica (Spring Boot, Thymeleaf, Flyway e Spring Security)  
> **Integrantes:** Gabriel Costa Solano (RM 562325), Kaiky Pereira Rodrigues Da Silva (RM 564578), Leandro Guarido de Oliveira (RM 561760).  
> **Preparação prévia para gravação:**
> 1. Servidor Spring Boot rodando (`.\mvnw.cmd spring-boot:run` ou já ativo).
> 2. VS Code aberto com o projeto.
> 3. Navegador com 2 abas preparadas:
>    - Aba 1: `http://localhost:8080/login`
>    - Aba 2: `http://localhost:8080/h2-console`

---

## 📌 PARTE 1: Apresentação e Abertura (0:00 - 1:00)

* **Abre tela:** Arquivo `README.md` no VS Code ou navegador com o nome do projeto e tabela de integrantes.
* **Fala isso:**
  > *"Olá professor, olá a todos! Nós somos o grupo responsável pelo desenvolvimento da plataforma Clyvo VET no Challenge FIAP 2026, composto por Gabriel Costa Solano, Kaiky Pereira e Leandro Guarido.*  
  > *Hoje vamos demonstrar a aplicação web completa que desenvolvemos em Spring Boot para dar suporte à rotina e aos cuidados preventivos da nossa clínica veterinária. Vamos apresentar a camada de visualização com Thymeleaf, o versionamento do banco de dados com Flyway, a camada de segurança e controle de acesso com Spring Security e a execução dos processos clínicos do sistema."*

---

## 📌 PARTE 2: Flyway Database Migrations (1:00 - 2:45)

* **Abre código:** No VS Code, abra a pasta `src/main/resources/db/migration/` e dê duplo clique em `V1__create_domain_tables.sql`.
* **Fala isso:**
  > *"Começando pela infraestrutura de banco de dados, implementamos o controle estrito de versões utilizando o Flyway. Aqui na migration V1, temos a criação das tabelas de domínio da clínica: pets, tutores, veterinários, agendamentos e prontuários médicos, com suas devidas restrições e chaves estrangeiras."*

* **Abre código:** Arquivo `V2__create_security_tables.sql` na mesma pasta.
* **Fala isso:**
  > *"Na migration V2, criamos a tabela de usuários com e-mail único, hash criptográfico de senha e o perfil de permissão para o Spring Security."*

* **Abre código:** Arquivo `src/main/resources/application.properties` (mostre a linha `spring.jpa.hibernate.ddl-auto=validate`).
* **Fala isso:**
  > *"No application.properties, configuramos o Hibernate com ddl-auto=validate. Dessa forma, o JPA apenas valida o esquema e não altera tabelas em tempo de execução. O Flyway é o único responsável pelo versionamento estrutural do banco de dados."*

* **Abre tela:** No navegador, vá para a aba do console H2 (`http://localhost:8080/h2-console`). Clique em **Connect**.
* **Ação na tela:** No menu lateral esquerdo, clique direto sobre a tabela **`flyway_schema_history`** (ou digite a consulta com aspas duplas: `SELECT * FROM "flyway_schema_history";`) e clique em **Run**.
* **Fala isso:**
  > *"Aqui no console do banco, podemos comprovar a tabela de metadados do Flyway com todas as migrações executadas com sucesso, seus checksums e as datas de aplicação."*

---

## 📌 PARTE 3: Spring Security e Controle de Acesso por Perfil (2:45 - 5:00)

* **Abre código:** `src/main/java/br/com/fiap/clyvovet/config/SecurityConfig.java` (mostre as linhas com `.requestMatchers`).
* **Fala isso:**
  > *"Na camada de segurança, configuramos o Spring Security com controle de acesso baseado em perfis (RBAC). A rota /atendimentos é restrita exclusivamente para veterinários com ROLE_VET, enquanto o agendamento de consultas é liberado para tutores e veterinários. Além disso, utilizamos senhas com hash BCrypt e proteção CSRF ativa em todos os formulários."*

* **Abre tela:** No navegador, acesse `http://localhost:8080/login`.
* **Fala isso:**
  > *"Esta é a tela de autenticação da aplicação, desenvolvida com Thymeleaf e visual moderno. Para facilitar a demonstração, disponibilizamos botões de acesso rápido com as credenciais cadastradas no sistema."*

* **Ação na tela:** Clique no botão `Leandro Silva (Tutor)` e depois clique em **Entrar no Sistema**.
* **Fala isso:**
  > *"Ao entrar com o perfil de Tutor, somos direcionados ao dashboard personalizado com os animais do Leandro e a identificação do perfil de tutor no menu."*

* **Ação na tela:** Digite na barra de endereços do navegador `http://localhost:8080/atendimentos` e aperte `Enter`.
* **Fala isso:**
  > *"Se o tutor tentar digitar na URL e acessar a área clínica restrita de atendimentos, o Spring Security intercepta imediatamente e exibe a nossa página personalizada de Acesso Negado HTTP 403, comprovando o isolamento de rotas."*

* **Ação na tela:** Clique no link "Trocar de Usuário" no topo da página.
* **Ação na tela:** Clique no botão `Dra. Camila Rocha (Veterinária)` e depois clique em **Entrar no Sistema**.
* **Fala isso:**
  > *"Agora efetuando login com a Dra. Camila, o sistema identifica a ROLE_VET. O menu superior se adapta e ela tem acesso liberado à Fila de Consultas Clínicas."*

---

## 📌 PARTE 4: Triagem Clínica & Prevenção de Conflito de Horário (5:00 - 7:15)

* **Abre código:** `src/main/java/br/com/fiap/clyvovet/service/TriagemAgendamentoService.java` (mostre o método `processarTriagemEAgendamento`).
* **Fala isso:**
  > *"Aqui no serviço de agendamento, implementamos regras de negócio que validam horários de funcionamento, impedem agendamentos aos domingos e utilizam uma consulta personalizada no repositório para evitar sobreposição na agenda médica do profissional."*

* **Abre tela:** No navegador, clique em **⚡ Novo Agendamento** (`/agendamentos/novo`).
* **Fala isso:**
  > *"Vamos preencher o formulário para agendar uma consulta com triagem sintomatológica."*

* **Ação na tela (Demonstrar a recusa por conflito de horário):**
  1. Paciente: selecione **Thor**.
  2. Veterinário: selecione **Dra. Camila Rocha**.
  3. Data e Hora: coloque `15/09/2026` às `10:00` *(horário em que a Dra. Camila já possui consulta no banco)*.
  4. Clique no botão **Processar Triagem e Confirmar Horário**.
* **Fala isso:**
  > *"Tentamos marcar exatamente às 10 horas do dia 15, horário em que a Dra. Camila já possui outro atendimento. Como esperado, o sistema recusa a gravação e apresenta um alerta amigável informando que o médico já possui compromisso nesse horário."*

* **Ação na tela (Demonstrar o sucesso):**
  1. Altere o horário para as `11:00`.
  2. Clique novamente em **Processar Triagem e Confirmar Horário**.
* **Fala isso:**
  > *"Ao selecionar as 11 horas, horário livre, a validação é aprovada com sucesso, o agendamento é confirmado e somos redirecionados com mensagem de confirmação para a listagem da clínica."*

---

## 📌 PARTE 5: Execução Clínica de Atendimento e Prontuário Eletrônico (7:15 - 9:15)

* **Abre código:** `src/main/java/br/com/fiap/clyvovet/service/ExecucaoConsultaService.java` (mostre as **linhas 47 e 48**, destacando a anotação `@Transactional` e o método `executarAtendimentoClinico`).
* **Fala isso:**
  > *"Para a realização da consulta médica, implementamos o ExecucaoConsultaService com a anotação @Transactional. Este método executa uma operação atômica coordenada em quatro entidades: atualiza o peso corporal do animal, gera um novo registro permanente no prontuário histórico, sincroniza os protocolos preventivos e altera o status do agendamento para concluído."*

* **Abre tela:** No navegador, clique em **🩺 Fila de Consultas** (`/atendimentos`).
* **Ação na tela:** Clique no botão **▶ Iniciar Consulta** no paciente Thor.
* **Fala isso:**
  > *"Na tela de atendimento clínico, a veterinária tem visão completa das informações do animal e de todo o histórico prévio de consultas e vacinas ao lado."*

* **Ação na tela (Preenchimento da consulta):**
  1. Peso Aferido Hoje: altere de `32.5` para `34.0` kg.
  2. Procedimento: `Consulta Clínica`.
  3. Anamnese & Conduta: digite *"Exame físico completo. Parâmetros vitais estáveis e animal saudável."*
  4. Prescrição: digite *"Prescrito reforço vitamínico e retorno semestral para avaliação preventiva."*
  5. Marque a caixinha: ☑ *"Atualizar Protocolo Preventivo Vacinal"*.
  6. Clique no botão **Concluir Atendimento & Salvar Prontuário 💾**.
* **Fala isso:**
  > *"Ao salvar, a transação atômica é concluída e somos redirecionados para o prontuário eletrônico completo do Thor. O peso do pet foi atualizado para 34 kg e o novo registro clínico com prescrição e diagnóstico já consta na linha do tempo permanente do paciente."*

---

## 📌 PARTE 6: Interoperabilidade Mobile & Conclusão (9:15 - 10:00)

* **Abre tela:** Nova aba no navegador com a documentação do Swagger UI: `http://localhost:8080/swagger-ui.html`.
* **Fala isso:**
  > *"Para finalizar, lembramos que todos os endpoints REST da aplicação foram mantidos e documentados com OpenAPI e Swagger, permitindo integração direta com o aplicativo móvel React Native que desenvolvemos anteriormente.*  
  > *Também preparamos um guia técnico detalhado sobre o ciclo de vida das requisições e a arquitetura em camadas no arquivo estudo_avaliacao_oral.md.*  
  > *Muito obrigado a todos pela atenção!"*

---

## 🎥 BÔNUS: Tour Visual do Frontend (Sem Narração — ~2 a 3 Minutos)
> **Objetivo:** Gravar uma demonstração contínua e dinâmica da interface web, mostrando a fluidez do sistema, a responsividade e todos os fluxos na prática.

### 🎬 Sequência de Ações e Cliques na Tela:

#### 1. Tela de Login (`/login`)
- Abra o navegador em `http://localhost:8080/login`.
- Role um pouco a tela para mostrar o formulário centralizado, o tema escuro e os botões de **Acesso Rápido**.
- Passe o mouse sobre os botões para mostrar o efeito *hover*.

#### 2. Visão do Tutor (`ROLE_TUTOR`)
- Clique no botão **Leandro Silva (Tutor)** (os campos de e-mail e senha são preenchidos automaticamente).
- Clique em **Entrar no Sistema**.
- No Dashboard do Tutor:
  - Passe o mouse pelo card de boas-vindas e pela tag azul **TUTOR**.
  - Mostre os cards dos pets vinculados (**Thor** e **Mel**).
  - Clique no botão **Ver Prontuário Completo →** no card do paciente **Thor**.
- Na tela de Prontuário (`/pets/1/prontuario`):
  - Role a página para mostrar os dados biométricos (espécie, raça, idade, peso).
  - Mostre a linha do tempo com o histórico de consultas e vacinas anteriores.
  - Mostre o bloco de protocolos preventivos ativos.

#### 3. Triagem e Agendamento Clínico
- Clique no botão **⚡ Agendar Nova Consulta** no topo da tela (ou vá em **⚡ Novo Agendamento** no menu).
- No formulário de agendamento (`/fluxos/novo-agendamento`):
  - Mostre o card lateral direito com as **Regras de Agendamento da Clínica**.
  - Selecione o Pet: **Thor**.
  - Selecione o Veterinário: **Dra. Camila Rocha**.
  - Tipo de Atendimento: **Consulta**.
  - Nível de Triagem: **Rotina / Preventivo**.
  - Data e Hora: Escolha uma data futura e horário livre (ex: `16/09/2026` às `14:00`).
  - Digite na queixa principal: *"Revisão semestral de rotina e aferição de peso."*
  - Clique em **Processar Triagem e Confirmar Horário**.
- Você será redirecionado para a lista de agendamentos (`/agendamentos`) com o banner verde de confirmação.

#### 4. Demonstração de Segurança (HTTP 403)
- Estando ainda com a conta do tutor, clique na barra de endereços do navegador.
- Digite `http://localhost:8080/atendimentos` e dê **Enter**.
- A tela de **Acesso Negado (HTTP 403)** será exibida com o aviso de permissão restrita.
- Clique no botão **← Voltar ao Início**.

#### 5. Visão da Médica Veterinária (`ROLE_VET`)
- Clique em **Trocar de Usuário** no menu superior.
- Na tela de login, clique no botão **Dra. Camila Rocha (Veterinária)** e clique em **Entrar no Sistema**.
- No Dashboard da Veterinária:
  - Mostre a tag verde **VETERINÁRIO** no menu.
  - Mostre que o menu agora exibe o botão **🩺 Fila de Consultas**.
  - Mostre o painel com as próximas consultas da clínica.

#### 6. Execução de Consulta e Prontuário ao Vivo
- Clique em **🩺 Fila de Consultas** no menu superior (`/atendimentos`).
- Na tabela de pacientes aguardando, localize a linha do paciente **Thor**.
- Clique no botão azul **▶ Iniciar Consulta**.
- Na tela de Atendimento Clínico (`/fluxos/realizar-consulta`):
  - Destaque que o lado esquerdo tem o formulário médico e o lado direito exibe o histórico clínico prévio do animal.
  - Altere o campo **Peso Aferido Hoje (kg)** para `34.0`.
  - No campo **Anamnese & Conduta Clínica**, digite: *"Paciente saudável, pelagem brilhante, frequência cardíaca regular."*
  - No campo **Prescrição & Orientações**, digite: *"Manter alimentação balanceada e vermifugação atualizada."*
  - Marque a caixa de seleção: ☑ **Atualizar Protocolo Preventivo Vacinal**.
  - Clique no botão **Concluir Atendimento & Salvar Prontuário 💾**.
- Você será redirecionado imediatamente para o Prontuário do Thor:
  - Destaque o novo peso de **34.0 kg** atualizado na biometria do animal.
  - Destaque a nova consulta recém-gravada no histórico médico oficial.

#### 7. Encerramento com a Documentação Swagger
- Abra uma nova aba no navegador e vá em `http://localhost:8080/swagger-ui.html`.
- Role rapidamente pelos endpoints da API REST (Pets, Agendamentos, Veterinários, Clínicas, etc.) mostrando que tudo está documentado e padronizado.
