# Sklep Internetowy

Aplikacja webowa sklepu internetowego z integracją Firebase (Firestore + Auth).

## Stack technologiczny

- **Backend:** Java 21 + Spring Boot 3.1.4 + Maven
- **Frontend:** Thymeleaf + CSS
- **Baza danych:** Firebase Firestore
- **Autoryzacja:** Firebase Auth + Spring Security
- **Testy:** JUnit 5 + Mockito

## Funkcjonalności

### Dla użytkowników:
- ✅ Przeglądanie produktów z filtrowaniem po kategorii
- ✅ Szczegóły produktu
- ✅ Dodawanie do koszyka
- ✅ Zarządzanie koszykiem (zmiana ilości, usuwanie)
- ✅ Składanie zamówień z danymi dostawy
- ✅ Historia zamówień użytkownika

### Dla administratorów:
- ✅ Zarządzanie produktami (CRUD)
- ✅ Przegląd wszystkich zamówień
- ✅ Zmiana statusu zamówień
- ✅ Zarządzanie użytkownikami i rolami

## Konfiguracja Firebase

### 1. Utwórz projekt Firebase

1. Przejdź do [Firebase Console](https://console.firebase.google.com/)
2. Kliknij "Add project" i postępuj zgodnie z instrukcjami
3. Po utworzeniu projektu przejdź do Settings → Project settings → Service accounts
4. Kliknij "Generate new private key" i pobierz plik JSON

### 2. Włącz Firestore

1. W menu Firebase wybierz "Firestore Database"
2. Kliknij "Create database"
3. Wybierz tryb "Start in test mode" (dla developmentu)
4. Wybierz lokalizację

### 3. Włącz Authentication

1. W menu Firebase wybierz "Authentication"
2. Kliknij "Get started"
3. Włącz metodę "Email/Password"

### 4. Skonfiguruj aplikację

Ustaw ścieżkę do pliku Service Account JSON:

**Opcja 1:** Zmienna środowiskowa
```bash
export FIREBASE_CREDENTIALS_PATH=/path/to/serviceAccountKey.json
```

**Opcja 2:** Bezpośrednio w `application.properties`
```properties
firebase.credentials.path=C:/Users/YourName/firebase/serviceAccountKey.json
```

## Uruchomienie aplikacji

### Wymagania
- Java 21
- Maven 3.6+

### Kroki

1. Sklonuj repozytorium:
```bash
git clone https://github.com/Sahyas/shop-internetowy.git
cd shop-internetowy
```

2. Skonfiguruj Firebase (patrz sekcja powyżej)

3. Uruchom aplikację:
```bash
mvn spring-boot:run
```

4. Otwórz przeglądarkę: http://localhost:8080

## Konta testowe

Aplikacja automatycznie tworzy konta testowe przy starcie:

| Rola | Email | Hasło |
|------|-------|-------|
| **Administrator** | admin@example.com | admin123 |
| **Użytkownik** | user@example.com | user123 |

## Struktura projektu

```
src/
├── main/
│   ├── java/com/example/shop/
│   │   ├── config/              # Konfiguracja (Firebase, Security)
│   │   ├── controller/          # Kontrolery MVC
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── model/               # Modele danych
│   │   ├── repository/          # Warstwa dostępu do Firestore
│   │   ├── security/            # Spring Security
│   │   └── service/             # Logika biznesowa
│   └── resources/
│       ├── static/css/          # Style CSS
│       ├── templates/           # Szablony Thymeleaf
│       └── application.properties
└── test/
    └── java/com/example/shop/
        └── service/             # Testy jednostkowe
```

## Instrukcja testowania

### 1. Logowanie

1. Otwórz http://localhost:8080/login
2. Zaloguj się jako admin (admin@example.com / admin123)

### 2. Przeglądanie sklepu

1. Przejdź do strony głównej: http://localhost:8080
2. Zobacz listę produktów
3. Użyj filtrów kategorii

### 3. Dodawanie do koszyka

1. Kliknij na produkt
2. Zmień ilość jeśli potrzeba
3. Kliknij "Dodaj do koszyka"
4. Przejdź do koszyka: http://localhost:8080/cart

### 4. Składanie zamówienia

1. W koszyku kliknij "Przejdź do kasy"
2. Wypełnij dane dostawy
3. Kliknij "Złóż zamówienie"
4. Zobacz potwierdzenie

### 5. Panel administracyjny

1. Zaloguj się jako admin
2. Przejdź do http://localhost:8080/admin

**Zarządzanie produktami:**
- Lista produktów: `/admin/products`
- Dodaj produkt: `/admin/products/new`
- Edytuj produkt: kliknij "Edytuj" przy produkcie
- Usuń produkt: kliknij "Usuń"

**Zarządzanie zamówieniami:**
- Zobacz zamówienia: `/admin/orders`
- Zmień status: wybierz z listy rozwijanej

**Zarządzanie użytkownikami:**
- Zobacz użytkowników: `/admin/users`
- Zmień rolę: wybierz z listy rozwijanej

## Uruchomienie testów

```bash
mvn test
```

## Przykładowe produkty

Aplikacja automatycznie dodaje przykładowe produkty przy pierwszym uruchomieniu:

1. **Laptop Dell XPS 15** - Elektronika - 4299.99 zł
2. **Słuchawki Sony WH-1000XM4** - Elektronika - 1199.00 zł
3. **Książka: Clean Code** - Książki - 69.90 zł
4. **Klawiatura mechaniczna Logitech** - Akcesoria - 399.99 zł
5. **Monitor Samsung 27 cali 4K** - Elektronika - 1599.00 zł

## Architektura

### Warstwa kontrolerów
- `HomeController` - strona główna i lista produktów
- `ProductController` - szczegóły produktu
- `CartController` - koszyk i checkout
- `OrderController` - zamówienia użytkownika
- `AdminController` - panel administracyjny
- `AuthController` - logowanie

### Warstwa serwisów
- `ProductService` - logika produktów
- `OrderService` - logika zamówień
- `CartService` - zarządzanie koszykiem (session-scoped)
- `UserService` - zarządzanie użytkownikami

### Warstwa repozytoriów
- `ProductRepository` - dostęp do Firestore dla produktów
- `OrderRepository` - dostęp do Firestore dla zamówień
- `UserRepository` - dostęp do Firestore dla użytkowników

## Bezpieczeństwo

- Spring Security z rolami `ROLE_USER` i `ROLE_ADMIN`
- Endpointy `/admin/**` wymagają roli ADMIN
- Endpointy `/cart/**`, `/checkout/**`, `/orders/**` wymagają autoryzacji
- CSRF protection włączony
- Session-based authentication

## Troubleshooting

### Firebase nie działa
- Sprawdź czy plik credentials JSON istnieje
- Sprawdź ścieżkę w `application.properties`
- Sprawdź logi aplikacji

### Błąd kompilacji
```bash
mvn clean install
```

### Port zajęty
Zmień port w `application.properties`:
```properties
server.port=8081
```

## Rozszerzenia (opcjonalne)

- [ ] Firebase Storage dla obrazków produktów
- [ ] Wyszukiwanie produktów
- [ ] Oceny i recenzje produktów
- [ ] Płatności online
- [ ] Email notifications
- [ ] Eksport zamówień do PDF

## Autor

Projekt sklepu internetowego - zaliczenie

## Licencja

Projekt edukacyjny

