package net.dkahn.starter.tools.domain.converter;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dev on 17/11/16.
 */
@Converter
public class IntArrayToStringConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        return attribute == null ? null : join(attribute,",");
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData))
            return new ArrayList<>();

        try (Stream<String> stream = Arrays.stream(dbData.split(","))) {
            return stream.map(Integer::parseInt).collect(Collectors.toList());
        }
    }


    private  static String join(Collection collection, String separator) {
        StringBuffer res = new StringBuffer();

        for(Iterator it = collection.iterator(); it.hasNext(); res.append(it.next())) {
            if(res.length() != 0) {
                res.append(separator);
            }
        }

        return res.toString();
    }

}
