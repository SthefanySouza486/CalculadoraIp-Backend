# 🖩 Calculadora de Sub-redes IP - Backend (Spring Boot)

Este é o backend da aplicação **Calculadora de Sub-redes**, desenvolvida como atividade prática para a disciplina de Redes de Computadores. A API é responsável por receber um endereço IPv4 junto com sua máscara (ou notação CIDR) e realizar os cálculos lógicos bit a bit para retornar os detalhes completos da rede.

## 🚀 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 4** (Web)
* **Lombok** (Para redução de boilerplate de código)
* **Maven** (Gerenciamento de dependências)

## ⚙️ Arquitetura

O projeto foi estruturado seguindo as boas práticas de separação de responsabilidades, mesmo não possuindo persistência em banco de dados:

* **Controller:** Recebe as requisições HTTP do frontend (Angular).
* **Service:** Contém a regra de negócio e a matemática binária (operações AND, OR, deslocamento de bits) para o cálculo das sub-redes.
* **DTO:** Objetos de transferência de dados (`IpRequest` e `IpResponse`) para trafegar informações limpas e tipadas.
* **Entity:** Representação em memória dos octetos do IP e da Máscara.

> **Nota:** A configuração de autoconfiguração de banco de dados (`DataSourceAutoConfiguration`) foi desativada intencionalmente na classe main, pois a aplicação realiza apenas cálculos em tempo de execução.

## 🛠️ Como executar o projeto

1. Certifique-se de ter o **Java 17+** e o **Maven** instalados na sua máquina.
2. Clone este repositório.
3. Navegue até a pasta raiz do projeto backend.
4. Execute o comando maven para iniciar a aplicação:

```bash
mvn spring-boot:run
```

5. O servidor iniciará na porta `8080`.

## 📡 Documentação da API

### Calcular Rede

Realiza o cálculo da sub-rede com base no IP e máscara informados.

* **URL:** `/api/calculadora/calcular`
* **Método:** `POST`
* **Corpo da Requisição (JSON):**

A máscara pode ser informada no formato decimal pontuado (ex: `255.255.255.0`) ou em notação CIDR (ex: `/24`).

```json
{
  "ip": "200.10.20.30",
  "maskOrCidr": "/30"
}
```

* **Resposta de Sucesso (200 OK):**

```json
{
  "ipInformado": "200.10.20.30",
  "mascaraSubRede": "255.255.255.252",
  "prefixoCidr": "/30",
  "enderecoRede": "200.10.20.28",
  "enderecoBroadcast": "200.10.20.31",
  "primeiroIpValido": "200.10.20.29",
  "ultimoIpValido": "200.10.20.30",
  "quantidadeHosts": 2,
  "classeIp": "C"
}
```

## 🧠 Como os cálculos funcionam (Resumo)

* **Endereço de Rede:** Obtido através de uma operação **AND bit a bit** entre os octetos do IP e os octetos da Máscara.
* **Endereço de Broadcast:** Obtido invertendo os bits da Máscara (operação NOT) e realizando uma operação **OR bit a bit** com o IP original.
* **Quantidade de Hosts:** Utiliza a fórmula $2^{(32 - CIDR)} - 2$, subtraindo os endereços de rede e broadcast.

## 🖥️ Frontend da Aplicação

Este projeto foi desenvolvido em uma arquitetura de repositórios separados (Split Stack).

A interface de usuário (Frontend), responsável por consumir esta API e apresentar a calculadora de forma interativa e com Dark Mode, foi desenvolvida utilizando **Angular**.

🔗 **Acesse o repositório do Frontend aqui:** [Link para o seu repositório do frontend]