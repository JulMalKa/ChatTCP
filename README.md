# Java Chat App

Wieloosobowy czat działający w architekturze klient-serwer przez TCP. Serwer obsługuje wielu klientów równolegle dzięki wątkom, a klienci mogą rozmawiać publicznie lub wysyłać prywatne wiadomości.

## Uruchomienie

**1. Skompiluj pliki:**
```bash
javac ChatServer.java ChatClient.java
```

**2. Uruchom serwer:**
```bash
java ChatServer
```

**3. Uruchom klienta (w osobnym terminalu):**
```bash
java ChatClient
```
## Komendy

| Komenda | Opis |
|---|---|
| `/list` | Lista aktualnie połączonych użytkowników |
| `/msg <user> <text>` | Wyślij prywatną wiadomość do użytkownika |
| `/all <text>` | Wyślij wiadomość do wszystkich |
| `/quit` | Rozłącz się z serwera |
| `/help` | Wyświetl listę komend |

## Struktura projektu

```
ChatServer.java   # Serwer – nasłuchuje na porcie 12345, zarządza klientami
ChatClient.java   # Klient – łączy się z serwerem, wysyła i odbiera wiadomości
```

