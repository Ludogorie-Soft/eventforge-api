package com.eventforge.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;

@Getter
public class OrganisationPriorityCategory {

    public static HashSet<String> categories = new HashSet<>(Arrays.asList("здравеопазване", "образование", "спорт", "защита на човешките права", "работа с младежи", "насърчаване на дарителство/доброволчество", "работа с деца", "околна среда", "проблеми на жените", "социални услуги", "развитие на местни общности", "социално предприятие", "друго"));

    public static void addNewOrganisationPriorityCategory(String category) {
        categories.add(category);
    }

}
