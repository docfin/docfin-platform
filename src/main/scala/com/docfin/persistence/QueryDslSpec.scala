package com.docfin.persistence



import scala.util.parsing.combinator.JavaTokenParsers


/**
  * Created by amit on 8/8/16.
  */
class QueryDslSpec extends JavaTokenParsers{

  /**
    * pathExpression ::= root"."{1 or more path elems}
    * groupByExpression ::= count "(" pathExpression ")" | max "(" pathExpression ")"
    * selectionExp ::= pathExpression { "," pathExpression | "," groupByExpression }
    * filterExpression ::= { pathExpression "==" value | pathExpression "!=" value }
    *
    */
  def simpleAggregateFunctionToken: Parser[String] = "max" | "count" | "min" | "avg"
  def infixPredicateOperatorToken: Parser[String] = "==" | "!=" | ">" | "<" | ">=" | "<="
  def logicalOperatorToken: Parser[String] = "and" | "or"

  def valueType: Parser[Any] = stringLiteral | floatingPointNumber | decimalNumber | wholeNumber


  def pathElem: Parser[Any] = ident ~ rep1("." ~ ident)
  def aggregationElem: Parser[Any] = simpleAggregateFunctionToken ~ "(" ~ pathElem ~ ")"
  def filterElem: Parser[Any] = (pathElem | valueType) ~ infixPredicateOperatorToken ~ (pathElem | valueType )


  def selectionExpr: Parser[Any] = pathElem ~ rep("," ~ (pathElem | aggregationElem))
  def filterExpr: Parser[Any] = filterElem  ~ rep( logicalOperatorToken ~ filterExpr)
  def filterGroupExpr: Parser[Any] = "(" ~ filterExpr ~ ")"
  def compositeFilterExpr: Parser[Any] = (filterExpr | filterGroupExpr) ~ rep( logicalOperatorToken ~ compositeFilterExpr)


  def queryExpr = selectionExpr ~ opt("where" ~ compositeFilterExpr)
}

object FasJobSpecParser extends QueryDslSpec{

  def main(args: Array[String]): Unit ={
    println("input: "+ args(0))
    println("output: "+ parseAll(queryExpr, args(0)))
  }
}
