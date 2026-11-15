
package br.com.ordensservico.gui;

import java.util.ArrayList;
import java.util.List;

public class DadosSistema {

    public static List<Cliente> clientes = new ArrayList<>();

    public static List<OrdemServico> ordens = new ArrayList<>();

    public static class Cliente {

        private int id;
        private String nome;
        private String telefone;
        private String endereco;

        public Cliente(int id, String nome, String telefone, String endereco) {
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;
            this.endereco = endereco;
        }

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }
    }

    public static class OrdemServico {

        private int numero;
        private String cliente;
        private String dataEntrada;
        private String dataEntrega;
        private int quantidade;
        private String status;
        private String descricao;
        private String observacoes;
        private String operacaoConcluida;
        private String operacaoAberta;

        public OrdemServico(
            int numero,
            String cliente,
            String dataEntrada,
            String dataEntrega,
            int quantidade,
            String status,
            String descricao,
            String observacoes
        ) {
            this.numero = numero;
            this.cliente = cliente;
            this.dataEntrada = dataEntrada;
            this.dataEntrega = dataEntrega;
            this.quantidade = quantidade;
            this.status = status;
            this.descricao = descricao;
            this.observacoes = observacoes;
        }

        public int getNumero() {
            return numero;
        }

        public String getCliente() {
            return cliente;
        }

        public String getDataEntrada() {
            return dataEntrada;
        }

        public String getDataEntrega() {
            return dataEntrega;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getStatus() {
            return status;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getObservacoes() {
            return observacoes;
        }
        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public void setDataEntrada(String dataEntrada) {
            this.dataEntrada = dataEntrada;
        }

        public void setDataEntrega(String dataEntrega) {
            this.dataEntrega = dataEntrega;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public void setObservacoes(String observacoes) {
            this.observacoes = observacoes;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getOperacaoConcluida() {
            return operacaoConcluida;
        }

        public void setOperacaoConcluida(String operacaoConcluida) {
            this.operacaoConcluida = operacaoConcluida;
        }

        public String getOperacaoAberta() {
            return operacaoAberta;
        }

        public void setOperacaoAberta(String operacaoAberta) {
            this.operacaoAberta = operacaoAberta;
        }
    }
}
