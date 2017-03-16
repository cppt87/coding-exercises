package com.buddybank.i18n.spi;

public class HostErrorLevelEnum
{
  public enum HostErrorLevel
  {
    INFO, WARN, ERROR;

    public boolean isError()
    {
      return equals(HostErrorLevel.ERROR);
    }


    public boolean isInfo()
    {
      return equals(HostErrorLevel.INFO);
    }


    public boolean isWarning()
    {
      return equals(HostErrorLevel.WARN);
    }

  }
}
