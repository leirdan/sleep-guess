#set document(title: [Sleep Token Guess Game])
#set text(font: "New Computer Modern", size: 11pt)
#set page(paper: "a4", margin: (x: 1.8cm, y: 1.3cm))
#set par(
  justify: true,
  leading: 0.52em,
  first-line-indent: 1em,
)
#set heading(numbering: "1.")

#show title: set text(size: 17pt)
#show title: set align(center)
#show title: set block(below: 3em)
#show grid: set block(below: 3em)
#show heading: set text(size: 13pt, weight: "regular")
#show heading: smallcaps


#grid(
  columns: 1fr,
  align(center)[
    Universidade Federal do Rio Grande do Norte \
    Departamento de Matemática e Informática Aplicada \
    DIM0438 - Redes de Computadores \
    Andriel Vinicius de Medeiros Fernandes \
    // #link("mailto:andrielvinnicius@gmail.com")
  ],
)

#title()

= Introdução
\

A aplicação _Sleep Token Guess Game_ consiste em um jogo simples _singleplayer_, onde o jogador deve identificar as músicas da banda britânica *Sleep Token* com base em trechos avulsos das letras, acumulando pontos quando acerta. A inspiração para este projeto veio da experiência anterior do autor no desenvolvimento de um jogo _multiplayer_ de chat compartilhado com a linguagem Rust. Tal projeto, apesar de funcional e divertido, não utilizou de protocolos bem definidos, gerando dificuldades em sua implementação e uso; além disso, o autor tem bastante apreço pela banda em questão.

A princípio, o jogo possui 3 níveis de dificuldade: \

- "Normal": dificuldade recomendada para iniciantes, imprime 1 linha por vez;
- "Fã": dificuldade mais avançada, imprime 1 linha por vez ocultando caracteres aleatórios;
- "Trve": dificuldade extrema, imprime 1 linha por vez sem nenhuma das vogais.

Para iniciar uma partida, o jogador deve informar seu nome de usuário bem como a dificuldade desejada para jogar. Antes do início da rodada, o jogo sorteia uma das músicas disponíveis e define como sendo a da rodada. A cada rodada, o jogador tem até 3 chances de palpites. O jogo sorteia uma linha da música definida e envia para o jogador; este, por sua vez, pode dar o seu palpite ou desistir imediatamente da partida, desconectando-se do jogo. Caso dê um palpite correto, o jogo computa $15 - (5 times n)$ pontos no score, onde $n$ é o número de tentativas usadas pelo jogador. Caso dê um palpite errado, o jogador pode receber uma nova linha da música e tentar novamente ou, se já tiver esgotado as tentativas, perder de vez o jogo. Ao conseguir adivinhar todas as músicas, o jogador ganha de forma definitiva o jogo. Nos cenários de derrota, desistência ou vitória, é exibido o total de pontos que o jogador acumulou até o momento.

O _Sleep Token Guess Game_ foi projetado para oferecer suporte somente ao modo _singleplayer_. Contudo, isso não significa que só possa haver 1 cliente conectado ao servidor por vez: $n$ clientes podem se conectar simultaneamente ao servidor e terão suas próprias sessões de jogo, sem nenhuma interferência entre si.

= Características
\
Pelo seu escopo simples, o jogo tem um conjunto limitado de funcionalidades:
- Jogador pode iniciar uma sessão com seu nome de usuário e dificuldade;
- Jogador pode dar palpites a cada rodada;
- Jogador pode desistir da partida enquanto ela decorre;
- Jogador pode solicitar ajuda para entender os comandos;
- Jogador pode ganhar o jogo ao descobrir todas as músicas;
- Jogador acumula pontos ao acertar.

Em termos de características de software, o jogo utiliza do protocolo _Guess Metal Song_ (*GMS*) como protocolo de camada 7, além do protocolo _Transmission Control Protocol_ (*TCP*) como protocolo de camada 4. A escolha do TCP decorre da necessidade da sessão de jogo ser contínua e integrada, não tolerante a interações perdidas na rede.

= Arquitetura

= Protocolo de aplicação GMS
\

O Protocolo GMS foi desenvolvido inicialmente para o contexto deste jogo, permitindo uma maneira eficaz de comunicação entre cliente-servidor. O GMS é *Stateful*, armazenando informações de uma sessão de jogo do lado do servidor. O GMS foi particularmente inspirado no protocolo _Simple Mail Transfer Protocol_ (*SMTP*) em razão de suas restrições, como o uso do protocolo TCP para transporte e a necessidade de manter um estado contínuo no servidor.

== Tipos de mensagem
\

O nome GMS sugere uma ligação direta com a temática do _heavy metal_. Durante um show, é comum que o público acompanhe as músicas cantando (ou gritando, a depender da música). Do mesmo modo, é esperado que o som produzido pelos instrumentos dos musicistas envolvidos ecoe e reverbere pelo ambiente, proporcionando prazer a quem escuta. Pensando nisso, os dois tipos de mensagens disponíveis na rede foram nomeados para referência com algum elemento desta temática:
- *Scream*: refere-se às mensagens enviadas pelo cliente ao servidor;
- *Reverb*: refere-se às mensagens enviadas do servidor ao cliente, independente se são fruto de mensagens do cliente ou se são independentes.

== Fases da conexão

== Comandos

= Como executar
\

A aplicação foi desenvolvida inteiramente utilizando Java 17 e a ferramenta de compilação Gradle, então certifique-se de que para executar tens instalado o Java a partir dessa versão. Para rodar o jogo, é necessário habilitar primeiro o servidor e em seguida o(s) cliente(s), por meio dos comandos
```sh
./gradlew server
./gradlew client
```

// Proposta: *Aplicação Cliente/Servidor*
// - O aluno deverá fazer o design de uma aplicação segundo o modelo Cliente/Servidor. A decisão sobre o tipo de aplicação (funcionamento, comunicação, etc) É RESTRITA ao aluno. Criatividade é aspecto que contribui em uma melhor avaliação.
// - elaborar um protocolo de rede proprietário para comunicação das aplicações. o protocolo deverá ser desenhado em um diagrama de sequência de mensagens, juntamente com a arquitetura da aplicação.
// - implementar a aplicação utilizando linguagem de programação java.
// - enviar em um arquivo zip (formato: nome-do-aluno.zip) os códigos do cliente e do servidor, documento (pdf) de especificação da aplicação (howto, arquitetura, funcionalidades, e especificação protocolar). é responsabilidade do aluno indicar a reprodutibilidade da aplicação.
