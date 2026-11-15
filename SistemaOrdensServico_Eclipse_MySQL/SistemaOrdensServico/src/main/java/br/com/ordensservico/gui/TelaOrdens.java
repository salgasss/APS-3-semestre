
package br.com.ordensservico.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TelaOrdens extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField campoPesquisa;

    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaOrdens() {

        setLayout(new BorderLayout(20, 20));
        setBackground(FUNDO);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        criarCabecalho();
        criarTabela();
        criarBotoes();
        atualizarTabela();
    }

    private void criarCabecalho() {

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(FUNDO);

        JLabel titulo = new JLabel("Todas as Ordens");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(AZUL_ESCURO);

        JLabel subtitulo = new JLabel(
            "Consulte e pesquise todas as ordens cadastradas."
        );

        campoPesquisa = new JTextField();
        campoPesquisa.setFont(new Font("Arial", Font.PLAIN, 14));
        campoPesquisa.setPreferredSize(new Dimension(350, 35));
        campoPesquisa.setMaximumSize(new Dimension(500, 35));

        campoPesquisa.setToolTipText(
            "Digite o número da OS ou o nome do cliente"
        );

        campoPesquisa.addActionListener(e -> pesquisarOrdens());

        campoPesquisa.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {

                public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {
                    pesquisarOrdens();
                }

                public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {
                    pesquisarOrdens();
                }

                public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {
                    pesquisarOrdens();
                }
            }
        );

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(8));
        topo.add(subtitulo);
        topo.add(Box.createVerticalStrut(20));
        topo.add(campoPesquisa);

        add(topo, BorderLayout.NORTH);
    }

    private void criarTabela() {

        modelo = new DefaultTableModel(
            new String[]{
                "Nº OS",
                "Cliente",
                "Data de entrada",
                "Data de entrega",
                "Quantidade",
                "Status",
                "Descrição"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(32);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));

        tabela.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout(0, 10));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tituloTabela = new JLabel("Ordens cadastradas");
        tituloTabela.setFont(new Font("Arial", Font.BOLD, 17));
        tituloTabela.setForeground(AZUL_ESCURO);

        painelTabela.add(tituloTabela, BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }

    private void criarBotoes() {

        JButton atualizar = new JButton("Atualizar");
        JButton limpar = new JButton("Limpar pesquisa");
        JButton editar = new JButton("Editar Ordem");
        
        editar.addActionListener(e -> editarOrdem());
        atualizar.setBackground(AZUL);
        atualizar.setForeground(Color.WHITE);
        atualizar.setFocusPainted(false);
        atualizar.addActionListener(e -> atualizarTabela());
        
        limpar.addActionListener(e -> {
            campoPesquisa.setText("");
            atualizarTabela();
        });
        

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        botoes.setBackground(FUNDO);
        botoes.add(limpar);
        botoes.add(editar);
        botoes.add(atualizar);
        

        add(botoes, BorderLayout.SOUTH);
    }

 

    private void pesquisarOrdens() {

        String pesquisa = campoPesquisa.getText()
            .trim()
            .toLowerCase();

        modelo.setRowCount(0);

        for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {

            String numero = String.valueOf(ordem.getNumero())
                .toLowerCase();

            String cliente = ordem.getCliente() == null
                ? "" : ordem.getCliente().toLowerCase();

            if (numero.contains(pesquisa)
                    || cliente.contains(pesquisa)) {

                modelo.addRow(new Object[]{
                    ordem.getNumero(),
                    ordem.getCliente(),
                    ordem.getDataEntrada(),
                    ordem.getDataEntrega(),
                    ordem.getQuantidade(),
                    ordem.getStatus(),
                    ordem.getDescricao()
                });
            }
        }
    }
    private void editarOrdem() {

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecione uma ordem para editar!"
            );

            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(
            linhaSelecionada
        );

        int numeroOS = (int) modelo.getValueAt(
            linhaModelo, 0
        );

        DadosSistema.OrdemServico ordemSelecionada = null;

        for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {

            if (ordem.getNumero() == numeroOS) {
                ordemSelecionada = ordem;
                break;
            }
        }

        if (ordemSelecionada != null) {

            JFrame janela = (JFrame) SwingUtilities.getWindowAncestor(this);

            TelaEditarOrdem telaEdicao = new TelaEditarOrdem(
                janela,
                ordemSelecionada
            );

            telaEdicao.setVisible(true);

            atualizarTabela();

            pesquisarOrdens();
        }
    }
    private void atualizarTabela() {

        modelo.setRowCount(0);

        for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {

            modelo.addRow(new Object[]{
                ordem.getNumero(),
                ordem.getCliente(),
                ordem.getDataEntrada(),
                ordem.getDataEntrega(),
                ordem.getQuantidade(),
                ordem.getStatus(),
                ordem.getDescricao()
            });
        }
    }
}
