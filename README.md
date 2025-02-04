# Telegram bot

Телеграм бот для игры в камень-ножницы-бумага.

## Требования

Для запуска проекта понадобятся:

- Java 8
- Docker

## Установка

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/ilia-mikhailov98/telegram-bot.git
   cd telegram-bot
   ```
2. Укажите toke и username в файле `docker-compose.yml`.

3. Запустите приложение:

   ```bash
   make build
   make up
   ```

# TODO

- [ ] Организовать парсинг пришедшего сообщения в отдельный компонент
- [ ] Добавить тесты
