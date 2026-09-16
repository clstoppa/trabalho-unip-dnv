-- =====================================================================
-- Sistema ERP de Compra e Venda - PPOO / LPBD - UNIP
-- Script de criação do banco de dados (PostgreSQL)
-- Execute dentro do banco "faculdade" (ou o schema que estiver usando)
-- Este script pode ser executado quantas vezes for necessário: ele
-- apaga as tabelas existentes antes de recriá-las.
-- =====================================================================

-- =====================================================================
-- Remove tabelas antigas (ordem inversa às dependências)
-- =====================================================================
DROP TABLE IF EXISTS compra_produto CASCADE;
DROP TABLE IF EXISTS compra CASCADE;
DROP TABLE IF EXISTS venda_pagto CASCADE;
DROP TABLE IF EXISTS venda_produto CASCADE;
DROP TABLE IF EXISTS venda CASCADE;
DROP TABLE IF EXISTS formapagto CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS fornecedor CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS pessoa CASCADE;

-- =====================================================================
-- PESSOA (superclasse de Cliente e Fornecedor)
-- =====================================================================
CREATE TABLE pessoa (
    pes_codigo      SERIAL PRIMARY KEY,
    pes_nome        VARCHAR(80)  NOT NULL,
    pes_fantasia    VARCHAR(80),
    pes_fisica      CHAR(1)      NOT NULL,      -- 'F' = física, 'J' = jurídica
    pes_cpfcnpj     VARCHAR(20)  NOT NULL,
    pes_rgie        VARCHAR(20),
    pes_cadastro    DATE         NOT NULL,
    pes_endereco    VARCHAR(120),
    pes_numero      VARCHAR(10),
    pes_complemento VARCHAR(30),
    pes_bairro      VARCHAR(50),
    pes_cidade      VARCHAR(80),
    pes_uf          CHAR(2),
    pes_cep         VARCHAR(9),
    pes_fone1       VARCHAR(16),
    pes_fone2       VARCHAR(16),
    pes_celular     VARCHAR(16),
    pes_site        VARCHAR(200),
    pes_email       VARCHAR(200),
    pes_ativo       CHAR(1)      NOT NULL DEFAULT 'S'
);

-- =====================================================================
-- CLIENTE
-- =====================================================================
CREATE TABLE cliente (
    cli_codigo     SERIAL PRIMARY KEY,
    pes_codigo     INT NOT NULL,
    cli_limitecred DECIMAL(18,2) DEFAULT 0,
    CONSTRAINT fk_cliente_pessoa FOREIGN KEY (pes_codigo) REFERENCES pessoa(pes_codigo)
);

-- =====================================================================
-- FORNECEDOR
-- =====================================================================
CREATE TABLE fornecedor (
    for_codigo   SERIAL PRIMARY KEY,
    pes_codigo   INT NOT NULL,
    for_contato  VARCHAR(80),
    CONSTRAINT fk_fornecedor_pessoa FOREIGN KEY (pes_codigo) REFERENCES pessoa(pes_codigo)
);

-- =====================================================================
-- USUARIO
-- =====================================================================
CREATE TABLE usuario (
    usu_codigo   SERIAL PRIMARY KEY,
    usu_nome     VARCHAR(80)  NOT NULL,
    usu_login    VARCHAR(20)  NOT NULL UNIQUE,
    usu_senha    VARCHAR(80)  NOT NULL,
    usu_cadastro DATE         NOT NULL,
    usu_ativo    CHAR(1)      NOT NULL DEFAULT 'S'
);

-- =====================================================================
-- PRODUTO
-- =====================================================================
CREATE TABLE produto (
    pro_codigo    SERIAL PRIMARY KEY,
    pro_nome      VARCHAR(80)   NOT NULL,
    pro_estoque   DECIMAL(14,4) NOT NULL DEFAULT 0,
    pro_unidade   VARCHAR(5),
    pro_preco     DECIMAL(18,2) NOT NULL DEFAULT 0,
    pro_custo     DECIMAL(18,2) NOT NULL DEFAULT 0,
    pro_atacado   DECIMAL(18,2) DEFAULT 0,
    pro_min       DECIMAL(14,4) DEFAULT 0,
    pro_max       DECIMAL(14,4) DEFAULT 0,
    pro_embalagem DECIMAL(9,0)  DEFAULT 0,
    pro_peso      DECIMAL(14,4) DEFAULT 0,
    pro_cadastro  DATE          NOT NULL,
    pro_obs       TEXT,
    pro_ativo     CHAR(1)       NOT NULL DEFAULT 'S'
);

