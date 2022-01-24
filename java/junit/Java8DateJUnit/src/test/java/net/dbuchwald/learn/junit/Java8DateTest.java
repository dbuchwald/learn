package net.dbuchwald.learn.junit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dawidbuchwald on 05.03.2017.
 */
public class Java8DateTest {

  private static final String TIMEZONE_UTC = "UTC";
  private static final String TIMEZONE_CET = "CET";
  private static final String TIMEZONE_EST = "EST";

  private static final String TIMEZONE_LONDON   = "Europe/London";
  private static final String TIMEZONE_WARSAW   = "Europe/Warsaw";
  private static final String TIMEZONE_NEW_YORK = "America/New_York";

  private static final TimeZone timeZoneUTC = TimeZone.getTimeZone(TIMEZONE_UTC);
  private static final TimeZone timeZoneCET = TimeZone.getTimeZone(TIMEZONE_CET);
  private static final TimeZone timeZoneEST = TimeZone.getTimeZone(TIMEZONE_EST);

  private static final ZoneId zoneUTC = timeZoneUTC.toZoneId();
  private static final ZoneId zoneCET = timeZoneCET.toZoneId();
  private static final ZoneId zoneEST = timeZoneEST.toZoneId();

  private static final TimeZone timeZoneLondon  = TimeZone.getTimeZone(TIMEZONE_LONDON);
  private static final TimeZone timeZoneWarsaw  = TimeZone.getTimeZone(TIMEZONE_WARSAW);
  private static final TimeZone timeZoneNewYork = TimeZone.getTimeZone(TIMEZONE_NEW_YORK);

  private static final Calendar calendarUTC = new Calendar.Builder()
      .setDate(2022, 1, 24)
      .setTimeOfDay(20,19,40)
      .setTimeZone(timeZoneUTC)
      .build();

  private static final Calendar calendarCET = new Calendar.Builder().setDate(2022, 1, 24)
      .setTimeOfDay(20,19,40)
      .setTimeZone(timeZoneCET)
      .build();

  private static final Calendar calendarEST = new Calendar.Builder().setDate(2022, 1, 24)
      .setTimeOfDay(14,19,40)
      .setTimeZone(timeZoneEST)
      .build();

  private static final Date dateUTC = calendarUTC.getTime();
  private static final Date dateCET = calendarCET.getTime();
  private static final Date dateEST = calendarEST.getTime();

  @Test
  @DisplayName("ensure that required timezones are available")
  void ensureTimezonesAvailability() {
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_UTC);
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_CET);
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_EST);
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_LONDON);
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_WARSAW);
    assertThat(TimeZone.getAvailableIDs()).contains(TIMEZONE_NEW_YORK);
  }

  @Test
  @DisplayName("simple Date and Instant comparison")
  void dateComparison() {
    assertNotEquals(dateCET, dateUTC);
    assertNotEquals(dateUTC.toInstant(), dateCET.toInstant());

    assertNotEquals(dateUTC.toInstant().atZone(zoneUTC), dateCET.toInstant().atZone(zoneCET));

    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.YEAR),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.YEAR));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.MONTH_OF_YEAR),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.MONTH_OF_YEAR));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.DAY_OF_MONTH),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.DAY_OF_MONTH));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.HOUR_OF_DAY),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.HOUR_OF_DAY));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.MINUTE_OF_HOUR),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.MINUTE_OF_HOUR));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.SECOND_OF_MINUTE),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.SECOND_OF_MINUTE));
    assertEquals(dateUTC.toInstant().atZone(zoneUTC).get(ChronoField.MILLI_OF_SECOND),
                 dateCET.toInstant().atZone(zoneCET).get(ChronoField.MILLI_OF_SECOND));

    assertEquals(dateCET, dateEST);
    assertEquals(dateCET.toInstant(), dateEST.toInstant());

    assertEquals(dateCET.toInstant().atZone(zoneEST).toInstant(),
                 dateEST.toInstant().atZone(zoneCET).toInstant());
  }

  @Test
  @DisplayName("Date to Instant conversion fails without timezone")
  void dateToInstantConversionShouldFailWithoutTimezone() {
    assertThrows(UnsupportedTemporalTypeException.class , () -> dateUTC.toInstant().get(ChronoField.YEAR));
  }
}
