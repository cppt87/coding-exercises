package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPCollectionUpdater;

public interface CollectionUpdater<T> extends Serializer<T>, Deserializer<T>, IDPCollectionUpdater<T> {
	
}
