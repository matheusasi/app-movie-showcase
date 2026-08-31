# Projeto Mobile

App Android básico em Kotlin com Jetpack Compose.

## Como rodar

1. Abra o Android Studio → **Open** → selecione esta pasta.
2. Espere o Gradle sincronizar (a primeira vez baixa dependências).
3. Escolha o emulador `Pixel_8` na barra superior e clique em ▶ **Run**.

Pelo terminal:

```bash
./gradlew assembleDebug          # gera o APK
./gradlew installDebug           # instala no emulador/celular conectado
```

## Estrutura

- `app/src/main/java/com/example/projetomobile/MainActivity.kt` — tela principal
- `app/src/main/res/values/` — strings e tema
- `gradle/libs.versions.toml` — versões das dependências
- `app/build.gradle.kts` — configuração do módulo do app

## Versões

Kotlin 2.1.20 · AGP 8.13.1 · Gradle 8.14.3 · compileSdk 36 · minSdk 24 · JDK 17
