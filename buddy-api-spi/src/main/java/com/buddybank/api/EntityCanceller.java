package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPEntityCanceller;

public interface EntityCanceller<T> extends Serializer<T>, IDPEntityCanceller<T> {

}
