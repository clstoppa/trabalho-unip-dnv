package br.com.compravenda.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Produto {

    private int codigo;
    private String nome;
    private BigDecimal estoque = BigDecimal.ZERO;
    private String unidade;
    private BigDecimal preco = BigDecimal.ZERO;
    private BigDecimal custo = BigDecimal.ZERO;
    private BigDecimal precoAtacado = BigDecimal.ZERO;
    private BigDecimal estoqueMinimo = BigDecimal.ZERO;
    private BigDecimal estoqueMaximo = BigDecimal.ZERO;
    private BigDecimal embalagem = BigDecimal.ZERO;
    private BigDecimal peso = BigDecimal.ZERO;
    private Date cadastro;
    private String obs;
    private String ativo = "S";

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getEstoque() { return estoque; }
    public void setEstoque(BigDecimal estoque) { this.estoque = estoque; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }

    public BigDecimal getPrecoAtacado() { return precoAtacado; }
    public void setPrecoAtacado(BigDecimal precoAtacado) { this.precoAtacado = precoAtacado; }

    public BigDecimal getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(BigDecimal estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    public BigDecimal getEstoqueMaximo() { return estoqueMaximo; }
    public void setEstoqueMaximo(BigDecimal estoqueMaximo) { this.estoqueMaximo = estoqueMaximo; }

    public BigDecimal getEmbalagem() { return embalagem; }
    public void setEmbalagem(BigDecimal embalagem) { this.embalagem = embalagem; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public Date getCadastro() { return cadastro; }
    public void setCadastro(Date cadastro) { this.cadastro = cadastro; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return nome;
    }
}
