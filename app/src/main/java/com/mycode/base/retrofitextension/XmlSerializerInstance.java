package com.mycode.base.retrofitextension;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Created by kyunghoon on 2019-05-03
 */
public class XmlSerializerInstance {

    /**
     * memo. "Persister"
     *
     * The Persister object is used to provide an implementation of a serializer.
     * This implements the Serializer interface and enables objects to be persisted and loaded from various sources.
     * This implementation makes use of Filter objects to replace template variables within the source XML document.
     *
     * "It is fully thread safe and can be shared by multiple threads without concerns."
     */
    private static Serializer mXmlSerializer;

    private static Object KEY = new Object();

    public static Serializer get() {
        if (mXmlSerializer == null) {
            synchronized (KEY) {
                if (mXmlSerializer == null) {
                    mXmlSerializer = new Persister();
                }
            }
        }
        return mXmlSerializer;
    }

}
