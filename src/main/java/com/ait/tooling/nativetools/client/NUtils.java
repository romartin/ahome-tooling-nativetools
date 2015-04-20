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

package com.ait.tooling.nativetools.client;

import com.ait.tooling.common.api.java.util.StringOps;
import com.ait.tooling.common.api.json.JSONType;
import com.ait.tooling.nativetools.client.polyfill.NativeToolsResources;
import com.ait.tooling.nativetools.client.util.Client;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;

public final class NUtils
{
    private NUtils()
    {
    }

    public final static String doStringRepair(final String value)
    {
        if (null == value)
        {
            return value;
        }
        return value.substring(0);
    }

    public final static String doKeyRepair(final String name)
    {
        return doKeyRepair(name, true);
    }

    public final static String doKeyRepair(final String name, final boolean trimmed)
    {
        final String trim = StringOps.requireTrimOrNull(name);

        return doStringRepair(((trimmed) ? trim : name));
    }

    public static final class Native
    {
        private static final NativeToolsResources rsrc = make();

        private static final NativeOps            nops = NativeOps.make();

        private Native()
        {
        }

        private static final NativeToolsResources make()
        {
            NativeToolsResources make = GWT.create(NativeToolsResources.class);

            Client.get().injectJs(make.json2());

            return make;
        }

        final static native JavaScriptObject parseJSON(String json)
        /*-{
			return $wnd.JSON.parse(json);
        }-*/;

        final static native JavaScriptObject parseJSON(String json, JavaScriptObject reviver)
        /*-{
			var f = reviver;
			return $wnd.JSON.parse(json, function(k, v) {
				if (k === '') {
					return v;
				}
				return f(k, v);
			});
        }-*/;

        final static native String toJSONString(JavaScriptObject value)
        /*-{
			return $wnd.JSON.stringify(value);
        }-*/;

        final static native String toJSONString(JavaScriptObject value, JavaScriptObject replacer)
        /*-{
			return $wnd.JSON.stringify(value, replacer);
        }-*/;

        final static native String toJSONString(JavaScriptObject value, String indent)
        /*-{
			return $wnd.JSON.stringify(value, null, indent);
        }-*/;

        final static native String toJSONString(JavaScriptObject value, int indent)
        /*-{
			return $wnd.JSON.stringify(value, null, indent);
        }-*/;

        final static native String toJSONString(JavaScriptObject value, JavaScriptObject replacer, String indent)
        /*-{
			return $wnd.JSON.stringify(value, replacer, indent);
        }-*/;

        final static native String toJSONString(JavaScriptObject value, JavaScriptObject replacer, int indent)
        /*-{
			return $wnd.JSON.stringify(value, replacer, indent);
        }-*/;

        public final static native JSONType getNativeTypeOfJSO(JavaScriptObject jso, String name)
        /*-{
			var nops = @com.ait.tooling.nativetools.client.NUtils.Native::nops;

			return nops.getNativeTypeOf(jso[name]);
        }-*/;

        public final static native JSONType getNativeTypeOfJSO(JavaScriptObject jso, int index)
        /*-{
			var nops = @com.ait.tooling.nativetools.client.NUtils.Native::nops;

			return nops.getNativeTypeOf(jso[index]);
        }-*/;

        public final static boolean is(final JavaScriptObject jso, final JSONType type)
        {
            return (type == getNativeTypeOfJSO(jso));
        }

        public final static native JSONType getNativeTypeOfJSO(JavaScriptObject jso)
        /*-{
			var nops = @com.ait.tooling.nativetools.client.NUtils.Native::nops;

			return nops.getNativeTypeOf(jso);
        }-*/;

        public final static boolean is(final NObjectJSO jso, final String name, final JSONType type)
        {
            return (type == getNativeTypeOf(jso, name));
        }

        public final static JSONType getNativeTypeOf(final NObjectJSO ojso, final String name)
        {
            return getNativeTypeOfJSO(ojso, name);
        }

        public final static boolean is(final NArrayJSO jso, final int index, final JSONType type)
        {
            return (type == getNativeTypeOf(jso, index));
        }

        public final static JSONType getNativeTypeOf(final NArrayJSO array, final int index)
        {
            if ((index >= 0) && (index < array.size()))
            {
                return getNativeTypeOfJSO(array, index);
            }
            return JSONType.UNDEFINED;
        }

        public final static NValue<?> getAsNValue(final NArrayJSO array, final int index)
        {
            if ((index >= 0) && (index < array.size()))
            {
                switch (getNativeTypeOf(array, index))
                {
                    case ARRAY:
                    {
                        return new NArray((NArrayJSO) array.getAsJSO(index).cast());
                    }
                    case OBJECT:
                    {
                        return new NObject((NObjectJSO) array.getAsJSO(index).cast());
                    }
                    default:
                    {
                        return null;
                    }
                }
            }
            return null;
        }

        public final static NValue<?> getAsNValue(final NObjectJSO ojso, final String name)
        {
            switch (getNativeTypeOf(ojso, name))
            {
                case ARRAY:
                {
                    return new NArray((NArrayJSO) ojso.getAsJSO(name).cast());
                }
                case OBJECT:
                {
                    return new NObject((NObjectJSO) ojso.getAsJSO(name).cast());
                }
                default:
                {
                    return null;
                }
            }
        }
    }

