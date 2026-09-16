package br.com.compravenda.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int codigo;
    private Usuario usuario;
    private Fornecedor fornecedor;
    private Date emissao;
    private BigDecimal valor = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private Date dataEntrada;
    private String obs;
    private List<CompraProduto> produtos = new ArrayList<>();

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Date getEmissao() { return emissao; }
    public void setEmissao(Date emissao) { this.emissao = emissao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Date getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(Date dataEntrada) { this.dataEntrada = dataEntrada; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public List<CompraProduto> getProdutos() { return produtos; }
    public void setProdutos(List<CompraProduto> produtos) { this.produtos = produtos; }
}
