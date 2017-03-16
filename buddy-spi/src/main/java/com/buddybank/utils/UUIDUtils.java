package com.buddybank.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;


public class UUIDUtils {
	
	public static String noop(String seed) {
		String id = checkNotNull(seed, "UUID Seed must be not null");
		return removeLeadingSlash(removeTrailingSlash(id.toLowerCase()));
	}

	public static String getUUID(String seed) throws NoSuchAlgorithmException {
	    SecureRandom srA = SecureRandom.getInstance("SHA1PRNG");
	    srA.setSeed(seed.getBytes(Charset.forName("UTF-8")));
	    return UUID.nameUUIDFromBytes(Long.valueOf(srA.nextLong()).toString().getBytes(Charset.forName("UTF-8"))).toString();
	}

    /**
     * Removes a leading slash if exists.
     * 
     * @param path
     * @return the path without a leading /
     */
    private static String removeLeadingSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }
    
    /**
     * Removes a trailing slash if exists.
     * 
     * @param path
     * @return the path without a trailing /
     */
    private static String removeTrailingSlash(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }
}
