package com.github.buster84.cron
import org.joda.time._
import scala.annotation.tailrec

case class Schedule( cronExpression: String, timezone: DateTimeZone = DateTimeZone.UTC ) {
  sealed trait TimeType
  case object Min extends TimeType
  case object Hour extends TimeType
  case object Day extends TimeType
  case object Month extends TimeType

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

  private def isSatisfiedNumber( num:Int, timing: Timing ): Boolean = {
    timing match {
      case Asta =>
        true
      case TimingSeq(list) =>
        list.contains(num)
      case Bounds(from, to) =>
        num >= from && num <= to
      case AstaPar(par) =>
        num % par == 0
      case Fraction(top, bottom) =>
        num % bottom == top
    }
  }

  private def isSatisfiedAll( time: DateTime ): Boolean = {
    isSatisfiedMinute( time ) && isSatisfiedHour( time ) && isSatisfiedDay( time ) && isSatisfiedMonth( time )
  }
  private def isSatisfiedMinute(time: DateTime): Boolean = {
    isSatisfiedNumber( time.getMinuteOfHour(), cron.min )
  }

  private def isSatisfiedHour(time: DateTime): Boolean = {
    isSatisfiedNumber( time.getHourOfDay(), cron.hour )
  }
  private def isSatisfiedDay(time: DateTime): Boolean = {
    isSatisfiedNumber( time.getDayOfMonth, cron.day )
  }
  private def isSatisfiedMonth(time: DateTime): Boolean = {
    isSatisfiedNumber( time.getMonthOfYear, cron.month )
  }
  private def isSatisfiedWeek( time: DateTime ): Boolean = {
    isSatisfiedNumber( time.getDayOfWeek, cron.dayOfWeek )
  }
}
