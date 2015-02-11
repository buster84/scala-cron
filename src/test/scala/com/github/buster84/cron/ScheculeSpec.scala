package com.github.buster84.cron
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.joda.time._

class ScheduleSpec extends FlatSpec with ShouldMatchers {
  "Schedule" should "change timezone but the return keeps given DateTime's time zone" in {
    val tokyoTimeZone = DateTimeZone.forID("Asia/Tokyo")
    Schedule( "0 * * * *", DateTimeZone.UTC ).getNextAfter(new DateTime(2015, 2, 11, 8, 50, 0).withZoneRetainFields(tokyoTimeZone)) should be === new DateTime( 2015, 2, 11, 9, 0, 0 ).withZoneRetainFields(tokyoTimeZone)
    Schedule( "0 10 * * *", tokyoTimeZone).getNextAfter(new DateTime(2015, 2, 11, 0, 50, 0).withZoneRetainFields(DateTimeZone.UTC)) should be === new DateTime( 2015, 2, 11, 1, 0, 0 ).withZoneRetainFields(DateTimeZone.UTC)
  }
  "getNextAfter" should "return next date after given time" in {
    Schedule( "0 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 1, 50, 0)) should be === new DateTime( 2015, 2, 11, 2, 0, 0 )
    Schedule( "* 1 * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 1, 50, 0)) should be === new DateTime( 2015, 2, 11, 1, 51, 0 )
    Schedule( "* 1 * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 12, 1, 0, 0 )
    Schedule( "* * 1 * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 3, 1, 0, 0, 0 )
    Schedule( "* * * 1 *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2016, 1, 1, 0, 0, 0 )
    Schedule( "* * * * 1", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 16, 0, 0, 0 )

    Schedule( "*/5 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 11, 2, 5, 0 )
    Schedule( "*/5 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 5, 0)) should be === new DateTime( 2015, 2, 11, 2, 10, 0 )
    Schedule( "*/5 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 6, 0)) should be === new DateTime( 2015, 2, 11, 2, 10, 0 )
    Schedule( "1/5 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 11, 2, 1, 0 )
    Schedule( "1/5 * * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 1, 0)) should be === new DateTime( 2015, 2, 11, 2, 6, 0 )

    Schedule( "0 10 * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 11, 10, 0, 0 )
    Schedule( "0 10 * * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11,10, 0, 0)) should be === new DateTime( 2015, 2, 12, 10, 0, 0 )

    Schedule( "0 10 1 * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 3, 1, 10, 0, 0 )
    Schedule( "0 10 1 * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 3, 1, 10, 0, 0)) should be === new DateTime( 2015, 4, 1, 10, 0, 0 )

    Schedule( "0 10 1/2 * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 2, 0, 0)) should be === new DateTime( 2015, 2, 11, 10, 0, 0 )
    Schedule( "0 10 1/2 * *", DateTimeZone.forID("Asia/Tokyo") ).getNextAfter(new DateTime(2015, 2, 11, 10, 0, 0)) should be === new DateTime( 2015, 2,13, 10, 0, 0 )
  }

}
