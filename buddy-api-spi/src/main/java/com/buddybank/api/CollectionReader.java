package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPCollectionReader;

public interface CollectionReader<T> extends Serializer<T>, IDPCollectionReader<T> {
	
}
