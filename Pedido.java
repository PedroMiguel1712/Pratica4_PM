public abstract class Pedido implements Processavel {
    private int id;
    private String nomeCliente;
    private double valor;
    private StatusPedido status;
    private Categoria categoria;

    public Pedido(int id, String nomeCliente, double valor, Categoria categoria)
            throws ValorInvalidoException {
        if (valor <= 0) {
            throw new ValorInvalidoException(
                    "O valor do pedido deve ser maior que zero. Valor informado: " + valor);
        }
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valor = valor;
        this.categoria = categoria;
        this.status = StatusPedido.PENDENTE;
    }

    public int getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public double getValor() { return valor; }
    public StatusPedido getStatus() { return status; }
    public Categoria getCategoria() { return categoria; }

    /**
     * Altera o status do pedido.
     * Um pedido ENTREGUE não pode mudar de status.
     */
    public void setStatus(StatusPedido novoStatus) throws PedidoInvalidoException {
        if (this.status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " já foi entregue e não pode mudar de status.");
        }
        this.status = novoStatus;
    }

    /**
     * Processa o pedido: valida o status e passa de PENDENTE para PROCESSANDO.
     */
    @Override
    public void processarPedido() throws PedidoInvalidoException {
        if (status == StatusPedido.CANCELADO) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " está cancelado e não pode ser processado.");
        }
        if (status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " já foi entregue e não pode ser processado novamente.");
        }
        if (status == StatusPedido.PENDENTE) {
            status = StatusPedido.PROCESSANDO;
        }
        // PROCESSANDO ou ENVIADO: já está em andamento, nada a alterar.
    }

    @Override
    public String toString() {
        return String.format(
                "Pedido #%d | Cliente: %s | Categoria: %s | Status: %s | Valor: R$ %.2f | Valor final: R$ %.2f",
                id, nomeCliente, categoria, status, valor, calcularValorFinal());
    }
}
