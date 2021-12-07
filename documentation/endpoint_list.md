## Lista de todos os _endpoints_ da API

URL base para execução local: `http://localhost:8080`

-------------------

### Account Controller (`/conta`)

- POST:
  - `/{cpfcnpj}` -> Cadastra um conta com o CPF ou CNPJ

- GET:
  - `/id/{id}` -> Consulta os dados da conta atraves do id
  - `/cpfcnpj/{cpfcnpj}` -> Consulta os dados da conta atraves do CPF ou CNPJ

- PUT:
  - `/update/{id}/{cpfcnpj}` -> Atualiza os dados pelo id ou CPF ou CNPJ

- DELETE:
  - `/delete/id/{id}` -> Desativa a conta do cliente pelo id
  - `/delete/cpfcnpj/{cpfcnpj}` -> Desativa todas as conta do cliente pelo CPF ou CNPJ

-------------------

### Client Controller (`/cliente`)
	
- POST:
  - `/` -> Cadastra um novo cliente com os dados pessoais

- GET:
  - `/{cpfcnpj}` ->  Consulta os dados de um cliente atraves do CPF ou CNPJ
  - `/nome/{nome}` -> Consulta os dados de um cliente atraves do nome

- PUT:
  - `/{cpfcnpj}` -> Atualiza o cadastro do cliente atraves do CPF ou CNPJ

- DELETE:
  - `/{cpfcnpj}` -> Desativa o cadastro do cliente

-------------------

### Loan Controller(`/emprestimo`)

- POST: 
  - `/{cpfcnpj}` -> Este método cria um empréstimo
  - `/pagamento/{emprestimoId}/{contaId}` -> Este método faz o pagamento de um empréstimo

- GET:
  - `/id/{emprestimoId}` -> Este método consulta um empréstimo pelo id
  - `/cpfcnpj/{cpfcnpj}")` -> Este método consulta um empréstimo pelo cpf/cnpj

-------------------

### Transaction Controller(`/transacao`)

- POST: 
  - `/saque/{id}` -> Este método faz um saque atraves de uma transação
  - `/deposito/{id}` -> Este método faz um depósito atraves de uma transação

- GET:
  - `/saldo/{id}` -> Este método consulta o saldo de uma conta
  - `/extrato/{id}` -> Este método consulta o extrato de uma conta
