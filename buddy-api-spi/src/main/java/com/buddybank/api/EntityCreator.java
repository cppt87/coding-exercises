package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPEntityCreator;

public interface EntityCreator<T> extends Serializer<T>, Deserializer<T>, IDPEntityCreator<T> {

}