-- =====================================================================
-- FORMA DE PAGAMENTO
-- =====================================================================
CREATE TABLE formapagto (
    fpg_codigo SERIAL PRIMARY KEY,
    fpg_nome   VARCHAR(80) NOT NULL,
    fpg_ativo  CHAR(1)     NOT NULL DEFAULT 'S'
);

-- =====================================================================
-- VENDA
-- =====================================================================
CREATE TABLE venda (
    vda_codigo   SERIAL PRIMARY KEY,
    usu_codigo   INT NOT NULL,
    cli_codigo   INT NOT NULL,
    vda_data     DATE NOT NULL,
    vda_valor    DECIMAL(18,2) NOT NULL DEFAULT 0,
    vda_desconto DECIMAL(18,2) DEFAULT 0,
    vda_total    DECIMAL(18,2) NOT NULL DEFAULT 0,
    vda_obs      TEXT,
    CONSTRAINT fk_venda_usuario FOREIGN KEY (usu_codigo) REFERENCES usuario(usu_codigo),
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cli_codigo) REFERENCES cliente(cli_codigo)
);

-- =====================================================================
-- VENDA_PRODUTO (itens da venda)
-- =====================================================================
CREATE TABLE venda_produto (
    vep_codigo   SERIAL PRIMARY KEY,
    vda_codigo   INT NOT NULL,
    pro_codigo   INT NOT NULL,
    vep_qtde     DECIMAL(14,4) NOT NULL,
    vep_preco    DECIMAL(18,2) NOT NULL,
    vep_desconto DECIMAL(18,2) DEFAULT 0,
    vep_total    DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_vendaproduto_venda   FOREIGN KEY (vda_codigo) REFERENCES venda(vda_codigo),
    CONSTRAINT fk_vendaproduto_produto FOREIGN KEY (pro_codigo) REFERENCES produto(pro_codigo)
);

-- =====================================================================
-- VENDA_PAGTO (formas de pagamento usadas na venda)
-- =====================================================================
CREATE TABLE venda_pagto (
    vdp_codigo SERIAL PRIMARY KEY,
    vda_codigo INT NOT NULL,
    fpg_codigo INT NOT NULL,
    vdp_valor  DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_vendapagto_venda      FOREIGN KEY (vda_codigo) REFERENCES venda(vda_codigo),
    CONSTRAINT fk_vendapagto_formapagto FOREIGN KEY (fpg_codigo) REFERENCES formapagto(fpg_codigo)
);

-- =====================================================================
-- COMPRA
-- =====================================================================
CREATE TABLE compra (
    cpr_codigo    SERIAL PRIMARY KEY,
    usu_codigo    INT NOT NULL,
    for_codigo    INT NOT NULL,
    cpr_emissao   DATE NOT NULL,
    cpr_valor     DECIMAL(18,2) NOT NULL DEFAULT 0,
    cpr_desconto  DECIMAL(18,2) DEFAULT 0,
    cpr_total     DECIMAL(18,2) NOT NULL DEFAULT 0,
    cpr_dtentrada DATE,
    cpr_obs       TEXT,
    CONSTRAINT fk_compra_usuario    FOREIGN KEY (usu_codigo) REFERENCES usuario(usu_codigo),
    CONSTRAINT fk_compra_fornecedor FOREIGN KEY (for_codigo) REFERENCES fornecedor(for_codigo)
);

-- =====================================================================
-- COMPRA_PRODUTO (itens da compra)
-- =====================================================================
CREATE TABLE compra_produto (
    cpp_codigo   SERIAL PRIMARY KEY,
    cpr_codigo   INT NOT NULL,
    pro_codigo   INT NOT NULL,
    cpr_qtde     DECIMAL(14,4) NOT NULL,
    cpr_preco    DECIMAL(18,2) NOT NULL,
    cpr_desconto DECIMAL(18,2) DEFAULT 0,
    cpr_total    DECIMAL(18,2) NOT NULL,
    CONSTRAINT fk_compraproduto_compra  FOREIGN KEY (cpr_codigo) REFERENCES compra(cpr_codigo),
    CONSTRAINT fk_compraproduto_produto FOREIGN KEY (pro_codigo) REFERENCES produto(pro_codigo)
);

-- =====================================================================
-- Usuário inicial para testar o login
-- =====================================================================
INSERT INTO usuario (usu_nome, usu_login, usu_senha, usu_cadastro, usu_ativo)
VALUES ('Administrador', 'admin', 'admin', CURRENT_DATE, 'S')
ON CONFLICT (usu_login) DO NOTHING;
