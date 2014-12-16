package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.BaseEntity;
import com.daoleen.banking.ejb.AbstractCrudBean;
import com.daoleen.banking.repository.local.CrudService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

/**
 * Created by alex on 12/7/14.
 */
@RunWith(Arquillian.class)
public abstract class AbstractIntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "InternetBankingEJB.jar")
                .addPackage(AbstractIntegrationTest.class.getPackage())
                .addPackage(AbstractCrudBean.class.getPackage())
                .addPackage(CrudService.class.getPackage())
                .addPackage(BaseEntity.class.getPackage())
                .addAsResource("META-INF/log4j.properties")
                .addAsResource("log4j.properties")
                .addAsResource("META-INF/logging.configuration")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("sql/city.sql")
                .addAsResource("META-INF/beans.xml");
        System.out.println("Jar contains: " + archive.toString(true));
        return archive;
    }

}
