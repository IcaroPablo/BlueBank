package com.bluebank.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluebank.project.dtos.LoanDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.models.Loan;
import com.bluebank.project.services.EmprestimoService;

@RestController
@RequestMapping("/emprestimo")
public class LoanController {
  
  @Autowired
  EmprestimoService emprestimoService;

  //criar emprestimo
  @PostMapping("/{cpfcnpj}")
  @ResponseStatus(HttpStatus.CREATED)
	public LoanDTO registerLoan(@PathVariable("cpfcnpj") String cpfcnpj, @Validated @RequestBody Loan emprestimo){
    return emprestimoService.criarEmprestimo(cpfcnpj, emprestimo);
  }

  //consultar emprestimo pelo id
  @GetMapping("/id/{emprestimoId}")
  @ResponseStatus(HttpStatus.OK)
  public LoanDTO consultLoanRegistryById(@PathVariable("emprestimoId") Long emprestimoId){
    return emprestimoService.consultarEmprestimoId(emprestimoId);
  }

  //consultar emprestimo pelo cpfcnpj
  @GetMapping("/cpfcnpj/{cpfcnpj}")
  @ResponseStatus(HttpStatus.OK)
  public List<LoanDTO> consultLoanRegistryByCpfcnpj(@PathVariable("cpfcnpj") String cpfcnpj){
    return emprestimoService.consultarEmprestimoCpfcnpj(cpfcnpj);
  }

  //pagar emprestimo
  @PostMapping("/pagamento/{emprestimoId}/{contaId}")
  @ResponseStatus(HttpStatus.CREATED)
  public TransferenceDTO payLoan(@PathVariable("emprestimoId") Long emprestimoId, @PathVariable("contaId") Long contaId){
    return emprestimoService.pagarEmprestimo(emprestimoId, contaId);
  }
}
