package crud

import dbconnection.DbConnection
import doobie.implicits._
import forms.CreateForm.ListItemRead
import monix.execution.Scheduler.Implicits.global
import javax.inject.{Inject, Singleton}

import scala.concurrent.Future

@Singleton
class CRUD @Inject() (db: DbConnection){

  import forms.CreateForm.ListItemRead
  import forms.CreateForm.ListItemWrite
  import forms.UpdateForm.UpdateClass

  // TODO: make the ID unique
  // Creates a new custom Item
  def createCustomItem(item: ListItemWrite): Future[Unit] = {
    sql"""
            insert into
            products
            values
            (${item.id}, ${item.itemType}, ${item.itemSubType}, ${item.brand}, ${item.SKU}, ${item.quantity})
         """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())

  }

  // Reads just the Items currently on your shopping List.
  def read: Future[List[ListItemRead]] = {
    sql"""
          select *
          from products
          where
          quantity > 0
       """
      .query[ListItemRead]
      .to[List]
      .transact(db.transactor)
      .runAsync
  }

  // Reads all of the Items neglecting the quantity
  // could be just TABLE products;
  def readAll: Future[List[ListItemRead]] = {
    sql"""
         select *
         from products
       """
      .query[ListItemRead]
      .to[List]
      .transact(db.transactor)
      .runAsync
  }

  // Updates the quantity of any Item
  // TODO: Use UpdateClass instead of raw values and review query
  def update(id: Int, quantity: Int): Future[Unit] = {

    val transformID = id.toString

    sql"""
          update
          products
          set
          quantity = ${quantity}
          where id = ${transformID}
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())
  }

  // Sets all of the Items to quantity 0
  // Not implemented yet
    def flushItems(): Future[Unit] = {
    sql"""
         update
         products
         set
         quantity = 0
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())

  }

  // Deletes any custom Item
  // TODO: Change the DB in order to avoid toString. DB Created by me in postgres accepts a string =>
  // TODO: Create a file for the hard coded creation of a postgres DB
  def delete(item: Int): Future[Unit] = {
    val itemToString = item.toString
    sql"""
          delete
          from
          products
          where
          id = ${itemToString}
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())
  }

  // restore list to default
    def restore(defaults: List[ListItemWrite]) = {

    sql"""
         delete
         from products
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())

    defaults.foreach(item =>
       sql"""
            insert into
            products
            values
            (${item.id}, ${item.itemType}, ${item.itemSubType}, ${item.brand}, ${item.SKU}, ${item.quantity})
         """
         .update
         .run
         .transact(db.transactor)
         .runAsync
         .map(_ => ())
    )
    }
}
