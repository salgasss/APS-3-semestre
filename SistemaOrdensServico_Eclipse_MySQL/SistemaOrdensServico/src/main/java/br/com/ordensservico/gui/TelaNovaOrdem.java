
package br.com.ordensservico.gui;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class TelaNovaOrdem extends JPanel {

    private JTextField campoNumero;
    private JTextField campoDataEntrada;
    private JTextField campoDataEntrega;
    private JTextField campoQuantidade;

    private JComboBox<String> comboCliente;
    private JComboBox<String> comboStatus;

    private JTextArea campoDescricao;
    private JTextArea campoObservacoes;

    private int proximoNumero = 1;

    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaNovaOrdem() {

        setLayout(new BorderLayout(20, 20));
        setBackground(FUNDO);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        criarCabecalho();
        criarFormulario();
        criarBotoes();

        carregarClientes();
        
        comboCliente.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                carregarClientes();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private void criarCabecalho() {

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(FUNDO);

        JLabel titulo = new JLabel("Nova Ordem de Serviço");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(AZUL_ESCURO);

        JLabel subtitulo = new JLabel(
            "Preencha os dados para abrir uma nova ordem."
        );

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(8));
        topo.add(subtitulo);

        add(topo, BorderLayout.NORTH);
    }

    private void criarFormulario() {

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        campoNumero = new JTextField(
            String.valueOf(proximoNumero)
        );

        campoNumero.setEditable(false);

        comboCliente = new JComboBox<>();

        campoDataEntrada = new JTextField(
            LocalDate.now().toString()
        );

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

        adicionarCampo(formulario, gbc, "Descrição do serviço:",
            new JScrollPane(campoDescricao), 0, 3);

        adicionarCampo(formulario, gbc, "Observações:",
            new JScrollPane(campoObservacoes), 1, 3);

        add(formulario, BorderLayout.CENTER);
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
        gbc.weightx = 1;

        painel.add(grupo, gbc);
    }

    private void criarBotoes() {

        JButton salvar = criarBotao("Salvar Ordem");
        JButton limpar = criarBotao("Limpar");
        JButton cancelar = criarBotao("Cancelar");

        salvar.addActionListener(e -> salvarOrdem());
        limpar.addActionListener(e -> limparCampos());
        cancelar.addActionListener(e -> limparCampos());

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        botoes.setBackground(FUNDO);

        botoes.add(cancelar);
        botoes.add(limpar);
        botoes.add(salvar);

        add(botoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto) {

        JButton botao = new JButton(texto);

        botao.setBackground(AZUL);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }

    private void carregarClientes() {

        String selecionado = (String) comboCliente.getSelectedItem();

        comboCliente.removeAllItems();

        comboCliente.addItem("Selecione um cliente");

        for (DadosSistema.Cliente cliente : DadosSistema.clientes) {
            comboCliente.addItem(cliente.getNome());
        }

        if (selecionado != null) {
            comboCliente.setSelectedItem(selecionado);
        } else {
            comboCliente.setSelectedIndex(0);
        }
    }


private void salvarOrdem() {

    String cliente = (String) comboCliente.getSelectedItem();

    if (cliente == null ||
        cliente.equals("Selecione um cliente")) {

        JOptionPane.showMessageDialog(
            this, "Selecione um cliente!"
        );

        return;
    }

    String dataEntrega = campoDataEntrega.getText().trim();
    String quantidadeTexto = campoQuantidade.getText().trim();
    String descricao = campoDescricao.getText().trim();

    if (dataEntrega.isEmpty()
        || quantidadeTexto.isEmpty()
        || descricao.isEmpty()) {

        JOptionPane.showMessageDialog(
            this, "Preencha os campos obrigatórios!"
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
            this, "Informe uma quantidade válida!"
        );

        return;
    }

    // Cria a ordem de serviço
    DadosSistema.OrdemServico ordem =
        new DadosSistema.OrdemServico(
            proximoNumero,
            cliente,
            campoDataEntrada.getText(),
            dataEntrega,
            quantidade,
            (String) comboStatus.getSelectedItem(),
            descricao,
            campoObservacoes.getText().trim()
        );

    // Armazena a ordem na lista compartilhada
    DadosSistema.ordens.add(ordem);

    JOptionPane.showMessageDialog(
        this,
        "Ordem de Serviço criada com sucesso!\n"
        + "Número: " + proximoNumero
        + "\nCliente: " + cliente
    );

    proximoNumero++;

    limparCampos();

    campoNumero.setText(String.valueOf(proximoNumero));
}


    private void limparCampos() {

        comboCliente.setSelectedIndex(0);

        campoDataEntrada.setText(LocalDate.now().toString());
        campoDataEntrega.setText("");
        campoQuantidade.setText("");
        campoDescricao.setText("");
        campoObservacoes.setText("");

        comboStatus.setSelectedIndex(0);
    }
}
