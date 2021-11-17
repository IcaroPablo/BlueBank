package com.bluebank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebank.project.models.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
	
	public Conta findByClienteId_Cpfcnpj (String cpfcnpj);
	
//	public void deleteByIdCliente_Cpfcnpj (String cpfcnpj);
}
