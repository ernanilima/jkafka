package br.com.ernanilima.producer;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ImportAutoConfiguration
@SpringBootTest(classes = {ProducerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class ProducerTestIT {

}

