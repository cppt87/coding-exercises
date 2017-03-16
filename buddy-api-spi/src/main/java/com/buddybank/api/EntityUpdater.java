package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPEntityUpdater;

public interface EntityUpdater<T> extends Serializer<T>, Deserializer<T>, IDPEntityUpdater<T> {

}
