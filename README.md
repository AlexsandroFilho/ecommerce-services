# Ecommerce Services

Este repositório contém um estudo pessoal sobre arquitetura de microsserviços em Java, com foco em integração entre APIs, comunicação assíncrona e gestão de estoque em um fluxo de e-commerce.

## Visão geral

O projeto é composto por duas APIs independentes:

- Ecommerce API: responsável pelo cadastro de produtos, criação de pedidos e publicação de eventos.
- Inventory API: responsável pela gestão de estoque, reserva de itens e consumo dos eventos gerados pela API principal.

O objetivo principal é demonstrar na prática conceitos importantes de desenvolvimento backend com Java e Spring Boot, incluindo microsserviços, mensageria, persistência e organização em camadas.

## Arquitetura

A comunicação entre as APIs ocorre de forma assíncrona por meio de mensagens com RabbitMQ. Quando um produto é criado ou um pedido é gerado, eventos são publicados e consumidos pela API de inventory para atualização do estoque.

## Funcionalidades

### Ecommerce API
- Cadastro e listagem de produtos
- Criação e consulta de pedidos
- Cálculo do valor total do pedido
- Publicação de eventos para integração com outros serviços

### Inventory API
- Consulta de estoque
- Abastecimento de estoque
- Reserva de estoque
- Consumo de eventos de criação de produto e pedido
- Registro de auditoria para ações relacionadas ao estoque

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Docker
- Lombok
- Validation
- Maven

## Pré-requisitos

- Java 21
- Maven
- Docker
- PostgreSQL

## Como executar

1. Suba os serviços de banco de dados e RabbitMQ com Docker
2. Configure as credenciais de conexão no arquivo de propriedades de cada API
3. Execute a API de ecommerce
4. Execute a API de inventory

## Endpoints principais

### Ecommerce API
- POST /product/create
- GET /product
- PUT /product/update/{id}
- POST /order/create
- GET /order

### Inventory API
- POST /inventory/supply
- POST /inventory/reserve
- GET /inventory/{productId}

## Objetivo do projeto

Este projeto foi desenvolvido como um estudo pessoal com foco em:
- arquitetura baseada em microsserviços
- comunicação assíncrona entre serviços
- boas práticas de desenvolvimento backend
- integração com banco de dados e mensageria
- organização de código em camadas e uso de DTOs

## Autor

Projeto pessoal desenvolvido para estudo e evolução técnica em Java e Spring Boot.
