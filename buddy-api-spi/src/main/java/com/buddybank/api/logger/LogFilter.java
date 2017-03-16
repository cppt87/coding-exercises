package com.buddybank.api.logger;

import static com.buddybank.api.logger.HttpRequestId.REQUESTID_ATTRIBUTE;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.routing.Filter;

public class LogFilter extends Filter {
	
	private final BBApiLogger logger = new BBApiLogger(LogFilter.class);
	
	private long startTime;
	
    public LogFilter(Context context) {
        super(context, null);
        
    }

    @Override                                                          
    protected int beforeHandle(Request request, Response response) {
    	startTime = System.currentTimeMillis();
    	request.getAttributes().put(REQUESTID_ATTRIBUTE, new HttpRequestId());
    	//System.err.println(" ############### API request filter beforeHandle ##################");
//    	logger.log();
//    	logger.append(request);
//    	logger.warn();
//    	logger.log();
//    	HeaderModel headerModel = new HeaderModel(request);
//		logger.append(headerModel);
//		logger.warn();
        return Filter.CONTINUE;
    }
    
    protected void afterHandle(Request request, Response response) {
    	//System.err.println(" ############### API request filter afterHandle ##################");
//    	logger.log();
//    	logger.append(response);
//    	logger.warn();
//    	logger.log();
//    	HeaderModel headerModel = new HeaderModel(response);
//		logger.append(headerModel);
//		logger.warn();
		long elapsed = System.currentTimeMillis() - startTime;
		String version = (String) getContext().getAttributes().get("api-version");
		if (version == null) {
			version = "1.0";
		}
		logger.log();
		response.getHeaders().add(new Header("api-version", version));
		logger.append(" elapsed", elapsed);
		logger.append(" api-version", version);
		logger.warn();
    }
}
