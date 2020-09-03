package com.suribada.rxjavabook.chap8;

import androidx.core.util.Pair;

import com.suribada.rxjavabook.SystemClock;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FlatMapTest {

    @Test
    public void mapAndFlatten() {
        getDepartments()
                .map(department -> getOutGoings(department)) // (1)
                .subscribe(System.out::println); // (2)
        SystemClock.sleep(2000);
    }

    @Test
    public void flatMapWithProblem() {
        getDepartments()
                .flatMap(department -> getOutGoings4(department)) // (1)
                .subscribe(System.out::println); // (2)
        SystemClock.sleep(2000);
    }

    @Test
    public void flatMap1() {
        getDepartments()
                .flatMap(department -> getOutGoings(department)) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    /**
     * 스레드 확인
     */
    @Test
    public void flatMap2() {
        getDepartments()
                .subscribeOn(Schedulers.io()) // (1)
                .doOnNext(value -> System.out.println("upstream thread=" + Thread.currentThread().getName()))
                .flatMap(department ->
                     getOutGoings(department)
                            .subscribeOn(Schedulers.io()) // (2)
                            .doOnNext(value -> System.out.println("inner thread=" + Thread.currentThread().getName() + ", value=" + value))
                ).subscribe(value -> System.out.println("observer thread=" + Thread.currentThread().getName() + ", value=" + value));
        SystemClock.sleep(2000);
    }

    @Test
    public void flatMap3() {
        getDepartments()
                .subscribeOn(Schedulers.io())
                .flatMap(department ->
                        getOutGoings(department)
                                .subscribeOn(Schedulers.io()),
                        40) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    private Observable<Department> getDepartments() {
        return Observable.just(new Department("안드로이드팀"), new Department("iOS팀"),
                new Department("FE팀"), new Department("서버팀"));
    }

    int i = 0;

    private Observable<OutGoing> getOutGoings(Department department) {
        return Observable.just(new OutGoing(++i), new OutGoing(++i),
                new OutGoing(++i), new OutGoing(++i), new OutGoing(++i));
    }

    @Test
    public void flatMap_withError() {
        getDepartments()
                .flatMap(department -> getOutGoings2(department), true)
                .subscribe(System.out::println,
                        e -> System.err.println(e));
        SystemClock.sleep(2000);
    }

    /**
     * 에러가 1개 발생하는 경우
     */
    private Observable<OutGoing> getOutGoings2(Department department) {
        switch (department.getName()) {
            case "안드로이드팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(2000));
                    emitter.onError(new IllegalAccessException());
                });
            case "iOS팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
            case "FE팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(8000));
                    emitter.onComplete();
                });
            case "서버팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
        }
        throw new IllegalArgumentException();
    }

    /**
     * CompositeException 발생
     */
    @Test
    public void flatMap_withError2() {
        getDepartments()
                .flatMap(department -> getOutGoings3(department), true)
                .subscribe(System.out::println,
                        e -> System.err.println(e));
        SystemClock.sleep(2000);
    }

    /**
     * 에러가 여러 개 발생하는 경우
     */
    private Observable<OutGoing> getOutGoings3(Department department) {
        switch (department.getName()) {
            case "안드로이드팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(2000));
                    emitter.onError(new IllegalAccessException());
                });
            case "iOS팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
            case "FE팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(8000));
                    emitter.onError(new IllegalStateException());
                });
            case "서버팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onError(new IllegalStateException());
                });
        }
        throw new IllegalArgumentException();
    }

    /**
     * onComplete를 하지 않은 Observalbe이 있음
     */
    private Observable<OutGoing> getOutGoings4(Department department) {
        switch (department.getName()) {
            case "안드로이드팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(2000));
                });
            case "iOS팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
            case "FE팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(8000));
                    emitter.onComplete();
                });
            case "서버팀":
                return Observable.create(emitter -> {
                    emitter.onNext(new OutGoing(3000));
                    emitter.onComplete();
                });
        }
        throw new IllegalArgumentException();
    }

    @Test
    public void flatMap_withResultSelector() {
        getDepartments()
                .flatMap(department -> getOutGoings(department),
                        (department, outgoing) -> Pair.create(department.getName(), outgoing.getAmount())) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void flatMap_withResultSelector2() {
        getDepartments()
                .flatMap(department -> getOutGoings(department).map(outgoing -> Pair.create(department.getName(), outgoing.getAmount()))) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void testGugudan() {
        Observable.range(2, 8)
                .flatMap(row -> Observable.range(1, 9)
                        .map(col -> String.format("%d x %d = %d", row, col, row * col)))
                .subscribe(System.out::println);

    }

    @Test
    public void testGugudan2() {
        Observable.range(2, 8)
                .flatMap(row -> Observable.range(1, 9),
                        (row, col) -> String.format("%d x %d = %d",  row, col, row * col))
                .subscribe(System.out::println);
    }
    
    @Test
    public void flatMap_withErrorObservable() {
        getPrograms()
                .subscribeOn(Schedulers.io())
                .flatMap(program -> playVideo(program), // (1)
                        e -> playVideo(new Program("문제 발생")), // (2)
                        () -> playVideo(new Program("애국가"))) // (3)
                .subscribe(System.out::println);
                        
    }

    private Observable<Video> playVideo(Program program) {
        return Observable.just(new Video(program));
    }

    private Observable<Program> getPrograms() {
        return Observable.just(new Program("가족입니다"), new Program("어벤저스"), new Program("가짜사나이"));
    }

    @Test
    public void flatMap_withErrorObservable2() {
        getPrograms2()
                .subscribeOn(Schedulers.io())
                .flatMap(program -> playVideo(program),
                        e -> playVideo(new Program("문제 발생")),
                        () -> playVideo(new Program("애국가")))
                .subscribe(System.out::println);

    }

    private Observable<Program> getPrograms2() {
        return Observable.create(emitter -> {
            emitter.onNext(new Program("가족입니다"));
            emitter.onError(new IllegalArgumentException());
        });
    }

    @Test
    public void flatMapIterable() {
        long start = System.currentTimeMillis();
        getStudents("fashion", "cooking")
                .subscribeOn(Schedulers.io())
                .flatMapIterable(students -> students) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    @Test
    public void flatMap_needless() {
        long start = System.currentTimeMillis();
        getStudents("fashion", "cooking")
                .subscribeOn(Schedulers.io())
                .flatMap(students -> Observable.fromIterable(students)) // (1)
                .subscribe(System.out::println);
        SystemClock.sleep(2000);
    }

    private Observable<List<Student>> getStudents(String... classes) {
        return Observable.just(Arrays.asList(new Student("노재춘"), new Student("강사룡")),
                Arrays.asList(new Student("권태환"), new Student("이효근")));
    }

}
