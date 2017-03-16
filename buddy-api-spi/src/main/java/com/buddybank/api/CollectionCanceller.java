package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPCollectionCanceller;

public interface CollectionCanceller<T> extends Serializer<T>, Deserializer<T>, IDPCollectionCanceller<T> {
	
}
