package io.reactivex.internal.queue;

import org.junit.Test;

import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;

public class QueueTest {

    @Test
    public void testQueueSize() {
        SpscLinkedArrayQueue<String> queue = new SpscLinkedArrayQueue<>(6);
        System.out.println("queue size=" + queue.consumerBuffer.length());
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        queue.offer("4");
        queue.offer("5");
        queue.offer("6");
        queue.offer("7");
        System.out.println("(1) queue size=" + queue.consumerBuffer.length());
        queue.offer("8");
        System.out.println("(2) queue size=" + queue.consumerBuffer.length());
        queue.offer("9");
        System.out.println("(3) queue size=" + queue.consumerBuffer.length());
        queue.offer("10");
        queue.offer("11");
        queue.offer("12");
        queue.offer("13");
        queue.offer("14");
        System.out.println("(4) queue size=" + queue.consumerBuffer.length());
        queue.offer("15");
        queue.offer("16");
        queue.offer("17");
        queue.offer("18");
        queue.offer("19");
        queue.offer("20");
        queue.offer("21");
        queue.offer("22");
        queue.offer("23");
        System.out.println("(4) queue size=" + queue.consumerBuffer.length());
    }

    @Test
    public void testQueueSize2() {
        SpscArrayQueue<String> queue = new SpscArrayQueue<>(6);
        System.out.println("queue size=" + queue.length());
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        queue.offer("4");
        queue.offer("5");
        queue.offer("6");
        queue.offer("7");
        System.out.println("(1) queue size=" + queue.length());
        System.out.println(queue.offer("8")); // true
        System.out.println("(2) queue size=" + queue.length());
        System.out.println(queue.offer("9")); // false
        System.out.println("(3) queue size=" + queue.length());
        queue.offer("10");
        queue.offer("11");
        queue.offer("12");
        queue.offer("13");
        queue.offer("14");
        System.out.println("(4) queue size=" + queue.length());
        queue.offer("15");
        queue.offer("16");
        queue.offer("17");
        queue.offer("18");
        queue.offer("19");
        queue.offer("20");
        queue.offer("21");
        queue.offer("22");
        System.out.println(queue.offer("23")); // false
        System.out.println("(4) queue size=" + queue.length());
    }
}
