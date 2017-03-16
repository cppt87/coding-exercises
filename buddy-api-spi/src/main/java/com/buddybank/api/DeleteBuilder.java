package com.buddybank.api;

import java.util.Collection;

import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buddybank.JsonProcessingException;
import com.buddybank.api.exceptions.RestletApplicationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

public class DeleteBuilder<T> extends Builder<T> {
		
		static final Logger LOG = LoggerFactory.getLogger(DeleteBuilder.class);

		private EntityCanceller<T> entityCanceller;
		private CollectionCanceller<T> collectionCanceller;	
		private JsonNode node;
		private RepresentationType type;
		//private T entity;
		private Collection<T> collection;
		private T responseEntity;
		private Collection<T> responseCollection;	
				
		static {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		
		public DeleteBuilder(ServerResource resource) {
			super(resource);
			this.params =  new RequestParameters(resource); 
		}

		public DeleteBuilder<T> setEntityUpdater(EntityCanceller<T> service) {
			this.entityCanceller = service;
			return this;
		}

		public DeleteBuilder<T> setCollectionUpdater(CollectionCanceller<T> service) {
			this.collectionCanceller = service;
			return this;
		}

		public DeleteBuilder<T> parseRequestAttribute(String attributeName) throws RestletApplicationException {
			addAttribute(attributeName);
			return this;
		}
		
		public DeleteBuilder<T> parseHeaderString() {
			Series<Header> headers = params.getResource().getRequest().getHeaders();
			if (headers!=null && headers.size()>0){
				for (Header header : headers)
				{
					String value = header.getValue();
					String name = header.getName();	
					LOG.info(String.format("name %s", name));
					LOG.info(String.format("value %s", value));
					params.add(new RequestParam(RequestParamType.HEADER, name, value));
				}
			}		
			return this;
		}
		
		public DeleteBuilder<T> validateAttribute(String msg, String attributeName) {
			super.prepareNotFoundError(msg, attributeName);
			return this;
		}
		
		public DeleteBuilder<T> parse(JsonRepresentation representation) throws JsonProcessingException, RestletApplicationException {
			node = super.deserialize(representation);
			type = super.checkType(node);
			switch (type) {
			case ENTITY:
				//entity = parseEntity(node, entityCanceller);
				break;
			case COLLECTION:
				collection = parseCollection(node, collectionCanceller);
				break;
			}
			return this;
		}

		public DeleteBuilder<T> delete() throws RestletApplicationException {
			if (type == null) {
				responseEntity = entityCanceller.delete(params);
				type=RepresentationType.ENTITY;
				return this;
			}
			switch (type) {
			case ENTITY:
				responseEntity = entityCanceller.delete(params);
				break;
			case COLLECTION:
				responseCollection = collectionCanceller.delete(params, collection);
				break;
			}
			return this;
		}

		public Representation returnResults() throws JsonProcessingException {
			String result = null;
			LOG.info("returnResults type={} resent={} entupd={}", type, responseEntity, entityCanceller);
			if (type != null && responseEntity!=null) {
				switch (type) {
				case ENTITY:
					result = serialize(responseEntity, entityCanceller);
					break;
				case COLLECTION:
					result = serialize(responseCollection, collectionCanceller);
					break;
				}
			}
			else if (responseEntity!=null){
				result = serialize(responseEntity, entityCanceller);
			}
			resource.setStatus(Status.SUCCESS_OK);
			return new StringRepresentation(result, MediaType.APPLICATION_JSON);
		}
}