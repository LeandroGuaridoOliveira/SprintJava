# Roteiro de Gravação e Demonstração em Vídeo (Até 10 minutos)
> **Projeto:** Clyvo VET — Plataforma Web (Spring Boot, Thymeleaf, Flyway e Spring Security)  
> **Integrantes:** Gabriel Costa Solano (RM 562325), Kaiky Pereira Rodrigues Da Silva (RM 564578), Leandro Guarido de Oliveira (RM 561760).

---

## ⏱️ Distribuição de Tempo Sugerida

| Bloco | Assunto | Duração Estimada |
| :--- | :--- | :---: |
| **Parte 1** | Apresentação do Grupo e Visão Geral da Plataforma | 01:00 min |
| **Parte 2** | Flyway Migrations e Banco de Dados Versionado | 02:00 min |
| **Parte 3** | Spring Security: Login, Perfis (ROLE_VET vs ROLE_TUTOR) e RBAC | 02:30 min |
| **Parte 4** | Triagem Clínica & Prevenção de Conflito de Horário | 02:00 min |
| **Parte 5** | Execução Clínica & Atualização Atômica de Prontuário | 01:45 min |
| **Parte 6** | Interoperabilidade Mobile & Conclusão | 00:45 min |
| **TOTAL** | — | **~10 minutos** |

---

## 🎬 Roteiro Passo a Passo (O que falar e o que mostrar)

### 📌 PARTE 1: Apresentação e Introdução (0:00 - 1:00)
* **O que mostrar na tela:** Abrir o arquivo `README.md` no VS Code ou navegador com a tabela de integrantes e escopo do projeto.
* **O que falar:**
  > *"Olá, professor e avaliadores! Nós somos o grupo responsável pelo desenvolvimento do ecossistema Clyvo VET no Challenge FIAP 2026, composto por Gabriel Costa Solano, Kaiky Pereira e Leandro Guarido.*  
  > *Nesta etapa do projeto, evoluímos a aplicação Java Spring Boot para uma plataforma web clínica completa com foco em quatro grandes pilares: Camada de visualização com Thymeleaf, versionamento de banco com Flyway, controle de acesso e autenticação com Spring Security e a implementação de fluxos completos de negócio para a rotina clínica. Vamos demonstrar cada uma dessas soluções funcionando na prática."*

---

### 📌 PARTE 2: Flyway Database Migrations (1:00 - 3:00)
* **O que mostrar na tela:**
  1. Abrir a pasta `src/main/resources/db/migration/` no VS Code.
  2. Mostrar os arquivos `V1__create_domain_tables.sql`, `V2__create_security_tables.sql` e `V3__insert_seed_data.sql`.
  3. Abrir o `application.properties` e destacar `spring.jpa.hibernate.ddl-auto=validate`.
  4. Abrir no navegador a URL `http://localhost:8080/h2-console`, logar e mostrar a tabela `FLYWAY_SCHEMA_HISTORY` com os registros executados com sucesso.
* **O que falar:**
  > *"Começando pela infraestrutura de banco de dados, implementamos o controle estrito de versões com o Flyway. Criamos três migrações versionadas e imutáveis:*  
  > *A V1 cria todas as 7 tabelas de domínio com constraints e chaves estrangeiras; a V2 cria a tabela t_clyvo_usuario para o Spring Security; e a V3 popula os dados essenciais de clínicas, veterinários, pets e agendamentos.*  
  > *No application.properties, configuramos o Hibernate com ddl-auto=validate. Isso assegura que o JPA não altere tabelas em tempo de execução, garantindo que o Flyway seja a única fonte de verdade da infraestrutura de banco de dados, como manda o padrão de mercado."*

---

### 📌 PARTE 3: Spring Security e Controle de Acesso por Perfil - RBAC (3:00 - 5:30)
* **O que mostrar na tela:**
  1. Abrir `SecurityConfig.java` no VS Code e mostrar as regras de rota (`hasRole('VET')`, `hasAnyRole('TUTOR', 'VET')`).
  2. No navegador, acessar `http://localhost:8080/login`.
  3. Mostrar os botões rápidos de preenchimento de teste.
  4. **Logar como Tutor:** Clicar em `tutor@clyvo.com` / `tutor123`.
      - Mostrar o dashboard do tutor com "Meus Animais" e a tag azul `TUTOR`.
      - Tentar digitar na barra de endereços `http://localhost:8080/atendimentos`.
      - Mostrar a tela de **Acesso Negado (HTTP 403)** personalizada explicando o bloqueio de perfil!
  5. Clicar em "Trocar de Usuário", fazer logout e **logar como Veterinário:** `veterinario@clyvo.com` / `admin123`.
      - Mostrar que o veterinário possui a tag verde `VETERINÁRIO` e tem acesso liberado à rota `/atendimentos` (Fila Clínica).
