package de.cuioss.portal.ui.api.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.annotation.RequestParameterMap;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@EnableAutoWeld
@AddBeanClasses(ParamProducer.class)
@ActivateScopes(RequestScoped.class)
class ParamProducerTest implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Produces
    @RequestParameterMap
    Map<String, String> requestParamMap;

    @Inject
    @Param(name = "string")
    private Provider<String> stringParam;

    @Inject
    @Param(name = "string", required = true)
    private Provider<String> requiredStringParam;

    @Inject
    @Param(name = "string", required = true, requiredMessage = "msg")
    private Provider<String> requiredStringParamWithMsg;

    @Inject
    @Param(name = "int")
    private Provider<Integer> intParam;

    @Inject
    @Param(name = "bool")
    private Provider<Boolean> boolParam;

    @BeforeEach
    void beforeEach() {
        requestParamMap = new HashMap<>();
    }

    @Test
    void stringParamTest() {
        requestParamMap.put("string", "value");
        String result = stringParam.get();
        assertEquals("value", result);
    }

    @Test
    void boolParamTest() {
        requestParamMap.put("bool", "true");
        boolean result = boolParam.get();
        assertTrue(result);
    }

    @Test
    void bool1ParamTest() {
        requestParamMap.put("bool", "1");
        boolean result = boolParam.get();
        assertFalse(result);
    }

    @Test
    void intParamTest() {
        requestParamMap.put("int", "42");
        int result = intParam.get();
        assertEquals(42, result);
    }

    @Test
    void missingParamTest() {
        String result = stringParam.get();
        assertNull(result);
    }

    @Test
    void requiredStringParamTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, requiredStringParam::get);
        assertEquals("Request parameter missing: string", ex.getMessage());
    }

    @Test
    void requiredStringParamWithMsgTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, requiredStringParamWithMsg::get);
        assertEquals("msg", ex.getMessage());
    }
}
