# Решённое задание отборочного этапа ШИФТ ЦФТ (Java)

## Особенности реализации

Программа сортирует содержимое нескольких файлов, _отбрасывая_ неотсортированные , а также некорректные данные.

## Информация о сборке

- Версия Java: 11
- Система сборки: Maven 3.6.3
- Нет сторонних библиотек
- IDE: Eclipse

## Инструкция по запуску

```
mvn package
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar <аргументы>
```

## Примеры команд для тестирования программы

Их можно скопировать.

```
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar -a -s out1.txt str_asc_input1.txt str_asc_input2.txt str_asc_input3.txt
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar -d -s out2.txt str_desc_input1.txt str_desc_input2.txt str_desc_input3.txt
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar -a -i out3.txt num_asc_input1.txt num_asc_input2.txt num_asc_input3.txt
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar -a -i out4.txt num_rand_input1.txt num_rand_input2.txt num_rand_input3.txt
java -jar target/cftShiftTestTask-0.0.1-SNAPSHOT.jar -d -i out5.txt num_rand_input1.txt num_rand_input2.txt num_rand_input3.txt
```

Открыв файлы out?.txt, можно убедиться, что данные в них отсортированы в нужном порядке.
