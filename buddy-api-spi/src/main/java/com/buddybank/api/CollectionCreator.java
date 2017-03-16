package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPCollectionCreator;

public interface CollectionCreator<T> extends Serializer<T>, Deserializer<T>, IDPCollectionCreator<T> {
	
}
