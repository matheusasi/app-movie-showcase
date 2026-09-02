# CINETECA

App Android de biblioteca pessoal de filmes, feito em Kotlin com Jetpack Compose.
Trabalho de Android — Etapa 2 (implementação em sala, 01/09).

## Integrantes

- Matheus Rodrigues Cassab Asinelli Beyersdorff
- Matheus Henrique Farias de Jesus
- João Adolfo Bonato

## Mockups (5)

As 5 telas do projeto estão desenhadas na pasta `mockups/`.

## Telas implementadas nesta etapa (3)

O trabalho pede 3 telas implementadas (login não conta — e não há login).

| # | Tela | Arquivo | Responsável |
|---|------|---------|-------------|
| 1 | Início — destaque da semana e recomendados | `ui/TelaInicio.kt` | Matheus Rodrigues Cassab Asinelli Beyersdorff |
| 2 | Detalhe — sinopse, nota média e ação | `ui/TelaDetalhe.kt` | João Adolfo Bonato |
| 3 | Avaliar — data, local, nota e comentário | `ui/TelaAvaliar.kt` | Matheus Henrique Farias de Jesus |

As telas de Buscar e Minha Lista ficam para a próxima etapa.

## Fluxo

Início → toca num filme → Detalhe → "Avaliar este filme" → Avaliar → preenche e
salva (aparece uma confirmação na tela). A barra de baixo tem **Início** e
**Avaliar** e troca de tela a qualquer momento.

## O que foi usado (só o conteúdo das aulas)

Tudo usa apenas o que foi apresentado nas aulas (Kotlin básico, `when`, coleções,
funções/lambdas, POO e as duas aulas de Jetpack Compose). Nenhuma biblioteca nova.

- **Navegação:** `var telaAtual by remember { mutableStateOf(...) }` + `when (telaAtual)`
  no `MainActivity.kt`. Sem `navigation-compose`.
- **Tela base:** `Scaffold { innerPadding -> ... }` (aula do formulário).
- **Layout:** `Column` / `Row` / `Box` + `Modifier` (`padding`, `height`, `weight`,
  `fillMaxWidth`, `background`, `clickable`).
- **Formulário (Avaliar):** `OutlinedTextField`, `RadioButton`, `Slider`,
  `DatePickerDialog` + `rememberDatePickerState` — os mesmos da aula.
- **Cores:** só constantes nomeadas (`Color.Black`, `Color.Red`, `Color.White`,
  `Color.Gray`, `Color.LightGray`), como `Color.Blue`/`Color.White` da aula.
- **Dados:** lista fixa de 15 filmes em `data/Filme.kt`. Sem internet, sem banco,
  sem carregar imagem (o poster é um retângulo cinza).

Única exceção: `@OptIn(ExperimentalMaterial3Api::class)` em `TelaAvaliar.kt`, que o
`DatePicker` exige para compilar (é o mesmo componente da aula `myapplication`).

## Observação sobre rolagem

As telas **não rolam** — rolagem (`verticalScroll` / `LazyColumn`) não foi dada em
aula, então as telas foram feitas para caber. Se o professor permitir, é só
adicionar `.verticalScroll(rememberScrollState())` no `Column` externo.

## Estrutura

```
app/src/main/java/com/example/projetomobile/
├── MainActivity.kt        navegação por estado + tela base
├── data/Filme.kt          modelos (Filme, Avaliacao) e catálogo fixo
└── ui/
    ├── theme/             ProjetoMobileTheme (gerado, igual ao das aulas)
    ├── Componentes.kt     barra superior/inferior, poster, item de filme, botão
    ├── TelaInicio.kt
    ├── TelaDetalhe.kt
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
