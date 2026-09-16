package br.com.compravenda.model;

import java.math.BigDecimal;

public class VendaProduto {

    private int codigo;
    private int vendaCodigo;
    private Produto produto;
    private BigDecimal quantidade = BigDecimal.ZERO;
    private BigDecimal preco = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public int getVendaCodigo() { return vendaCodigo; }
    public void setVendaCodigo(int vendaCodigo) { this.vendaCodigo = vendaCodigo; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
