package net.dkahn.starter.apps.webapps.common.controller.utils;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class I18nEnumTranslate {

    static public void create(ModelMap map, Enum[]... enums) {

        ArrayList<Enum> allI18nEnumNeeded = Lists.newArrayList();

        for (Enum[] anEnum : enums) {
            allI18nEnumNeeded.addAll(Arrays.asList(anEnum));
        }

        map.addAttribute("allI18nEnumNeeded",allI18nEnumNeeded);

    }
}
