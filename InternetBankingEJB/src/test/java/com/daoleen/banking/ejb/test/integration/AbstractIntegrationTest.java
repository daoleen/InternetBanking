package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.beans.PasswordEncoderProducer;
import com.daoleen.banking.beans.qualifiers.UserPasswordEncoder;
import com.daoleen.banking.converter.MoneyReservationStatusConverter;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.ejb.AbstractBean;
import com.daoleen.banking.enums.MoneyReservationStatus;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.repository.Repository;
import com.daoleen.banking.repository.local.BankRepository;
import com.daoleen.banking.repository.remote.BankRepositoryRemote;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Created by alex on 12/7/14.
 */
@RunWith(Arquillian.class)
public abstract class AbstractIntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "InternetBankingEJB.jar")
                .addPackage(AbstractIntegrationTest.class.getPackage())
                .addPackage(AbstractBean.class.getPackage())
                .addPackage(Repository.class.getPackage())
                .addPackage(BankRepository.class.getPackage())
                .addPackage(BankRepositoryRemote.class.getPackage())
                .addPackage(Identifiable.class.getPackage())
                .addPackage(NoEnoughMoneyException.class.getPackage())
                .addPackage(MoneyReservationStatusConverter.class.getPackage())
                .addPackage(MoneyReservationStatus.class.getPackage())

                .addPackage(UserPasswordEncoder.class.getPackage())
                .addPackage(org.springframework.security.crypto.password.PasswordEncoder.class.getPackage())
                .addPackage(PasswordEncoderProducer.class.getPackage())
                .addPackage(StandardPasswordEncoder.class.getPackage())
                .addPackage(org.springframework.security.crypto.keygen.BytesKeyGenerator.class.getPackage())
                .addPackage(org.springframework.security.crypto.codec.Utf8.class.getPackage())
                .addPackage(org.springframework.security.crypto.util.EncodingUtils.class.getPackage())

                .addAsResource("META-INF/log4j.properties")
                .addAsResource("log4j.properties")
                .addAsResource("META-INF/logging.configuration")
                .addAsResource("META-INF/persistence.xml")
//                .addAsResource("sql/data.sql")
                .addAsResource("sql/create.sql")
                .addAsResource("sql/drop.sql")
                .addAsResource("sql/insert.sql")
                .addAsResource("META-INF/beans.xml");
        System.out.println("Jar contains: " + archive.toString(true));
        return archive;
    }
}
