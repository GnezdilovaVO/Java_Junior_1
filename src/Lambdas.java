import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Stream;

public class Lambdas {
    static int length(String arg) {
        return arg.length();
    }
    public static void main(String[] args) {
//        comparatorDemo();
//        methodReferences();
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            integers.add(ThreadLocalRandom.current().nextInt(100));
        }

//        Stream<Integer> intStream = integers.stream();
//        Stream<Integer> filtered = intStream.filter(x -> x < 50);
//        Stream<Integer> mapped = filtered.map(x -> x*2);
//        mapped.forEach(x -> System.out.println(x));

        integers.stream()
//                .parallel()
                .filter(x -> x<50)
                .map(x -> x*2)
//                .sequential()
                .forEach(x -> System.out.println(x));

//        for (Integer integer: integers) {
//            if (integer < 50) {
//                System.out.println(integer*2);
//            }
//        }

        List<Integer> list = Stream.generate(() -> ThreadLocalRandom.current().nextInt(100))
                .limit(5)
                .toList();




    }

    private static void methodReferences() {
        Function<String, Integer> stringLengthExtractor = String::length;
        System.out.println(stringLengthExtractor.apply("hello"));

//        Predicate<String> javaTester = str->"java".equals(str);
        Predicate<String> javaTester = "java"::equals;
        System.out.println(javaTester.test("hello"));
        System.out.println(javaTester.test("java"));
    }

    private static void comparatorDemo() {
        List<Integer> integers = new ArrayList<>();
        Random random = new Random();
        integers.add(random.nextInt(101));
        integers.add(random.nextInt(101));
        integers.add(random.nextInt(101));
        integers.add(random.nextInt(101));
        integers.add(random.nextInt(101));
        System.out.println(integers);

        //Сортировка от большого
//        Collections.sort(integers);
//        Collections.reverse(integers);
//        System.out.println(integers);

        //Сортировка от четных
//        Comparator<Integer> customComparator = (x, y) -> {
//          if (x % 2 == 0 && y % 2 != 0) {
//              return -1;
//          } else if (x % 2 != 0 && y % 2 == 0) {
//              return 1;
//          }
////          return x - y;
//          return Integer.compare(x, y);
//        };
//        Collections.sort(integers, (x, y) -> {
//            if (x % 2 == 0 && y % 2 != 0) {
//                return -1;
//            } else if (x % 2 != 0 && y % 2 == 0) {
//                return 1;
//            }
////          return x - y;
//            return Integer.compare(x, y);
//        });
        Collections.sort(integers, (x, y) -> compareIntegers(x, y));
        Collections.sort(integers, Lambdas::compareIntegers);
        System.out.println(integers);
    }

    private static int compareIntegers(int x, int y){
        if (x % 2 == 0 && y % 2 != 0) {
            return -1;
        } else if (x % 2 != 0 && y % 2 == 0) {
            return 1;
        }
//          return x - y;
        return Integer.compare(x, y);
    }
    static void lambdasIntro() {
        SquareInterface squareInterface = x -> x * x;
        System.out.println(squareInterface.square(5));

        UnaryOperator<Integer> suare = x -> x * x;

        Function<String, Integer> lengthExtractor = str -> str.length();
        System.out.println(lengthExtractor.apply("Hello"));

        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("Hello, world!");

        Supplier<Integer> ramdomer  = () -> new Random().nextInt(101);
        System.out.println(ramdomer.get());

        Runnable runnable = () -> printer.accept(String.valueOf(ramdomer.get()));
        runnable.run();

        Predicate<Integer> evenTester = (n) -> n%2 == 0;
        System.out.println(evenTester.test(5));
    }

    interface Foo {
        void foo();
    }
    interface SquareInterface{
        int square(int x);
    }
}
