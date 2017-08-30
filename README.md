[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6db5949932f040c6aef91611b8c15e7d)](https://www.codacy.com/app/promoscow/2017-08-18_test_task?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=promoscow/2017-08-18_test_task&amp;utm_campaign=Badge_Grade)

<h2>Тестовое задание для одной крупной компании.</h2>

Дано: таблица TEST в произвольной БД (использование in memory баз данных не рекомендуется), содержащая один столбец целочисленного типа (FIELD).

Необходимо написать консольное приложение на Java, использующее стандартную библиотеку JDK7 (желательно) либо JDK8 и реализующее следующий функционал:

1. Основной класс приложения должен следовать правилам JavaBean, то есть инициализироваться через setter'ы. Параметры инициализации - данные для подключения к БД и число N. 

2. После запуска, приложение вставляет в TEST N записей со значениями 1..N. Если в таблице TEST находились записи, то они удаляются перед вставкой.

3. Затем приложение запрашивает эти данные из TEST.FIELD и формирует корректный XML-документ вида
<entries>
    <entry>
        <field>значение поля field</field>
    </entry>
    ...
    <entry>
        <field>значение поля field</field>
    </entry>
</entries>
(с N вложенных элементов <entry>)
Документ сохраняется в файловую систему как "1.xml".

4. Посредством XSLT, приложение преобразует содержимое "1.xml" к следующему виду:
<entries>
    <entry field="значение поля field">
    ...
    <entry field="значение поля field">
</entries>
(с N вложенных элементов <entry>)
Новый документ сохраняется в файловую систему как "2.xml".

5. Приложение парсит "2.xml" и выводит арифметическую сумму значений всех атрибутов field в консоль. 

6. При больших N (~1000000) время работы приложения не должно быть более пяти минут.

<h2>Решение:</h2>

1. Для выполнения этой задачи я решил использовать PostgreSQL и стандартный JDBC.

2. В пункте 4 технического задания присутствует формат тегов, не корректный для .xml
В связи с этим, реализованы два решения технического задания:
    - Реализация в формате, указанном в техническом задании, а именно: файл 1.xml преобразуется с включением тегов формата <entry field="value">. Для реализации этого решения необходимо установить значение impl=tech.task в config.properties (установлено по умолчанию).
    
    - Реализация в формате, стандартном для XML-файлов. Для реализации этого решения установите значение impl=correct в config.properties
