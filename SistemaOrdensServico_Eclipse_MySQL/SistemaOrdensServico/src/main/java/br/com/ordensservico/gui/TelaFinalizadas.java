
package br.com.ordensservico.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TelaFinalizadas extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;

    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaFinalizadas() {
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

        JLabel titulo = new JLabel("Ordens Finalizadas");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(AZUL_ESCURO);

        JLabel subtitulo = new JLabel(
            "Consulte os serviços que já foram concluídos."
        );

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(8));
        topo.add(subtitulo);

        add(topo, BorderLayout.NORTH);
    }

    private void criarTabela() {
        modelo = new DefaultTableModel(
            new String[]{
                "Nº OS",
                "Cliente",
                "Descrição",
                "Quantidade",
                "Data de entrega",
                "Status"
            }, 0
        );

        tabela = new JTable(modelo);
        tabela.setRowHeight(32);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );
        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout(0, 10));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tituloTabela = new JLabel("Histórico de serviços");
        tituloTabela.setFont(new Font("Arial", Font.BOLD, 17));
        tituloTabela.setForeground(AZUL_ESCURO);

        painelTabela.add(tituloTabela, BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }

    private void criarBotoes() {
        JButton atualizar = new JButton("Atualizar");
        atualizar.setBackground(AZUL);
        atualizar.setForeground(Color.WHITE);
        atualizar.setFont(new Font("Arial", Font.BOLD, 12));
        atualizar.setFocusPainted(false);
        atualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        atualizar.addActionListener(e -> atualizarTabela());

        JPanel botoes = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );
        botoes.setBackground(FUNDO);
        botoes.add(atualizar);

        add(botoes, BorderLayout.SOUTH);
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);

        for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {

            if (ordem.getStatus().equals("Finalizada")) {

                modelo.addRow(new Object[]{
                    ordem.getNumero(),
                    ordem.getCliente(),
                    ordem.getDescricao(),
                    ordem.getQuantidade(),
                    ordem.getDataEntrega(),
                    ordem.getStatus()
                });
            }
        }
    }
}
