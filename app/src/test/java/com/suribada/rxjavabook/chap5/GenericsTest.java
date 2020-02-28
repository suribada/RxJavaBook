package com.suribada.rxjavabook.chap5;

import com.suribada.rxjavabook.model.Animal;
import com.suribada.rxjavabook.model.BullDog;
import com.suribada.rxjavabook.model.Dog;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

/**
 * Created by lia on 2018-07-17.
 */

public class GenericsTest {

    @Test
    public void testIterate() {
        List<Long> values = Arrays.asList(1L, 100L, 1000L, 100000000000L);
        Observable.fromIterable(values)
                .subscribe(System.out::println);
        Observable.<Number>fromIterable(values)
                .subscribe(value -> System.out.println(value.intValue()));
    }

    @Test
    public void testCreate() {
        // Observable<Object>로 생성됨
        Observable.create(emitter -> {
            emitter.onNext("char");
            emitter.onNext(new Integer(100));
        }).subscribe(value -> System.out.println(value.toString() + "," + value.getClass().getSimpleName()));

        // 타입 추론은 됨
        Observable<String> obs1 = Observable.create(emitter -> {
            emitter.onNext("char");
            //emitter.onNext(new Integer(100));
        });
        obs1.subscribe(value -> System.out.println(value.toString() + "," + value.getClass().getSimpleName()));
    }

    @Test
    public void assign() {
        Number number = 1;
        //Integer a = number; // (1) 컴파일 에러
        Integer b = 7;
        Number number2 = b; // (2)
        List<Number> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(1.1f);

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        //List<Number> numbers = integers; // (1)
        List<? extends Number> numbers2 = integers; // (2)
    }

    @Test
    public void lowerBound() {
        List<Object> objects = new ArrayList<>();
        List<Animal> animals = new ArrayList<>();
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog("happy"));
        dogs.add(new Dog("marry"));
        for (Dog each : dogs) { // (1) 시작
            objects.add(each);
        } // (1) 끝
        for (Dog each : dogs) { // (2) 시작
            animals.add(each);
        } // (2) 끝
        objects.clear();
        animals.clear();
        addDogs(dogs, objects);
        addDogs(dogs, animals);
    }

    private void addDogs(List<Dog> dogs, List<? super Dog> outputs) {
        for (Dog each : dogs) {
            outputs.add(each);
        }
    }

    @Test
    public void lowerBoundAssign() {
        List<Object> objects = new ArrayList<>();
        List<Animal> animals = new ArrayList<>();
        List<Dog> dogs = new ArrayList<>();
        List<BullDog> bulldogs = new ArrayList<>();
        List<? super Dog> dogs2 = new ArrayList<>();
        //dogs = objects; // (1) (시작) 컴파일 에러
        //dogs = animals;
        //dogs = bulldogs; // (1) 끝
        dogs2 = objects; // (2) 시작
        dogs2 = animals;
        dogs2 = dogs; // (2) 끝
        //dogs2 = bulldogs; // (3) 컴파일 에러
    }

    @Test
    public void assignList() {
        List<? extends Dog> dogs = new ArrayList<>();
        List<? super Dog> dogs2 = new ArrayList<>();
        List<?> objects = dogs;
        List<?> objects2 = dogs2;
    }

    @Test
    public void map() {
        Observable<Dog> dogs = Observable.just(new Dog("happy"), new Dog("그냥개"), new Dog("모짜렐라"));
        Observable<CharSequence> names1 = dogs.map(dog -> dog.name()); // (1)
        Function<Dog, CharSequence> mapFunction2 = new Function<Dog, CharSequence>() { // (2)
            @Override
            public CharSequence apply(Dog input) throws Exception {
                return input.name();
            }
        };
        Observable<CharSequence> names2 = dogs.map(mapFunction2); // (2)

        Function<Animal, CharSequence> mapFunction3 = new Function<Animal, CharSequence>() {
            @Override
            public CharSequence apply(Animal input) throws Exception {
                return "just animal";
            }
        };
        Observable<CharSequence> names3 = dogs.map(mapFunction3); // (3)

        Function<Animal, String> mapFunction4 = new Function<Animal, String>() {
            @Override
            public String apply(Animal input) throws Exception {
                return "just animal";
            }
        };
        Observable<CharSequence> names4 = dogs.map(mapFunction4); // (4)

        Function<BullDog, CharSequence> mapFunction5 = new Function<BullDog, CharSequence>() {
            @Override
            public CharSequence apply(BullDog input) throws Exception {
                return input.name();
            }
        };
        //Observable<CharSequence> names5 = dogs.map(mapFunction5); // (5) 컴파일 에러
    }

    @Test
    public void inferType() {
        Observable.just("박응주", "하동현", "신중섭"); // (1)
        Observable.just(1, "최희탁", "이창신", new Dog("happy")); // (2)
    }

    @Test
    public void inferType2() {
        Observable.<CharSequence>just("박응주", "하동현", "신중섭"); // (1)
        Observable<CharSequence> reviewers = Observable.just("박응주", "하동현", "신중섭"); // ()
    }

    @Test
    public void inferType3() {
        Observable.create(emitter -> {
            emitter.onNext("알리바바");
            emitter.onNext("40인의 도둑");
            emitter.onComplete();
        }).subscribe(value -> {
            //System.out.println(value.length()); // 컴파일 에러
        });
        // option 1
        Observable.<String>create(emitter -> {
            emitter.onNext("알리바바");
            emitter.onNext("40인의 도둑");
            emitter.onComplete();
        }).subscribe(value -> {
            System.out.println(value.length());
        });
        // option 2
        Observable<String> persons = Observable.create(emitter -> {
            emitter.onNext("알리바바");
            emitter.onNext("40인의 도둑");
            emitter.onComplete();
        });
        persons.subscribe(value -> {
            System.out.println(value.length());
        });
    }

}
