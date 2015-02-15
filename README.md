
# scala-cron [![Build Status](https://travis-ci.org/buster84/scala-cron.png?branch=master)](https://travis-ci.org/buster84/scala-cron)
The scala-cron can parse cron synstax string and compute nex time schedule.

## Installation
TODO

## Usage
```
$ activator console
scala> import com.github.buster84.cron.Schedule
import com.github.buster84.cron.Schedule

scala> import org.joda.time._
import org.joda.time._

scala> val schedule = Schedule("0 10 * * *",DateTimeZone.forID("Asia/Tokyo"))
schedule: com.github.buster84.cron.Schedule = Schedule(0 10 * * *,Asia/Tokyo)

scala> schedule.getNextAfter(new DateTime( 2015, 2, 11, 2, 0, 0 ))
res2: org.joda.time.DateTime = 2015-02-11T10:00:00.000+09:00

```
