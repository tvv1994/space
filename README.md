# В приложении используй технологии:
1.  Maven (для сборки проекта);
2.  Tomcat 9 (для запуска своего приложения);
3.  Spring;
4.  Spring Data JPA;
5.  MySQL (база данных (БД)).

# Задание:
Нужно дописать приложение, которое ведет учет космических кораблей. Должны быть реализованы следующие
возможности:
1. получать список всех существующих кораблей;
2. создавать новый корабль;
3. редактировать характеристики существующего корабля;
4. удалять корабль;
5. получать корабль по id;
6. получать отфильтрованный список кораблей в соответствии с переданными фильтрами;
7. получать количество кораблей, которые соответствуют фильтрам. 

Для этого необходимо реализовать REST API в соответствии с документацией.

В проекте должна использоваться сущность Ship, которая имеет поля:

| Параметр | Описание |
|----------------|:---------:|
| Long id | ID корабля |
| String name | Название корабля (до 50 знаков включительно) |
| String planet | Планета пребывания (до 50 знаков включительно) |
| ShipType shipType | Тип корабля |
| Date prodDate | Дата выпуска. Диапазон значений года 2800..3019 включительно |
| Boolean isUsed | Использованный / новый |
| Double speed | Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно. Используй математическое округление до сотых. |
| Integer crewSize | Количество членов экипажа. Диапазон значений 1..9999 включительно. |
| Double rating | Рейтинг корабля. Используй математическое округление до сотых. |

Также должна присутствовать бизнес-логика. Перед сохранением корабля в базу данных (при добавлении нового или при апдейте характеристик существующего), должен высчитываться рейтинг корабля и сохраняться в БД. Рейтинг корабля рассчитывается по формуле:

R = (80 * v * k)/(y0 - y1 + 1)
, где:
v — скорость корабля;
k — коэффициент, который равен 1 для нового корабля и 0,5 для
использованного;
y0 — текущий год (не забудь, что «сейчас» 3019 год);
y1 — год выпуска корабля.

# Обрати внимание:
1. Если в запросе на создание корабля нет параметра “isUsed”, то считаем, что пришло значение “false”.
2. Параметры даты между фронтом и сервером передаются в миллисекундах (тип Long) начиная с 01.01.1970.
3. При обновлении или создании корабля игнорируем параметры “id” и “rating” из тела запроса.
4. Если параметр pageNumber не указан – нужно использовать значение 0.
5. Если параметр pageSize не указан – нужно использовать значение 3.
6. Не валидным считается id, если он:
- не числовой
- не целое число
- не положительный
7. При передаче границ диапазонов (параметры с именами, которые начинаются на «min» или «max») границы нужно использовать включительно. 
