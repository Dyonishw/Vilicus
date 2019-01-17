package crud

import dbconnection.DbConnection
import doobie.implicits._
import forms.CreateForm.ListItemRead
import monix.execution.Scheduler.Implicits.global
import javax.inject.{Inject, Singleton}

import java.util.logging.Logger


import doobie._
import cats._
import cats.data._
import cats.effect.IO
import cats.implicits._
import scala.concurrent.ExecutionContext

import scala.concurrent.Future

@Singleton
class CRUD @Inject() (db: DbConnection){

  import forms.CreateForm.ListItemRead
  import forms.CreateForm.ListItemWrite
  import forms.UpdateForm.UpdateClass
  import forms.DeleteForm.DeleteIdClass

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
  // FIXME: Change the DB in order to avoid toString.
  def update(updateVals: UpdateClass): Future[Unit] = {
    sql"""
          update products
          set quantity = ${updateVals.quantity}
          where id = ${updateVals.id.toString}
        """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())
  }

  // Sets all of the Items to quantity 0
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
  // FIXME: Change the DB in order to avoid toString.
  // TODO: Create a file for the hard coded creation of a postgres DB
  def delete(item: DeleteIdClass): Future[Unit] = {
    sql"""
          delete
          from
          products
          where
          id = ${item.deleteId.toString}
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())
  }

  // restore list to default
    def restore(defaults: List[ListItemWrite]): Future[Unit] = {

    sql"""
         delete
         from products
       """
      .update
      .run
      .transact(db.transactor)
      .runAsync
      .map(_ => ())

    Future.successful(
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
      ))
    }
}