* **O que falar:**
  > *"No quesito segurança, implementamos o padrão RBAC com autenticação baseada em banco de dados usando BCryptPasswordEncoder.*  
  > *Temos dois tipos de usuário principais: o Veterinário (ROLE_VET) e o Tutor (ROLE_TUTOR).*  
  > *Como vocês podem ver, quando entramos como tutor, temos acesso aos nossos pets e à marcação de consultas. Mas se o tutor tentar forçar a URL da fila clínica (/atendimentos), o Spring Security intercepta imediatamente e direciona para a nossa página personalizada de erro 403.*  
  > *Ao trocar para a conta da Dra. Camila (ROLE_VET), o menu se adapta e ela consegue acessar a fila de consultas normalmente."*

---

### 📌 PARTE 4: Triagem Clínica & Prevenção de Conflito de Horário (5:30 - 7:30)
* **O que mostrar na tela:**
  1. Clicar em "⚡ Triagem & Agendar" (`/agendamentos/novo`).
  2. Mostrar o formulário com dados do pet, veterinário, modalidade, nível de triagem e data/hora.
  3. **Demonstrar a Rejeição por Conflito:**
     - Selecionar o pet "Thor".
     - Selecionar a "Dra. Camila Rocha".
     - Colocar a data `15/09/2026` às `10:00` (horário já ocupado no banco pela V3).
     - Clicar em "Processar Triagem e Confirmar Horário".
     - Mostrar o alerta vermelho: *"O(A) médico(a) Dra. Camila Rocha já possui atendimento marcado neste horário"*.
  4. **Demonstrar o Sucesso:**
     - Alterar o horário para as `11:00`.
     - Submeter novamente.
     - Mostrar o redirecionamento para `/agendamentos` com o banner de sucesso e a nova consulta no status `CONFIRMADO`.
* **O que falar:**
  > *"Agora demonstrando o processo de Triagem e Agendamento Inteligente:*  
  > *Ele valida regras comerciais: impede agendamentos fora do horário das 8h às 18h, bloqueia domingos e executa uma query JPQL que previne sobreposição de agenda médica (double-booking).*  
  > *Tentamos marcar exatamente no horário que a Dra. Camila já tinha consulta, e o sistema recusou a operação com mensagem amigável. Ao ajustar para um horário livre, o agendamento foi processado e confirmado com sucesso."*

---

### 📌 PARTE 5: Execução de Consulta & Atualização Atômica de Prontuário (7:30 - 9:15)
* **O que mostrar na tela:**
  1. Como veterinário, ir em "🩺 Fila Clínica" (`/atendimentos`).
  2. Clicar em "▶ Iniciar Consulta" no agendamento do paciente Thor.
  3. Mostrar a tela com o histórico clínico anterior do animal exibido ao lado.
  4. Preencher a consulta:
     - Peso aferido hoje: mudar de 32.5 para `34.0 kg`.
     - Procedimento: `Consulta Clínica`.
     - Anamnese & Conduta: *"Animal ativo, parâmetros vitais normais, vermifugação em dia."*
     - Prescrição: *"Prescrito reforço alimentar e retorno em 6 meses."*
     - Marcar a caixinha: *"Atualizar Protocolo Preventivo Vacinal"*.
  5. Clicar em "Concluir Atendimento & Salvar Prontuário 💾".
  6. Mostrar o redirecionamento para o Prontuário Clínico (`/pets/1/prontuario`):
     - Mostrar o novo peso de `34.0 kg` atualizado na biometria do animal.
     - Mostrar a nova linha inserida no histórico médico oficial (`EventoSaude`).
     - Mostrar que o status do agendamento passou para `CONCLUIDO`.
* **O que falar:**
  > *"Na sequência, apresentamos a Execução Clínica de Atendimento. No ExecucaoConsultaService, anotamos o método com @Transactional para garantir consistência ACID em quatro entidades ao mesmo tempo:*  
  > *Ele atualiza o peso corporal do animal, gera um registro permanente na tabela t_clyvo_evento_saude com a conduta e diagnóstico, valida os protocolos preventivos da espécie e conclui o agendamento.*  
  > *Ao finalizar, somos direcionados para o prontuário eletrônico completo do pet, onde vemos o histórico de saúde atualizado em tempo real."*

---

### 📌 PARTE 6: Interoperabilidade Mobile & Conclusão (9:15 - 10:00)
* **O que mostrar na tela:**
  1. Abrir uma aba com a documentação do Swagger UI (`http://localhost:8080/swagger-ui.html`) ou disparar uma requisição em `/api/pets`.
  2. Lembrar que a API continua atendendo perfeitamente o aplicativo mobile da Sprint 2.
  3. Mostrar o arquivo `documentos/estudo_avaliacao_oral.md` criado para a avaliação presencial.
* **O que falar:**
  > *"Para encerrar, destacamos que todos os endpoints da API REST foram preservados com suporte a CORS e documentação Swagger, garantindo 100% de integração com o aplicativo mobile React Native da Sprint 2.*  
  > *Preparamos também uma documentação detalhada de estudo para a avaliação em sala.*  
  > *Com isso, cobrimos com êxito todas as funcionalidades e padrões da plataforma Clyvo VET. Muito obrigado!"*
