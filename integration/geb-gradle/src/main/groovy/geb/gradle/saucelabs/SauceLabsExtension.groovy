/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.gradle.saucelabs

import geb.gradle.cloud.BrowserSpec
import geb.gradle.cloud.CloudBrowsersExtension
import groovy.transform.InheritConstructors

@InheritConstructors
class SauceLabsExtension extends CloudBrowsersExtension {

    SauceConnect connect
    SauceAccount account
    boolean useTunnel = true

    void addExtensions() {
        browsers = project.container(BrowserSpec) { new BrowserSpec("saucelabs", it) }
        extensions.browsers = browsers
        account = new SauceAccount()
        connect = new SauceConnect(project, project.logger, account, project.configurations.sauceConnect, project.tasks.unpackSauceConnect.sauceConnectDir)
    }

    void task(Closure configuration) {
        extensions.browsers.all { BrowserSpec browser ->
            project.tasks["${browser.displayName}Test"].configure configuration
        }
    }

    void account(Closure configuration) {
        project.configure(account, configuration)
        configureTestTasksWith(account)
    }

    void connect(Closure configuration) {
        project.configure(connect, configuration)
        configureTestTasksWith(connect)
    }
}
