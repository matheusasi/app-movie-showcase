# CINETECA

App Android de biblioteca pessoal de filmes, feito em Kotlin com Jetpack Compose.

## Telas implementadas (7)

| # | Tela | Arquivo | Rota |
|---|------|---------|------|
| 1 | Login — e-mail, senha e acesso | `ui/LoginScreen.kt` | `Login` |
| 2 | Cadastro — nome, e-mail e senha | `ui/CadastroScreen.kt` | `Cadastro` |
| 3 | Início — destaque da semana e recomendados | `ui/TelaInicio.kt` | `Inicio` |
| 4 | Buscar — campo de texto + filtro por gênero | `ui/TelaBuscar.kt` | `Buscar` |
| 5 | Minha Lista — abas Quero ver / Assistidos | `ui/TelaLista.kt` | `Lista` |
| 6 | Detalhe — sinopse, nota média e ações | `ui/TelaDetalhe.kt` | `Detalhe(id)` |
| 7 | Avaliar — data, local, nota e comentário | `ui/TelaAvaliar.kt` | `Avaliar(id)` |

## Fluxo principal

Login → (ou Cadastro → volta pro Login) → Início → toca num filme → Detalhe →
"Avaliar este filme" → Avaliar → salva e o filme vai para a aba "Assistidos" da
Minha Lista. A barra inferior troca de tela a qualquer momento. Login e Cadastro
são as duas únicas telas sem as barras.

## Navegação

Navegação com **Navigation Compose** e rotas type-safe:

- `navigation/Routes.kt` — cada rota é um `@Serializable data object` (ou
  `data class` quando carrega argumento: `Detalhe(id)`, `Avaliar(id)`).
- `navigation/AppNavigation.kt` — o `NavHost` com um `composable<T>` por rota,
  `startDestination = Login`, `navigate`, `popBackStack` e `popUpTo`.
  A rota `Detalhe` entra com transição animada (`slideIntoContainer` /
  `slideOutOfContainer`).
- `navigation/Menu.kt` — a barra inferior, dirigida pelo `navController`.
- `MainActivity.kt` — `Scaffold` com `topBar`/`bottomBar`, escondendo as duas
  barras em Login e Cadastro via `currentBackStackEntryAsState()`.

## Banco de dados (SQLite)

`data/local/AppDatabase.kt` estende `SQLiteOpenHelper` (versão 2) e cria as
tabelas `usuarios`, `avaliacoes` e `filmes` (já com os 15 filmes iniciais). Os DAOs em `data/dal/` usam `writableDatabase`
(insert, update, delete) e `readableDatabase` (query) e devolvem os models
`Usuario`, `Avaliacao` e `Filme`.

## Autenticação e sessão

- `auth/AuthManager.kt` gera salt e hash PBKDF2 da senha.
- `auth/SessaoUsuario.kt` guarda o id do usuário logado; o botão de sair da
  barra superior encerra a sessão.
- `AppNavigation` usa um `LaunchedEffect` que manda para o Login qualquer tela
  protegida aberta sem sessão. Login e Cadastro não mostram as barras.

## CRUDs (MVVM)

| CRUD | ViewModel | Telas |
|------|-----------|-------|
| Usuários | `UsuarioViewModel` | `UsuarioScreen` (lista/excluir), `CadastroScreen` (criar), `UsuarioEditarScreen` (alterar) |
| Avaliações | `AvaliacaoViewModel` | `TelaAvaliacoes` (lista/excluir), `TelaAvaliar` (criar), `TelaEditarAvaliacao` (alterar) |
| Filmes | `FilmeViewModel` | `TelaFilmes` (lista/excluir), `TelaFilmeFormulario` (criar/alterar) |

O catálogo vem do banco: Início, Buscar, Lista, Detalhe e Avaliar leem os filmes
pelo `FilmeViewModel`, então um filme criado aparece no app todo.

As listas usam `LazyColumn` e recarregam ao voltar para a tela. Cada usuário vê
só as próprias avaliações, e cada filme só pode ser avaliado uma vez por usuário.

`data/ListaManager.kt` (abas Quero ver / Assistidos) continua em memória.

## Estrutura

```
app/src/main/java/com/example/projetomobile/
├── MainActivity.kt        Scaffold + barras + NavHost
├── auth/
│   ├── AuthManager.kt     hash e salt das senhas
│   └── SessaoUsuario.kt   usuário logado
├── navigation/
│   ├── Routes.kt          rotas @Serializable
│   ├── AppNavigation.kt   NavHost
│   └── Menu.kt            barra inferior
├── data/
│   ├── Filme.kt           model Filme, GENEROS, LOCAIS e formatarNota
│   ├── Usuario.kt         model
│   ├── Avaliacao.kt       model
│   ├── ListaManager.kt    estado da Minha Lista
│   ├── local/AppDatabase.kt   SQLiteOpenHelper
│   └── dal/               UsuarioDAO, AvaliacaoDAO, FilmeDAO
├── validation/            CadastroValidator, AvaliacaoValidator, FilmeValidator
└── ui/
    ├── viewmodel/         AuthViewModel, UsuarioViewModel, AvaliacaoViewModel, FilmeViewModel
    ├── Componentes.kt     barra superior, poster, item de filme, botões
    ├── LoginScreen.kt
    ├── CadastroScreen.kt
    ├── TelaInicio.kt
    ├── TelaBuscar.kt
    ├── TelaDetalhe.kt
    ├── TelaLista.kt
    └── TelaAvaliar.kt
```

## Como rodar

1. Abra o Android Studio → **Open** → selecione esta pasta.
2. Espere o Gradle sincronizar.
3. Escolha o emulador `Pixel_8` e clique em ▶ **Run**.

Pelo terminal:

```bash
./gradlew assembleDebug          # gera o APK
./gradlew installDebug           # instala no emulador/celular conectado
```

No primeiro uso, toque em **Cadastre-se** e depois entre com o mesmo e-mail e
senha. Os dados ficam salvos no SQLite do aparelho.

## Versões

Kotlin 2.1.20 · AGP 8.13.1 · Gradle 8.14.3 · compileSdk 36 · minSdk 24 · JDK 17
Navigation Compose 2.8.9 · kotlinx-serialization-json 1.7+
