import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static int proximoId = 1;

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1: cadastrarPedido(); break;
                case 2: alterarStatus(); break;
                case 3: processarPedidos(); break;
                case 4: removerCancelados(); break;
                case 5: exibirPedidos(); break;
                case 6: exibirEntregaveis(); break;
                case 0: System.out.println("Encerrando o sistema..."); break;
                default: System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
        sc.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===== SISTEMA GERENCIADOR DE PEDIDOS =====");
        System.out.println("1 - Cadastrar pedido");
        System.out.println("2 - Alterar status de um pedido");
        System.out.println("3 - Processar pedidos");
        System.out.println("4 - Remover cancelados");
        System.out.println("5 - Exibir lista de pedidos");
        System.out.println("6 - Exibir pedidos entregáveis");
        System.out.println("0 - Encerrar");
    }

    // ---------- Leitura segura de dados ----------

    private static int lerInteiro(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    private static double lerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número (ex: 150.50).");
            }
        }
    }

    // ---------- Funcionalidades ----------

    private static void cadastrarPedido() {
        System.out.println("\n--- Cadastrar pedido ---");
        System.out.println("1 - Eletrônico | 2 - Roupa | 3 - Alimento");
        int tipo = lerInteiro("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido!");
            return;
        }

        System.out.print("Nome do cliente: ");
        String nome = sc.nextLine().trim();
        double valor = lerDouble("Valor do pedido: R$ ");

        try {
            Pedido p;
            switch (tipo) {
                case 1: p = new PedidoEletronico(proximoId, nome, valor); break;
                case 2: p = new PedidoRoupa(proximoId, nome, valor); break;
                default: p = new PedidoAlimento(proximoId, nome, valor); break;
            }
            pedidos.add(p);
            proximoId++;
            System.out.println("Pedido cadastrado com sucesso! ID: " + p.getId());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private static Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static void alterarStatus() {
        System.out.println("\n--- Alterar status ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        int id = lerInteiro("ID do pedido: ");
        Pedido p = buscarPorId(id);
        if (p == null) {
            System.out.println("Pedido não encontrado!");
            return;
        }

        System.out.println("Status atual: " + p.getStatus());
        StatusPedido[] valores = StatusPedido.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + " - " + valores[i]);
        }
        int escolha = lerInteiro("Novo status: ");
        if (escolha < 1 || escolha > valores.length) {
            System.out.println("Status inválido!");
            return;
        }

        try {
            p.setStatus(valores[escolha - 1]);
            System.out.println("Status alterado para " + p.getStatus());
        } catch (PedidoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Usa Iterator para processar todos os pedidos
    private static void processarPedidos() {
        System.out.println("\n--- Processando pedidos ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            Pedido p = it.next();
            try {
                p.processarPedido();
                System.out.println("Pedido #" + p.getId() + " -> status: " + p.getStatus());
            } catch (PedidoInvalidoException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    // Usa Iterator para remover os cancelados
    private static void removerCancelados() {
        System.out.println("\n--- Removendo cancelados ---");
        int removidos = 0;
        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            Pedido p = it.next();
            if (p.getStatus() == StatusPedido.CANCELADO) {
                it.remove();
                removidos++;
            }
        }
        System.out.println(removidos + " pedido(s) cancelado(s) removido(s).");
    }

    private static void exibirPedidos() {
        System.out.println("\n--- Lista de pedidos (sem cancelados) ---");
        boolean achou = false;
        for (Pedido p : pedidos) {
            if (p.getStatus() != StatusPedido.CANCELADO) {
                System.out.println(p);
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum pedido para exibir.");
        }
    }

    private static void exibirEntregaveis() {
        System.out.println("\n--- Pedidos entregáveis ---");
        boolean achou = false;
        for (Pedido p : pedidos) {
            if (p instanceof Entregavel && p.getStatus() != StatusPedido.CANCELADO) {
                Entregavel e = (Entregavel) p;
                System.out.printf("%s | Frete: R$ %.2f%n", p, e.calcularFrete());
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum pedido entregável.");
        }
    }
}
