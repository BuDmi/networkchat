# Сетевой чат

Проект представляет собой сетевой чат, в котором обмен текстовыми сообщениями происходит по сети с помощью консоли 
(терминала) между двумя и более пользователями.

- `Cервер чата`.

- `Клиент чата`.

Все сообщения чата записыватся в file.log как на сервере, так и на клиентах. 
File.log дополняется при каждом запуске, а также при отправленном или полученном сообщении. 
Выход из чата осуществляется по команде `/exit`.

## Сервер чата
- установка порта для подключения клиентов через файл настроек (settings.txt);
- поддерживает возможность подключиться к серверу в любой момент и присоединиться к чату;
- отправляет новые сообщения клиентам;
- записывает все отправленные через сервер сообщения с указанием имени пользователя и времени отправки.

## Клиент чата
- выбор имени для участия в чате;
- чтение настройки приложения из файла настроек - например, номер порта сервера;
- подключение к указанному в настройках серверу (settings.txt);
- для выхода из чата нужно набрать команду выхода - “/exit”;
- каждое сообщение участников записывается в текстовый файл - файл логирования.