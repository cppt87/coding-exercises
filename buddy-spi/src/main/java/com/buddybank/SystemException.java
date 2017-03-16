package com.buddybank;

import java.util.Locale;

import com.buddybank.i18n.TranslationStrategy;
import com.buddybank.i18n.spi.BundleTranslation;
import com.buddybank.i18n.spi.NoTranslation;

/**
 * General application exception transporting internationalized user message as well as technical English details. 
 * 
 * @author Ivan Saorin (US01889)
 * @version 1.0
 * @since 1.0							
 * @see com.buddybank.union.SystemException
 */

public class SystemException extends RuntimeException
{
  private static final long serialVersionUID = -5313825135442052450L;
  
  public static final TranslationStrategy defaultMessage = new BundleTranslation("error.default");
  private TranslationStrategy userInfoStrategy;
  private TranslationStrategy logInfoStrategy;
  protected boolean isBlocking = true;

  public SystemException()
  {
    super();
    initialize();
  }


  public SystemException(Throwable cause)
  {
    super(cause);
    initialize();
  }
  public SystemException(Throwable cause, String logInfo)
  {
    super(cause);
    initialize();
    logInfoStrategy = new NoTranslation(logInfo);
  }

  public SystemException(String message)
  {
    super();
    initialize();
    logInfoStrategy = new NoTranslation(message);
  }


  public SystemException(TranslationStrategy userInfoStrategy)
  {
    this.userInfoStrategy = userInfoStrategy;
  }


  public SystemException(TranslationStrategy userInfoStrategy, Throwable cause)
  {
    super(cause);
    this.userInfoStrategy = userInfoStrategy;
  }


  public SystemException(TranslationStrategy userInfoStrategy, String logInfo)
  {
    this.userInfoStrategy = userInfoStrategy;
    logInfoStrategy = new NoTranslation(logInfo);
  }


  public SystemException(TranslationStrategy userInfoStrategy, TranslationStrategy logInfoStrategy)
  {
    this.userInfoStrategy = userInfoStrategy;
    this.logInfoStrategy = logInfoStrategy;
  }


  public String getUserInfo(Locale locale)
  {
    return userInfoStrategy.translateMessage(locale);
  }


  public String getUserInfo()
  {
    return getUserInfo(getDefaultLocale());
  }


  private void initialize()
  {
    userInfoStrategy = defaultMessage;
  }


  public static Locale getDefaultLocale()
  {
    return Locale.getDefault(); //getGebLocale().getLocale();
  }

  public boolean isSuppressDetailMessage()
  {
    return (!userInfoStrategy.equals(defaultMessage));
  }


  public boolean isBlocking()
  {
    return isBlocking;
  }


  /*
   * If a translation strategy has been configured, a translated message will be returned using the specified locale.
   * Otherwise, super.getLocalizedMessage() is called.
   */
  public String getLocalizedMessage(Locale locale)
  {
    if ((userInfoStrategy == null) && (logInfoStrategy == null))
    {
      return super.getLocalizedMessage();
    }
    else if ((logInfoStrategy == null) && (userInfoStrategy != null))
    {
      return userInfoStrategy.translateMessage(locale);
    }

    StringBuffer sb = new StringBuffer();
    if (notNull(userInfoStrategy)) {
      sb.append("Message displayed: ").append(userInfoStrategy.translateMessage(locale));
    }
    if (notNull(logInfoStrategy)) {
      sb.append("\r\n");
      sb.append("Exception Message: ").append(logInfoStrategy.translateMessage(locale));
    }
    return sb.toString();
  }


  private boolean notNull(Object value)
  {    
    return value!=null;
  }


  /*
   * If a translation strategy has been configured, a translated message will be returned using user locale(or default GebLocale).
   */
  @Override
  public String getLocalizedMessage()
  {
      return getMessage();
  }
  
  @Override
  public String getMessage(){
	Locale l = Locale.getDefault(); 
	/*
    Locale l = null;
    try {
      GebLocale locale = getGebLocale();
      l = locale.getLocale();
    } catch (Throwable t) {
      try{
        SendMail sm = new SendMail();
        String[] DEFAULT_DESTINATARI_MAIL = {"SZanchin.Consultant@unicreditgroup.eu"};
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String dump = sw.toString();
        ISystem system = (ISystem)StdSpringHelper.getService("system");
        sm.sendMail("", DEFAULT_DESTINATARI_MAIL, "GEB_LOCALE_ERROR@Zanchin.Asap.it", system.getEnvironmentCode()+" "+system.getSiteCode()+ "- Tipo errore: LOCALE" , dump, false);
      }catch (Throwable d){
        System.err.println("S.Z. Fa niente ma non sbomba.."+d.getMessage() +" ORIGINALE"+t.getMessage());
      }
      l = Locale.getDefault();
    }
    */
    return getLocalizedMessage(l);
    
  }

  public TranslationStrategy getTranslationStrategy()
  {
    return userInfoStrategy;
  }
}
