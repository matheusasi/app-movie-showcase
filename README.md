# CINETECA

App Android de biblioteca pessoal de filmes, feito em Kotlin com Jetpack Compose.
Trabalho de Android — Etapa 2 (implementação em sala, 01/09).

## Telas implementadas (5)

| # | Tela | Arquivo | Responsável |
|---|------|---------|-------------|
| 1 | Início — destaque da semana e recomendados | `ui/TelaInicio.kt` | integrante 1 |
| 2 | Buscar — campo de texto + filtro por gênero | `ui/TelaBuscar.kt` | integrante 2 |
| 3 | Detalhe — sinopse, nota média e ações | `ui/TelaDetalhe.kt` | integrante 3 |
| 4 | Minha Lista — abas Quero ver / Assistidos | `ui/TelaLista.kt` | integrante 1 |
| 5 | Avaliar — data, local, nota e comentário | `ui/TelaAvaliar.kt` | integrante 2 |

> Troque "integrante N" pelos nomes reais antes de entregar.

## Fluxo principal

Início → toca num filme → Detalhe → "Avaliar este filme" → Avaliar → salva e o
filme vai para a aba "Assistidos" da Minha Lista. A barra inferior troca de tela
a qualquer momento.

## Restrições respeitadas

Só foram usados componentes do Jetpack Compose + Material 3 que já vêm no projeto.
Nenhuma biblioteca nova foi adicionada — em especial, **não** usamos
`navigation-compose`, `ViewModel`, Room, Retrofit nem carregamento de imagens.

- Navegação: estado simples (`remember { mutableStateOf(...) }`) no `MainActivity.kt`.
- Estilo: cores aplicadas inline (`Color(0xFFE8341C)` etc.), sem arquivo de tema.
- Dados: lista fixa em `data/Filme.kt` (15 filmes), sem internet e sem banco.
- Estado da lista/avaliações: `mutableStateListOf` no `MainActivity.kt`.

## Estrutura

```
app/src/main/java/com/example/projetomobile/
├── MainActivity.kt        navegação por estado + estado compartilhado
├── data/Filme.kt          modelos (Filme, Avaliacao) e catálogo fixo
└── ui/
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
3. Escolha o emulador `Pixel_8` e clique em ▶ **Run**.

Pelo terminal:

```bash
./gradlew assembleDebug          # gera o APK
./gradlew installDebug           # instala no emulador/celular conectado
```

## Versões

Kotlin 2.1.20 · AGP 8.13.1 · Gradle 8.14.3 · compileSdk 36 · minSdk 24 · JDK 17
