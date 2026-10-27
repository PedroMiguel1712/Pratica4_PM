public class PedidoRoupa extends Pedido implements Entregavel {

    public PedidoRoupa(int id, String nomeCliente, double valor)
            throws ValorInvalidoException {
        super(id, nomeCliente, valor, Categoria.ROUPA);
    }

    @Override
    public double calcularValorFinal() {
        if (getValor() > 200) {
            return getValor() * 0.80; // desconto de 20%
        }
        return getValor();
    }

    @Override
    public double calcularFrete() {
        return 10 + 0.05 * getValor();
    }
}
