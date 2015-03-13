/*
   Copyright (c) 2014,2015 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.tooling.nativetools.client.primitive;

import java.util.Collection;

import com.ait.tooling.nativetools.client.NObjectBaseJSO;
import com.ait.tooling.nativetools.client.NUtils;

/**
 * Simple, super-fast minimal native Map that by default uses a String as a key, but does not fully implement the Map interface
 * 
 * For our purposes, in benchmarking, this is 50-60% faster than HashMap
 */
public final class NFastStringMap<V>
{
    private final NFastStringMapJSO<V> m_jso;

    public NFastStringMap()
    {
        m_jso = NFastStringMapJSO.make().cast();
    }

    /**
     * Add <key, value> to the map.
     * @param key
     * @param value
     */
    public final void put(final String key, final V value)
    {
        m_jso.put(NUtils.doKeyRepair(key, true), value);
    }

    /**
     * Get the value based on the key passed in.
     * @param key
     * @return
     */
    public final V get(final String key)
    {
        return m_jso.get(NUtils.doKeyRepair(key, true));
    }

    /**
     * Remove the value based on the key passed in as argument.
     * @param key
     */
    public final void remove(final String key)
    {
        m_jso.remove(key);
    }

    /**
     * Returns true if the map has a value for the specified key
     * @param key
     */
    public final boolean isDefined(final String key)
    {
        return m_jso.isDefined(key);
    }

    public final boolean isNull(final String key)
    {
        return m_jso.isNull(key);
    }

    /**
     * Returns the number of key-value mappings in this map
     */
    public final int size()
    {
        return m_jso.size();
    }

    public final Collection<String> keys()
    {
        return m_jso.keys();
    }

    /**
     * Returns true if this map contains no key-value mappings
     */
    public final boolean isEmpty()
    {
        return m_jso.isEmpty();
    }

    private static final class NFastStringMapJSO<V> extends NObjectBaseJSO<NFastStringMapJSO<V>>
    {
        protected NFastStringMapJSO()
        {
        }

        private static final NFastStringMapJSO<?> make()
        {
            return NObjectBaseJSO.createNObjectBaseJSO().cast();
        }

        public final native void put(String key, V value)
        /*-{
			this[key] = value;
        }-*/;

        public final native V get(String key)
        /*-{
			return this[key];
        }-*/;
    }
}