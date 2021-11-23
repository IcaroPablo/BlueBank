package com.bluebank.project.dtos;

import java.util.Date;

import com.bluebank.project.models.Cliente;

public class EmprestimoDTO {

  private Cliente cliente;
  private String numeroContrato;
  private Date dataInicio;
  private Date dataFim;
  private double valorEmprestimo;
  private double percentualJuros;
  private int quantParcelas;
  
  public EmprestimoDTO() {
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public String getNumeroContrato() {
    return numeroContrato;
  }

  public void setNumeroContrato(String numeroContrato) {
    this.numeroContrato = numeroContrato;
  }

  public Date getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(Date dataInicio) {
    this.dataInicio = dataInicio;
  }

  public Date getDataFim() {
    return dataFim;
  }

  public void setDataFim(Date dataFim) {
    this.dataFim = dataFim;
  }

  public double getValorEmprestimo() {
    return valorEmprestimo;
  }

  public void setValorEmprestimo(double valorEmprestimo) {
    this.valorEmprestimo = valorEmprestimo;
  }

  public double getPercentualJuros() {
    return percentualJuros;
  }

  public void setPercentualJuros(double percentualJuros) {
    this.percentualJuros = percentualJuros;
  }

  public int getQuantParcelas() {
    return quantParcelas;
  }

  public void setQuantParcelas(int quantParcelas) {
    this.quantParcelas = quantParcelas;
  }

}
