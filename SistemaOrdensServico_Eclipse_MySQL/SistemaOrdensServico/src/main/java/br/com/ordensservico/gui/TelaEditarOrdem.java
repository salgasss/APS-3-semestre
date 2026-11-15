
package br.com.ordensservico.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaEditarOrdem extends JDialog {

    private DadosSistema.OrdemServico ordem;

    private JTextField campoNumero;
    private JTextField campoDataEntrada;
    private JTextField campoDataEntrega;
    private JTextField campoQuantidade;

    private JComboBox<String> comboCliente;
    private JComboBox<String> comboStatus;

    private JTextArea campoDescricao;
    private JTextArea campoObservacoes;

    public TelaEditarOrdem(
        JFrame janela,
        DadosSistema.OrdemServico ordem
    ) {

        super(janela, "Editar Ordem de Serviço", true);

        this.ordem = ordem;

        setSize(750, 650);
        setLocationRelativeTo(janela);
        setLayout(new BorderLayout(15, 15));

        criarFormulario();
        carregarDados();

    }

    private void criarFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        campoNumero = new JTextField();
        campoNumero.setEditable(false);

        comboCliente = new JComboBox<>();

        campoDataEntrada = new JTextField();
        campoDataEntrega = new JTextField();
        campoQuantidade = new JTextField();

        comboStatus = new JComboBox<>(
            new String[]{"Aberta", "Em produção", "Finalizada"}
        );

        campoDescricao = new JTextArea(3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);

        campoObservacoes = new JTextArea(3, 20);
        campoObservacoes.setLineWrap(true);
        campoObservacoes.setWrapStyleWord(true);

        adicionarCampo(formulario, gbc, "Número da OS:",
            campoNumero, 0, 0);

        adicionarCampo(formulario, gbc, "Cliente:",
            comboCliente, 1, 0);

        adicionarCampo(formulario, gbc, "Data de entrada:",
            campoDataEntrada, 0, 1);

        adicionarCampo(formulario, gbc, "Data de entrega:",
            campoDataEntrega, 1, 1);

        adicionarCampo(formulario, gbc, "Quantidade:",
            campoQuantidade, 0, 2);

        adicionarCampo(formulario, gbc, "Status:",
            comboStatus, 1, 2);

        adicionarCampo(formulario, gbc, "Descrição:",
            new JScrollPane(campoDescricao), 0, 3);

        adicionarCampo(formulario, gbc, "Observações:",
            new JScrollPane(campoObservacoes), 1, 3);

        add(formulario, BorderLayout.CENTER);

        JButton salvar = new JButton("Salvar alterações");
        JButton cancelar = new JButton("Cancelar");

        salvar.addActionListener(e -> salvarAlteracoes());
        cancelar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        botoes.add(cancelar);
        botoes.add(salvar);

        add(botoes, BorderLayout.SOUTH);
    }

    private void adicionarCampo(
        JPanel painel,
        GridBagConstraints gbc,
        String titulo,
        JComponent campo,
        int coluna,
        int linha
    ) {

        JPanel grupo = new JPanel(new BorderLayout(5, 5));
        grupo.setBackground(Color.WHITE);

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Arial", Font.BOLD, 12));

        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);

        gbc.gridx = coluna;
        gbc.gridy = linha;

        painel.add(grupo, gbc);
    }

    private void carregarDados() {

        campoNumero.setText(
            String.valueOf(ordem.getNumero())
        );

        for (DadosSistema.Cliente cliente : DadosSistema.clientes) {
            comboCliente.addItem(cliente.getNome());
        }

        comboCliente.setSelectedItem(ordem.getCliente());

        campoDataEntrada.setText(ordem.getDataEntrada());
        campoDataEntrega.setText(ordem.getDataEntrega());

        campoQuantidade.setText(
            String.valueOf(ordem.getQuantidade())
        );

        comboStatus.setSelectedItem(ordem.getStatus());

        campoDescricao.setText(ordem.getDescricao());
        campoObservacoes.setText(ordem.getObservacoes());
    }

    private void salvarAlteracoes() {

        String cliente = (String) comboCliente.getSelectedItem();
        String dataEntrada = campoDataEntrada.getText().trim();
        String dataEntrega = campoDataEntrega.getText().trim();
        String quantidadeTexto = campoQuantidade.getText().trim();
        String descricao = campoDescricao.getText().trim();
        String observacoes = campoObservacoes.getText().trim();

        if (cliente == null || cliente.isEmpty()
            || dataEntrada.isEmpty()
            || dataEntrega.isEmpty()
            || quantidadeTexto.isEmpty()
            || descricao.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Preencha todos os campos obrigatórios!"
            );

            return;
        }

        int quantidade;

        try {

            quantidade = Integer.parseInt(quantidadeTexto);

            if (quantidade <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                this,
                "Informe uma quantidade válida!"
            );

            return;
        }

        ordem.setCliente(cliente);
        ordem.setDataEntrada(dataEntrada);
        ordem.setDataEntrega(dataEntrega);
        ordem.setQuantidade(quantidade);
        ordem.setStatus((String) comboStatus.getSelectedItem());
        ordem.setDescricao(descricao);
        ordem.setObservacoes(observacoes);

        JOptionPane.showMessageDialog(
            this,
            "Ordem atualizada com sucesso!"
        );

        dispose();
    }
}
