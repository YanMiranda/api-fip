# FIPE Microservices (Quarkus)

Conjunto de microserviços em Quarkus para integração e exposição de dados relacionados à FIPE, com módulos independentes e compartilhamento de domínio.

## Visão Geral

- Multi-módulo Maven (módulo de domínio + duas APIs)
- Java 17 (Amazon Corretto)
- Quarkus (RESTEasy Reactive, Hibernate ORM, REST Client Reactive)
- Integrações: Oracle (JDBC), Redis (cache), RabbitMQ (mensageria), JWT (segurança)
- Configuração via YAML
- Empacotamento nativo do Quarkus (`quarkus-app`) e imagens Docker por módulo

## Arquitetura em Alto Nível

- fipe-domain: modelos e componentes compartilhados entre as APIs (ex.: entidades JPA).
- magnum-fipe-api-1: primeiro microserviço da solução FIPE (porta padrão 8081 em Docker).
- magnum-fipe-api-2: segundo microserviço da solução FIPE (porta padrão 8082 em Docker).

Cada API é empacotada como aplicação Quarkus e pode ser executada isoladamente. O domínio comum é publicado como dependência interna entre os módulos.

## Tecnologias e Extensões

- Quarkus: Resteasy Reactive, Rest Client Reactive (Jackson), Hibernate ORM
- Persistência: JDBC Oracle
- Segurança: SmallRye JWT
- Cache: Redis Client
- Mensageria: SmallRye Reactive Messaging (RabbitMQ)
- Configuração: quarkus-config-yaml
- Testes: JUnit 5, RestAssured
- Lombok para redução de boilerplate no domínio

## Pré-requisitos

- Java 17 (Amazon Corretto recomendado)
- Maven 3.9+
- Acesso a:
    - Banco Oracle
    - Redis
    - RabbitMQ
- Docker (opcional, para containerização)

## Build e Execução (Local)

### 1) Compilar o projeto completo
- bash mvn clean install -DskipTests


### 2) Rodar em modo docker
- docker-compose up --build
