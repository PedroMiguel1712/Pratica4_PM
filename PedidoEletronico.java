public class PedidoEletronico extends Pedido implements Entregavel {

    public PedidoEletronico(int id, String nomeCliente, double valor)
            throws ValorInvalidoException {
        super(id, nomeCliente, valor, Categoria.ELETRONICO);
    }

    @Override
    public double calcularValorFinal() {
        if (getValor() > 100) {
            return getValor() * 0.90; // desconto de 10%
        }
        return getValor();
    }

    @Override
    public double calcularFrete() {
        return 20 + 0.10 * getValor();
    }
}
