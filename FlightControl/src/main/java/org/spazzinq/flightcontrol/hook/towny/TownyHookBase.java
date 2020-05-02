/*
 * This file is part of FlightControl, which is licensed under the MIT License.
 *
 * Copyright (c) 2020 Spazzinq
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

package org.spazzinq.flightcontrol.hook.towny;

import org.bukkit.entity.Player;
import org.spazzinq.flightcontrol.object.Check;
import org.spazzinq.flightcontrol.object.Hook;

public class TerritoryHookBase extends Hook implements Check {
    public boolean enable(Player p) {
        return isOwnTerritory(p) || isTrustedTerritory(p);
    }

   public boolean disable(Player p) {
        return false;
    }

<<<<<<< Updated upstream:FlightControl/src/main/java/org/spazzinq/flightcontrol/hook/towny/TownyHookBase.java
public class TownyHookBase extends Hook {
    public boolean townyOwn(Player p) {
=======
    public boolean isOwnTerritory(Player p) {
>>>>>>> Stashed changes:FlightControl/src/main/java/org/spazzinq/flightcontrol/hook/territory/TerritoryHookBase.java
        return false;
    }

    public boolean wartime() {
        return false;
    }
}
