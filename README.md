# Green Data

### Постановка задачи
https://docs.google.com/document/d/1c-Ts6kPQ6kD2Dq6s1TAjpEdczhspAGthZxlfmLpXflI/edit

### Сборка и запуск приложения
* `mvn clean package` собирает приложение и докер-образ
* `docker-compose up` запускает приложение

после запуска доступен сваггер на порту 8080

#### Метод api filter

Позволяет строить динамические sql-запросы типа
``` 
select ... where (customer1_.name like ? or bank2_.name in (? , ? , ?)) and deposit0_.percent>5.0 order by deposit0_
.percent
 asc, deposit0_.id desc limit ? 
 ``` 

используя json-запрос
<pre>
{
   "page":0,
   "predicate":{
      "condition":{
         "key":"percent",
         "logicalCondition":"GT",
         "value":5,
         "values":[

         ]
      },
      "connectionType":"AND",
      "predicates":[
         {
            "condition":{
               "key":"bank.name",
               "logicalCondition":"IN",
               "values":[
                  "bank1",
                  "bank2",
                  "bank3"
               ]
            },
            "connectionType":"OR",
            "predicates":[
               {
                  "condition":{
                     "key":"customer.name",
                     "logicalCondition":"LIKE",
                     "value":"customer%"
                  },
                  "predicates":[

                  ]
               }
            ]
         }
      ]
   },
   "size":10,
   "sort":{
      "percent":"ASC",
      "id":"DESC"
   }
}
</pre>