CREATE DATABASE IF NOT EXISTS ordens_servico
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ordens_servico;

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(30),
    endereco VARCHAR(180)
);

CREATE TABLE IF NOT EXISTS ordens_servico (
    id_os INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data_abertura DATE NOT NULL,
    CONSTRAINT fk_os_cliente
        FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE TABLE IF NOT EXISTS em_producao (
    id_os INT PRIMARY KEY,
    operacoes_concluidas TEXT,
    operacoes_abertas TEXT,
    CONSTRAINT fk_producao_os
        FOREIGN KEY (id_os) REFERENCES ordens_servico(id_os)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS finalizadas (
    id_os INT PRIMARY KEY,
    data_finalizacao DATE NOT NULL,
    nota_fiscal VARCHAR(60) NOT NULL,
    CONSTRAINT fk_finalizadas_os
        FOREIGN KEY (id_os) REFERENCES ordens_servico(id_os)
        ON DELETE CASCADE
);

-- Dados de exemplo opcionais:
-- INSERT INTO clientes (nome, telefone, endereco)
-- VALUES ('Cliente Teste', '(19) 99999-9999', 'Campinas - SP');
