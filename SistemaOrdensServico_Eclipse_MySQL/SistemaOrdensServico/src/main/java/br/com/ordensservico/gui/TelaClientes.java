
package br.com.ordensservico.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TelaClientes extends JPanel {

    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEndereco;
    private JPanel formulario;
    private boolean modoEdicao = false;

    private JTable tabela;
    private DefaultTableModel modelo;

    private int proximoId = 1;
    private int idSelecionado = -1;

    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaClientes() {

        setLayout(new BorderLayout(20, 20));
        setBackground(FUNDO);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        criarCabecalho();
        criarTabela();
        criarFormulario();
        criarBotoes();

        atualizarTabela();
    }

    private void criarCabecalho() {

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(FUNDO);

        JLabel titulo = new JLabel("Cadastro de Clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(AZUL_ESCURO);

        JLabel subtitulo = new JLabel(
            "Gerencie os clientes cadastrados no sistema."
        );

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(FUNDO);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(8));
        textos.add(subtitulo);

        topo.add(textos, BorderLayout.WEST);
        add(topo, BorderLayout.NORTH);
    }

    private void criarTabela() {

        modelo = new DefaultTableModel(
            new String[]{"ID", "Nome", "Telefone", "Endereço"}, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));

        tabela.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormulario();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);

        scroll.setBorder(BorderFactory.createLineBorder(
            new Color(225, 230, 235)
        ));

        JPanel painelTabela = new JPanel(new BorderLayout(0, 10));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tituloTabela = new JLabel("Clientes cadastrados");
        tituloTabela.setFont(new Font("Arial", Font.BOLD, 17));
        tituloTabela.setForeground(AZUL_ESCURO);

        painelTabela.add(tituloTabela, BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }

    private void criarFormulario() {

    	formulario = new JPanel(new GridLayout(2, 3, 15, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(15, 15, 15, 15));
        formulario.setVisible(false);
        
        campoNome = new JTextField();
        campoTelefone = new JTextField();
        campoEndereco = new JTextField();

        formulario.add(criarCampo("Nome completo", campoNome));
        formulario.add(criarCampo("Telefone", campoTelefone));
        formulario.add(criarCampo("Endereço", campoEndereco));

        formulario.add(new JLabel());
        formulario.add(new JLabel());
        formulario.add(new JLabel());

        JPanel painelInferior = new JPanel(new BorderLayout(0, 10));
        painelInferior.setBackground(FUNDO);

        painelInferior.add(formulario, BorderLayout.CENTER);

        add(painelInferior, BorderLayout.SOUTH);
    }

    private JPanel criarCampo(String titulo, JTextField campo) {

        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBackground(Color.WHITE);

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Arial", Font.BOLD, 12));

        painel.add(label, BorderLayout.NORTH);
        painel.add(campo, BorderLayout.CENTER);

        return painel;
    }

    private void criarBotoes() {

        JButton novo = criarBotao("Novo");
        JButton salvar = criarBotao("Salvar");
        JButton editar = criarBotao("Editar");
        JButton excluir = criarBotao("Excluir");
        JButton atualizar = criarBotao("Atualizar");

        novo.addActionListener(e -> {
            limparCampos();
            formulario.setVisible(true);
            revalidate();
            repaint();
        });
        salvar.addActionListener(e -> {

            if (modoEdicao) {
                salvarEdicao();
            } else {
                salvarCliente();
            }

        });
        novo.addActionListener(e -> {
            modoEdicao = false;
            limparCampos();
            formulario.setVisible(true);
            revalidate();
            repaint();
        });
        editar.addActionListener(e -> editarCliente());
        excluir.addActionListener(e -> excluirCliente());
        atualizar.addActionListener(e -> atualizarTabela());

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        botoes.setBackground(FUNDO);

        botoes.add(novo);
        botoes.add(salvar);
        botoes.add(editar);
        botoes.add(excluir);
        botoes.add(atualizar);

        // Adiciona os botões ao painel inferior
        JPanel painelInferior = (JPanel) getComponent(2);
        painelInferior.add(botoes, BorderLayout.SOUTH);
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

    private void salvarCliente() {

        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String endereco = campoEndereco.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Informe o nome do cliente!"
            );
            return;
        }

        DadosSistema.Cliente cliente = new DadosSistema.Cliente(
            proximoId++, nome, telefone, endereco
        );

        DadosSistema.clientes.add(cliente);

        atualizarTabela();
        limparCampos();

        formulario.setVisible(false);
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(
            this, "Cliente cadastrado com sucesso!"
        );
    }

    private void editarCliente() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecione um cliente na tabela!"
            );
            return;
        }

        idSelecionado = (int) modelo.getValueAt(linha, 0);

        preencherFormulario();
        modoEdicao = true;

        formulario.setVisible(true);

        campoNome.requestFocus();
    }

    private void salvarEdicao() {

        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String endereco = campoEndereco.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Informe o nome do cliente!"
            );
            return;
        }

        for (DadosSistema.Cliente cliente : DadosSistema.clientes) {

            if (cliente.getId() == idSelecionado) {

                cliente.setNome(nome);
                cliente.setTelefone(telefone);
                cliente.setEndereco(endereco);

                break;
            }
        }

        atualizarTabela();

        modoEdicao = false;

        limparCampos();

        formulario.setVisible(false);

        revalidate();
        repaint();

        JOptionPane.showMessageDialog(
            this,
            "Cliente atualizado com sucesso!"
        );
    }
    private void excluirCliente() {

        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(
                this, "Selecione um cliente na tabela!"
            );
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir este cliente?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {

            DadosSistema.clientes.removeIf(
                cliente -> cliente.getId() == idSelecionado
            );

            atualizarTabela();
            limparCampos();
        }
    }

    private void atualizarTabela() {

        modelo.setRowCount(0);

        for (DadosSistema.Cliente cliente : DadosSistema.clientes) {

            modelo.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco()
            });
        }
    }

    private void preencherFormulario() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            return;
        }

        idSelecionado = (int) modelo.getValueAt(linha, 0);

        campoNome.setText(
            modelo.getValueAt(linha, 1).toString()
        );

        campoTelefone.setText(
            modelo.getValueAt(linha, 2).toString()
        );

        campoEndereco.setText(
            modelo.getValueAt(linha, 3).toString()
        );
    }

    private void limparCampos() {

        tabela.clearSelection();
        idSelecionado = -1;

        campoNome.setText("");
        campoTelefone.setText("");
        campoEndereco.setText("");

        campoNome.requestFocus();
    }
}
