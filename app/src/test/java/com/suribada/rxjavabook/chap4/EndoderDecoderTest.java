package com.suribada.rxjavabook.chap4;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import io.reactivex.rxjava3.core.Single;

import static junit.framework.Assert.assertEquals;

/**
 * Created by lia on 2018-05-17.
 */
public class EndoderDecoderTest {

    public static String encode(String input, String enc) {
        try {
            return URLEncoder.encode(input, enc);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String decode(String input, String enc) {
        try {
            return URLDecoder.decode(input, enc);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static Single<String> encodeRx(String input, String enc) {
        return Single.fromCallable(() -> URLEncoder.encode(input, enc));
    }

    public static Single<String> decodeRx(String input, String enc) {
        return Single.fromCallable(() -> URLDecoder.decode(input, enc));
    }

    @Test
    public void testNormalEncodeDecode() {
        String input = "승마왕";
        String enc = "UTF-8";
        String encoded = encode(input, enc);
        String decoded = decode(encoded, enc);
        System.out.println("encoded=" + encoded);
        assertEquals(input, decoded);
    }

    /**
     * 테스트 실패 케이스
     */
    @Test
    public void testEncodeDecodeError() {
        String input = "승마왕";
        String enc = "UTF-64";
        String encoded = encode(input, enc);
        String decoded = decode(encoded, enc);
        System.out.println("encoded=" + encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testNormalEncodeDecodeRx() {
        String input = "승마왕";
        String enc = "UTF-8";
        String encoded = encodeRx(input, enc).onErrorReturnItem("인코딩 실패").blockingGet();
        String decoded = decodeRx(encoded, enc).onErrorReturnItem("디코딩 실패").blockingGet();
        System.out.println("encoded=" + encoded);
        assertEquals(input, decoded);
    }

    @Test
    public void testEncodeDecodeErrorRx() {
        String input = "승마왕";
        String enc = "UTF-64";
        String encoded = encodeRx(input, enc).onErrorReturnItem("인코딩 실패").blockingGet();
        String decoded = decodeRx(encoded, enc).onErrorReturnItem("디코딩 실패").blockingGet();
        System.out.println("encoded=" + encoded);
        assertEquals("인코딩 실패", encoded);
    }

}
