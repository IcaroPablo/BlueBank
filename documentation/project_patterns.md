## Ferramentas e Técnicas

Neste arquivo estão descritas algumas ferramentas que usamos ao longo do projeto assim como as razões para a sua aplicação

### Stack das principais tecnologias usadas no projeto 

![Java](https://img.shields.io/badge/-Java-black?style=flat-square&logo=Java)
![Spring](https://img.shields.io/badge/-Spring-black?style=flat-square&logo=Spring)
![MySQL](https://img.shields.io/badge/-MySQL-black?style=flat-square&logo=MySQL)
![AWS](https://img.shields.io/badge/-AWS-black?style=flat-square&logo=amazon-aws)
![Eclipse](https://img.shields.io/badge/-Eclipse-black?style=flat-square&logo=Eclipse)
![Intellij](https://img.shields.io/badge/-Intellij-black?style=flat-square&logo=Intellij-IDEA)
![Git](https://img.shields.io/badge/-Git-black?style=flat-square&logo=Git)
![Github](https://img.shields.io/badge/-Github-black?style=flat-square&logo=Github)

- Java
- Spring framework
- Banco de dados: MySQL
- AWS
- IDEs Eclipse e IntelliJ
- Git e Github 

### Padrões de Projeto e Boas Práticas

- **DTO**: do inglês, Data transfer object, é um padrão de projeto de software usado para transferir dados entre subsistemas de um software. Usar um `DTO` nos permite enviar aos [`endpoints`](./assets/endpoint_list.md) da aplicação apenas as informações pertinentes sobre as entidades consultadas ou até mesmo alterá-las para que facilitem a compreensão geral do usuário.

- **MVC**: do inglês, Model View Controller, é um padrão de arquitetura de software que define 3 camadas com responsabilidades distintas em uma aplicação. Model, uma estrutura para gerenciar e agrupar os dados que serão utilizados na aplicação. View, uma interface de interação para o usuário que o permitirá acessar os serviços da aplicação de maneira ágiil e prática. Controller, a camada responsável por intermediar as requisições recebidas pelo usuário e chamar os métodos necessários para seu processamento. Essa arquitetura é usada pelo framework que utilizamos, o `Spring`.

- **SOLID**: o [`SOLID`](https://medium.com/desenvolvendo-com-paixao/o-que-%C3%A9-solid-o-guia-completo-para-voc%C3%AA-entender-os-5-princ%C3%ADpios-da-poo-2b937b3fc530) é um acrônimo para um conjunto de princípios que guiam o desenvolvimento orientado a objetos. São regras simples, mas que facilitam a construção e a manutenção de um código limpo e elegante, permitindo que as implementações de suas funcionalidades possam ser escritas de maneira sucinta.

- **Java Code Conventions**: tentamos, de acordo com nossos conhecimentos, seguir o estilo de código definido pela Sun Microsystems (RIP): [`Java Code Conventions`](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf). O guia é muito extenso então nem todas as orientações puderam ser revisadas pelo nosso time, mas tentamos ao máximo tornar o código legível e elegante usando, por exemplo, nomes de variáveis e assinaturas de métodos bem descritivas escritas em inglês.

- **Mappers**: em certos pontos do código, precisamos passar a informação contida nas entidades difinidas pelo já mencionado `model` aos também já mencionados `DTOs`, há alguns frameworks que realizam essa função com um alto grau de customização até. Porém, por problemas de compatibilidade decidimos implementar nossas próprias classes de `mapping`, elas estão divididas correspondentemente aos models e seus respectivos DTOs.

- **Exception Handling**: é um _must have_ em qualquer código bem escrito, nos atentamos a isso não só com o objetivo de manter o código funcionando corretamente mesmo com eventuais adversidades, mas também com o objetivo de informar ao usuário e ao administrador do sistema as possíveis causas dos erros ocorridos. Criamos um pacote exclusivo para as classes de `exception handling` e tentamos enquadrar todos os erros encontrados pelo nosso time nas categorias de exception definidas nesse pacote bem como retornar ao endpoint uma mensagem explicativa e um `status HTTP` adequado.

### Documentação e Testes

- **Javadoc**: usamos o ja conhecido `javadoc` por ser uma ferramenta completa quanse se diz respeito a documentação de referência de código, decidimos incluir nele explicações sobre os métodos criados em inglês, para que seja mais acessível. Por boa prática, decidimos restringir a anotação `@author` apenas às classes, pois ela poluiria o código com muitas linhas desnecessárias. Na verdade o uso dessa anotação não é necessário já que usamos `git` para controle de versão, mas ela ajuda a entender mais facilmente a distribuição de tarefas ao longo do código.
 
- **Swagger**: seguindo a sugestão do desafio, usamos o `Swagger` para documentar os [`endpoints`](./endpoint_list.md) da aplicação. As funções de cada endpoint definido nos controller da aplicação estão devidamente descritas numa página gerada em `buildtime` acessível pelo link [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
 
- **Testes**: foram realizados usando tanto o console da `IDE` quanto ferramentas para testes nos [`endpoints`](./endpoint_list.md) especificados no código, dentre elas: `Postman`, `Insomnia`, `Swagger` e até mesmo extensões do `VScode`. Além delas tembém usamos `Junit` para realizar testes na aplicação de maneira direta (sem envio de requisições) e permitindo que eles fossem facilmente documentados.

### Menções Honrosas

- **Anotação _@Transactional_**: esta anotação é usada em nossos métodos no pacote de service e ela garante que todo o processo siga o princípio da atomicidade, ou seja, ele não pode ser fracionado, feito parcialmente (é "tudo ou nada"). Assim quando forem realizados procedimentos bancários, temos a garantia que nenhuma informação será persistida a menos que todo o processo tenha 100% de êxito. Isso evita inconsistência no banco de dados.

- **Derived Query Methods**: são usados nas interfaces do pacote de `repositories`, pois eles descrevem queries específicas. Isso mesmo, nós não precisamos escrever queries usando `SQL` caso elas não estejam definidas pelos métodos básicos da JPA. Usando este recurso muito útil, os `Derived Query Methods`, deixamos que a própria JPA se baseie na assinatura do método em java e crie as queries no dialeto SQL adequado ao banco usado. A vantagem de se usar esse recurso é que ele aumenta a portabilidade do código.

- **Interface para transações na conta**: buscando uma maior elegância e legibilidade no código, criamos uma interface que deve ser implementada por todos os DTOs de transações. Esta interface não possui nenhum método, ela serve simplesmente para uso de polimorfismo em alguns casos pertinentes, assim todos os DTOs de transações serão relacionados apesar de possuírem estruturas bastante distintas. Optamos por usar interface em vez de herança por boa prática já que, assim, o código se torna mais desacoplado e maleável.
