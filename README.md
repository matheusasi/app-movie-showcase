# CINETECA

App Android de biblioteca pessoal de filmes, feito em Kotlin com Jetpack Compose.
Trabalho de Android — Etapa 2 (implementação em sala, 01/09).

## Integrantes

- Matheus Rodrigues Cassab Asinelli Beyersdorff
- Matheus Henrique Farias de Jesus
- João Adolfo Bonato

## Telas implementadas (5)

| # | Tela | Arquivo | Responsável |
|---|------|---------|-------------|
| 1 | Início — destaque da semana e recomendados | `ui/TelaInicio.kt` | Matheus Rodrigues Cassab Asinelli Beyersdorff |
| 2 | Buscar — campo de texto + filtro por gênero | `ui/TelaBuscar.kt` | Matheus Henrique Farias de Jesus |
| 3 | Detalhe — sinopse, nota média e ações | `ui/TelaDetalhe.kt` | João Adolfo Bonato |
| 4 | Minha Lista — abas Quero ver / Assistidos | `ui/TelaLista.kt` | Matheus Rodrigues Cassab Asinelli Beyersdorff |
| 5 | Avaliar — data, local, nota e comentário | `ui/TelaAvaliar.kt` | Matheus Henrique Farias de Jesus |

## Fluxo principal

Início → toca num filme → Detalhe → "Avaliar este filme" → Avaliar → salva e o
filme vai para a aba "Assistidos" da Minha Lista. A barra inferior troca de tela
a qualquer momento.

## O que foi usado (só o conteúdo das aulas)

Todo o app usa apenas o que foi apresentado nas aulas (Kotlin básico, `when`,
coleções, funções/lambdas, POO e as duas aulas de Jetpack Compose). Nada de
biblioteca nova.

- **Navegação:** um estado `var telaAtual by remember { mutableStateOf(...) }` +
  `when (telaAtual)` no `MainActivity.kt`. Sem `navigation-compose`.
- **Estado das listas:** `var ... by remember { mutableStateOf(listOf(...)) }`.
  Para incluir/remover um id, funções que montam uma lista nova com
  `mutableListOf` + `forEach` + `add` (aula de coleções). Sem `mutableStateListOf`.
- **Listas na tela:** `Column { lista.forEach { ... } }` — como no formulário da
  aula. Sem `LazyColumn`.
- **Tela base:** `Scaffold { innerPadding -> ... }` (aula do formulário).
- **Formulário (Avaliar):** `OutlinedTextField`, `RadioButton`, `Slider`,
  `DatePickerDialog` + `rememberDatePickerState` — os mesmos da aula.
- **Cores:** só as constantes nomeadas (`Color.Black`, `Color.Red`, `Color.White`,
  `Color.Gray`, `Color.LightGray`), como `Color.Blue`/`Color.White` da aula.
- **Dados:** lista fixa de 15 filmes em `data/Filme.kt`. Sem internet, sem banco,
  sem carregar imagem (o poster é um retângulo cinza).

Única exceção: `@OptIn(ExperimentalMaterial3Api::class)` em `TelaAvaliar.kt`, que
o `DatePicker` exige para compilar (é o mesmo componente da aula `myapplication`).

## Observação sobre rolagem

As telas **não rolam** — a rolagem (`verticalScroll` / `LazyColumn`) não foi dada
em aula, então as telas foram feitas para caber. Em telas de celular menores, a
tela de Busca (com muitos resultados) e a de Avaliar podem cortar conteúdo
embaixo. Se o professor permitir, basta adicionar `.verticalScroll(rememberScrollState())`
no `Column` externo dessas telas.

## Estrutura

```
app/src/main/java/com/example/projetomobile/
├── MainActivity.kt        navegação por estado + estado compartilhado
├── data/Filme.kt          modelos (Filme, Avaliacao) e catálogo fixo
└── ui/
    ├── theme/             ProjetoMobileTheme (gerado, igual ao das aulas)
    ├── Componentes.kt     barra superior/inferior, poster, item de filme, botões
    ├── TelaInicio.kt
    ├── TelaBuscar.kt
    ├── TelaDetalhe.kt
    ├── TelaLista.kt
    └── TelaAvaliar.kt
```

## Como rodar

1. Abra o Android Studio → **Open** → selecione esta pasta.
2. Espere o Gradle sincronizar.
3. Escolha um emulador (ex.: `Pixel_8`) e clique em ▶ **Run**.

Pelo terminal:

```bash
./gradlew assembleDebug
```

```bash
./gradlew installDebug
```

## Versões

Kotlin 2.1.20 · AGP 8.13.1 · Gradle 8.14.3 · compileSdk 36 · minSdk 24 · JDK 17
