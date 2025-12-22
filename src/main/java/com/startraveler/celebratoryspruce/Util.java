package com.startraveler.celebratoryspruce;


import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {



    public static <A, B, R> Stream<R> zip(Stream<A> stream1, Stream<B> stream2, BiFunction<? super A, ? super B, ? extends R> zipper) {
        Iterator<A> iterator1 = stream1.iterator();
        Iterator<B> iterator2 = stream2.iterator();
        Iterable<R> iterable = () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(iterator1.next(), iterator2.next());
            }
        };
        return StreamSupport.stream(iterable.spliterator(), false).onClose(stream1::close)
                .onClose(stream2::close);
    }

}
