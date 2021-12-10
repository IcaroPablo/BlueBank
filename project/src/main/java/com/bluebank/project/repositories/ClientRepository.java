package com.bluebank.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluebank.project.models.Client;

/**
* Interface for creating queries to the clients table
*/
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	/**
	* Returns the client whom given cpf or cnpj belongs to
	* 
	* @param  cpfcnpj  a string formed just by numbers of the client entity's cpf or cnpj attribute
	* @return a client object whose cpf or cnpj matches the search containing all the database table's columns data into its attributes
	*/
	public Optional<Client> findByCpfcnpj (String cpfcnpj);
	
	/**
	* Returns the client whom given cpf or cnpj belongs to
	* 
	* @param  name  a string containing a complete or part of the client entity's name attribute
	* @return a list of client objects whose name matches the search containing all the database table's columns data into its attributes
	*/
	public Optional<List<Client>> findByNameContaining(String name);
	
}
