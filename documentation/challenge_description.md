# Descrição do desafio final do bootcamp Pan Academy

O banco fictício BlueBank está construindo uma nova plataforma e precisa de uma API para gerenciar as transações. Utilize suas novas habilidades com Java, Banco
de dados e AWS.

O sistema deve permitir cadastro de novos clientes, incluindo dados pessoais e dados para contato. O cliente deve ser atrelado a uma conta bancária e registrar histórico de transações entre as contas.

## SUGESTÃO DE ORGANIZAÇÃO DO PROJETO

Elaboração do kanban com definição dos entregáveis:

1. Elaboração do kanban(sugestão de utilização: Trello,Jira, etc);
2. Criação do backlog(com tarefas referente ao desenvolvimento);
3. Detalhamento descritivo das tarefas da squad dentro dos seus cards (e não apenas com títulos genéricos no card);
4. Formatação do kanban padrão "to do, doing, done";
5. Definição de data de entrega das tarefas nos cards;
6. Definição de responsável pelo card ou checklist de completude;
7. Priorização dos cards (ex: tags com cores para maior relevância ou com títulos descritivos para nível de importância na priorização).

## ENTREGAS MÍNIMAS

- Metodologias Ágeis:
  - Kanban com todas as tarefas organizadas e responsáveis definidos.

- Back-end:
  - Cadastro de clientes;
  - Listagem de clientes;
  - Atualização de clientes;
  - Deletar clientes;
  - Histórico de transações entre contas.

- Banco de Dados:
  - Tabelas bem estruturadas e populadas com valores para testes;
  - O Banco deve ser entregue em script SQL junto ao repositório.

- Gerais:
  - O código deve ser entregue em um repositório no Github;
  - A API deveserdisponibilizada em ambiente AWS com EC2 ou em Beanstalk;
  - A aplicação deve ter um pipeline em Jenkins ou no Aws Build;
  - A aplicação precisa ser configurada no API Gateway da AWS;
  - A aplicação precisa ter no mínimo um endpoint de SNS para cadastro de e-mails e verificação automática;
  - A aplicação precisa ter no mínimo um Lambda;
  - Listagem dos endpoints no README.md.

## ENTREGA OPCIONAL

- Testes automatizados (podem ser testes unitários ou testes de integração).

## CRITÉRIOS DE AVALIAÇÃO

- Documentação da Aplicação:
  - Sugestão de documentação com swagger.
