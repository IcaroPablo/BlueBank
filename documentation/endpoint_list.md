## Lista de todos os _endpoints_ da API

URL base para execução local: `http://localhost:8080`

-------------------

### Account Controller (`/conta`)

- POST:
  - `/{cpfcnpj}` -> Realiza a criação de uma conta no nome de um cliente já registrado dado seu CPF ou CNPJ

- GET:
  - `/id/{id}` -> Consulta os dados de uma conta registrada dado o seu ID
  - `/cpfcnpj/{cpfcnpj}` -> Consulta os dados de todas as contas de um cliente dado seu CPF ou CNPJ

- PUT:
  - `/update/{id}/{cpfcnpj}` -> Altera o titular de uma conta dado o seu ID e o CPF ou CNPJ do novo titular

- DELETE:
  - `/delete/id/{id}` -> Desativa uma conta registrada dado o seu ID
  - `/delete/cpfcnpj/{cpfcnpj}` -> Desativa todas as contas de um cliente dado CPF ou CNPJ

-------------------

### Client Controller (`/cliente`)
	
- POST:
  - `/` -> Cadastra um novo cliente com os dados pessoais

- GET:
  - `/{cpfcnpj}` ->  Consulta os dados de um cliente registrado dado seu CPF ou CNPJ
  - `/nome/{nome}` -> Exibe os dados de todos os clientes que possuem o nome fornecido

- PUT:
  - `/{cpfcnpj}` -> Atualiza o cadastro de um cliente registrado dado seu CPF ou CNPJ

- DELETE:
  - `/{cpfcnpj}` -> Desativa o cadastro de um cliente registrado dado seu CPF ou CNPJ

-------------------

### Loan Controller(`/emprestimo`)

- POST: 
  - `/{cpfcnpj}` -> Realiza a criação de um empréstimo dado o CPF ou o CNPJ de um cliente registrado
  - `/pagamento/{emprestimoId}/{contaId}` -> Realiza o pagamento do valor emprestado dados o ID do empréstimo e o ID conta da qual o valor será transferido

- GET:
  - `/id/{emprestimoId}` -> Consulta informações de um empréstimo registrado dado o seu ID
  - `/cpfcnpj/{cpfcnpj}")` -> Consulta informações de um empréstimo registrado dado o CPF ou o CNPJ do cliente que o solicitou

-------------------

### Transaction Controller(`/transacao`)

- POST: 
  - `/saque/{id}` -> Realização de saque dado o ID de uma conta registrada
  - `/deposito/{id}` -> Realização de depósito dado o ID de uma conta registrada

- GET:
  - `/saldo/{id}` -> Consulta o saldo de uma conta registrada dado o seu ID
  - `/extrato/{id}` -> Consulta o extrato de uma conta registrada dado o seu ID
