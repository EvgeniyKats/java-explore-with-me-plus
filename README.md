# Дополнительная функциональность - комментарии

## Описание

Функциональность представляет из себя новую сущность **комментарий** и **API endpoints** для взаимодействия с ней.

Ниже представлена таблица, показывающая **наличие эндпоинов** по уровням доступа:

| Операция   | Public | User | Admin |
|------------|:------:|:----:|:-----:|
| Создание   |   -    |  +   |   -   |
| Обновление |   -    |  +   |   -   |
| Удаление   |   -    |  +   |   +   |
| Получение  |   +    |  -   |   -   |

## API endpoints

**Server**: http://localhost:8080

*Формат обмена данными с сервером:* `JSON`

*Обязательные параметры эндпоинов отмечены символом* `*`*, например* `id*`

------------

### POST

#### Создание комментария, доступно только пользователям:

POST `/users/{userId}/events/{eventId}/comment`

**Параметры**:

- `userId*` ($int64) - id пользователя
- `eventId*` ($int64) - id события
- Request body: `CommentDto*`

**Ответы сервера**:

- Code `201` - комментарий создан

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "id": 1,
  "text": "Хочу участвовать!",
  "created": "2024-12-31 15:10:05",
  "eventId": 1,
  "author": {
    "id": 1,
    "name": "Фёдор"
  }
}
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найден пользователь или событие

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Event with id=13 was not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `409` - событие ещё не опубликовано

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "FORBIDDEN",
  "reason": "For the requested operation the conditions are not met.",
  "message": "It is allowed to add the comment to only PUBLISHED event.",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

------------

### PATCH

#### Обновление комментария, доступно только пользователям:

Особенности:

- Комментарий может быть обновлен в течение 24 часов после создания

PATCH `/users/{userId}/events/{eventId}/comments/{commentId}`

**Параметры**:

- `userId*` ($int64) - id пользователя
- `eventId*` ($int64) - id события
- `commentId` ($int64) - id комментария
- Request body: `CommentDto*`

**Ответы сервера**:

- Code `200` - комментарий обновлен

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "id": 1,
  "text": "Хочу участвовать!",
  "created": "2024-12-31 15:10:05",
  "eventId": 1,
  "author": {
    "id": 1,
    "name": "Фёдор"
  }
}
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найден комментарий по указанному пути

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Comment not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `409` - при обновлении коментария спустя 24 часа после создания

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "FORBIDDEN",
  "reason": "For the requested operation the conditions are not met.",
  "message": "It is allowed to change the comment within 24 hours after create",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

------------

### DELETE

#### Удаление комментария пользователем:

DELETE `/users/{userId}/events/{eventId}/comments/{commentId}`

**Параметры**:

- `userId*` ($int64) - id пользователя
- `eventId*` ($int64) - id события
- `commentId*` ($int64) - id комментария

**Ответы сервера**:

- Code `204` - комментарий удалён

<details>
  <summary>Пример ответа (NO_CONTENT)</summary>
<p>

```JSON
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найден комментарий по указанному пути

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Comment not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

------------

#### Удаление комментария администратором:

DELETE `/admin/events/{eventId}/comments/{commentId}`

**Параметры**:

- `eventId*` ($int64) - id события
- `commentId*` ($int64) - id комментария

**Ответы сервера**:

- Code `204` - комментарий удалён

<details>
  <summary>Пример ответа (NO_CONTENT)</summary>
<p>

```JSON
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найден комментарий по указанному пути

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Comment not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

------------

### GET

#### Получение одиночного комментария события (публичный API)

GET `events/{eventId}/comments/{commentId}`

**Параметры**:

- `eventId*` ($int64) - id события
- `commentId*` ($int64) - id комментария

**Ответы сервера**:

- Code `200` - комментарий получен

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "id": 1,
  "text": "Хочу участвовать!",
  "created": "2024-12-31 15:10:05",
  "eventId": 1,
  "author": {
    "id": 1,
    "name": "Фёдор"
  }
}
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найден комментарий по указанному пути

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Comment not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

#### Получение нескольких комментариев события (публичный API)

GET `events/{eventId}/comments`

**Параметры**:

- `eventId*` ($int64) - id события
- `from` ($int32, default = 0) - количество элементов, которые нужно пропустить для формирования текущего набора
- `size` ($int32, default = 10) - количество элементов в наборе
- `sort` (string, default = COMMENTS_NEW) - сортировка по старым или новым комментариям
    - Допустимые значения: `COMMENTS_NEW`, `COMMENTS_OLD`

**Ответы сервера**:

- Code `200` - комментарии получены

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
[
  {
    "id": 1,
    "text": "Хочу участвовать!",
    "created": "2024-12-31 15:10:05",
    "eventId": 1,
    "author": {
      "id": 1,
      "name": "Фёдор"
    }
  },
  {
    "id": 2,
    "text": "Интересно",
    "created": "2024-12-31 16:15:20",
    "eventId": 1,
    "author": {
      "id": 2,
      "name": "Андрей"
    }
  }
]
```

</p>
</details>

- Code `400` - запрос составлен некорректно

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "BAD_REQUEST",
  "reason": "Incorrectly made request.",
  "message": "Failed to convert value of type java.lang.String to required type int; nested exception is java.lang.NumberFormatException: For input string: ad",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

- Code `404` - не найдено событие

<details>
  <summary>Пример ответа</summary>
<p>

```JSON
{
  "status": "NOT_FOUND",
  "reason": "The required object was not found.",
  "message": "Event with id=13 was not found",
  "timestamp": "2022-09-07 09:10:50"
}
```

</p>
</details>

------------

## Schemas

### CommentDto

**Описание**: используется для создания и обновления комментария

- `text*` (string) - текст комментария
    - minLength: 1
    - maxLength: 5000
    - example: Хочу участвовать!

### GetCommentDto

**Описание**: используется для получения информации о комментарии

- `id` ($int64) - id комментария
- `text` (string) - текст комментария
- `created` (string) - дата и время создания комментария (в формате "yyyy-MM-dd HH:mm:ss")
- `eventId` ($int64) - id комментируемого события
- `author` (UserShortDto) - краткая информация об авторе комментария:
    - id ($int64) - id пользователя
    - name (string) - имя пользователя

### EventFullDto и EventShortDto

**Описание**: дополнительно включают 10 последних комментариев

- `comments` (GetCommentDto) - последние комментарии