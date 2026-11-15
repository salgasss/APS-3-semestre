
package br.com.ordensservico.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.com.ordensservico.gui.DadosSistema.OrdemServico;

import javax.swing.event.TableModelEvent;

public class TelaProducao extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;
    private boolean atualizandoTabela = false;

    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaProducao() {
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

        JLabel titulo = new JLabel("Ordens em Produção");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(AZUL_ESCURO);

        JLabel subtitulo = new JLabel(
            "Acompanhe os serviços que estão sendo executados."
        );

        topo.add(titulo);
        topo.add(Box.createVerticalStrut(8));
        topo.add(subtitulo);

        add(topo, BorderLayout.NORTH);
    }

    private void criarTabela() {
        modelo = new DefaultTableModel(
            new String[]{
                "Nº OS", "Cliente", "Descrição", "Quantidade",
                "Entrega", "Operação concluída", "Operação em aberto"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return coluna == 5 || coluna == 6;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(32);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        modelo.addTableModelListener(e -> salvarOperacoes(e));

        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelTabela = new JPanel(new BorderLayout(0, 10));
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel tituloTabela = new JLabel("Serviços em andamento");
        tituloTabela.setFont(new Font("Arial", Font.BOLD, 17));
        tituloTabela.setForeground(AZUL_ESCURO);

        painelTabela.add(tituloTabela, BorderLayout.NORTH);
        painelTabela.add(scroll, BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }


private void criarBotoes() {
    JButton atualizar = new JButton("Atualizar");
    JButton finalizar = new JButton("Finalizar Ordem");

    atualizar.setBackground(AZUL);
    atualizar.setForeground(Color.WHITE);
    atualizar.setFocusPainted(false);
    atualizar.addActionListener(e -> atualizarTabela());

    finalizar.setBackground(new Color(35, 135, 85));
    finalizar.setForeground(Color.WHITE);
    finalizar.setFocusPainted(false);

    finalizar.addActionListener(e -> finalizarOrdem());

    JPanel botoes = new JPanel(
        new FlowLayout(FlowLayout.RIGHT)
    );

    botoes.setBackground(FUNDO);
    botoes.add(atualizar);
    botoes.add(finalizar);

    add(botoes, BorderLayout.SOUTH);
}


    private void salvarOperacoes(TableModelEvent evento) {
        if (atualizandoTabela || evento.getType() != TableModelEvent.UPDATE) {
            return;
        }

        int linha = evento.getFirstRow();
        int coluna = evento.getColumn();

        if (linha < 0 || (coluna != 5 && coluna != 6)) {
            return;
        }

        List<DadosSistema.OrdemServico> ordens = obterOrdensEmProducao();

        if (linha >= ordens.size()) {
            return;
        }

        DadosSistema.OrdemServico ordem = ordens.get(linha);

        if (coluna == 5) {
            ordem.setOperacaoConcluida(
                String.valueOf(modelo.getValueAt(linha, coluna))
            );
        } else {
            ordem.setOperacaoAberta(
                String.valueOf(modelo.getValueAt(linha, coluna))
            );
        }
    }

    private void atualizarTabela() {
        atualizandoTabela = true;
        modelo.setRowCount(0);

        for (DadosSistema.OrdemServico ordem : obterOrdensEmProducao()) {
            modelo.addRow(new Object[]{
                ordem.getNumero(),
                ordem.getCliente(),
                ordem.getDescricao(),
                ordem.getQuantidade(),
                ordem.getDataEntrega(),
                ordem.getOperacaoConcluida() == null
                    ? "" : ordem.getOperacaoConcluida(),
                ordem.getOperacaoAberta() == null
                    ? ordem.getDescricao() : ordem.getOperacaoAberta()
            });
        }

        atualizandoTabela = false;
    }

private void finalizarOrdem() {

    int linha = tabela.getSelectedRow();

    if (linha == -1) {
        JOptionPane.showMessageDialog(
            this,
            "Selecione uma ordem para finalizar.",
            "Atenção",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int linhaModelo = tabela.convertRowIndexToModel(linha);

    List<DadosSistema.OrdemServico> ordens =
        obterOrdensEmProducao();

    DadosSistema.OrdemServico ordem = ordens.get(linhaModelo);

    int confirmacao = JOptionPane.showConfirmDialog(
        this,
        "Deseja realmente finalizar a ordem nº "
            + ordem.getNumero() + "?",
        "Confirmar finalização",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmacao == JOptionPane.YES_OPTION) {

        ordem.setStatus("Finalizada");

        atualizarTabela();

        JOptionPane.showMessageDialog(
            this,
            "Ordem finalizada com sucesso!"
        );
    }
}


private List<DadosSistema.OrdemServico> obterOrdensEmProducao() {

    List<DadosSistema.OrdemServico> lista = new ArrayList<>();

    if (DadosSistema.ordens == null) {
        return lista;
    }

    for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {

        if ("Em produção".equals(ordem.getStatus())) {
            lista.add(ordem);
        }
    }

    return lista;
}

}


