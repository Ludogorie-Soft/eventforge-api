package com.eventforge.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class OrganisationPriorityCategory {

    private OrganisationPriorityCategory(){}

    public static final Set<String> staticCategories =
            new HashSet<>(Arrays.asList("здравеопазване", "образование", "спорт", "защита на човешките права", "работа с младежи", "насърчаване на дарителство/доброволчество", "работа с деца", "околна среда", "проблеми на жените", "социални услуги", "развитие на местни общности", "социално предприятие"));


}
