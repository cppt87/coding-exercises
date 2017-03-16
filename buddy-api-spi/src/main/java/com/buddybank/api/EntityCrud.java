package com.buddybank.api;

public interface EntityCrud<T> extends EntityCreator<T>, EntityReader<T>, EntityUpdater<T>, EntityCanceller<T> {

}
