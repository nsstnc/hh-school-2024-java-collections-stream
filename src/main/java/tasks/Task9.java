package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {


  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // проверяем пустоту списка методом isEmpty()
    if (persons.isEmpty()) {
      return Collections.emptyList();
    }
    // не удаляем первый элемент, а используем subList для конвертации, т.к. это более безопасно
    return persons.subList(1, persons.size())
        .stream()
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // Set, гарантирует уникальность элементов, возвращаем его без disctinct
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // добавляем проверку объекта на Null
    if (person == null) {
      return "";
    }
    // используем String.join, вместо кучи if'ов
    // добавляем отчество, вместо двойной фамилии в изначальном варианте
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    if (persons == null) {
      return Collections.emptyMap();
    }
    // используем более краткую запись через stream и игнорируем дубликаты
    return persons.stream()
        .collect(Collectors.toMap(
            Person::id,
            this::convertPersonToString,
            (existing, replacement) -> existing
        ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    if (persons1 == null || persons2 == null) {
      return false;
    }
    // используем пересечение множеств
    Set<Person> set1 = new HashSet<>(persons1);
    for (Person person : persons2) {
      if (set1.contains(person)) {
        return true;
      }
    }
    return false;
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    if (numbers == null) {
      return 0;
    }
    // используем count вместо forEach
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    // при преобразовании в строку, Set использует сортированный порядок
    assert snapshot.toString().equals(set.toString());
    // можно проверить сохранение порядка без преобразования в строку
    assert new HashSet<>(snapshot).equals(set);

  }
}
