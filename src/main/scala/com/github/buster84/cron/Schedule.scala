package com.github.buster84.cron
import org.joda.time._
import scala.annotation.tailrec

case class Schedule( cronExpression: String, timezone: DateTimeZone = DateTimeZone.UTC ) {

  private val cron = CronParser( cronExpression ).get

  def getNextAfter( time: DateTime ): DateTime = {
    val originTimeZone = time.getZone()
    getNextAfterRec( time.withZone(timezone).plusMinutes( 1 ) ).withZone( originTimeZone ) // Plus one minute because same datetime is not good for this method because of "AFTER"
  }

  @tailrec
  private def getNextAfterRec( time: DateTime ): DateTime = {
    if( !isSatisfiedMinute( time ) ){
      getNextAfterRec( time.plusMinutes( 1 ) )
    } else if ( !isSatisfiedHour( time ) ) {
      getNextAfterRec( time.plusHours( 1 ).withMinuteOfHour(0) )
    } else if ( !isSatisfiedDay( time ) || !isSatisfiedWeek( time ) ) {
      getNextAfterRec( time.plusDays( 1 ).withHourOfDay( 0 ).withMinuteOfHour( 0 ) )
    } else if ( !isSatisfiedMonth( time ) ){
      getNextAfterRec( time.plusMonths( 1 ).withDayOfMonth( 1 ).withHourOfDay( 0 ).withMinuteOfHour( 0 ) )
    } else {
      time
    }
  }

  private def isSatisfiedNumber( timing: Timing ): (Int) => Boolean = {
    timing match {
      case Asta =>
        { num: Int => true }
      case TimingSeq(list) =>
        { num: Int => list.contains(num) }
      case Bounds(from, to) =>
        { num: Int => num >= from && num <= to }
      case AstaPar(par) =>
        { num: Int => num % par == 0 }
      case Fraction(top, bottom) =>
        { num: Int => num % bottom == top }
    }
  }
  private val isSatisfiedMinuteNumber : (Int) => Boolean = isSatisfiedNumber( cron.min )
  private val isSatisfiedHourNumber : (Int) => Boolean = isSatisfiedNumber( cron.hour )
  private val isSatisfiedDayNumber : (Int) => Boolean = isSatisfiedNumber( cron.day )
  private val isSatisfiedMonthNumber : (Int) => Boolean = isSatisfiedNumber( cron.month )
  private val isSatisfiedWeekNumber : (Int) => Boolean = isSatisfiedNumber( cron.dayOfWeek )

  private def isSatisfiedMinute(time: DateTime): Boolean = {
    isSatisfiedMinuteNumber( time.getMinuteOfHour() )
  }

  private def isSatisfiedHour(time: DateTime): Boolean = {
    isSatisfiedHourNumber(time.getHourOfDay())
  }
  private def isSatisfiedDay(time: DateTime): Boolean = {
    isSatisfiedDayNumber(time.getDayOfMonth)
  }
  private def isSatisfiedMonth(time: DateTime): Boolean = {
    isSatisfiedMonthNumber(time.getMonthOfYear)
  }
  private def isSatisfiedWeek( time: DateTime ): Boolean = {
    isSatisfiedWeekNumber(time.getDayOfWeek)
  }
}
