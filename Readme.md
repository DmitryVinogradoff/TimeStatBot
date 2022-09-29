# TimeStatBot

Телеграм бот для для учета времени, затраченного на выполнение задач.
> https://t.me/TimeStatBot

Требования для развертывания бота на сервере
-
- **Java 17**
- **Maven**
- **Docker**
- **Docker compose**

Запуск бота на сервере
-
- Клонируем репозиторий
```bash
$ git clone git@github.com:DmitryVinogradoff/TimeStatBot.git
```
- Переименовываем файл **key.env.example** в **key.env**
```bash
$ cd TimeStatBot
$ mv src/main/resources/key.env.example src/main/resources/key.env
```
- Открываем файл **key.env** и меняем значения переменных на свои. 
  Эти данные будут использоваться при поднятии Docker-контейнера
```
BOT_TOKEN=1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij
BOT_NAME=BotName
POSTGRES_USER=username
POSTGRES_PASSWORD=password
POSTGRES_DB=db_name
```

- Переименовываем файл **application.prorepties.example** в **application.prorepties**

```bash
$ mv src/main/resources/application.properties.example src/main/resources/application.properties
```

- Открываем файл **application.prorepties** и меняем значения переменных на свои.

```
logging.level.org.hibernate.SQL=DEBUG

spring.datasource.url=jdbc:postgresql://address_to_db_server:port/db_name
spring.datasource.username=username
spring.datasource.password=password

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```
- Упаковываем исходники в **jar** файл. Для этого переходим в корень проекта и запускаем упаковку
```bash
$ mvn clean package
```
- Поднимаем бота в Docker-контейнере
```bash
docker-compose up
```