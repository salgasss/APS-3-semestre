package br.com.ordensservico.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelConteudo;
    private JPanel inicio;
    private final Color AZUL_ESCURO = new Color(25, 42, 65);
    private final Color AZUL = new Color(45, 105, 165);
    private final Color FUNDO = new Color(245, 247, 250);

    public TelaPrincipal() {

        setTitle("Sistema de Ordens de Serviço");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarMenu();
        criarConteudo();

    }

    private void criarMenu() {

        JPanel menu = new JPanel();
        menu.setBackground(AZUL_ESCURO);
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel titulo = new JLabel("ORDENS DE SERVIÇO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 17));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(titulo);
        menu.add(Box.createVerticalStrut(40));

        adicionarBotao(menu, "Início", "inicio");
        adicionarBotao(menu, "Clientes", "clientes");
        adicionarBotao(menu, "Nova Ordem", "nova");
        adicionarBotao(menu, "Todas as Ordens", "ordens");
        adicionarBotao(menu, "Em Produção", "producao");
        adicionarBotao(menu, "Finalizadas", "finalizadas");

        menu.add(Box.createVerticalGlue());

        JLabel rodape = new JLabel("Sistema de Gestão");
        rodape.setForeground(Color.LIGHT_GRAY);
        rodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(rodape);

        add(menu, BorderLayout.WEST);
    }

    private void adicionarBotao(JPanel menu, String texto,
                                String tela) {

        JButton botao = new JButton(texto);

        botao.setMaximumSize(new Dimension(200, 45));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setBackground(AZUL_ESCURO);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.PLAIN, 14));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addActionListener(e -> {

            if (tela.equals("inicio")) {

                painelConteudo.remove(inicio);

                inicio = criarInicio();

                adicionarTela("inicio", inicio);
            }

            cardLayout.show(painelConteudo, tela);

            painelConteudo.revalidate();
            painelConteudo.repaint();
        });

        menu.add(botao);
        menu.add(Box.createVerticalStrut(10));
    }

    private void criarConteudo() {

        painelConteudo = new JPanel();
        cardLayout = new CardLayout();

        painelConteudo.setLayout(cardLayout);
        painelConteudo.setBackground(FUNDO);

        inicio = criarInicio();
        adicionarTela("inicio", inicio);
        adicionarTela("clientes", new TelaClientes());
        adicionarTela("nova", new TelaNovaOrdem());
        adicionarTela("ordens", new TelaOrdens());
        adicionarTela("producao", new TelaProducao());
        adicionarTela("finalizadas", new TelaFinalizadas());
        
        add(painelConteudo, BorderLayout.CENTER);

        cardLayout.show(painelConteudo, "inicio");
    }

    private void adicionarTela(String nome, JPanel painel) {
        painelConteudo.add(painel, nome);
    }


private JPanel criarInicio() {

    JPanel painel = new JPanel(new BorderLayout(20, 20));
    painel.setBackground(FUNDO);
    painel.setBorder(new EmptyBorder(35, 35, 35, 35));

    // CABEÇALHO
    JPanel topo = new JPanel();
    topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
    topo.setBackground(FUNDO);

    JLabel titulo = new JLabel("Painel de Controle");
    titulo.setFont(new Font("Arial", Font.BOLD, 28));
    titulo.setForeground(AZUL_ESCURO);

    JLabel subtitulo = new JLabel(
        "Visão geral das atividades e ordens de serviço."
    );
    subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));

    topo.add(titulo);
    topo.add(Box.createVerticalStrut(10));
    topo.add(subtitulo);

    painel.add(topo, BorderLayout.NORTH);

    // INDICADORES
    JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
    cards.setBackground(FUNDO);

    cards.add(criarIndicador(
        "CLIENTES CADASTRADOS",
        String.valueOf(DadosSistema.clientes.size()),
        new Color(45, 105, 165)
    ));

    cards.add(criarIndicador(
        "TOTAL DE ORDENS",
        String.valueOf(DadosSistema.ordens.size()),
        new Color(105, 85, 170)
    ));

    cards.add(criarIndicador(
        "EM PRODUÇÃO",
        String.valueOf(contarOrdens("Em produção")),
        new Color(220, 145, 45)
    ));

    cards.add(criarIndicador(
        "FINALIZADAS",
        String.valueOf(contarOrdens("Finalizada")),
        new Color(35, 135, 85)
    ));

    painel.add(cards, BorderLayout.CENTER);

    // RODAPÉ
    JLabel rodape = new JLabel(
        "Sistema de Gerenciamento de Ordens de Serviço"
    );
    rodape.setFont(new Font("Arial", Font.PLAIN, 12));
    rodape.setForeground(Color.GRAY);

    painel.add(rodape, BorderLayout.SOUTH);

    return painel;
}


private JPanel criarIndicador(String titulo, String valor, Color cor) {

    JPanel card = new JPanel(new BorderLayout(10, 10));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(225, 230, 235)),
        new EmptyBorder(25, 25, 25, 25)
    ));

    JLabel textoTitulo = new JLabel(titulo);
    textoTitulo.setFont(new Font("Arial", Font.BOLD, 13));
    textoTitulo.setForeground(new Color(100, 110, 125));

    JLabel textoValor = new JLabel(valor);
    textoValor.setFont(new Font("Arial", Font.BOLD, 36));
    textoValor.setForeground(cor);

    card.add(textoTitulo, BorderLayout.NORTH);
    card.add(textoValor, BorderLayout.CENTER);

    return card;
}

private int contarOrdens(String status) {

    int total = 0;

    for (DadosSistema.OrdemServico ordem : DadosSistema.ordens) {
        if (status.equals(ordem.getStatus())) {
            total++;
        }
    }

    return total;
}


    private JPanel criarCard(String titulo, String descricao) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(225, 230, 235)),
            new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel nome = new JLabel(titulo);
        nome.setFont(new Font("Arial", Font.BOLD, 19));
        nome.setForeground(AZUL);

        JLabel texto = new JLabel(descricao);
        texto.setFont(new Font("Arial", Font.PLAIN, 13));
        texto.setForeground(Color.DARK_GRAY);

        card.add(nome);
        card.add(Box.createVerticalStrut(12));
        card.add(texto);

        return card;
    }

    private JPanel criarTelaSimples(String titulo) {

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(FUNDO);
        painel.setBorder(new EmptyBorder(35, 35, 35, 35));

        JLabel texto = new JLabel(titulo);
        texto.setFont(new Font("Arial", Font.BOLD, 26));
        texto.setForeground(AZUL_ESCURO);

        painel.add(texto, BorderLayout.NORTH);

        return painel;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });

    }
}