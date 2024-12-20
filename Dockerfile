# Базовый образ
FROM openjdk:17-jdk-slim

# Рабочая директория
WORKDIR /app

# Копируем собранный JAR-файл
COPY target/wallet-app-0.0.1-SNAPSHOT.jar app.jar

# Команда запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]

# Открываем порт 8080
EXPOSE 8080
