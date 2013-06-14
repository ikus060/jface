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
package com.patrikdufresne.jface.dialogs;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.jface.databinding.dialog.ValidationMessageProvider;

/**
 * This implementation of ValidationMessageProvider allow the user to specify a
 * default message when everything is ok.
 * 
 * @author Patrik Dufresne
 * 
 */
public class DefaultValidationMessageProvider extends ValidationMessageProvider {

    /**
     * The default message value.
     */
    private String defaultMessage;

    /**
     * Create a new validation message provider with default message.
     * 
     * @param defaultMessage
     */
    public DefaultValidationMessageProvider(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * This implementation return the default message if everything is ok.
     */
    @Override
    public String getMessage(ValidationStatusProvider statusProvider) {
        String message = super.getMessage(statusProvider);
        if (message == null || message.isEmpty()) {
            return this.defaultMessage;
        }
        return message;
    }

}
