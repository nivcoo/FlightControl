/*
 * This file is part of FlightControl-parent, which is licensed under the MIT License
 *
 * Copyright (c) 2019 Spazzinq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.spazzinq.flightcontrol.api.objects;

import org.bukkit.plugin.Plugin;
import org.spazzinq.flightcontrol.api.APIManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class FlightListener {
    private List<HandlerMethod> handlers;
    private Plugin pl;

    // Initializer block
    public FlightListener() {
        handlers = new ArrayList<>();
        Method[] declaredMethods = getClass().getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(FlightEventHandler.class)) {
                if (method.getParameterCount() == 1) {
                    Class firstParameter = method.getParameterTypes()[0];
                    if (APIManager.getInstance().getEvents().contains(firstParameter)) {
                        handlers.add(new HandlerMethod(this, method));
                    } else new InstantiationException("Method " + method.getName() + " does not contain a specific Event parameter").printStackTrace();
                } else new InstantiationException("Method " + method.getName() + " contains more than one parameter").printStackTrace();
            }
        }
    }

    public FlightListener setPlugin(Plugin pl) {
        this.pl = pl;
        return this;
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Plugin getPlugin() {
        return pl;
    }
    public List<HandlerMethod> getHandlers() {
        return handlers;
    }
}