    public static final class JSON
    {
        public static final NValue<?> parse(final String json) throws Exception
        {
            return parse(json, null);
        }

        public static final NValue<?> parse(String json, final NJSONReviver reviver) throws Exception
        {
            if (null == json)
            {
                return null;
            }
            json = json.trim();

            if (json.isEmpty())
            {
                return null;
            }
            if ("null".equals(json))
            {
                return null;
            }
            JavaScriptObject root = null;

            try
            {
                if (null != reviver)
                {
                    final JavaScriptObject func = reviver.reviver();

                    if (Native.is(func, JSONType.FUNCTION))
                    {
                        root = Native.parseJSON(json, func);
                    }
                    else
                    {
                        root = Native.parseJSON(json);
                    }
                }
                else
                {
                    root = Native.parseJSON(json);
                }
            }
            catch (Exception e)
            {
                Client.get().error(e.getMessage());
            }
            if (null == root)
            {
                return null;
            }
            switch (Native.getNativeTypeOfJSO(root))
            {
                case OBJECT:
                {
                    return new NObject((NObjectJSO) root.cast());
                }
                case ARRAY:
                {
                    return new NArray((NArrayJSO) root.cast());
                }
                default:
                {
                    return null;
                }
            }
        }

        public static final String toJSONString(final JavaScriptObject value)
        {
            if (null == value)
            {
                return null;
            }
            return Native.toJSONString(value);
        }

        public static final String toJSONString(final JavaScriptObject value, final NJSONReplacer replacer)
        {
            if (null == value)
            {
                return null;
            }
            if (null == replacer)
            {
                return Native.toJSONString(value);
            }
            final JavaScriptObject func = replacer.replacer();

            if (Native.is(func, JSONType.FUNCTION))
            {
                return Native.toJSONString(value, func);
            }
            return Native.toJSONString(value);
        }

        public static final String toJSONString(final JavaScriptObject value, final String indent)
        {
            if (null == value)
            {
                return null;
            }
            if (null == indent)
            {
                return Native.toJSONString(value);
            }
            return Native.toJSONString(value, indent);
        }

        public static final String toJSONString(final JavaScriptObject value, final NJSONReplacer replacer, String indent)
        {
            if (null == value)
            {
                return null;
            }
            if (null == replacer)
            {
                if (null == indent)
                {
                    return Native.toJSONString(value);
                }
                return Native.toJSONString(value, indent);
            }
            final JavaScriptObject func = replacer.replacer();

            if (Native.is(func, JSONType.FUNCTION))
            {
                if (null == indent)
                {
                    return Native.toJSONString(value, func);
                }
                return Native.toJSONString(value, func, indent);
            }
            if (null == indent)
            {
                return Native.toJSONString(value);
            }
            return Native.toJSONString(value);
        }

        public static final String toJSONString(final JavaScriptObject value, final int indent)
        {
            if (null == value)
            {
                return null;
            }
            return Native.toJSONString(value, Math.max(0, indent));
        }

        public static final String toJSONString(final JavaScriptObject value, final NJSONReplacer replacer, final int indent)
        {
            if (null == value)
            {
                return null;
            }
            if (null == replacer)
            {
                return Native.toJSONString(value, Math.max(0, indent));
            }
            final JavaScriptObject func = replacer.replacer();

            if (Native.is(func, JSONType.FUNCTION))
            {
                return Native.toJSONString(value, func, Math.max(0, indent));
            }
            return Native.toJSONString(value, Math.max(0, indent));
        }
    }

    static final class NativeOps extends JavaScriptObject
    {
        static final NativeOps make()
        {
            final NativeOps self = JavaScriptObject.createObject().cast();

            self.init();

            return self;
        }

        protected NativeOps()
        {
        }

        private final native void init()
        /*-{
			this.getNativeTypeOf = function(value) {
				if (null == value) {
					return @com.ait.tooling.common.api.json.JSONType::NULL;
				}
				var type = typeof value;

				switch (type) {
				case 'string': {
					return @com.ait.tooling.common.api.json.JSONType::STRING;
				}
				case 'boolean': {
					return @com.ait.tooling.common.api.json.JSONType::BOOLEAN;
				}
				case 'number': {
					if (isFinite(value)) {
						return @com.ait.tooling.common.api.json.JSONType::NUMBER;
					}
					return @com.ait.tooling.common.api.json.JSONType::UNDEFINED;
				}
				case 'object': {
					if ((value instanceof Array)
							|| (value instanceof $wnd.Array)) {
						return @com.ait.tooling.common.api.json.JSONType::ARRAY;
					}
					if (value === Object(value)) {
						return @com.ait.tooling.common.api.json.JSONType::OBJECT;
					}
					return @com.ait.tooling.common.api.json.JSONType::UNDEFINED;
				}
				case 'function': {
					return @com.ait.tooling.common.api.json.JSONType::FUNCTION;
				}
				}
				return @com.ait.tooling.common.api.json.JSONType::UNDEFINED;
			};
        }-*/;
    }
}
