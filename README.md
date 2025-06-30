
# Java Data Filter Utility

## 📦 Сборка

```bash
mvn clean package
```

## 🚀 Пример запуска

```bash
java -jar target/data-filter-util-1.0-SNAPSHOT-jar-with-dependencies.jar -f -a -p sample- -o output/ in1.txt in2.txt
```

## 📁 Входные файлы

- `in1.txt`
- `in2.txt`

## 📂 Выход

Файлы будут записаны в `output/sample-integers.txt`, `sample-strings.txt`, `sample-floats.txt` и выведется статистика.
