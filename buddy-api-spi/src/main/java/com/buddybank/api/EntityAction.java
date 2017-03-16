package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPEntityAction;

public interface EntityAction<T> extends EntityReader<T>, Deserializer<T>, IDPEntityAction<T> {
	
}
