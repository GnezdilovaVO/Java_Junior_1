import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args) {
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department #" + i));

        }
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            persons.add(new Person("Person #" + i,
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20000, 100000) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
                    ));
        }
        persons.add(new Person ("Anna", 35, 50000 * 1.0,
                departments.get(ThreadLocalRandom.current().nextInt(departments.size()))));


//        Получает больше всех
        persons.stream()
                .max(Comparator.comparing(Person::getSalary))
                .ifPresent(it -> System.out.println(it));
        Function<Person, Integer> personDepartmentNumberExtraction = person -> {
            String departmentName = person.getDepartment().getName();

            return Integer.parseInt(departmentName.split("#")[1]);
        };

//        Старше 40 лет и департамент больше и сохранить их в LinkedList
        persons.stream()
                .filter(it -> it.getAge() > 40)
                .filter(it -> personDepartmentNumberExtraction.apply(it) > 3)
                .collect(Collectors.toCollection(LinkedList::new));

//        Департаменты с сотрудниками зп выше среднего
        double averageSalary = persons.stream()
                .mapToDouble(Person::getSalary)
                .average().orElse(0.0);

        persons.stream()
                .filter(it -> it.getSalary() > averageSalary)
                .map(Person::getDepartment)
                .distinct()
                .forEach(System.out::println);


//        Собрать Map<String, List<Person>> имя отдела и сотрудники которые  нем работают
        Map<String, List<Person>> personsGroupedByName = persons.stream()
                .collect(Collectors.groupingBy(it -> it.getDepartment().getName()));

        System.out.println(personsGroupedByName);

//        Собрать Map<String, List<Person>> имя отдела и сотрудник с высокой зарплатой
        Comparator<Person> salaryComparator = Comparator.comparing(Person::getSalary);
        Map<String, Person> maxSalary = persons.stream()
                .collect(Collectors.toMap(it -> it.getDepartment().getName(), it -> it, new BinaryOperator<Person>() {

                    @Override
                    public Person apply(Person person, Person person2) {
                        if (salaryComparator.compare(person, person2) > 0) {
                            return person;
                        }
                        return person2;
                    }
                }));
        System.out.println(maxSalary);
        printNamesOrdered(persons);
        System.out.println(findFirstPersons(persons));
        System.out.println(printDepartmentOldestPerson(persons));
        System.out.println(findFirstPersons(persons));


    }

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public static void printNamesOrdered(List<Streams.Person> persons) {
        persons.stream()
                .map(it -> it.getName())
                .sorted()
                .forEach(System.out::println);
    }
    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public static List<Streams.Person> findFirstPersons(List<Streams.Person> persons) {
        List<Streams.Person> list = persons.stream()
                .filter(it -> it.getAge() < 30)
                .filter(it -> it.getSalary() > 50000)
                .limit(10)
                .toList();
         return list;


    }
    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public static Map<Streams.Department, Streams.Person> printDepartmentOldestPerson(List<Streams.Person> persons) {
//        throw new UnsupportedOperationException();
        Comparator<Streams.Person> ageComparator = Comparator.comparing(Streams.Person::getAge);
        Map<Streams.Department, Streams.Person> maxAge = persons.stream()
                .collect(Collectors.toMap(Streams.Person:: getDepartment, Function.identity(), (person, person2) -> {
                    if (ageComparator.compare(person, person2) > 0) {
                        return person;
                    }
                    return person2;
                }));
        return maxAge;

    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public static Optional<Streams.Department> findTopDepartment(List<Streams.Person> persons) {
//        throw new UnsupportedOperationException();
        Optional<Streams.Department> highSalary = persons.stream()
                .collect(Collectors.groupingBy(it -> it.getDepartment(), Collectors.summingDouble(it -> it.getSalary())))
                .entrySet().stream()
                .sorted(Comparator.comparing(it -> it.getValue()))
                .map(it -> it.getKey())
                .findFirst();
        return highSalary;
//        Неверное работает, не успела доделать



    }




    static class Person{
        private String name;
        private int age;
        private double salary;
        private Department department;

        public Person(String name, int age, double salary, Department department) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getSalary() {
            return salary;
        }

        public Department getDepartment() {
            return department;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", department=" + department +
                    '}';
        }
    }
    public static class Department {
        private String name;

        public Department(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Department that = (Department) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
