package com.buddybank.i18n.spi;


import java.io.Serializable;
import java.util.Locale;

import com.buddybank.i18n.TranslationStrategy;



/*
 * This class is used for passing messages which are already localized to the presentation layer.
 * This is a rare case as it is impossible for the db layer to know what locale the user is in.
 * In practice, the only use for this class is when working with the deprecated JAV0 Error
 * reporting system in Gauss where the message cannot be localized because Italian is the only choice.
 * It is hoped that someday the JAV0 Error system will not be used anymore, in which case
 * this class may be deleted.
 */
public class NoTranslation implements TranslationStrategy, Serializable
{
  private static final long serialVersionUID = 3156357316082902644L;
  
  private String messageText;


  public NoTranslation(String messageText)
  {
    this.messageText = messageText;
  }


  public String translateMessage(Locale locale)
  {
    return messageText;
  }


  public String formatMessage(Locale locale, String resourceBundle)
  {
    return messageText;
  }


  @Override
  public String toString()
  {
    return messageText;
  }
}
