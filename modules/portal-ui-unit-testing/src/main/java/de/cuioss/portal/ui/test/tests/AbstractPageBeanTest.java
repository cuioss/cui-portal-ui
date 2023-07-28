package de.cuioss.portal.ui.test.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.commons.support.AnnotationSupport;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.TestContract;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.contract.BeanPropertyContractImpl;
import de.cuioss.test.valueobjects.contract.EqualsAndHashcodeContractImpl;
import de.cuioss.test.valueobjects.contract.ReflectionUtil;
import de.cuioss.test.valueobjects.contract.SerializableContractImpl;
import de.cuioss.test.valueobjects.generator.TypedGeneratorRegistry;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.property.PropertyMetadata;
import de.cuioss.test.valueobjects.util.GeneratorAnnotationHelper;
import de.cuioss.test.valueobjects.util.GeneratorRegistry;
import de.cuioss.test.valueobjects.util.ReflectionHelper;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for testing Page / Backing beans.
 * <p>
 * It uses {@link EnablePortalUiEnvironment} for environment setup and acts as
 * {@link JsfEnvironmentConsumer}.
 * </p>
 * <p>
 * An instance of {@link PortalTestConfiguration} is accessible as protected
 * field {@code configuration}
 * </p>
 * <p>
 * It implements to actual tests:
 * <ul>
 * <li>{@code verifyBeanProperties()}: It will be run if the test-class is
 * annotated with {@link VerifyBeanProperty}. For further configuration see
 * {link de.icw.cui.test.valueobjects.api}</li>
 * <li>{@code  verifyObjectContracts()}: Verifies the contract of
 * {@link Object#equals(Object)}, {@link Object#hashCode()},
 * {@link Object#toString()} and {@link Serializable} by serializing /
 * deserializing the object. <em>Caution:</em> it will check less detailed
 * compared to previous variants. If you want a full blown test use
 * {@link ValueObjectTest} directly. If you want to adapt the test you must
 * override it. Previous annotation will not work.</li>
 * <ul>
 * </p>
 *
 * @author Oliver Wolff
 *
 * @param <T> the actual bean under test, must be at least {@link Serializable}
 */
@EnableGeneratorController
public abstract class AbstractPageBeanTest<T extends Serializable>
        implements JsfEnvironmentConsumer, ShouldBeNotNull<T>, GeneratorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPageBeanTest.class);

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @PortalConfigurationSource
    protected PortalTestConfiguration configuration;

    @Test
    protected void verifyBeanProperties() {
        logger.debug(() -> "Checking de.icw.cui.test.valueobjects.api.contracts.VerifyBeanProperty");
        if (!AnnotationSupport.findAnnotation(getClass(), VerifyBeanProperty.class).isPresent()) {
            logger.info(
                    () -> "In order to test Bean-Properties you need to annotate you test class with de.icw.cui.test.valueobjects.api.contracts.VerifyBeanProperty");
            return;
        }

        logger.debug(() -> "Setup Generators");
        GeneratorAnnotationHelper.handleGeneratorsForTestClass(getClass(), registerAdditionalGenerators());

        logger.debug(() -> "Gathering metadata");
        var targetBeanClass = getUnproxiedClass(getUnderTest().getClass());

        logger.debug(() -> "Using targetBeanClass " + targetBeanClass);

        List<PropertyMetadata> metadata = ReflectionHelper.handlePropertyMetadata(getClass(), targetBeanClass);

        logger.debug(() -> "Using metadata " + metadata);

        Optional<TestContract<T>> contract = BeanPropertyContractImpl.createBeanPropertyTestContract(targetBeanClass,
                getClass(), metadata);
        if (!contract.isPresent()) {
            logger.debug(() -> "No bean properties configured");
            TypedGeneratorRegistry.clear();
            return;
        }
        logger.debug(() -> "Executing bean-property-tests ");
        contract.get().assertContract();
        logger.info(() -> "Bean test run successfully, no problems found");
        TypedGeneratorRegistry.clear();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getUnproxiedClass(Class<? extends Serializable> class1) {
        if (isProxied(class1)) {
            return (Class<T>) class1.getSuperclass();
        }
        return (Class<T>) class1;
    }

    private boolean isProxied(Class<? extends Serializable> currentClass) {
        {
            if ((currentClass == null) || (currentClass.getSuperclass() == null)) {
                return false;
            }

            var name = currentClass.getName();
            return name.startsWith(currentClass.getSuperclass().getName()) && (name.contains("$$") // CDI
                    || name.contains("_ClientProxy") // Quarkus
                    || name.contains("$HibernateProxy$")); // Hibernate
        }
    }

    @Test
    protected void verifyObjectContracts() {
        logger.debug(() -> "Verifying Object-Contracts");
        var underTest = getUnderTest();

        logger.debug(() -> "Verifying EqualsAndHashCode");
        EqualsAndHashcodeContractImpl.assertBasicContractOnEquals(underTest);
        EqualsAndHashcodeContractImpl.assertBasicContractOnHashCode(underTest);

        logger.debug(() -> "Verifying Serializable");
        SerializableContractImpl.serializeAndDeserialize(underTest);

        logger.debug(() -> "Verifying ToString");
        ReflectionUtil.assertToStringMethodIsOverriden(underTest.getClass());
        assertNotNull(underTest.toString(), "toString must not return 'null'");
    }
}
