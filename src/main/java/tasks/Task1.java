package tasks;

import common.Person;
import common.PersonService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;
/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }
  // сложность по времени O(n + m), где n - количество id в списке, а m - количество уникальных Person
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);

    Map<Integer, Person> personMap = persons.stream()
        .collect(Collectors.toMap(Person::id, person -> person));

    return personIds.stream()
        .map(personMap::get)
        .collect(Collectors.toList());
  }
}
