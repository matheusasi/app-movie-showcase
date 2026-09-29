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

## Autenticação

`auth/AuthManager.kt` é um `object` que guarda os usuários em memória e expõe
`login(email, senha)` e `cadastrar(nome, email, senha)`, ambos devolvendo
`Boolean`. As telas chamam direto e usam `Toast` para o retorno ao usuário.
Não há persistência: os dados vivem enquanto o app estiver aberto.

## Estado da lista

`data/ListaManager.kt` segue o mesmo padrão do `AuthManager`: um `object` com a
lista em memória e funções que devolvem `Boolean` (`naLista`, `adicionar`,
`remover`, `avaliar`). Usa `mutableStateListOf` para o Compose recompor sozinho.

## Restrições

Só Jetpack Compose + Material 3. Sem ViewModel, Room, Retrofit ou carregamento
de imagens.

- Estilo: cores aplicadas inline (`Color(0xFFE8341C)` etc.), sem arquivo de tema.
- Dados: lista fixa em `data/Filme.kt` (15 filmes), sem internet e sem banco.

## Estrutura

```
app/src/main/java/com/example/projetomobile/
├── MainActivity.kt        Scaffold + barras + NavHost
├── auth/
│   └── AuthManager.kt     login e cadastro em memória
├── navigation/
│   ├── Routes.kt          rotas @Serializable
│   ├── AppNavigation.kt   NavHost
│   └── Menu.kt            barra inferior
├── data/
│   ├── Filme.kt           modelos (Filme, Avaliacao) e catálogo fixo
│   └── ListaManager.kt    estado da Minha Lista
└── ui/
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

Como não há persistência, o primeiro passo no app é **Cadastre-se**; depois
entre com o mesmo e-mail e senha.

## Versões

Kotlin 2.1.20 · AGP 8.13.1 · Gradle 8.14.3 · compileSdk 36 · minSdk 24 · JDK 17
Navigation Compose 2.8.9 · kotlinx-serialization-json 1.7+
