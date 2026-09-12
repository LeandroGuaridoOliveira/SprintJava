# Clyvo VET — Plataforma Web & Backend Clínico
> **Challenge FIAP 2026** — Solução corporativa para jornada contínua de saúde preventiva e rotina clínica veterinária.

---

## 👥 Integrantes do Grupo

| Nome | RM |
| :--- | :--- |
| **Gabriel Costa Solano** | 562325 |
| **Kaiky Pereira Rodrigues Da Silva** | 564578 |
| **Leandro Guarido de Oliveira** | 561760 |

---

## 🎥 Demonstração em Vídeo

- **Link do Vídeo no YouTube:** [ASSISTIR DEMONSTRAÇÃO NO YOUTUBE](https://youtu.be/yk9q2y_ON3s?si=mfa_b1cLjt1ysj7K) & https://youtu.be/yk9q2y_ON3s?si=mfa_b1cLjt1ysj7K 

---

## 🔐 Credenciais de Acesso (Ambiente de Demonstração)

A aplicação conta com contas pré-configuradas e botões de preenchimento automático na própria tela de login:

| Perfil / Role | E-mail de Acesso | Senha | Permissões no Sistema |
| :--- | :--- | :--- | :--- |
| **Veterinário** (`ROLE_VET`) | `veterinario@clyvo.com` | `admin123` | Acesso à fila clínica, início e conclusão de atendimentos, atualização de prontuários e biometria, catálogo geral de pets. |
| **Tutor** (`ROLE_TUTOR`) | `tutor@clyvo.com` | `tutor123` | Visualização dos seus próprios animais, solicitação de agendamento com triagem, histórico de saúde. **Bloqueado de realizar consultas clínicas (HTTP 403)**. |
| **Administrador** (`ROLE_ADMIN`) | `admin@clyvo.com` | `admin123` | Acesso administrativo e supervisão clínica global. |

---

## ⚡ Processos Clínicos Integrados

### 🩺 Triagem Clínica & Agendamento Inteligente com Prevenção de Conflitos
- **Objetivo**: Garantir que as marcações de consulta respeitem a capacidade clínica da equipe e não gerem duplicidade de atendimento (*double-booking*).
- **Regras de Negócio Implementadas**:
  1. **Prevenção Ativa de Sobreposição**: O sistema verifica no banco de dados se o médico veterinário selecionado já possui consulta confirmada no intervalo de 30 minutos em torno do horário solicitado. Caso haja conflito, recusa a operação com mensagem explicativa.
  2. **Disponibilidade do Paciente**: Impede que o mesmo animal seja marcado em procedimentos concorrentes no mesmo período.
  3. **Validação de Expediente**: Atendimentos permitidos apenas entre **08:00 e 18:00**, de segunda a sábado (domingos bloqueados).
  4. **Antecedência Mínima**: Exige ao menos 1 hora de antecedência em relação ao momento atual.
  5. **Triagem Sintomatológica**: Classifica a prioridade (Rotina, Urgência Leve, Pós-Operatório) e solicita recomendação de jejum prévio quando necessário.

### 💉 Execução Clínica de Atendimento & Atualização Atômica de Prontuário
- **Objetivo**: Operação transacional atômica (`@Transactional`) coordenada pelo médico veterinário que consolida a consulta em um único fluxo de gravação.
- **Entidades Atualizadas Simultaneamente**:
  1. **`Pet`**: Atualização do peso corporal aferido durante o exame físico.
  2. **`EventoSaude`**: Criação do registro oficial permanente no prontuário histórico do animal, contendo anamnese clínica, exame físico e prescrição terapêutica.
  3. **`Protocolo`**: Sincronização do status preventivo vacinal do animal.
  4. **`Agendamento`**: Transição de status para `CONCLUIDO`, registrando a data/hora exata do encerramento e o médico responsável.

---

## 🛠️ Tecnologias e Arquitetura Utilizadas

- **Java 17 (LTS)**
- **Spring Boot 3.2.5**
- **Spring Security 6** (Form Login, RBAC, BCryptPasswordEncoder, CSRF)
- **Spring Data JPA & Hibernate** (com validação estrita de schema)
- **Flyway Migration** (Gerenciamento versionado do banco H2)
- **Thymeleaf + Thymeleaf Extras Spring Security 6**
- **Bean Validation (Jakarta Validation)**
- **Springdoc OpenAPI (Swagger UI)**
- **Lombok**

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
- JDK 17 instalado e configurado no `JAVA_HOME`.
- Maven 3.8+ (ou utilize o wrapper `./mvnw` já incluído no repositório).

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/LeandroGuaridoOliveira/SprintJava.git
   cd SprintJava
   ```

2. **Compile e execute a aplicação:**
   ```bash
   # Windows
   .\mvnw.cmd spring-boot:run

   # Linux / macOS
   ./mvnw spring-boot:run
   ```

3. **Acesse as interfaces no navegador:**
   - **Portal Web Clyvo VET:** `http://localhost:8080` (redireciona para `/login`)
   - **Console do Banco H2:** `http://localhost:8080/h2-console`
     - *JDBC URL:* `jdbc:h2:mem:clyvovet`
     - *User:* `sa`
     - *Password:* *(em branco)*
   - **Documentação Swagger UI:** `http://localhost:8080/swagger-ui.html`
   - **OpenAPI JSON:** `http://localhost:8080/api-docs`

---

## 📱 Compatibilidade com o Aplicativo Mobile

As rotas da API REST (`/api/**`) permanecem totalmente operacionais e compatíveis com a aplicação React Native desenvolvida na Sprint 2, garantindo interoperabilidade entre o aplicativo dos tutores e o portal web administrativo da clínica.

