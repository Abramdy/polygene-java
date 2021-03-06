/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.polygene.gradle.code

import org.gradle.api.artifacts.maven.MavenDeployer

class MavenMetadata
{
  static void applyTo( MavenDeployer mavenDeployer )
  {
    mavenDeployer.pom {
      project {
        url 'https://polygene.apache.org/'
        organization {
          name 'The Apache Software Foundation'
          url 'https://apache.org/'
        }
        inceptionYear '2007'
        issueManagement {
          system 'jira'
          url 'https://issues.apache.org/jira/browse/POLYGENE'
        }
        scm {
          url "https://github.com/apache/polygene-java"
          connection "scm:git:https://git-wip-us.apache.org/repos/asf/polygene-java.git"
          developerConnection "scm:git:https://git-wip-us.apache.org/repos/asf/polygene-java.git"
        }
        licenses {
          license {
            name 'Apache License, version 2.0.'
            url 'http://www.apache.org/licenses/LICENSE-2.0'
          }
        }
        mailingLists {
          mailingList {
            name 'Users List'
            subscribe 'users-subscribe@polygene.apache.org'
            unsubscribe 'users-unsubscribe@polygene.apache.org'
            post 'users@polygene.apache.org'
            archive 'https://mail-archives.apache.org/mod_mbox/polygene-users/'
            otherArchives {
              otherArchive 'https://www.apache.org/foundation/mailinglists.html#archives'
            }
          }
          mailingList {
            name 'Development List'
            subscribe 'dev-subscribe@polygene.apache.org'
            unsubscribe 'dev-unsubscribe@polygene.apache.org'
            post 'dev@polygene.apache.org'
            archive 'https://mail-archives.apache.org/mod_mbox/polygene-dev/'
            otherArchives {
              otherArchive 'https://www.apache.org/foundation/mailinglists.html#archives'
            }
          }
          mailingList {
            name 'Commits List'
            subscribe 'commits-subscribe@polygene.apache.org'
            unsubscribe 'commits-unsubscribe@polygene.apache.org'
            post 'commits@polygene.apache.org'
            archive 'https://mail-archives.apache.org/mod_mbox/polygene-commits/'
            otherArchives {
              otherArchive 'https://www.apache.org/foundation/mailinglists.html#archives'
            }
          }
        }
        developers {
          developer {
            id 'niclas@hedhman.org'
            name 'Niclas Hedhman'
            email 'niclas@hedhman.org'
            roles {
              role 'Core Team'
            }
            organizationUrl 'http://polygene.apache.org'
            timezone 'UTC+8'
          }
          developer {
            id 'rickardoberg'
            name 'Rickard \u00F6berg'
            email 'rickard.oberg@jayway.se'
            roles {
              role 'Core Team'
            }
            url 'http://www.neotechnology.com'
            organization 'Neo Technology AB'
            organizationUrl 'http://www.neotechnology.com'
            timezone 'UTC+8'
          }
          developer {
            id 'edward.yakop@gmail.com'
            name 'Edward Yakop'
            email 'efy@codedragons.com'
            roles {
              role 'Core Team'
            }
            organizationUrl 'http://polygene.apache.org'
            timezone 'UTC+8'
          }
          developer {
            id 'adreghiciu@gmail.com'
            name 'Alin Dreghiciu'
            email 'adreghiciu@codedragons.com'
            roles {
              role 'Core Team'
            }
            organizationUrl 'http://polygene.apache.org'
            timezone 'UTC+2'
          }
          developer {
            id 'mesirii'
            name 'Michael Hunger'
            email 'qi4j@jexp.de'
            roles {
              role 'Core Team'
            }
            timezone 'CET'
          }

          developer {
            id "muhdkamil"
            name "Muhd Kamil bin Mohd Baki"
            roles {
              role 'Platform Team'
            }
            timezone "UTC+8"
          }

          developer {
            id "ops4j@leangen.net"
            name "David Leangen"
            organization "Bioscene"
            email "ops4j@leangen.net"
            roles {
              role 'Platform Team'
            }
            timezone "UTC+9"
          }

          developer {
            id "sonny.gill@jayway.net"
            name "Sonny Gill"
            email "sonny.public@gmail.com"
            roles {
              role 'Community Team'
            }
            timezone "UTC+8"
          }

          developer {
            id "taowen"
            name "Tao Wen"
            organization ""
            email "taowen@gmail.com"
            roles {
              role 'Community Team'
            }
            timezone "UTC+8"
          }

          developer {
            id "thobe"
            name "Tobias Ivarsson"
            email "tobias@neotechnology.com"
            url "http://www.neotechnology.com"
            organization "NeoTechnology"
            organizationUrl "http://www.neotechnology.com"
            roles {
              role "Platform Team"
            }
            timezone "CET"
          }

          developer {
            id "boon"
            name "Lan Boon Ping"
            email "boonping81@gmail.com"
            roles {
              role 'Platform Team'
            }
            timezone "UTC+8"
          }

          developer {
            id "jan.kronquist@gmail.com"
            name "Jan Kronquist"
            email "jan.kronquist@gmail.com"
            organization "Jayway"
            roles {
              role 'Platform Team'
            }
            timezone "CET"
          }

          developer {
            id "nmwael"
            name "Nino Saturnino Martinez Vazquez Wael"
            roles {
              role 'Platform Team'
            }
            timezone "CET"
          }

          developer {
            id "peter@neubauer.se"
            name "Peter Neubauer"
            email "peter@neubauer.se"
            roles {
              role 'Platform Team'
            }
            timezone "CET"
          }

          developer {
            id "rwallace"
            name "Richard Wallace"
            email "rwallace@thewallacepack.net"
            roles {
              role 'Platform Team'
            }
            timezone "UTC-7"
          }

          developer {
            id "siannyhalim@gmail.com"
            name "Sianny Halim"
            email "siannyhalim@gmail.com"
            roles {
              role 'Platform Team'
            }
            timezone "UTC+8"
          }

          developer {
            id "paul@nosphere.org"
            name "Paul Merlin"
            email "paul@nosphere.org"
            roles {
              role 'Core Team'
            }
            timezone "CET"
          }

          developer {
            id "stas.dev+qi4j@gmail.com"
            name "Stanislav Muhametsin"
            email "stas.dev+qi4j@gmail.com"
            roles {
              role 'Platform Team'
            }
            timezone "UTC+2"
          }

          developer {
            id "tonny"
            name "Tonny Kohar"
            roles {
              role "Community Team"
            }
            email "tonny.kohar@gmail.com"
            timezone "UTC+7"
          }
        }
      }
    }
  }
}
