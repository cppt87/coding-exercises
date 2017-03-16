package com.buddybank.api;

import java.util.Collection;
import java.util.Set;

import org.restlet.data.Form;
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

public class UpdateBuilder<T> extends Builder<T> {
		
		static final Logger LOG = LoggerFactory.getLogger(UpdateBuilder.class);

		private EntityUpdater<T> entityUpdater;
		private CollectionUpdater<T> collectionUpdater;	
		private JsonNode node;
		private RepresentationType type;
		private T entity;
		private Collection<T> collection;
		private T responseEntity;
		private Collection<T> responseCollection;	
				
		static {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		
		public UpdateBuilder(ServerResource resource) {
			super(resource);
			this.params =  new RequestParameters(resource); 
		}

		public UpdateBuilder<T> setEntityUpdater(EntityUpdater<T> service) {
			this.entityUpdater = service;
			return this;
		}

		public UpdateBuilder<T> setCollectionUpdater(CollectionUpdater<T> service) {
			this.collectionUpdater = service;
			return this;
		}

		public UpdateBuilder<T> parseRequestAttribute(String attributeName) throws RestletApplicationException {
			addAttribute(attributeName);
			return this;
		}
		

		public UpdateBuilder<T> validateAttribute(String msg, String attributeName) {
			super.prepareNotFoundError(msg, attributeName);
			return this;
		}
		
		public UpdateBuilder<T> parseHeaderString() {
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
		
		public UpdateBuilder<T> parseQueryString() {
			Form queryParams = params.getResource().getQuery();
			Set<String> names = queryParams.getNames();
			for (String name : names) {
				LOG.debug(String.format("name %s", name));
				params.add(new RequestParam(RequestParamType.QUERY, name, queryParams.getFirstValue(name)));
			}
			return this;
		}
		
		public UpdateBuilder<T> parse(JsonRepresentation representation) throws JsonProcessingException, RestletApplicationException {
			
			node = super.deserialize(representation);
			type = super.checkType(node);
			LOG.debug("parse {} node: {}", type, node);
			
			switch (type) {
			case ENTITY:
				entity = parseEntity(node, entityUpdater);
				break;
			case COLLECTION:
				collection = parseCollection(node, collectionUpdater);
				break;
			}
			return this;
		}

		public UpdateBuilder<T> update() throws RestletApplicationException {
			switch (type) {
			case ENTITY:
				responseEntity = entityUpdater.update(params, entity);
				break;
			case COLLECTION:
				responseCollection = collectionUpdater.update(params, collection);
				break;
			}
			return this;
		}

		public Representation returnResults() throws JsonProcessingException {
			String result = null;
			LOG.debug("returnResults type={} resent={} entupd={}", type, responseEntity, entityUpdater);
			switch (type) {
			case ENTITY:
				result = serialize(responseEntity, entityUpdater);
				
				break;
			case COLLECTION:
				result = serialize(responseCollection, collectionUpdater);
				break;
			}
			
			resource.setStatus(Status.SUCCESS_OK);
			return new StringRepresentation(result, MediaType.APPLICATION_JSON);
		}
}