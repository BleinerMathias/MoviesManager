# 📽️MoviesManager

🏗️ **Em desenvolvimento 📚**

## Especificação

O projeto consiste no desenvolvimento de um aplicativo nativo ANDROID simples de lista com uso de
persistência. O aplicativo permite criar, apagar e gerenciar filmes da preferência do usuário.

O aplicativo deve possuir uma opção para inserção de novos filmes que vão aparecer em lista na
tela principal. Na tela principal deve haver opções para que sejam adicionadas novos, para
visualizar detalhes e para apagar filmes cadastrados.

Cada filme deve possuir um nome (que deve ser único), ano de lançamento, um estúdio ou produtora, um tempo de duração (em minutos), uma flag (para indicar se o filme já foi assistido ou não), uma nota dada pelo usuário (de 0 a 10 que pode inicialmente ser vazia e atribuída depois de assistir o filme) e um gênero que pode ser romance, aventura, terror etc.

Na tela principal deve ser possível ordernar os filmes por nome ou nota. Todos os campos devem ser editáveis, **exceto o nome do filme**.

Deve-se manter observância aos conceitos de desenvolvimento de um banco de dados. A persistência deve ser implementada usando SQLite e o uso de uma arquitetura de software.

## 💻TODO

1. **Configuração Inicial:**
    - [X]  Criar um repositório no GitHub para o projeto.
    - [X]  Configurar a estrutura básica do projeto.
    - [X]  Incluir as dependências/plugins.
        - [X]  Room
        - [X]  KSP
        - [X]  Parcelize
        - [X]  Navigation Safe
2. **Desenvolvimento do Modelo de Dados:**
    - [X]  Criar a classe/modelo para representar os filmes.
    - [X]  Criar a classe/modelo para representar os gêneros.
    - [X]  Adicione os atributos necessários: nome, ano de lançamento, produtora, duração, flag de assistido, nota e gênero.
3. **Implementação do Banco de Dados:**
    - [X]  Configurar a classe de gerenciamento do banco de dados usando SQLite.
    - [X]  Criar métodos para criar, ler, atualizar e excluir filmes no banco de dados.
    - [ ]  Criar tabela com os gêneros → que posteriormente poderão ser cadastrados pelo usuário
4. **Desenvolvimento da Interface Gráfica (UI):**
    - [ ]  Tela principal que exibirá a lista de filmes.
    - [ ]  Menu de ações para adicionar, visualizar detalhes e apagar filmes.
    - [ ]  Implementar um componente Spinner para seleção de gênero ao cadastrar um filme.
5. **Implementação das Funcionalidades:**
    - [ ]  Desenvolver a lógica para adicionar novos filmes.
    - [ ]  Permitir visualizar detalhes de um filme ao clicar na célula correspondente.
    - [ ]  Implementar a funcionalidade de apagar filmes cadastrados.
6. **Ordenação da Lista:**
    - [ ]  Adicionar opções para ordenar os filmes por nome ou nota na tela principal.
7. **Edição dos Campos:**
    - [ ]  Permitir a edição de todos os campos, exceto o nome do filme, na tela de detalhes.
8. **Cadastro e Remoção de Gêneros:**
    - [ ]  Implementar a funcionalidade para cadastrar e remover outros gêneros.
9. **Arquitetura do Software:**
    - [ ]  Projeto seguindo uma arquitetura de software MVVC.
10. **Commits e Mensagens:**
    - [ ]  Realizar commits regulares com mensagens claras e descritivas.
11. **Testes:**
    - [ ]  Realize testes para garantir o funcionamento adequado das funcionalidades.
12. **Vídeo de Demonstração:**
    - [ ]  Grave um vídeo mostrando o aplicativo em uso, narrando e demonstrando as principais funcionalidades (máximo de 5 minutos).
    - [ ]  Adicione o vídeo ao repositório.

### ⚠️Casos de uso

1. **Adicionar Filme:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode adicionar um novo filme à lista, fornecendo informações como nome, ano de lançamento, produtora, duração, gênero, etc.
    - **Fluxo Principal:**
        1. O usuário acessa a tela principal do aplicativo.
        2. Seleciona a opção para adicionar um novo filme.
        3. Preenche os campos necessários, como nome, ano, produtora, etc.
        4. Confirma a adição do filme.
        5. O sistema salva as informações no banco de dados.
        6. A lista de filmes é atualizada para refletir o novo cadastro.
2. **Visualizar Detalhes do Filme:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode visualizar todos os detalhes de um filme específico ao clicar na célula correspondente na tela principal.
    - **Fluxo Principal:**
        1. O usuário acessa a tela principal do aplicativo.
        2. Clica na célula de um filme na lista.
        3. O sistema exibe uma tela com todos os detalhes do filme, como nome, ano, produtora, etc.
3. **Apagar Filme:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode remover um filme da lista, excluindo suas informações do banco de dados.
    - **Fluxo Principal:**
        1. O usuário acessa a tela principal do aplicativo.
        2. Seleciona a opção para apagar filmes.
        3. Escolhe o filme que deseja excluir.
        4. Confirma a exclusão.
        5. O sistema remove as informações do filme do banco de dados.
        6. A lista de filmes é atualizada para refletir a remoção.
4. **Ordenar Lista de Filmes:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode ordenar a lista de filmes na tela principal com base no nome ou na nota.
    - **Fluxo Principal:**
        1. O usuário acessa a tela principal do aplicativo.
        2. Seleciona a opção para ordenar filmes.
        3. Escolhe o critério de ordenação (por nome ou nota).
        4. A lista de filmes é reorganizada conforme o critério escolhido.
5. **Editar Detalhes do Filme:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode editar os detalhes de um filme específico, **exceto o nome**, na tela de visualização de detalhes.
    - **Fluxo Principal:**
        1. O usuário acessa a tela principal do aplicativo.
        2. Clica na célula de um filme na lista.
        3. Na tela de detalhes, seleciona a opção para editar.
        4. Faz as alterações desejadas nos campos.
        5. Confirma as alterações.
        6. O sistema atualiza as informações do filme no banco de dados.
6. **Cadastrar Gênero:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode cadastrar novos gêneros para associar aos filmes.
    - **Fluxo Principal:**
        1. O usuário acessa menu de opções.
        2. Seleciona a opção para cadastrar gênero.
        3. Insere o nome do novo gênero.
        4. Confirma o cadastro do novo gênero.
        5. O sistema adiciona o novo gênero à lista disponível para associação aos filmes.
7. **Remover Gênero:**
    - **Ator Principal:** Usuário
    - **Descrição:** O usuário pode remover gêneros existentes que não deseja mais associar aos filmes.
    - **Fluxo Principal:**
        1. O usuário acessa a tela de configurações ou opções.
        2. Seleciona a opção para remover gênero.
        3. Escolhe o gênero a ser removido.
        4. Confirma a remoção do gênero.
        5. O sistema exclui o gênero selecionado da lista disponível para associação aos filmes.