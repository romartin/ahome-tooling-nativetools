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

package com.ait.tooling.nativetools.client.datamodel;

import com.ait.tooling.nativetools.client.NObject;

public abstract class AbstractDataModelID<T extends AbstractDataModelID<T>> extends AbstractJSONDataModel implements IDataModelID<T>
{
    protected AbstractDataModelID()
    {
        super(new NObject());
    }

    protected AbstractDataModelID(final NObject object)
    {
        super((null == object) ? (new NObject()) : (object));
    }

    @Override
    public final String getId()
    {
        return getModel().getAsString("id");
    }

    @Override
    public final void setId(final String id)
    {
        getModel().put("id", id);
    }
}
