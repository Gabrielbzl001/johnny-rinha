# johnny-rinha

Rinha de Backend 2025 - Sistema de Intermediação de Pagamentos

# NOME DO PROJETO

SISTEMA DE AUTOMAÇÃO PARA PAGAMENTOS

# Tecnologias  

## Linguagem & Framework  
![Java](https://img.shields.io/badge/Java-20.4-%23ED8B00?logo=openjdk&logoColor=white)  
![Spring](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=white)    

## Ferramentas & Infraestrutura
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)   

## Instruções

Para iniciar os containers, execute o seguinte comando:

```bash
docker compose -f docker-compose-local.yml up --build -d
```

O serviço de backend estará acessível na porta 9999.

# TODO

- [x] configurar load balancer(nginx)
- [ ] configurar limits cpu e memória
- [x] implementar verificação service-health(chooseDefault)
- [ ] implementar fila de processamento(provavelmente no redis mesmo)
- [x] tornar os parâmetros `from` e `get` do endpoint `GET /payments-summary` opcionais
