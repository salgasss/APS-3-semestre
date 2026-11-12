# Sistema de Ordens de Serviço

Projeto acadêmico desenvolvido em Java (console), com MySQL e JDBC, para abrir no Eclipse.

## Requisitos

- JDK 17 ou superior
- Eclipse IDE com suporte a Maven
- MySQL Server 8.0 ou compatível
- MySQL Workbench (opcional, para executar o SQL visualmente)

## 1. Criar o banco de dados

1. Abra o MySQL Workbench.
2. Abra o arquivo `database/ordens_servico.sql`.
3. Execute o script inteiro.
4. Confirme que o banco `ordens_servico` e as quatro tabelas foram criados.

As tabelas são:
- `clientes`
- `ordens_servico`
- `em_producao`
- `finalizadas`

## 2. Configurar a conexão

Abra:

`src/main/java/br/com/ordensservico/Conexao.java`

Altere estas linhas para os dados do seu MySQL:

```java
private static final String USUARIO = "root";
private static final String SENHA = "SUA_SENHA_AQUI";
```

Se seu MySQL usa outra porta ou endereço, ajuste também a constante `URL`.

## 3. Importar no Eclipse

1. Extraia o arquivo ZIP.
2. Abra o Eclipse.
3. Vá em **File > Import**.
4. Selecione **Maven > Existing Maven Projects**.
5. Em **Root Directory**, escolha a pasta `SistemaOrdensServico` extraída.
6. Marque o `pom.xml` e clique em **Finish**.
7. Aguarde o Maven baixar o MySQL Connector/J.
8. Abra `src/main/java/br/com/ordensservico/Main.java`.
9. Clique com o botão direito e escolha **Run As > Java Application**.

Se o Eclipse perguntar sobre a configuração do Java, selecione um JDK 17 ou superior.

## 4. Como usar

1. Cadastre um cliente na opção 1.
2. Cadastre uma ordem para esse cliente na opção 2.
3. Consulte as ordens pelas opções 3, 4 e 5.
4. Atualize as operações pela opção 6.
5. Finalize a ordem pela opção 7, informando o número da nota fiscal.
6. Pesquise uma ordem específica pela opção 8.
7. Use a opção 10 para filtrar por nome do cliente, data de abertura e status. Os campos de cliente e data podem ficar vazios.

## Regras do sistema

- Toda ordem nova é criada com status **em produção**.
- A ordem só pode ser finalizada se estiver em produção.
- Ao finalizar, o registro correspondente sai de `em_producao` e é inserido em `finalizadas`.
- O ID da ordem é a chave primária e também a chave estrangeira nas tabelas de situação.
- A abertura da ordem e a criação do registro em produção usam uma transação.
- A finalização também usa uma transação para evitar que a ordem fique em um estado incompleto.

## Observação

O campo de operações aceita texto livre. Por exemplo:
- Operações concluídas: `Diagnóstico; troca de peça`
- Operações abertas: `Teste final`

Este é um projeto inicial funcional para fins acadêmicos. Antes de usar em uma empresa real, seria necessário acrescentar autenticação, controle de permissões, validações adicionais, cópias de segurança e auditoria.
