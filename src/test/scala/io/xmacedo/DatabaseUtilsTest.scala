package io.xmacedo

import java.math.BigDecimal

class DatabaseUtilsTest extends UnitSpec with DatabaseUtils {
  "A string with valid date" should "be converted to a TO_DATE sql instruction" in {
    assert(parseSqlDate("20170101") === "TO_DATE('2017-01-01','YYYY-MM-DD')")
    assert(parseSqlDate("20171015") === "TO_DATE('2017-10-15','YYYY-MM-DD')")
    assert(parseSqlDate("20171231") === "TO_DATE('2017-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("20181231") === "TO_DATE('2018-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("20160229") === "TO_DATE('2016-02-29','YYYY-MM-DD')")
  }

  "A string with valid date" should "be converted to the last day and a TO_DATE sql instruction" in {
    assert(parseSqlDate("20170101", true) === "TO_DATE('2017-01-31','YYYY-MM-DD')")
    assert(parseSqlDate("20160201", true) === "TO_DATE('2016-02-29','YYYY-MM-DD')")
    assert(parseSqlDate("20170201", true) === "TO_DATE('2017-02-28','YYYY-MM-DD')")
    assert(parseSqlDate("20170301", true) === "TO_DATE('2017-03-31','YYYY-MM-DD')")
    assert(parseSqlDate("20170401", true) === "TO_DATE('2017-04-30','YYYY-MM-DD')")
    assert(parseSqlDate("20170501", true) === "TO_DATE('2017-05-31','YYYY-MM-DD')")
    assert(parseSqlDate("20170601", true) === "TO_DATE('2017-06-30','YYYY-MM-DD')")
    assert(parseSqlDate("20170701", true) === "TO_DATE('2017-07-31','YYYY-MM-DD')")
    assert(parseSqlDate("20170801", true) === "TO_DATE('2017-08-31','YYYY-MM-DD')")
    assert(parseSqlDate("20170901", true) === "TO_DATE('2017-09-30','YYYY-MM-DD')")
    assert(parseSqlDate("20171001", true) === "TO_DATE('2017-10-31','YYYY-MM-DD')")
    assert(parseSqlDate("20171101", true) === "TO_DATE('2017-11-30','YYYY-MM-DD')")
    assert(parseSqlDate("20171201", true) === "TO_DATE('2017-12-31','YYYY-MM-DD')")
  }

  "A string with valid date and specific format" should "be converted to a TO_DATE sql instruction" in {
    assert(parseSqlDate("2017-01-01",false,"yyyy-MM-dd") === "TO_DATE('2017-01-01','YYYY-MM-DD')")
    assert(parseSqlDate("2017-10-15",false,"yyyy-MM-dd") === "TO_DATE('2017-10-15','YYYY-MM-DD')")
    assert(parseSqlDate("2017-12-31",false,"yyyy-MM-dd") === "TO_DATE('2017-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("2018-12-31",false,"yyyy-MM-dd") === "TO_DATE('2018-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("2016-02-29",false,"yyyy-MM-dd") === "TO_DATE('2016-02-29','YYYY-MM-DD')")
    assert(parseSqlDate("01012017",false,"ddMMyyyy") === "TO_DATE('2017-01-01','YYYY-MM-DD')")
    assert(parseSqlDate("15102017",false,"ddMMyyyy") === "TO_DATE('2017-10-15','YYYY-MM-DD')")
    assert(parseSqlDate("31122017",false,"ddMMyyyy") === "TO_DATE('2017-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("31122018",false,"ddMMyyyy") === "TO_DATE('2018-12-31','YYYY-MM-DD')")
    assert(parseSqlDate("29022016",false,"ddMMyyyy") === "TO_DATE('2016-02-29','YYYY-MM-DD')")
  }

  "A string with invalid date" should "be throw IllegalArgumentException" in {
    assertThrows[IllegalArgumentException] { parseSqlDate("20170001") }
    assertThrows[IllegalArgumentException] { parseSqlDate("20170229") }
    assertThrows[IllegalArgumentException] { parseSqlDate("20171032") }
    assertThrows[IllegalArgumentException] { parseSqlDate("20171331") }
    assertThrows[IllegalArgumentException] { parseSqlDate("20170229") }
    assertThrows[IllegalArgumentException] { parseSqlDate("01002017",false,"ddMMyyyy") }
    assertThrows[IllegalArgumentException] { parseSqlDate("29022017",false,"ddMMyyyy") }
    assertThrows[IllegalArgumentException] { parseSqlDate("32102017",false,"ddMMyyyy") }
    assertThrows[IllegalArgumentException] { parseSqlDate("31132017",false,"ddMMyyyy") }
    assertThrows[IllegalArgumentException] { parseSqlDate("29022017",false,"ddMMyyyy") }
  }

  "A string with valid date" should "should be converted to a truncated TO_DATE sql instruction" in {
    assert(parseTruncatedSqlDate("20170101") === "TRUNC(TO_DATE('2017-01-01','YYYY-MM-DD'))")
    assert(parseTruncatedSqlDate("20171015") === "TRUNC(TO_DATE('2017-10-15','YYYY-MM-DD'))")
    assert(parseTruncatedSqlDate("20171231") === "TRUNC(TO_DATE('2017-12-31','YYYY-MM-DD'))")
    assert(parseTruncatedSqlDate("20181231") === "TRUNC(TO_DATE('2018-12-31','YYYY-MM-DD'))")
    assert(parseTruncatedSqlDate("20160229") === "TRUNC(TO_DATE('2016-02-29','YYYY-MM-DD'))")
  }

  "A string with only zeros" should "be converted to null" in {
    assert(parseSqlDate("0000") === "null")
    assert(parseSqlDate("000000") === "null")
    assert(parseSqlDate("00000000") === "null")
    assert(parseSqlDate("0000000000") === "null")
  }

  "A string with a decimal number" should "be converted to BigDecimal with 2 decimals" in {
    assert(parseBigDecimal("123456") === new BigDecimal("1234.56"))
    assert(parseBigDecimal("11112222333334444455555666677") === new BigDecimal("111122223333344444555556666.77"))
  }

  "A string with a invalid decimal number" should "be converted to BigDecimal.ZERO" in {
    assert(parseBigDecimal("") === BigDecimal.ZERO)
    assert(parseBigDecimal("abc") === BigDecimal.ZERO)
  }

  "A string with a decimal number and specific decimals" should "be converted to BigDecimal with specified decimals" in {
    assert(parseBigDecimal("123456",4) === new BigDecimal("12.3456"))
    assert(parseBigDecimal("11112222333334444455555666677", 7) === new BigDecimal("1111222233333444445555.5666677"))
  }
}
