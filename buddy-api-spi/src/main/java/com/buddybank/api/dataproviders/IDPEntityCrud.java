package com.buddybank.api.dataproviders;

public interface IDPEntityCrud<T>
		extends IDPEntityReader<T>, IDPEntityCreator<T>, IDPEntityUpdater<T>, IDPEntityCanceller<T> {

}
