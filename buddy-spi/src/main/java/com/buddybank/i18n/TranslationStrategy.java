package com.buddybank.i18n;

import java.util.Locale;

/**
 * Interface to access the i18n service. 
 * 
 * @author Ivan Saorin (US01889)
 * @version 1.0
 * @since 1.0							
 */

public interface TranslationStrategy
{
  public static final String DEFAULT_BUNDLE = "bundles.com.buddybank";


  public abstract String translateMessage(Locale locale);
}
