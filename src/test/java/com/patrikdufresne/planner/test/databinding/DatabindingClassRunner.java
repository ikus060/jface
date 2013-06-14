/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrikdufresne.planner.test.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class DatabindingClassRunner extends BlockJUnit4ClassRunner {

    public DatabindingClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(final RunNotifier notifier) {
        Display display = Display.getDefault();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
            @Override
            public void run() {
                DatabindingClassRunner.super.run(notifier);
            }
        });
    }
}
