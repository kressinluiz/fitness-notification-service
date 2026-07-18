# Fitness Notification Service

Microserviço responsável pelo agendamento e envio de notificações para treinos cadastrados na Fitness API.

Projeto desenvolvido para demonstrar conceitos de sistemas distribuídos: mensageria assíncrona (RabbitMQ), cache distribuído (Redis), persistência (JPA), agendamento de tarefas, idempotência e arquitetura orientada a eventos, integrando-se à [Fitness API](https://github.com/kressinluiz/fitness-api) através de eventos de domínio.

## Objetivos

- Receber eventos publicados pela Fitness API (`WorkoutScheduledEvent`).
- Persistir notificações de treinos.
- Agendar notificações recorrentes e em datas específicas.
- Enviar notificações no momento correto.
- Evitar envios duplicados através de controle de idempotência.
- Permitir expansão para múltiplos canais de comunicação (e-mail, push, SMS...).

## Arquitetura

```
                    +-------------------+
                    |    Fitness API    |
                    +---------+---------+
                              |
                    WorkoutScheduledEvent
                              |
                              v
                       RabbitMQ Exchange
                       (fitness.events)
                              |
                              v
               +-------------------------------+
               | Fitness Notification Service  |
               +-------------------------------+
               | NotificationListener          |
               | NotificationService  ---> DB  |
               | NotificationScheduler (cron)  |
               | NotificationSchedulerService  |
               | WorkoutPlanCache (Redis)      |
               +---------------+---------------+
                               |
                     FitnessApiClient (REST)
                               |
                               v
                        Fitness API (fallback
                        quando não há cache)
```

## Fluxo da aplicação

1. A Fitness API publica um evento ao criar/agendar um Workout Plan.
2. O RabbitMQ entrega o evento ao `NotificationListener`.
3. O `NotificationService` persiste uma `Notification` com status `ACTIVE` (idempotência via `messageId` único).
4. O `NotificationScheduler` roda a cada 50s e busca notificações `ACTIVE`.
5. Para cada notificação, o serviço consulta o Workout Plan (via cache Redis, com fallback para a Fitness API) e verifica se algum `ScheduleEntry` bate com o horário atual.
6. Se bater e ainda não houver registro em `NotificationHistory` para aquele horário, a notificação é enviada e o histórico é gravado.

## Tecnologias

- Java 26 / Spring Boot 4.1
- Spring Data JPA + H2 (runtime atual)
- Spring AMQP (RabbitMQ)
- Spring Data Redis
- Spring Scheduling
- Maven, JUnit 5

## Estrutura do projeto

```
src/main/java/com/kressin/fitness_notification_service
├── client            # FitnessApiClient + cache + DTOs da Fitness API
├── config            # RabbitMQ, Redis
├── messaging         # eventos, listener, mapper
├── notification       # domínio: entity, repository, service, mapper
└── scheduler          # job periódico (@Scheduled)
```

## Executando localmente

Requisitos: Java 21+, Docker (para Redis e RabbitMQ).

```bash
git clone <repo>
cd fitness_notification_service
docker-compose up -d      # sobe Redis e RabbitMQ
./mvnw spring-boot:run
```

A Fitness API precisa estar rodando em `http://localhost:8080` (configurável via `fitness-api.base-url`).

## Melhorias

- [ ] Corrigir `Jackson2JsonMessageConverter` para receber o `ObjectMapper` com `JavaTimeModule` (`new Jackson2JsonMessageConverter(mapper)`), evitando falha silenciosa na serialização de datas.
- [ ] Fazer `NotificationSchedulerService` depender da interface `NotificationSender` (injetada via `@Qualifier` ou strategy map por `NotificationType`/canal), não da classe concreta.
- [ ] Corrigir `NotificationMapper` para usar o horário correto do evento em `scheduledAt`, em vez de `ZonedDateTime.now()`.
- [ ] Remover o parâmetro morto `type` de `NotificationHistory` ou efetivamente persistir/usá-lo.
- [ ] Trocar o `catch (Exception e)` genérico em `NotificationService` por `DataIntegrityViolationException`, deixando outros erros propagarem (ou serem logados como falha real, não como "duplicado").
- [ ] Adicionar timeout de conexão/leitura no `RestClient` do `FitnessApiClient` (hoje uma chamada lenta à Fitness API pode travar o scheduler indefinidamente).
- [ ] Configurar Dead Letter Queue (DLQ) e retry com backoff no `RabbitListener`, para evitar "poison messages" em loop infinito de redelivery.
- [ ] Mover credenciais do RabbitMQ (e futuramente do banco) para variáveis de ambiente — hoje estão hardcoded em `application.properties`.

- [ ] Adicionar driver PostgreSQL e configurar profiles `dev` / `test` / `prod` (H2 apenas para `test`).
- [ ] Introduzir Flyway com migrations versionadas (hoje o schema depende do `ddl-auto` do Hibernate, inseguro para produção).
- [ ] Criar Dockerfile multi-stage com usuário non-root para o próprio serviço.
- [ ] Adicionar o serviço (e a Fitness API) ao `docker-compose.yml`, que hoje só sobe Redis e RabbitMQ.
- [ ] Publicar imagem Docker (GitHub Container Registry ou Docker Hub).

- [ ] Adicionar Spring Boot Actuator (`/health`, `/metrics`, `/info`).
- [ ] Substituir o `System.out.println` do scheduler por log estruturado via `Logger`.
- [ ] Expor métricas com Micrometer + Prometheus (contagem de notificações enviadas, falhas, cache hit/miss).
- [ ] Adicionar `traceId`/`correlationId` propagado da Fitness API até este serviço, para rastrear uma notificação de ponta a ponta entre os dois microsserviços.

- [ ] Circuit breaker (Resilience4j) nas chamadas ao `FitnessApiClient`.
- [ ] Rate limiter para proteger a Fitness API de picos de chamada quando o cache expira em massa.
- [ ] ShedLock (ou equivalente) no `@Scheduled`, para permitir múltiplas instâncias do serviço sem duplicar o processamento.
- [ ] Invalidação ativa do cache Redis quando a Fitness API alterar um Workout Plan (hoje depende só do TTL de 10 min, podendo enviar notificação com dados desatualizados por até esse tempo).

- [ ] Testes unitários para `NotificationSchedulerService` (regras de `shouldTriggerNotification`, especialmente fusos horários e `RECURRING` vs `SPECIFIC_DATES`).
- [ ] Testes unitários para `NotificationService` (idempotência).
- [ ] Testes de integração com Testcontainers (RabbitMQ, Redis, Postgres).
- [ ] Testes do `NotificationListener` fim a fim (publicar evento → esperar persistência).
- [ ] Pipeline de CI (GitHub Actions) rodando build + testes a cada push/PR.
- [ ] Cobertura mínima com JaCoCo.

- [ ] Implementar `EmailNotificationSender` (hoje lança `UnsupportedOperationException`).
- [ ] Endpoint administrativo (ou pelo menos endpoint via Actuator custom) para consultar histórico de notificações e status.
- [ ] Suporte a múltiplos canais simultâneos por usuário (hoje o `NotificationSender` é singular).
- [ ] Registrar `errorMessage` em `NotificationHistory` quando o envio falhar (o campo já existe na entidade, mas nunca é populado).
