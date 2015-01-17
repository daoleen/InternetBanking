package com.daoleen.banking.web.controller;

import com.daoleen.banking.web.infrastructure.FioTest;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Validate;
import com.daoleen.banking.web.infrastructure.annotations.Var;

/**
 * Created by alex on 1/17/15.
 */
public class TestController extends AbstractController {

    // http://localhost:8080/InternetBankingWeb/app/test
    // http://localhost:8080/InternetBankingWeb/app/test/index
    @Get
    public ViewResult index() {
        return viewResult.add("name", "Саня")
                .setViewName("test/index");
    }

    // http://localhost:8080/InternetBankingWeb/app/test/simpleparam?name=Alex
    @Get
    public ViewResult simpleparam(@Var("name") String name) {
        return viewResult.add("name", name)
                .setViewName("test/index");
    }

    // http://localhost:8080/InternetBankingWeb/app/test/someSimpleParams?firstname=Alex&lastname=Kozlov
    @Get
    public ViewResult someSimpleParams(@Var("firstname") String name, @Var("lastname") String lastName) {
        return viewResult.add("name", name + " " + lastName)
                .setViewName("test/index");
    }

    // http://localhost:8080/InternetBankingWeb/app/test/singleObjectParam?firstName=Alex&lastName=Kozlov&patrName=Valer
    @Get
    public ViewResult singleObjectParam(@Var("fio") FioTest fio) {
        return viewResult.add("name", fio.toString())
                .setViewName("test/index");
    }


    // http://localhost:8080/InternetBankingWeb/app/test/complexObjectParams?firstName=Alex&lastName=Kozlov&patrName=Valerevich&name=COMPLeX&age=14&pi=3.14&e=1.21&accept=true
    @Get
    public ViewResult complexObjectParams(@Var("name") String name, @Var("fio") FioTest fio, @Var("age") int age,
                                          @Var("pi") double pi, @Var("e") float e, @Var("accept") boolean accept) {
        return viewResult.add("name", String.format("name: %s, age: %d, username: %s, pi: %f, e: %f, accept: %s", name, age,
                        fio.toString(), pi, e, accept)
        ).setViewName("test/index");
    }

    // http://localhost:8080/InternetBankingWeb/app/test/validateObjectSuccess?firstName=Alex&lastName=Kozlov&patrName=Valer
    @Get
    public ViewResult validateObjectSuccess(@Validate @Var("fio") FioTest fio) {
        return viewResult.add("name", String.format("constraintFiolations: %s, fio: %s", validationErrors, fio))
                .setViewName("test/index");
    }

    // http://localhost:8080/InternetBankingWeb/app/test/validateObjectFail?firstName=Alex&lastName=Kozlov&patrName=Valerevich
    @Get
    public ViewResult validateObjectFail(@Validate @Var("fio") FioTest fio) {
        return viewResult.add("name", String.format("constraintFiolations: %s, fio: %s", validationErrors, fio))
                .setViewName("test/index");
    }
}
