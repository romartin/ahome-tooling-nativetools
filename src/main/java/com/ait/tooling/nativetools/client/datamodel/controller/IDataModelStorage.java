/*
   Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.

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

package com.ait.tooling.nativetools.client.datamodel.controller;

import java.util.Collection;

import com.ait.tooling.common.api.java.util.function.Consumer;
import com.ait.tooling.common.api.java.util.function.Function;
import com.ait.tooling.nativetools.client.NObject;
import com.ait.tooling.nativetools.client.datamodel.AbstractJSONDataModel;
import com.ait.tooling.nativetools.client.datamodel.ModelIDList;

public interface IDataModelStorage<T extends AbstractJSONDataModel>
{
    public boolean exists(String id);

    public T get(String id);

    public Collection<T> get(ModelIDList list);

    public String getVersion();

    public boolean isPersistent();

    public Collection<String> keys();

    public void persist(Consumer<Boolean> callback);

    public void put(String id, T value);

    public void refresh(Consumer<Boolean> callback);

    public void remove(String id);

    public void remove(ModelIDList list);

    public Collection<T> values();

    public String getName();

    public Function<NObject, T> getObjectBuilder();
}
