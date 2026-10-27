// Somente processável: NÃO implementa Entregavel.
public class PedidoAlimento extends Pedido {

    public PedidoAlimento(int id, String nomeCliente, double valor)
            throws ValorInvalidoException {
        super(id, nomeCliente, valor, Categoria.ALIMENTO);
    }

    @Override
    public double calcularValorFinal() {
        return getValor(); // sem desconto
    }
}
