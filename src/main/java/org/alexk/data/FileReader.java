package org.alexk.data;

import lombok.val;
import org.alexk.business.models.BaseProduct;
import org.alexk.business.models.MenuItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileReader {
    private static final String DIVIDER = ",| ,";

    private FileReader() {
    }

    public static List<MenuItem> getProducts(InputStream inputStream) throws Exception {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().skip(1)
                .map(x -> {
                    val result = x.split(DIVIDER);
                    result[0] = result[0]
                            .replaceAll("\"\"", "\"")
                            .replaceAll("^\"", "")
                            .replaceAll(" *\"$", "");

                    return result;
                })
                .filter(distinctByKey(arr -> arr[0]))
                .map(x -> mapToProduct(x))
                .collect(Collectors.toList());
    }

    private static BaseProduct mapToProduct(String[] arr) {
        val product = new BaseProduct();

        product.updateId();
        product.setTitle(arr[0]);
        product.setRating(Double.parseDouble(arr[1]));
        product.setCalories(Double.parseDouble(arr[2]));
        product.setProtein(Double.parseDouble(arr[3]));
        product.setFat(Double.parseDouble(arr[4]));
        product.setSodium(Double.parseDouble(arr[5]));
        product.setPrice(Double.parseDouble(arr[6]));

        return product;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
