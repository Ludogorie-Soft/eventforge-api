package com.eventforge.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Constant {

    private Constant(){}

    public static final Set<String> staticCategories =
            new HashSet<>(Arrays.asList("здравеопазване", "образование", "спорт", "защита на човешките права", "работа с младежи", "насърчаване на дарителство/доброволчество", "работа с деца", "околна среда", "проблеми на жените", "социални услуги", "развитие на местни общности", "социално предприятие"));


    public static final Set<String> staticSubjects = new HashSet<>(Arrays.asList("Общи запитвания" ,"Грешка в данните на събитие/организация","Проблем с регистрацията",
            "Мобилна версия","Заявка за обмен на връзки","Достъпът до акаунта ми е забранен"));

}
