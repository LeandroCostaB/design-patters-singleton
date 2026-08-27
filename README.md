# Padrão Singleton — Configuração da aplicação

Este projeto refatora um sistema de configuração para garantir que toda a
aplicação compartilhe uma única instância de `AppConfig`.

## Estrutura

```text
src/main/java/br/com/singleton/
├── AppConfig.java
├── Main.java
├── ReportService.java
└── UserService.java
```

## Implementação

`AppConfig` utiliza o padrão **Initialization-on-demand holder**. A classe
interna `InstanceHolder` somente é carregada quando `getInstance()` é chamado
pela primeira vez. A inicialização de classes feita pela JVM é segura entre
threads, portanto duas threads não conseguem criar duas instâncias.

O construtor é privado, os dados de configuração são imutáveis e o acesso é
feito da seguinte forma:

```java
AppConfig config = AppConfig.getInstance();
```

## Como executar

No PowerShell, a partir da raiz do projeto:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse src/main/java/*.java).FullName
java -cp out br.com.singleton.Main
```

A saída começa com o primeiro acesso ao Singleton feito simultaneamente por
duas threads. Depois, demonstra o uso da configuração pelos dois serviços e
compara as referências mantidas por eles e as obtidas sequencialmente. Todas
as comparações devem informar que a instância é a mesma.

## Questões para reflexão

### a) Qual problema existente no código inicial foi resolvido com o Singleton?

O código inicial permitia que cada serviço criasse sua própria `AppConfig`.
Isso espalhava a responsabilidade de criação, desperdiçava recursos e poderia
fazer partes da aplicação usarem estados diferentes. O Singleton centraliza a
criação e garante que todos acessem o mesmo objeto de configuração.

### b) Por que o construtor da classe Singleton deve ser privado?

Para impedir que código externo execute `new AppConfig()` e crie outras
instâncias. Assim, a própria classe controla sua instanciação e preserva a
garantia de uma única instância.

### c) Onde está o ponto global de acesso na sua implementação?

No método estático público `AppConfig.getInstance()`. Ele pode ser chamado por
qualquer parte da aplicação que tenha acesso à classe e sempre devolve a mesma
referência.

### d) Quais cuidados devem ser considerados ao utilizar Singleton em sistemas com múltiplas threads?

A criação da instância precisa ser atômica e ter visibilidade de memória entre
as threads. Uma implementação preguiçosa ingênua, como `if (instance == null)`,
pode permitir que duas threads observem `null` ao mesmo tempo e criem objetos
diferentes. Neste projeto, a inicialização da classe interna pela JVM resolve
esse problema. Além disso, a segurança da criação não torna automaticamente
seguro qualquer estado mutável dentro do Singleton: esse estado também teria
de ser imutável, sincronizado ou protegido por estruturas concorrentes. Aqui,
os campos são `final`, evitando alterações depois da construção.

### e) Quais são as desvantagens ou riscos de utilizar Singleton em excesso?

- Introduz estado global e dependências ocultas.
- Aumenta o acoplamento entre as classes e a implementação concreta.
- Pode dificultar testes isolados e a substituição por objetos simulados.
- Pode concentrar responsabilidades demais em uma única classe.
- Estado mutável compartilhado pode causar condições de corrida.
- Pode limitar a evolução do sistema caso futuramente sejam necessárias várias
  configurações ou instâncias com ciclos de vida diferentes.

Singleton deve ser usado apenas quando a existência de uma única instância for
realmente uma regra do domínio ou uma necessidade técnica bem justificada.
