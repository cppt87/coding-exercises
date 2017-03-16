package com.buddybank.i18n.spi;


import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.buddybank.Check;
import com.buddybank.i18n.TranslationStrategy;


public class BundleTranslation implements TranslationStrategy, Serializable
{
  private static final long serialVersionUID = 3655934263110352513L;
  
  private String messageKey;
  private String bundle;
  private Object[] params;


  public BundleTranslation(String messageKey)
  {
    bundle = null;
    this.messageKey = messageKey;
  }


  public BundleTranslation(String messageKey, String bundle)
  {
    this.messageKey = messageKey;
    this.bundle = bundle;
  }


  public BundleTranslation(String messageKey, String bundle, Object... params)
  {
    this.messageKey = messageKey;
    this.bundle = bundle;
    this.params = params;
  }


  public String translateMessage(Locale locale)
  {
    String b = (bundle == null) ? TranslationStrategy.DEFAULT_BUNDLE : bundle;
    return formatMessage(locale, b);
  }


  public String formatMessage(Locale locale, String bundle)
  {
    try
    {
      ResourceBundle rb = ResourceBundle.getBundle(bundle, locale);
      String template = rb.getString(messageKey);
      if (Check.isNull(params))
          return template;
      else
        return MessageFormat.format(template, params);
    }
    catch (Throwable t)
    {
      return "???" + messageKey + ": " + t;
    }
  }
}
