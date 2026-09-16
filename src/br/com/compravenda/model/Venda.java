package br.com.compravenda.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int codigo;
    private Usuario usuario;
    private Cliente cliente;
    private Date data;
    private BigDecimal valor = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private String obs;
    private List<VendaProduto> produtos = new ArrayList<>();
    private List<VendaPagamento> pagamentos = new ArrayList<>();

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public List<VendaProduto> getProdutos() { return produtos; }
    public void setProdutos(List<VendaProduto> produtos) { this.produtos = produtos; }

    public List<VendaPagamento> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<VendaPagamento> pagamentos) { this.pagamentos = pagamentos; }
}
