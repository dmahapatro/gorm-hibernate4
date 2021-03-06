/*
 * Copyright 2015 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.gorm.tests

import grails.persistence.Entity
import org.grails.orm.hibernate.AbstractGrailsHibernateTests
import org.grails.orm.hibernate.GormSpec
import org.junit.Test

import java.sql.ResultSet


/**
 * @author Graeme Rocher
 */
class DefaultMappingWithEmbeddedTests extends AbstractGrailsHibernateTests {

    @Test
    void testDefaultMappingWithEmbedded() {
         new DefaultMappingWithEmbedded(name:"Bob", my: new MyEmbedded(good: true)).save(flush:true)

        def resultSet = session.connection().prepareStatement('select my_good from dmwe').executeQuery()

        assert resultSet.next()
        assert resultSet.getString(1) == 'Y'
    }

    @Override
    protected getDomainClasses() {
        return [DefaultMappingWithEmbedded]
    }

    @Override
    protected ConfigObject getConfig() {
        new ConfigSlurper().parse('''
grails.gorm.default.mapping = {
     'user-type'(type: org.hibernate.type.YesNoType, class: Boolean)
}
''')
    }
}

@Entity
class DefaultMappingWithEmbedded {
    Long id
    Long version
    String name
    MyEmbedded my

    static mapping = {
        table "dmwe"
    }

    static embedded = ['my']
}

class MyEmbedded  {
    Boolean good
}
