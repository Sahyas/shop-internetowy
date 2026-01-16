# Wymagania projektu - Sklep Internetowy

## Kontekst
Repozytorium GitHub: https://github.com/Sahyas/shop-internetowy

**Stack technologiczny:**
- Backend: Java 21 + Spring Boot 3 + Maven
- Frontend: Thymeleaf (server-side rendering)
- Baza danych: Firebase (Firestore, Auth)
- Bezpieczeństwo: Spring Security (role ADMIN, USER)

## Wymagania do spełnienia

### 1. Autoryzacja i autentykacja
- [ ] Rejestracja nowych użytkowników (email + hasło)
- [ ] Logowanie (sesje po stronie serwera lub tokeny)
- [ ] Rola ADMIN i USER — panel admina dostępny tylko dla admina
- [ ] Integracja z Firebase Auth (Email/Password) — zamiast lokalnej bazy, używaj Firebase
- [ ] Po stronie backendu weryfikuj tokeny Firebase lub sesje

### 2. Funkcjonalność sklepu
**Strona główna:**
- [ ] Lista wszystkich produktów z paginacją/scrollem

**Szczegóły produktu:**
- [ ] Cena, opis, opcjonalnie obraz
- [ ] Przycisk "Dodaj do koszyka"

**Koszyk:**
- [ ] Przechowywany w sesji (session-backed) lub w Firestore
- [ ] Możliwość modyfikacji ilości
- [ ] Możliwość usunięcia przedmiotów
- [ ] Wyświetlanie sumy zamówienia

**Checkout (symulacja):**
- [ ] Formularz adresu dostawy
- [ ] Utworzenie zamówienia w Firestore
- [ ] Potwierdzenie zamówienia (bez integracji płatności)
- [ ] Zmiana statusu zamówienia na "Złożone"

### 3. Historia zamówień
- [ ] Użytkownik widzi swoją historię zamówień (z datą, statusem, produktami)
- [ ] Dane przechowywane w Firestore (kolekcja `orders`)

### 4. Panel administracyjny (dostęp tylko dla ADMIN)
**Zarządzanie produktami:**
- [ ] CRUD (Create, Read, Update, Delete)
- [ ] Pola: nazwa, opis, cena, kategoria, ilość na stanie
- [ ] Upload obrazu (opcjonalnie — Firebase Storage; jeśli pominiesz, to OK)

**Przegląd zamówień:**
- [ ] Lista zamówień użytkowników
- [ ] Możliwość zmiany statusu (Złożone → Wysłane → Dostarczono)

**Zarządzanie użytkownikami:**
- [ ] Lista użytkowników
- [ ] Przydzielanie ról (ADMIN ↔ USER)

### 5. Baza danych (Firebase Firestore)
**Kolekcje:**
- [ ] `users` — profil użytkownika, rola
- [ ] `products` — produkty sklepu
- [ ] `orders` — zamówienia z historią
- [ ] `categories` (opcjonalnie — dla filtrowania produktów)

### 6. Jakość kodu i architektura
- [ ] Warstwowa architektura: Controller → Service → Repository
- [ ] DTO: używaj DTO do transferu danych między warstwami
- [ ] Walidacja: Bean Validation (@NotNull, @NotEmpty, @DecimalMin, itp.)
- [ ] Obsługa błędów: zwracaj odpowiednie kody HTTP (400, 401, 403, 404, 500)
- [ ] Testy jednostkowe: JUnit 5 + Mockito dla serwisów (przynajmniej 3–5 testów)
- [ ] Konfiguracja: zmienne środowiskowe lub application.properties dla Firebase

### 7. Frontend (Thymeleaf) — prosty i czytelny
- [ ] Responsywne CSS (nie musisz być designerem, wystarczy funkcjonalne)
- [ ] Navigacja: Menu z linkami (Sklep, Moje zamówienia, Panel admina [tylko dla admina], Wyloguj)
- [ ] Brak zaawansowanego JS — maksymalnie vanilla JS do obsługi koszyka lub potwierdzenia
- [ ] Wszystkie teksty w języku polskim

