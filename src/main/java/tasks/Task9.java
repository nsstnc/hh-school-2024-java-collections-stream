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
    // не удаляем первый элемент, а пропускаем его с помощью skip
    return persons.stream()
        .skip(1)
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
    // используем объединение строк через collectors, вместо кучи if'ов
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
    return persons1 != null && persons2 != null &&
        persons1.stream().anyMatch(new HashSet<>(persons2)::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // используем count вместо forEach
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    // это происходит из-за особенности вычисления хешей во время преобразования в hashset.
    // хэш-код вычисляется по модулю емкости hashset, стандартная емкость равна 16, но при конвертации бОльшего списка в hashset, она изменяется так, чтобы кол-во bucket'ов было с запасом
    Set<Integer> set = new HashSet<>(integers);
    // когда мы выполняем set.toString() bucket'ы внутри HashSet перебираются по порядку, это и влияет на то, что в итоге элементы выводятся в отсортированном порядке
    assert snapshot.toString().equals(set.toString());
  }
}
