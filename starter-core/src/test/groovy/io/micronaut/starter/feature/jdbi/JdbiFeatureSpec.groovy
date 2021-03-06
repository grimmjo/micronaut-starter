package io.micronaut.starter.feature.jdbi

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class JdbiFeatureSpec extends BeanContextSpec {

    @Subject
    @Shared
    JdbiFeature jdbiFeature = beanContext.getBean(JdbiFeature)

    @Unroll("feature sql-jdbi works for application type: #applicationType")
    void "feature sql-jdbi works for every type of application type"(ApplicationType applicationType) {
        expect:
        jdbiFeature.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    @Unroll
    void 'dependency is included with maven and feature sql-jdbi for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sql-jdbi'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.configuration</groupId>
      <artifactId>micronaut-jdbi</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'dependency is included with gradle and feature sql-jdbi for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['sql-jdbi'], language)).render().toString()

        then:
        template.contains('implementation("io.micronaut.configuration:micronaut-jdbi")')

        where:
        language << Language.values()
    }
}
