# Sklep internetowy — szkic projektu

Prosty sklep internetowy (backend: Java Spring Boot, frontend: Thymeleaf). Projekt jest przygotowany jako szkic — integracja z Firebase jest opisana w README i oznaczona komentarzami w kodzie.

Szybkie kroki lokalne:

1. Zainstaluj JDK 17 i Maven.
2. (Opcjonalnie) Zainstaluj `gh` (GitHub CLI), aby łatwo utworzyć repozytorium.
3. Uruchom aplikację:

```bash
mvn spring-boot:run
```

Konfiguracja Firebase (opcjonalna, do pełnej integracji):
- Utwórz projekt Firebase i dodaj Authentication (Email/Password) oraz Firestore.
- Pobierz plik Service Account JSON i umieść go lokalnie jako `serviceAccountKey.json` obok `application.properties` (NIE commituj do repo).
- Ustaw zmienną środowiskową `GOOGLE_APPLICATION_CREDENTIALS` wskazującą na plik JSON.

W repo znajdziesz prosty, działający serwis przechowujący produkty w pamięci (do szybkiego testu). Zamiast tego możesz podłączyć Firestore — w kodzie są komentarze, gdzie to zrobić.

Tworzenie repozytorium GitHub (lokalnie z `gh`):

```bash
git init
git add .
git commit -m "inicjalny szkielet projektu sklepu"
gh repo create <nazwa-repo> --public --source=. --remote=origin --push
```

Jeśli chcesz, mogę przygotować pull request, wygenerować repo na GitHub (wymaga autoryzacji) lub wypchnąć kod za Ciebie — daj znać.
