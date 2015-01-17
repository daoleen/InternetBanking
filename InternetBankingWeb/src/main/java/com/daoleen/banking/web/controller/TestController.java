package com.daoleen.banking.web.controller;

import com.daoleen.banking.repository.remote.CityRepositoryRemote;
import com.daoleen.banking.web.infrastructure.FioTest;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Post;
import com.daoleen.banking.web.infrastructure.annotations.Validate;
import com.daoleen.banking.web.infrastructure.annotations.Var;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 * Created by alex on 1/17/15.
 */
@RequestScoped
public class TestController extends AbstractController {

    @EJB(lookup = "java:global/InternetBankingEJB/CityBean!com.daoleen.banking.repository.remote.CityRepositoryRemote")
    private CityRepositoryRemote cityRepository;


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


    // $ curl --data "name=Shurik&age=24" http://localhost:8080/InternetBankingWeb/app/test/postRequest
    @Post
    public ViewResult postRequest(@Var("name") String name, @Var("age") int age) {
        return viewResult.add("name", name + " " + age)
                .setViewName("test/index");
    }


    // $ curl --data "firstName=Alex&lastName=Kozlov&patrName=Valerevich&name=COMPLeX&age=14&pi=3.14&e=1.21&accept=true" http://localhost:8080/InternetBankingWeb/app/test/complexObjectParamsPost
    @Post
    public ViewResult complexObjectParamsPost(@Var("name") String name, @Var("fio") FioTest fio, @Var("age") int age,
                                              @Var("pi") double pi, @Var("e") float e, @Var("accept") boolean accept) {
        return viewResult.add("name", String.format("name: %s, age: %d, username: %s, pi: %f, e: %f, accept: %s", name, age,
                        fio.toString(), pi, e, accept)
        ).setViewName("test/index");
    }


    // http://localhost:8080/InternetBankingWeb/app/test/ejbInjectionTest
    @Get
    public ViewResult ejbInjectionTest() {
        StringBuilder cityString = new StringBuilder();
        cityRepository.findAll().parallelStream().forEach(c -> cityString.append(c.getName()).append(' '));
        return viewResult.add("name", cityString.toString())
                .setViewName("test/index");
    }
}
