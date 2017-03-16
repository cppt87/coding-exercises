package com.buddybank.api;

import com.buddybank.api.dataproviders.IDPEntityReader;

public interface EntityReader<T> extends Serializer<T>, IDPEntityReader<T> {

}