### 8. README.md i dokumentacja
- [ ] Instrukcja: jak skonfigurować Firebase (Service Account JSON, ustawić zmienne)
- [ ] Kroki uruchomienia: `mvn spring-boot:run`
- [ ] Dane testowe:
  - Konta: `admin@example.com / admin123` (ROLE_ADMIN) oraz `user@example.com / user123` (ROLE_USER)
  - Przykładowe produkty (seed data w @PostConstruct lub startup hook)
- [ ] Instrukcja testowania dla prowadzącego (jak się zalogować, dodać produkt, złożyć zamówienie)

### 9. Wskazówki dotyczące „niewykrywalności AI"
**Kod naturalny:**
- [ ] Używaj rzeczywistych nazw zmiennych
- [ ] Unikaj przesadnie perfekcyjnych wzorców

**Komentarze:**
- [ ] Krótkie, polskie, naturalne
- [ ] Nie generuj 10 komentarzy do jednej metody

**Brak sygnałów AI:**
- [ ] Nie dołączaj meta-informacji o generowaniu
- [ ] Unikaj identycznych struktur we wszystkich kontrolerach
- [ ] Dodaj drobne, realistyczne niejednorodności stylu (np. czasami `var`, czasami jawny typ)

**Commit messages:**
- [ ] Naturalne polskie opisy, np. „dodaj logowanie", „obsługa zamówień", „UI produktów"

**Dane testowe:**
- [ ] Opisy produktów nie mogą być identyczne
- [ ] Dodaj różne ceny, kategorie, długości opisów

### 10. Obsługa obrazów (opcjonalnie — można pominąć w v1)
- [ ] Jeśli zaimplementujesz: Firebase Storage, upload z panelu admina, wyświetlanie w liście i szczegółach produktu
- [ ] Jeśli pominiesz: pole na URL obrazu (tekstowe) wystarczy

## Instrukcja wykonania

1. **Pobierz kod z GitHub:** `git clone https://github.com/Sahyas/shop-internetowy`
2. **Rozwin projekt** w myśl powyższych wymagań — dodaj serwisy, repozytoria, szablony
3. **Integruj Firebase:**
   - Utwórz projekt Firebase (jeśli jeszcze go nie masz)
   - Konfiguruj Firestore, Auth (Email/Password)
   - Pobierz Service Account JSON
   - Załaduj go w aplikacji za pośrednictwem GoogleCredentials (patrz komentarz w ShopApplication.java)
4. **Testy:** dodaj co najmniej 3 testy jednostkowe do serwisów
5. **Dokumentacja:** uzupełnij README.md instrukcjami i danymi testowymi
6. **Commit:** regularnie commituj zmiany z naturalnymi opisami po polsku
7. **Sprawdzenie:** uruchom aplikację lokalnie, przetestuj wszystkie ścieżki (logowanie, sklep, koszyk, panel admin, zamówienia)

## Kryteria oceniania (dla prowadzącego)

✅ Aplikacja uruchamia się bez błędów (`mvn spring-boot:run`)  
✅ Logowanie działa (admin i user mogą się zalogować za pomocą testowych kont)  
✅ Panel admina dostępny tylko dla roli ADMIN  
✅ Sklep wyświetla produkty, można dodać do koszyka, złożyć zamówienie  
✅ Historia zamówień wyświetla się dla użytkownika  
✅ Admin może dodawać/usuwać produkty, zmieniać statusy zamówień  
✅ Kod jest czysty, warstwowy, z testami  
✅ Firebase prawidłowo zintegrowany (dane w Firestore, autoryzacja przez Firebase Auth)  
✅ README zawiera instrukcje i dane testowe  

## Dodatkowe wskazówki

- **Spring Security:** użyj `@PreAuthorize("hasRole('ADMIN')")` do ochrony metod
- **Firebase SDK:** dodaj zależność `firebase-admin` do pom.xml (już tam jest)
- **Session:** użyj `HttpSession` lub cookies do przechowywania koszyka między żądaniami
- **CSRF:** nie wyłączaj CSRF w Spring Security — zamiast tego dodaj tokeny w formularzach (Thymeleaf automatycznie to robi)
- **Thymeleaf:** `th:*` atrybuty są wbudowane, nie potrzebujesz dodatkowych bibliotek
- **Logging:** dodaj slf4j do debugowania (już w Spring Boot)
- **Możliwość rozszerzenia:** jeśli chcesz, dodaj filtrowanie produktów po kategorii, wyszukiwanie, oceny produktów
